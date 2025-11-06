package net.tally.mixin;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.tally.DebugThings;
import net.tally.effects.DebugEffects;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Block.class)
public abstract class BlockMixin {
    @Unique
    private final Block instance = (Block) (Object) this;

    @Inject(method = "onLandedUpon(Lnet/minecraft/world/World;Lnet/minecraft/block/BlockState;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/entity/Entity;F)V", at = @At("HEAD"), cancellable = true)
    private void debugthings$onLandInterject(World world, BlockState state, BlockPos pos, Entity entity, float fallDistance, CallbackInfo ci){
        if (entity.isLiving() && (((LivingEntity) entity).hasStatusEffect(DebugEffects.RUBBER) || InflictionHandler.hasInfliction(Inflictions.RUBBER, (LivingEntity) entity))) {
            if (!entity.bypassesLandingEffects()) {
                entity.handleFallDamage(fallDistance, 0.0F, world.getDamageSources().fall());
                ci.cancel();
            }
        }
    }

    @Inject(method = "onEntityLand(Lnet/minecraft/world/BlockView;Lnet/minecraft/entity/Entity;)V", at = @At("HEAD"), cancellable = true)
    private void debugthings$onEntityLandInterject(BlockView world, Entity entity, CallbackInfo ci){
        if (entity.isLiving() && (((LivingEntity) entity).hasStatusEffect(DebugEffects.RUBBER) || InflictionHandler.hasInfliction(Inflictions.RUBBER, (LivingEntity) entity))) {
            if (!entity.bypassesLandingEffects()) {
                Vec3d vec3d = entity.getVelocity();
                if (vec3d.y < 0.0) {
                    double d = entity instanceof LivingEntity ? 1.0 : 0.8;
                    entity.setVelocity(vec3d.x, -vec3d.y * d, vec3d.z);
                }
                ci.cancel();
            }
        }
    }
}
