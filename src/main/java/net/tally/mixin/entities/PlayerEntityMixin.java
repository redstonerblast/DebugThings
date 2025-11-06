package net.tally.mixin.entities;

import net.fabricmc.fabric.api.mininglevel.v1.MiningLevelManager;
import net.minecraft.block.BlockState;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.MovementType;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.MiningToolItem;
import net.minecraft.server.command.CommandOutput;
import net.minecraft.util.Nameable;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tally.attributes.DebugAttributes;
import net.tally.attributes.Sign;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PlayerEntity.class)
abstract class PlayerEntityMixin extends LivingEntity implements Nameable, CommandOutput {
    @Unique
    private final PlayerEntity instanceP = (PlayerEntity) (Object) this;

    protected PlayerEntityMixin(EntityType<? extends LivingEntity> entityType, World world) {
        super(entityType, world);
    }

    @ModifyConstant(method = "getOffGroundSpeed", constant = @Constant(floatValue = 0.025999999F))
    private float debugthings$modifyFlySpeed(float constant) {
        return constant * (float) instanceP.getAttributeValue(DebugAttributes.AIR_SPEED);
    }

    @ModifyConstant(method = "getOffGroundSpeed", constant = @Constant(floatValue = 0.02F))
    private float debugthings$modifyFlySpeedB(float constant) {
        return constant * (float) instanceP.getAttributeValue(DebugAttributes.AIR_SPEED);
    }

    @Redirect(method = "canHarvest(Lnet/minecraft/block/BlockState;)Z", at = @At(value = "INVOKE", target = "Lnet/minecraft/item/ItemStack;isSuitableFor(Lnet/minecraft/block/BlockState;)Z"))
    private boolean debugthings$updateMiningLevel(ItemStack instance, BlockState state) {
        if(instance.getItem() instanceof MiningToolItem tool) {
            int toolMiningLevel = (int) Math.floor(DebugAttributes.applyAttributeModifiers(tool.getMaterial().getMiningLevel(), Sign.POSITIVE.wrap(instanceP.getAttributeInstance(DebugAttributes.TOOL_LEVEL))));
            return (toolMiningLevel >= MiningLevelManager.getRequiredMiningLevel(state)) && (tool.getMiningSpeedMultiplier(instance, state) != 1.0F);
        }
        return instance.isSuitableFor(state);
    }

    @Inject(method = "createPlayerAttributes", at = @At("RETURN"))
    private static void debugthings$injectAtCreatePlayerAttributes(
            CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir
    ) {
        cir.getReturnValue()
                .add(DebugAttributes.TOOL_LEVEL, 0d)
                .add(DebugAttributes.CRIT_CHANCE, 0.05d)
                .add(DebugAttributes.CRIT_MULT, 2.0d);
    }

    @Inject(method = "updateSwimming", at = @At("TAIL"))
    private void debugthings$updateSwimmingPower(CallbackInfo ci) {
        if (InflictionHandler.hasInfliction(Inflictions.BLUE, this)) {
            this.setSwimming(this.isSprinting() && !this.hasVehicle());
            this.touchingWater = this.isSwimming();
            if (this.isSwimming()) {
                this.fallDistance = 0.0F;
                Vec3d look = this.getRotationVector();
                move(MovementType.SELF, new Vec3d(look.x / 4, look.y / 4, look.z / 4));
            }
        }
    }

    @ModifyVariable(method = "attack(Lnet/minecraft/entity/Entity;)V", at = @At(value = "LOAD"), ordinal = 2)
    private boolean debugthings$crit(boolean b) {
        return false;
    }
}