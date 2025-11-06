package net.tally.mixin;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.tally.helpers.BlockEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockEntity.class)
public class BlockEntityMixin implements BlockEntityData {
    @Unique
    private int tickRate = 1;

    @Override
    public void debugged_things_1_20_4$setTickRate(int speed) {
        tickRate = speed;
    }

    @Override
    public int debugged_things_1_20_4$getTickRate() {
        return tickRate;
    }

    @Inject(method = "writeNbt", at = @At("HEAD"))
    protected void debugthings$injectWriteMethod(NbtCompound nbt, CallbackInfo ci) {
        nbt.putInt("tickRate", tickRate == 0 ? 1 : tickRate);
    }

    @Inject(method = "readNbt", at = @At("HEAD"))
    protected void debugthings$injectReadMethod(NbtCompound nbt, CallbackInfo info) {
        if (nbt.contains("tickRate")) {
            tickRate = nbt.getInt("tickRate");
        } else {
            tickRate = 1;
        }
    }

}
