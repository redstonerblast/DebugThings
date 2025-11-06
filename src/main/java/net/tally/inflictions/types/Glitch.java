package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.tally.components.DebugThingsComponents;
import net.tally.helpers.IEntityDataSaver;
import net.tally.inflictions.*;
import org.jetbrains.annotations.Nullable;

import java.util.Map;
import java.util.Set;

public class Glitch extends Infliction {
    public Glitch() {
        super(InflictionCategory.NEUTRAL);
    }

    @Override
    public void onApply(LivingEntity entity, InflictionInstance instance, NbtCompound oldData) {
        NbtCompound eData = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();

        if (instance.getStacks() > 0 && instance.getType() != Inflictions.RIFT) {
            instance.setStacks(instance.getStacks() + InflictionHandler.getInflictions(Inflictions.RIFT, entity));
        }

        for (int i = instance.getStacks(); i > 0; i--) {
            @Nullable String key = oldData.getKeys().stream().skip(entity.getRandom().nextInt(oldData.getKeys().size())).findFirst().orElse(null);
            if (key != null) {
                NbtList list = oldData.getList(key, NbtElement.COMPOUND_TYPE);
                if (list.isEmpty()) { continue; }
                int index = entity.getRandom().nextInt(list.size());
                NbtCompound element = (NbtCompound) list.get(index);

                element.putInt("stacks", element.getInt("stacks") + 1);

                list.set(index, element);

                oldData.put(key, list);
            }
        }

        eData.put("inflictions", oldData);

        instance.setStacks(0);
    }
}