package net.tally.mixin.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.tag.FluidTags;
import net.minecraft.util.math.Vec3d;
import net.tally.effects.DebugEffects;
import net.tally.helpers.IEntityDataSaver;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Entity.class)
public abstract class EntityMixinData implements IEntityDataSaver {
    @Unique
    private NbtCompound persistentData;

    @Unique
    private final Entity instance = (Entity) (Object) this;

    @Override
    public NbtCompound debugged_things_1_20_4$getPersistentData() {
        if(this.persistentData == null) {
            this.persistentData = new NbtCompound();
        }
        return persistentData;
    }

    @Inject(method="move", at=@At("HEAD"), cancellable = true)
    public void debugthings$movementMod(MovementType movementType, Vec3d movement, CallbackInfo ci){
        if (instance instanceof LivingEntity livingEntity) {
            if (livingEntity.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE)) {
                ci.cancel();
            }
        }
    }

    @Inject(method = "isWet()Z", at = @At("HEAD"), cancellable = true)
    private void debugthings$preventExtinguishingFromSwimming(CallbackInfoReturnable<Boolean> cir) {
        if (instance instanceof LivingEntity livingEntity) {
            if (InflictionHandler.hasInfliction(Inflictions.BLUE, livingEntity) && livingEntity.isSwimming() && !(instance.getFluidHeight(FluidTags.WATER) > 0)) {
                cir.setReturnValue(false);
            }
        }
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void debugthings$injectWriteMethod(NbtCompound nbt, CallbackInfoReturnable info) {
        if(persistentData != null) {
            nbt.put("debugthings.red_data", persistentData);
        }
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void debugthings$injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("debugthings.red_data", 10)) {
            persistentData = nbt.getCompound("debugthings.red_data");
        }
    }

    @Inject(method="baseTick", at = @At("HEAD"))
    protected void debugthings$injectedResetPush(CallbackInfo ci) {
        NbtCompound nbt = ((IEntityDataSaver)instance).debugged_things_1_20_4$getPersistentData();

        nbt.putBoolean("mimicPushed", false);
    }
}
