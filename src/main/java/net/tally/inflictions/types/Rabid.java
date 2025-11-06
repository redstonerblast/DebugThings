package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.InflictionInstance;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Rabid extends Infliction {

    private static final TargetPredicate ACTIVE = TargetPredicate.createAttackable().setBaseMaxDistance(20).setPredicate(LivingEntity::isMobOrPlayer);

    public Rabid() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void onApply(LivingEntity entity, InflictionInstance instance, NbtCompound oldData) {
        if (entity instanceof MobEntity) {
            List<LivingEntity> list = entity.getWorld().getTargets(LivingEntity.class, ACTIVE, entity, entity.getBoundingBox().expand(15));
            if (!list.isEmpty()) {
                LivingEntity livingEntity2 = list.get(entity.getRandom().nextInt(list.size()));
                ((MobEntity) entity).setTarget(livingEntity2);
                entity.setAttacker(livingEntity2);
            }
        }
        super.onApply(entity, instance, oldData);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        if (entity instanceof MobEntity) {
            List<LivingEntity> list = entity.getWorld().getTargets(LivingEntity.class, ACTIVE, entity, entity.getBoundingBox().expand(15));
            if (!list.isEmpty()) {
                LivingEntity livingEntity2 = list.get(entity.getRandom().nextInt(list.size()));
                ((MobEntity) entity).setTarget(livingEntity2);
                entity.setAttacker(livingEntity2);
            }
        }
        super.applyTick(stacks, entity, source);
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 80 == 0;
    }
}
