package net.tally.holders;

import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;

public class DebugDamageTypes {
    public static final RegistryKey<DamageType> BLEED = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "bleed"));
    public static final RegistryKey<DamageType> BURN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "burn"));
    public static final RegistryKey<DamageType> FROSTBURN = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "frostburn"));
    public static final RegistryKey<DamageType> POISON = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "poison"));
    public static final RegistryKey<DamageType> SHOCK = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "shock"));
    public static final RegistryKey<DamageType> HOLY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "holy"));
    public static final RegistryKey<DamageType> UNHOLY = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "unholy"));

    public static final RegistryKey<DamageType> BLEED_NAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "bleed_natural"));
    public static final RegistryKey<DamageType> BURN_NAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "burn_natural"));
    public static final RegistryKey<DamageType> FROSTBURN_NAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "frostburn_natural"));
    public static final RegistryKey<DamageType> POISON_NAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "poison_natural"));
    public static final RegistryKey<DamageType> SHOCK_NAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "shock_natural"));
    public static final RegistryKey<DamageType> HOLY_NAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "holy_natural"));
    public static final RegistryKey<DamageType> UNHOLY_NAT = RegistryKey.of(RegistryKeys.DAMAGE_TYPE, Identifier.of(DebugThings.MOD_ID, "unholy_natural"));

    public static void init() {

    }
}
