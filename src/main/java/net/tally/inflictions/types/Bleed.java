package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.registry.RegistryKeys;
import net.tally.DebugThings;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import org.jetbrains.annotations.Nullable;

public class Bleed extends Infliction {

    public Bleed() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        super.applyTick(stacks, entity, source);
        DamageSource damageSource;
        if (source != null) {
            damageSource = new DamageSource(
                    entity.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE)
                            .entryOf(DebugDamageTypes.BLEED), source);
        } else {
            damageSource = new DamageSource(
                    entity.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE)
                            .entryOf(DebugDamageTypes.BLEED_NAT));
        }
        entity.damage(damageSource, stacks);
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 20 == 0;
    }
}
