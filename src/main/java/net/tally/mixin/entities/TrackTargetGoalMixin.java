package net.tally.mixin.entities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.ai.goal.Goal;
import net.minecraft.entity.ai.goal.TrackTargetGoal;
import net.minecraft.entity.mob.MobEntity;
import net.tally.helpers.IEntityDataSaver;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TrackTargetGoal.class)
public abstract class TrackTargetGoalMixin extends Goal {
    @Final
    @Shadow
    protected MobEntity mob;

    @Shadow @Nullable protected LivingEntity target;

    @Inject(at = @At(value = "HEAD"), method = "shouldContinue()Z", cancellable = true)
    private void debugthings$init(CallbackInfoReturnable<Boolean> cir) {
        LivingEntity livingEntity = mob;
        LivingEntity livingEntityTarget = mob.getTarget();

        if (target != null) {
            if (!((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").isEmpty() && ((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").equals(((IEntityDataSaver) target).debugged_things_1_20_4$getPersistentData().getString("squad"))) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "canTrack(Lnet/minecraft/entity/LivingEntity;Lnet/minecraft/entity/ai/TargetPredicate;)Z", cancellable = true)
    private void debugthings$inject(LivingEntity target, TargetPredicate targetPredicate, CallbackInfoReturnable<Boolean> cir) {
        if (target != null) {
            if (!((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").isEmpty() && ((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").equals(((IEntityDataSaver) target).debugged_things_1_20_4$getPersistentData().getString("squad"))) {
                cir.setReturnValue(false);
            }
        }
    }
}