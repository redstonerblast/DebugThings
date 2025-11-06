package net.tally.components;

import dev.onyxstudios.cca.api.v3.component.ComponentKey;
import dev.onyxstudios.cca.api.v3.component.ComponentRegistryV3;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentFactoryRegistry;
import dev.onyxstudios.cca.api.v3.entity.EntityComponentInitializer;
import net.minecraft.entity.LivingEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;

public class DebugThingsComponents implements EntityComponentInitializer {
    public static final ComponentKey<NbtComponent> INFLICT_RENDER =
            ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("debugthings:infliction_render"), NbtComponent.class);
    public static final ComponentKey<NbtListComponent> IMMUNITIES =
            ComponentRegistryV3.INSTANCE.getOrCreate(new Identifier("debugthings:immunities"), NbtListComponent.class);

    @Override
    public void registerEntityComponentFactories(EntityComponentFactoryRegistry registry) {
        registry.registerFor(LivingEntity.class, INFLICT_RENDER, SyncedRenderInflicts::new);
        registry.registerFor(LivingEntity.class, IMMUNITIES, SyncedImmunities::new);
    }

    public static void addImmunity(LivingEntity provider, String string) {
        if (provider.getWorld().isClient) { return; }
        provider.getComponent(IMMUNITIES).addValue(string);
    }

    public static void removeImmunity(LivingEntity provider, String string) {
        if (provider.getWorld().isClient) { return; }
        provider.getComponent(IMMUNITIES).removeValue(string);
    }

    public static boolean hasImmunity(LivingEntity provider, String string) {
        return provider.getComponent(IMMUNITIES).hasValue(string);
    }

    public static void setInflictRender(LivingEntity provider, NbtCompound nbt) {
        if (provider.getWorld().isClient) { return; }
        provider.getComponent(INFLICT_RENDER).setValue(nbt);
    }

    public static NbtCompound getInflictRender(LivingEntity provider) {
       return provider.getComponent(INFLICT_RENDER).getValue();
    }
}
