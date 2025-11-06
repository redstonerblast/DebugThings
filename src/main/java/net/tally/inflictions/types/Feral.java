package net.tally.inflictions.types;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.mob.HostileEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryKeys;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.*;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Feral extends Infliction {

    private static final TargetPredicate ACTIVE = TargetPredicate.createAttackable().setBaseMaxDistance(20).setPredicate(LivingEntity::isMobOrPlayer);

    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:confusion", "debugthings:rabid")
    );

    public Feral() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void onApply(LivingEntity entity, InflictionInstance instance, NbtCompound oldData) {
        if (entity instanceof MobEntity) {
            List<LivingEntity> list = entity.getWorld().getTargets(LivingEntity.class, ACTIVE, entity, entity.getBoundingBox().expand(10));
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
            List<LivingEntity> list = entity.getWorld().getTargets(LivingEntity.class, ACTIVE, entity, entity.getBoundingBox().expand(10));
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
        return time % 140 == 0;
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
