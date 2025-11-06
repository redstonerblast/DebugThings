package net.tally.inflictions;

import de.dafuqs.additionalentityattributes.AdditionalEntityAttributes;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registry;
import net.tally.DebugThings;
import net.tally.DebugThingsClient;
import net.tally.attributes.DebugAttributes;
import net.tally.helpers.CustomRegistryHandler;
import net.tally.inflictions.types.*;

import java.util.ArrayList;
import java.util.List;

public class Inflictions {
    public static final Infliction BLEED = register(
            "bleed",
            new Bleed()
    );

    public static final Infliction POISON = register(
            "poison",
            new Poison()
    );

    public static final Infliction NIGHTSHADE = register(
            "nightshade",
            new Nightshade()
    );

    public static final Infliction BURN = register(
            "burn",
            new Burn()
    );

    public static final Infliction WET = register(
            "wet",
            new Wet()
    );

    public static final Infliction EMPTY = register(
            "empty",
            new Empty()
    );

    public static final Infliction FROST = register(
            "frost",
            new Frost()
    );

    public static final Infliction FROSTBURN = register(
            "frostburn",
            new Frostburn()
    );

    public static final Infliction SHOCK = register(
            "shock",
            new Shock()
    );

    public static final Infliction RED = register(
            "red",
            new Red()
    );

    public static final Infliction ORANGE = register(
            "orange",
            new Orange()
    );

    public static final Infliction YELLOW = register(
            "yellow",
            new Yellow().addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "e3634f5b-623d-43a5-a4cb-a7df74aa34c1", 0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    public static final Infliction LIME = register(
            "lime",
            new Lime()
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "e3eb8f5f-8621-4d0e-8638-499fd5c9e44c", 0.5, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(AdditionalEntityAttributes.JUMP_HEIGHT, "de1af133-3b11-4c27-859f-f23391a9625b", 0.4, EntityAttributeModifier.Operation.ADDITION)
    );

    public static final Infliction GREEN = register(
            "green",
            new Green()
    );

    public static final Infliction CYAN = register(
            "cyan",
            new Cyan()
    );

    public static final Infliction LIGHT_BLUE = register(
            "light_blue",
            new LightBlue()
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "40756d3d-9876-4cd9-8ee3-0207342956ee", -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    public static final Infliction BLUE = register(
            "blue",
            new Blue()
    );

    public static final Infliction PURPLE = register(
            "purple",
            new Purple()
    );

    public static final Infliction MAGENTA = register(
            "magenta",
            new Magenta()
    );

    public static final Infliction PINK = register(
            "pink",
            new Pink()
    );

    public static final Infliction INFERNO = register(
            "inferno",
            new Inferno()
    );

    public static final Infliction SHADE = register(
            "shade",
            new Shade()
    );

    public static final Infliction HOLY = register(
            "holy",
            new Holy()
    );

    public static final Infliction UNHOLY = register(
            "unholy",
            new Unholy()
    );

    public static final Infliction BLESS = register(
            "bless",
            new Bless()
    );

    public static final Infliction CURSE = register(
            "curse",
            new Curse()
    );

    public static final Infliction SANCTIFIED = register(
            "sanctified",
            new Sanctified()
    );

    public static final Infliction FORSAKEN = register(
            "forsaken",
            new Forsaken()
    );

    public static final Infliction FORTIFY = register(
            "fortify",
            new Fortify()
                    .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "9395f0ea-bb34-4b96-80e0-1af48760e848", 1, EntityAttributeModifier.Operation.ADDITION)
    );

    public static final Infliction BRITTLE = register(
            "brittle",
            new Brittle()
                    .addAttributeModifier(EntityAttributes.GENERIC_ARMOR, "7fd3cef6-bdf7-410f-8043-ac4da717e9bf", -1, EntityAttributeModifier.Operation.ADDITION)
    );

    public static final Infliction ROOTED = register(
            "rooted",
            new Rooted()
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "16def8e3-f966-4d85-8879-d02a4e13dba1", -0.25, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    public static final Infliction DILATION = register(
            "dilation",
            new Generic(InflictionCategory.NEUTRAL)
    );

    public static final Infliction RIFT = register(
            "rift",
            new Generic(InflictionCategory.NEUTRAL)
    );

    public static final Infliction REFLECT = register(
            "reflect",
            new Generic(InflictionCategory.BENEFICIAL)
    );

    public static final Infliction RUBBER = register(
            "rubber",
            new Generic(InflictionCategory.BENEFICIAL)
    );

    public static final Infliction HASTE = register(
            "haste",
            new Generic(InflictionCategory.BENEFICIAL)
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "b92729c0-972a-4e55-a11f-697e72c3225d", 0.1, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(DebugAttributes.AIR_SPEED, "282d9e7b-58d7-448f-9aa0-13e902cefd4e", 0.42, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "c580efa3-0ee9-44fa-bfb9-dcc6e7513c0d", 0.12, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    public static final Infliction GLITCH = register(
            "glitch",
            new Glitch()
    );

    public static final Infliction REGEN = register(
            "regen",
            new Regen()
    );

    public static final Infliction FERAL = register(
            "feral",
            new Feral()
    );

    public static final Infliction CONFUSION = register(
            "confusion",
            new Confusion()
    );

    public static final Infliction RABID = register(
            "rabid",
            new Rabid()
    );

    public static final Infliction VITALITY = register(
            "vitality",
            new Vitality()
    );

    public static final Infliction ENTANGLED = register(
            "entangled",
            new Entangled()
                    .addAttributeModifier(EntityAttributes.GENERIC_MOVEMENT_SPEED, "b1b0d644-632a-401f-81f5-92c9be2e3258", -0.2, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(EntityAttributes.GENERIC_ATTACK_SPEED, "041cc474-06f5-4337-9e04-e02817968fce", -0.3, EntityAttributeModifier.Operation.MULTIPLY_TOTAL)
    );

    private static Infliction register(String id, Infliction infliction) {
        return Registry.register(CustomRegistryHandler.REGISTRY_INFLICTION, "debugthings:" + id, infliction);
    }

    public static void init() {
        DebugThings.LOGGER.info("LOADING INFLICTIONS");
    }
}
