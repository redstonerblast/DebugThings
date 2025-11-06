package net.tally.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityTicker;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.WorldChunk;
import net.tally.helpers.BlockEntityData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "net.minecraft.world.chunk.WorldChunk$DirectBlockEntityTickInvoker")
public class WorldChunkMixin {
    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/entity/BlockEntityTicker;tick(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;Lnet/minecraft/block/entity/BlockEntity;)V"))
    public<T extends BlockEntity> void debugthings$tickRateMod(BlockEntityTicker<T> instance, World world, BlockPos blockPos, BlockState blockState, T blockEntity) {
        long tick = ((BlockEntityData) blockEntity).debugged_things_1_20_4$getTickRate();
        if (tick >= 0) {
            if (world.getTime() % (tick == 0 ? 1 : tick) == 0) {
                instance.tick(world, blockPos, blockState, blockEntity);
            }
        } else {
            for(; tick < 1; tick++) {
                instance.tick(world, blockPos, blockState, blockEntity);
            }
        }
    }
}
