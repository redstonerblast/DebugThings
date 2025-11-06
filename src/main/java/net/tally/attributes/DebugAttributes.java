package net.tally.attributes;

import net.minecraft.entity.attribute.ClampedEntityAttribute;
import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeInstance;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;
import net.tally.mixin.entities.EntityAttributeInstanceInvoker;

public class DebugAttributes {
    public static final Identifier TOOL_LEVEL_ID
            = DebugAttributes.createAttributeIdentifier("player", "tool_level");
    public static final EntityAttribute TOOL_LEVEL = createClampedAttribute(
            TOOL_LEVEL_ID,
            0.0,
            -50.0,
            50.0
    ).setTracked(true);

    public static final Identifier POSITIVE_POT_DUR_ID
            = DebugAttributes.createAttributeIdentifier("generic", "pos_potion_dur");
    public static final EntityAttribute POSITIVE_POT_DUR = createClampedAttribute(
            POSITIVE_POT_DUR_ID,
            1.0,
            0.0,
            50.0
    ).setTracked(true);

    public static final Identifier NEGATIVE_POT_DUR_ID
            = DebugAttributes.createAttributeIdentifier("generic", "neg_potion_dur");
    public static final EntityAttribute NEGATIVE_POT_DUR = createClampedAttribute(
            NEGATIVE_POT_DUR_ID,
            1.0,
            0.0,
            50.0
    ).setTracked(true);

    public static final Identifier INVUL_SELF_ID
            = DebugAttributes.createAttributeIdentifier("generic", "invul_self");
    public static final EntityAttribute INVUL_SELF = createClampedAttribute(
            INVUL_SELF_ID,
            1.0,
            0.0,
            50.0
    ).setTracked(true);

    public static final Identifier AIR_SPEED_ID
            = DebugAttributes.createAttributeIdentifier("generic", "air_speed");
    public static final EntityAttribute AIR_SPEED = createClampedAttribute(
            AIR_SPEED_ID,
            1.0,
            0.0,
            50.0
    ).setTracked(true);

    public static final Identifier INVUL_ATTACK_ID
            = DebugAttributes.createAttributeIdentifier("generic", "invul_attack");
    public static final EntityAttribute INVUL_ATTACK = createClampedAttribute(
            INVUL_ATTACK_ID,
            1.0,
            0.0,
            50.0
    ).setTracked(true);

    public static final Identifier DODGE_CHANCE_ID
            = DebugAttributes.createAttributeIdentifier("generic", "dodge_chance");
    public static final EntityAttribute DODGE_CHANCE = createClampedAttribute(
            DODGE_CHANCE_ID,
            0.0,
            0.0,
            1.0
    ).setTracked(true);

    public static final Identifier PARRY_CHANCE_ID
            = DebugAttributes.createAttributeIdentifier("generic", "parry_chance");
    public static final EntityAttribute PARRY_CHANCE = createClampedAttribute(
            PARRY_CHANCE_ID,
            0.0,
            0.0,
            1.0
    ).setTracked(true);

    public static final Identifier TICK_RATE_ID
            = DebugAttributes.createAttributeIdentifier("generic", "tick_rate");
    public static final EntityAttribute TICK_RATE = createClampedAttribute(
            TICK_RATE_ID,
            0.0,
            -4.0,
            4.0
    ).setTracked(true);

    public static final Identifier CRIT_CHANCE_ID
            = DebugAttributes.createAttributeIdentifier("player", "crit_chance");
    public static final EntityAttribute CRIT_CHANCE = createClampedAttribute(
            CRIT_CHANCE_ID,
            0.0,
            0.0,
            10.0
    ).setTracked(true);

    public static final Identifier CRIT_MULT_ID
            = DebugAttributes.createAttributeIdentifier("player", "crit_mult");
    public static final EntityAttribute CRIT_MULT = createClampedAttribute(
            CRIT_MULT_ID,
            0.0,
            0.0,
            100.0
    ).setTracked(true);

    public static void setup() {
        Registry.register(Registries.ATTRIBUTE, TOOL_LEVEL_ID, TOOL_LEVEL);
        Registry.register(Registries.ATTRIBUTE, DODGE_CHANCE_ID, DODGE_CHANCE);
        Registry.register(Registries.ATTRIBUTE, PARRY_CHANCE_ID, PARRY_CHANCE);
        Registry.register(Registries.ATTRIBUTE, TICK_RATE_ID, TICK_RATE);
        Registry.register(Registries.ATTRIBUTE, CRIT_CHANCE_ID, CRIT_CHANCE);
        Registry.register(Registries.ATTRIBUTE, CRIT_MULT_ID, CRIT_MULT);
        Registry.register(Registries.ATTRIBUTE, NEGATIVE_POT_DUR_ID, NEGATIVE_POT_DUR);
        Registry.register(Registries.ATTRIBUTE, POSITIVE_POT_DUR_ID, POSITIVE_POT_DUR);
        Registry.register(Registries.ATTRIBUTE, INVUL_SELF_ID, INVUL_SELF);
        Registry.register(Registries.ATTRIBUTE, INVUL_ATTACK_ID, INVUL_ATTACK);
        Registry.register(Registries.ATTRIBUTE, AIR_SPEED_ID, AIR_SPEED);
    }

    public static Identifier createIdentifier(String path) {
        return new Identifier(DebugThings.MOD_ID, path);
    }

    public static Identifier createAttributeIdentifier(String type, String name) {
        return createIdentifier(type + "." + name);
    }

    public static EntityAttribute createClampedAttribute(Identifier id, double fallback, double min, double max) {
        return new ClampedEntityAttribute(
                id.toTranslationKey("attribute"),
                fallback,
                min,
                max
        );
    }

    public static EntityAttribute createDynamicAttribute(Identifier id) {
        return new DynamicEntityAttribute(
                id.toTranslationKey("attribute")
        );
    }

    @SafeVarargs
    public static double applyAttributeModifiers(
            double initial,
            Signed<EntityAttributeInstance>... attributes
    ) {
        for (var signedAttribute : attributes) {
            for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
                    .invokeGetModifiersByOperation(EntityAttributeModifier.Operation.ADDITION)
            ) {
                switch (signedAttribute.sign()) {
                    case POSITIVE -> initial += modifier.getValue();
                    case NEGATIVE -> initial -= modifier.getValue();
                    default -> throw new IllegalStateException();
                }
            }
        }
        double result = initial;
        for (var signedAttribute : attributes) {
            for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
                    .invokeGetModifiersByOperation(EntityAttributeModifier.Operation.MULTIPLY_BASE)
            ) {
                switch (signedAttribute.sign()) {
                    case POSITIVE -> result += initial * modifier.getValue();
                    case NEGATIVE -> result -= initial * modifier.getValue();
                    default -> throw new IllegalStateException();
                }
            }
        }
        for (var signedAttribute : attributes) {
            for (var modifier : ((EntityAttributeInstanceInvoker) signedAttribute.value())
                    .invokeGetModifiersByOperation(EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
            ) {
                switch (signedAttribute.sign()) {
                    case POSITIVE -> result *= 1.0 + modifier.getValue();
                    case NEGATIVE -> result *= 1.0 - modifier.getValue();
                    default -> throw new IllegalStateException();
                }
            }
        }
        for (var signedAttribute : attributes) {
            result = signedAttribute.value().getAttribute().clamp(result);
        }
        return result;
    }
}
