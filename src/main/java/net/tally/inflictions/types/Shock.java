package net.tally.inflictions.types;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;

public class Shock extends Infliction {

    public Shock() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        super.applyTick(stacks, entity, source);
        DamageSource damageSource;
        float dmg = InflictionHandler.getInflictions(Inflictions.WET, entity) + 1f;
        if (source != null) {
            damageSource = new DamageSource(
                    entity.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE)
                            .entryOf(DebugDamageTypes.SHOCK), source);
        } else {
            damageSource = new DamageSource(
                    entity.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE)
                            .entryOf(DebugDamageTypes.SHOCK_NAT));
        }
        for (Entity entity1 : entity.getWorld().getOtherEntities(entity, entity.getBoundingBox().expand(stacks + 3.5d))) {
            if (entity1 instanceof LivingEntity livingEntity1 && InflictionHandler.hasInfliction(Inflictions.SHOCK, livingEntity1)) {
                entity1.damage(damageSource, dmg);
            }
        }
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 30 == 0;
    }
}
