package net.tally.inflictions;

import com.google.common.collect.Maps;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.*;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.Util;
import net.tally.DebugThings;
import net.tally.helpers.CustomRegistryHandler;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class Infliction {
    private final Map<EntityAttribute, AttributeModifierCreator> attributeModifiers = Maps.newHashMap();
    private final InflictionCategory category;

    private static final Map<String, String> COMBINES = Map.ofEntries();

    @Nullable
    private String translationKey;

    protected Infliction(InflictionCategory category) {
        this.category = category;
        DebugThings.addToCategory(category, this);
    }

    protected String loadTranslationKey() {
        if (this.translationKey == null) {
            this.translationKey = Util.createTranslationKey("infliction", CustomRegistryHandler.REGISTRY_INFLICTION.getId(this));
        }

        return this.translationKey;
    }

    public InflictionCategory getCategory() {
        return category;
    }

    public void onApply(LivingEntity entity, InflictionInstance instance, NbtCompound oldData) {
        instance.getType().onStackChange(entity.getAttributes(), instance.getStacks());
        combine(entity, instance, oldData);

        if (instance.getStacks() > 0 && instance.getType() != Inflictions.RIFT) {
            instance.setStacks(instance.getStacks() + InflictionHandler.getInflictions(Inflictions.RIFT, entity));
        }

        if (instance.getDuration() > 0 && instance.getType() != Inflictions.DILATION) {
            instance.setFullDuration((int) (instance.getDuration() * ((InflictionHandler.getInflictions(Inflictions.DILATION, entity) * 0.1) + 1)));
        }

        if (instance.getType().getCategory() == InflictionCategory.HARMFUL && InflictionHandler.hasInfliction(Inflictions.SANCTIFIED, entity)) {
            if (instance.getType() != Inflictions.FORSAKEN && instance.getType() != Inflictions.CURSE) {
                instance.setStacks(0);
            }
        }

        if (instance.getType().getCategory() == InflictionCategory.BENEFICIAL && InflictionHandler.hasInfliction(Inflictions.FORSAKEN, entity)) {
            if (instance.getType() != Inflictions.SANCTIFIED && instance.getType() != Inflictions.BLESS) {
                instance.setStacks(0);
            }
        }


        if (InflictionHandler.hasInfliction(Inflictions.REFLECT, entity)) {
            if (instance.getSource() != null) {
                Entity source = entity.getWorld().getEntityById(instance.getSource());
                if (source instanceof LivingEntity livingEntity && entity != livingEntity) {
                    InflictionHandler.addInfliction(livingEntity, instance, (ServerWorld) entity.getWorld());
                }
            }
        }
    }

    public void combine(LivingEntity entity, InflictionInstance instance, NbtCompound oldData) {
        Map<String, String> map = new HashMap<>(getCombines());

        if (instance.getType().getCategory() == InflictionCategory.HARMFUL) {
            map.put("debugthings:bless", "debugthings:empty");
        }

        if (instance.getType().getCategory() == InflictionCategory.BENEFICIAL) {
            map.put("debugthings:curse", "debugthings:empty");
        }

        boolean update = false;

        while (true) {
            Optional<String> key = oldData.getKeys().stream().filter(map::containsKey).findFirst();
            if (key.isEmpty()) {
                break;
            }
            if (instance.getStacks() <= 0) {
                break;
            }
            NbtList inflList = oldData.getList(key.get(), NbtElement.COMPOUND_TYPE);

            if (inflList.isEmpty()) {
                oldData.remove(key.get());
                continue;
            }

            update = true;

            int oldDuration = ((NbtCompound) inflList.get(0)).getInt("duration");
            int compareStacks = ((NbtCompound) inflList.get(0)).getInt("stacks");
            int takeStacks = Math.min(compareStacks, instance.getStacks());

            Infliction output = entity.getWorld().getRegistryManager().get(CustomRegistryHandler.INFLICTION).get(Identifier.tryParse(map.get(key.get())));
            Infliction inputb = entity.getWorld().getRegistryManager().get(CustomRegistryHandler.INFLICTION).get(Identifier.tryParse(key.get()));

            takeStacks = combineOverrideSelf(output, instance.getType(), inputb) ? takeStacks : compareStacks;

            if (combineOverrideOther(output, instance.getType(), inputb)) {
                if (takeStacks == compareStacks) {
                    inflList.remove(0);
                    if (inflList.isEmpty()) {
                        inputb.onRemoved(entity.getAttributes());
                    }
                } else {
                    ((NbtCompound) inflList.get(0)).putInt("stacks", compareStacks - takeStacks);
                }
            }
            if (output != Inflictions.EMPTY) {
                combinedAdd(oldDuration, instance, output, takeStacks, entity);
            }
            if (combineOverrideSelf(output, instance.getType(), inputb)) {
                int newStack = instance.getStacks() - takeStacks;
                instance.setStacks(newStack);
                instance.getType().onStackChange(entity.getAttributes(), newStack);
            }
        }
        if (update) {
            InflictionHandler.forceUpdateInflicts(entity, (ServerWorld) entity.getWorld());
        }
    }

    public void combinedAdd(int baseDur, InflictionInstance addition, Infliction newType, int stacks, LivingEntity entity) {
        InflictionInstance newInfliction = new InflictionInstance(newType,
                Math.max(baseDur, addition.getDuration()),
                stacks,
                "combined_" + addition.getName(),
                addition.variant(),
                addition.getSource());

        InflictionHandler.addInfliction(entity, newInfliction, "highest", "combine", (ServerWorld) entity.getWorld());
    }

    public boolean combineOverrideSelf(Infliction base, Infliction inputA, Infliction inputB) {
        return true;
    }

    public boolean combineOverrideOther(Infliction base, Infliction inputA, Infliction inputB) {
        return true;
    }

    public Map<String, String> getCombines() {
        return COMBINES;
    }

    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {

    }

    public boolean canApply(long time, LivingEntity livingEntity) {
        return true;
    }

    public Infliction addAttributeModifier(EntityAttribute attribute, String uuid, double amount, EntityAttributeModifier.Operation operation) {
        this.attributeModifiers.put(attribute, new Infliction.InflictionAttributeModifierCreator(UUID.fromString(uuid), amount, operation));
        return this;
    }

    public Map<EntityAttribute, AttributeModifierCreator> getAttributeModifiers() {
        return this.attributeModifiers;
    }

    public void onRemoved(AttributeContainer attributeContainer) {
        for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : this.attributeModifiers.entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier(entry.getValue().getUuid());
            }
        }
    }

    public void onStackChange(AttributeContainer attributeContainer, int stacks) {
        for (Map.Entry<EntityAttribute, AttributeModifierCreator> entry : this.attributeModifiers.entrySet()) {
            EntityAttributeInstance entityAttributeInstance = attributeContainer.getCustomInstance(entry.getKey());
            if (entityAttributeInstance != null) {
                entityAttributeInstance.removeModifier(entry.getValue().getUuid());
                entityAttributeInstance.addPersistentModifier(entry.getValue().createAttributeModifier(stacks));
            }
        }
    }

    public String getId() {
        return CustomRegistryHandler.REGISTRY_INFLICTION.getId(this).toString();
    }

    public String getTranslationKey() {
        return this.loadTranslationKey();
    }

    public Text getName() {
        return Text.translatable(this.getTranslationKey());
    }

    class InflictionAttributeModifierCreator implements AttributeModifierCreator {
        private final UUID uuid;
        private final double baseValue;
        private final EntityAttributeModifier.Operation operation;

        public InflictionAttributeModifierCreator(UUID uuid, double baseValue, EntityAttributeModifier.Operation operation) {
            this.uuid = uuid;
            this.baseValue = baseValue;
            this.operation = operation;
        }

        @Override
        public UUID getUuid() {
            return this.uuid;
        }

        @Override
        public EntityAttributeModifier createAttributeModifier(int amplifier) {
            return new EntityAttributeModifier(this.uuid, Infliction.this.getTranslationKey() + " " + amplifier, this.baseValue * amplifier, this.operation);
        }
    }
}
