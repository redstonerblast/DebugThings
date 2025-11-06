package net.tally.mixin;

import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.tally.DebugThings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(BlockItem.class)
public abstract class BlockItemMixin {

    @Inject(method = "place(Lnet/minecraft/item/ItemPlacementContext;)Lnet/minecraft/util/ActionResult;", at = @At("HEAD"), cancellable = true)
    private void debugthings$onBlockAttemptedPlace(ItemPlacementContext context, CallbackInfoReturnable<ActionResult> cir){
        if (context.getWorld().getWorldChunk(context.getBlockPos()).hasAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC) && context.getWorld().getWorldChunk(context.getBlockPos()).getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC).contains(context.getBlockPos())) {
            cir.setReturnValue(ActionResult.FAIL);
        }
    }

}