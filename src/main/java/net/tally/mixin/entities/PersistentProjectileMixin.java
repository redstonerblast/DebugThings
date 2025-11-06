package net.tally.mixin.entities;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyVariable;

@Mixin(PersistentProjectileEntity.class)
public class PersistentProjectileMixin {

    @ModifyVariable(method = "setVelocity", at = @At("HEAD"), ordinal = 1, argsOnly = true)
    private double debugthings$divergenceMod(double value) {
        @Nullable Entity entity = ((PersistentProjectileEntity) (Object) this).getOwner();
        if (entity instanceof LivingEntity && InflictionHandler.hasInfliction(Inflictions.CONFUSION, (LivingEntity) entity)) {
            return value * 4;
        }
        return value;
    }
}
