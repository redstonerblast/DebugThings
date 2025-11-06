package net.tally.effects;

import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.util.Identifier;
import net.tally.attributes.DebugAttributes;

public class DebugEffects {
    public static final StatusEffect ROT = new RotEffect().addAttributeModifier(EntityAttributes.GENERIC_MAX_HEALTH, "e9839285-e7fd-4604-b245-78a61e8a79a4", -0.2, EntityAttributeModifier.Operation.MULTIPLY_BASE);
    public static final StatusEffect WOUNDED = new WoundedEffect();
    public static final StatusEffect RUBBER = new RubberEffect();
    public static final StatusEffect TEMPORAL_COMPRESS = new TemporalCompressEffect().addAttributeModifier(DebugAttributes.TICK_RATE, "88c13f46-31dc-461a-b54c-85fc10de97e9", -1, EntityAttributeModifier.Operation.ADDITION);
    public static final StatusEffect TEMPORAL_STRETCH = new TemporalStretchEffect().addAttributeModifier(DebugAttributes.TICK_RATE, "83777c31-809f-42ba-8cce-3ef6deb987f0", 1, EntityAttributeModifier.Operation.ADDITION);
    public static final StatusEffect TEMPORAL_FREEZE = new TemporalFreezeEffect();

    public static final TagKey<StatusEffect> PERSISTENT_EFFECTS = TagKey.of(RegistryKeys.STATUS_EFFECT, new Identifier("debugthings", "persistent_effects"));

    public static void init() {
        Registry.register(Registries.STATUS_EFFECT, new Identifier("debugthings", "rot"), ROT);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("debugthings", "wounded"), WOUNDED);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("debugthings", "rubber"), RUBBER);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("debugthings", "temporal_compress"), TEMPORAL_COMPRESS);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("debugthings", "temporal_stretch"), TEMPORAL_STRETCH);
        Registry.register(Registries.STATUS_EFFECT, new Identifier("debugthings", "temporal_freeze"), TEMPORAL_FREEZE);
    }
}
