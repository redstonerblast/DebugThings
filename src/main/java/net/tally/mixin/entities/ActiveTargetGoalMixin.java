package net.tally.mixin.entities;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.goal.ActiveTargetGoal;
import net.tally.helpers.IEntityDataSaver;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ActiveTargetGoal.class)
public abstract class ActiveTargetGoalMixin extends TrackTargetGoalMixin {
    @Unique
    private final ActiveTargetGoal<?> instance = (ActiveTargetGoal<?>) (Object) this;

    @Shadow
    @Nullable
    protected LivingEntity targetEntity;

    @Inject(at = @At(value = "RETURN"), method = "canStart()Z", cancellable = true)
    public void debugThings$skewera(CallbackInfoReturnable<Boolean> cir) {
        if (targetEntity != null) {
            if (!InflictionHandler.hasInfliction(Inflictions.RABID, mob)) {
                if (!((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").isEmpty() && ((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").equals(((IEntityDataSaver) targetEntity).debugged_things_1_20_4$getPersistentData().getString("squad"))) {
                    mob.setTarget(null);
                    targetEntity = null;
                    cir.setReturnValue(false);
                }
            }
        }
    }

    @Inject(at = @At(value = "HEAD"), method = "start")
    public void debugThings$skewerb(CallbackInfo ci) {
        if (targetEntity != null) {
            if (!InflictionHandler.hasInfliction(Inflictions.RABID, mob)) {
                if (!((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").isEmpty() && ((IEntityDataSaver) mob).debugged_things_1_20_4$getPersistentData().getString("squad").equals(((IEntityDataSaver) targetEntity).debugged_things_1_20_4$getPersistentData().getString("squad"))) {
                    mob.setTarget(null);
                    targetEntity = null;
                }
            }
        }
    }
}
