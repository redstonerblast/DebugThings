package net.tally.inflictions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.Keyable;
import com.mojang.serialization.MapCodec;
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.RemoveEntityStatusEffectS2CPacket;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.Identifier;
import net.minecraft.world.World;
import net.tally.DebugThings;
import net.tally.components.DebugThingsComponents;
import net.tally.helpers.CustomRegistryHandler;
import net.tally.helpers.IEntityDataSaver;
import net.tally.helpers.InflictionAccessor;
import net.tally.networking.ClientMessages;
import org.jetbrains.annotations.Nullable;

import java.util.*;
import java.util.stream.IntStream;

public class InflictionHandler {

    public static boolean hasInfliction(Infliction infliction, LivingEntity entity) {
        if (DebugThingsComponents.getInflictRender(entity) == null) {
            return false;
        }
        return DebugThingsComponents.getInflictRender(entity).contains(infliction.getId());
    }

    public static int getInflictions(Infliction infliction, LivingEntity entity) {
        if (DebugThingsComponents.getInflictRender(entity) == null) {
            return 0;
        }
        return DebugThingsComponents.getInflictRender(entity).getInt(infliction.getId());
    }

    public static boolean addInfliction(LivingEntity livingEntity, InflictionInstance inflictionInstance, ServerWorld serverWorld) {
        if (!canInflict()) {return false;}

        NbtCompound eData = ((IEntityDataSaver)livingEntity).debugged_things_1_20_4$getPersistentData();
        NbtCompound inflicitons = eData.getCompound("inflictions");

        inflictionInstance.getType().onApply(livingEntity, inflictionInstance, inflicitons);

        if(inflictionInstance.getStacks() == 0) {return true;}

        NbtList inflList = inflicitons.getList(inflictionInstance.getType().getId(), NbtElement.COMPOUND_TYPE);

        NbtCompound element = new NbtCompound();
        inflictionInstance.writeTypelessNbt(element);

        inflList.add(element);

        inflicitons.put(inflictionInstance.getType().getId(), inflList);

        eData.put("inflictions", inflicitons);

        return true;
    }

    public static boolean addInfliction(LivingEntity livingEntity, InflictionInstance inflictionInstance, String applicationdur, String applicationstack, ServerWorld serverWorld) {
        if (!canInflict()) {return false;}

        NbtCompound eData = ((IEntityDataSaver)livingEntity).debugged_things_1_20_4$getPersistentData();
        NbtCompound inflicitons = eData.getCompound("inflictions");

        inflictionInstance.getType().onApply(livingEntity, inflictionInstance, inflicitons);

        if(inflictionInstance.getStacks() == 0) {return true;}

        NbtList inflList = inflicitons.getList(inflictionInstance.getType().getId(), NbtElement.COMPOUND_TYPE);

        if (inflList.isEmpty()) {
            NbtCompound element = new NbtCompound();
            inflictionInstance.writeTypelessNbt(element);
            inflList.add(element);
        } else {
            NbtCompound element = new NbtCompound();
            inflictionInstance.writeTypelessNbt(element);

            if (inflList.stream().anyMatch(n -> ((NbtCompound) n).getString("name").equals(inflictionInstance.getName()))) {
                OptionalInt index = IntStream.range(0, inflList.size())
                        .filter(i -> inflictionInstance.getName().equals(((NbtCompound)inflList.get(i)).getString("name")))
                        .findFirst();
                NbtCompound elementOld = inflList.getCompound(index.getAsInt());
                InflictionInstance compiledInflict = new InflictionInstance(inflictionInstance.getType(),
                        Objects.equals(applicationdur, "replace") ? inflictionInstance.getDuration() :
                                Objects.equals(applicationdur, "highest") ? Math.max(inflictionInstance.getDuration(), elementOld.getInt("duration")) :
                                        Objects.equals(applicationstack, "lowest") ? Math.min(inflictionInstance.getStacks(), elementOld.getInt("duration")) :
                                            inflictionInstance.getDuration() + elementOld.getInt("duration"),
                        Objects.equals(applicationstack, "replace") ? inflictionInstance.getStacks() :
                                Objects.equals(applicationstack, "highest") ? Math.max(inflictionInstance.getStacks(), elementOld.getInt("stacks")) :
                                        Objects.equals(applicationstack, "lowest") ? Math.min(inflictionInstance.getStacks(), elementOld.getInt("stacks")) :
                                            inflictionInstance.getStacks() + elementOld.getInt("stacks"),
                        inflictionInstance.getName(),
                        inflictionInstance.variant(),
                        Objects.equals(applicationdur, "replace") ? inflictionInstance.getMaxDuration() :
                                Objects.equals(applicationdur, "highest") ? Math.max(inflictionInstance.getMaxDuration(), elementOld.getInt("maxduration")) :
                                        Objects.equals(applicationstack, "lowest") ? Math.min(inflictionInstance.getStacks(), elementOld.getInt("maxduration")) :
                                            inflictionInstance.getDuration() + elementOld.getInt("duration")
                );
                compiledInflict.writeTypelessNbt(element);
                inflList.set(index.getAsInt(), element);
            } else {
                inflList.add(element);
            }
        }

        inflicitons.put(inflictionInstance.getType().getId(), inflList);

        eData.put("inflictions", inflicitons);

        return true;
    }

    public static void clearInflictions(LivingEntity entity) {
        if (entity instanceof ServerPlayerEntity serverPlayerEntity) {
            sendClientPacket(serverPlayerEntity, new NbtCompound());
        }

        NbtCompound eData = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();

        NbtCompound inflictions = eData.getCompound("inflictions");
        for (String infKeys : inflictions.getKeys()) {
            Infliction infl = entity.getWorld().getRegistryManager().get(CustomRegistryHandler.INFLICTION).get(Identifier.tryParse(infKeys));
            infl.onRemoved(entity.getAttributes());
        }

        eData.remove("inflictions");
        DebugThingsComponents.setInflictRender(entity, new NbtCompound());
    }

    public static void clearInfliction(LivingEntity entity, Infliction infliction) {
        NbtCompound eData = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();
        NbtCompound inflictions = eData.getCompound("inflictions");
        infliction.onRemoved(entity.getAttributes());
        inflictions.remove(infliction.getId());

        forceUpdateInflicts(entity, (ServerWorld) entity.getWorld());
    }

    public static void clearInflictions(LivingEntity entity, Infliction infliction, int fullStack) {
        NbtCompound eData = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();
        NbtCompound inflictions = eData.getCompound("inflictions");

        while (true) {
            NbtList inflList = inflictions.getList(infliction.getId(), NbtElement.COMPOUND_TYPE);

            if (inflList.isEmpty()) {
                infliction.onRemoved(entity.getAttributes());
                break;
            }

            if (fullStack <= 0) {
                break;
            }

            int compareStacks = ((NbtCompound) inflList.get(0)).getInt("stacks");
            int takeStacks = Math.min(compareStacks, fullStack);

            if (takeStacks == compareStacks) {
                inflList.remove(0);
            } else {
                ((NbtCompound) inflList.get(0)).putInt("stacks", compareStacks - takeStacks);
            }
            fullStack -= takeStacks;
        }

        forceUpdateInflicts(entity, (ServerWorld) entity.getWorld());
    }

    public static void clearInflictions(LivingEntity entity, int fullStack) {
        NbtCompound eData = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();
        NbtCompound inflictions = eData.getCompound("inflictions");

        while (true) {
            Optional<String> key = inflictions.getKeys().stream().findFirst();

            if (key.isEmpty()) {
                break;
            }

            NbtList inflList = inflictions.getList(key.get(), NbtElement.COMPOUND_TYPE);
            Infliction inflict = entity.getWorld().getRegistryManager().get(CustomRegistryHandler.INFLICTION).get(Identifier.tryParse(key.get()));

            if (inflList.isEmpty()) {
                inflictions.remove(key.get());
                inflict.onRemoved(entity.getAttributes());
                continue;
            }

            if (fullStack <= 0) {
                break;
            }

            int compareStacks = ((NbtCompound) inflList.get(0)).getInt("stacks");
            int takeStacks = Math.min(compareStacks, fullStack);

            if (takeStacks == compareStacks) {
                inflList.remove(0);
            } else {
                ((NbtCompound) inflList.get(0)).putInt("stacks", compareStacks - takeStacks);
            }
            fullStack -= takeStacks;
        }

        forceUpdateInflicts(entity, (ServerWorld) entity.getWorld());
    }

    public static NbtCompound returnInflicts(LivingEntity entity) {
        NbtCompound eData = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();
        return eData.getCompound("inflictions");
    }

    public static boolean canInflict() {
        return true;
    }

    public static void forceUpdateInflicts(LivingEntity livingEntity, ServerWorld s) {
        NbtCompound eData = ((IEntityDataSaver)livingEntity).debugged_things_1_20_4$getPersistentData();
        if(!eData.contains("inflictions")) { return; }
        @Nullable NbtCompound inflictions = eData.getCompound("inflictions");
        if (inflictions == null || inflictions.isEmpty()) { return; }
        NbtCompound repInflict = new NbtCompound();
        NbtCompound cInflictions = new NbtCompound();

        for (String key : inflictions.getKeys()) {
            Infliction infliction = CustomRegistryHandler.REGISTRY_INFLICTION.get(Identifier.tryParse(key));
            NbtList list = inflictions.getList(key, NbtElement.COMPOUND_TYPE);
            if (list.isEmpty()) {
                continue;
            }
            int stack = 0;
            for (NbtElement element : list.copy()) {
                InflictionInstance inflictionI = InflictionInstance.fromNbt(infliction, ((NbtCompound) element));
                stack += inflictionI.getStacks();
            }

            if (stack > 0) {
                infliction.onStackChange(livingEntity.getAttributes(), stack);
                cInflictions.putInt(key, stack);
            } else {
                infliction.onRemoved(livingEntity.getAttributes());
            }

            repInflict.put(key, list);

            if (repInflict.isEmpty()) {
                eData.remove("inflictions");
            } else {
                eData.put("inflictions", repInflict);
            }
        }

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            sendClientPacket(serverPlayerEntity, cInflictions);
        }

        DebugThingsComponents.setInflictRender(livingEntity, cInflictions);
    }

    public static void processInflictions(LivingEntity livingEntity, long time, ServerWorld s) {
        NbtCompound eData = ((IEntityDataSaver)livingEntity).debugged_things_1_20_4$getPersistentData();
        if(!eData.contains("inflictions")) { return; }
        @Nullable NbtCompound inflictions = eData.getCompound("inflictions");
        if (inflictions == null || inflictions.isEmpty()) { return; }
        NbtCompound repInflict = new NbtCompound();
        NbtCompound cInflictions = new NbtCompound();

        for (String key : inflictions.getKeys()) {
            Infliction infliction = CustomRegistryHandler.REGISTRY_INFLICTION.get(Identifier.tryParse(key));
            NbtList list = inflictions.getList(key, NbtElement.COMPOUND_TYPE);
            if (list.isEmpty()) {
                continue;
            }
            int stack = 0;
            int ind = 0;
            boolean changed = false;
            Integer id = -1;
            for (NbtElement element : list.copy()) {
                InflictionInstance inflictionI = InflictionInstance.fromNbt(infliction, ((NbtCompound) element));
                int dur = inflictionI.getDuration() - 1;
                if (inflictionI.getSource() != null) {
                    id = inflictionI.getSource();
                }
                if (dur > 0) {
                    stack += inflictionI.getStacks();
                    ((NbtCompound) element).putInt("duration", dur);
                    list.set(ind, element);
                } else {
                    changed = true;
                    if (inflictionI.variant() == 1 && inflictionI.getStacks() != 1) {
                        stack += inflictionI.getStacks() - 1;
                        ((NbtCompound) element).putInt("stacks", inflictionI.getStacks() - 1);
                        ((NbtCompound) element).putInt("duration", inflictionI.getMaxDuration());
                        list.set(ind, element);
                    } else {
                        list.remove(ind);
                        ind -= 1;
                    }
                }
                ind += 1;
            }

            if (stack > 0) {
                cInflictions.putInt(key, stack);
                if (changed) {
                    infliction.onStackChange(livingEntity.getAttributes(), stack);
                }
            } else {
                infliction.onRemoved(livingEntity.getAttributes());
            }

            repInflict.put(key, list);

            if (repInflict.isEmpty()) {
                eData.remove("inflictions");
            } else {
                eData.put("inflictions", repInflict);
            }

            if(infliction.canApply(time, livingEntity)) { infliction.applyTick(stack, livingEntity, (LivingEntity) livingEntity.getWorld().getEntityById(id)); }
        }

        if (livingEntity instanceof ServerPlayerEntity serverPlayerEntity) {
            sendClientPacket(serverPlayerEntity, cInflictions);
        }

        DebugThingsComponents.setInflictRender(livingEntity, cInflictions);
    }

    public static void sendClientPacket(ServerPlayerEntity player, NbtCompound nbtCompound) {
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeNbt(nbtCompound);
        ServerPlayNetworking.send(player, ClientMessages.INFLICTIONSC, buf);
    }
}
