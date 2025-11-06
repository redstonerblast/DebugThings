package net.tally.inflictions.types;


import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import org.jetbrains.annotations.Nullable;

public class Empty extends Infliction {
    public Empty() {
        super(InflictionCategory.NEUTRAL);
    }
}
