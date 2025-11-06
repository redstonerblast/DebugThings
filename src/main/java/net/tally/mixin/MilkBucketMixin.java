package net.tally.mixin;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.MilkBucketItem;
import net.tally.DebugThings;
import net.tally.effects.DebugEffects;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.Collection;
import java.util.Iterator;

@Mixin(MilkBucketItem.class)
public abstract class MilkBucketMixin {
    @Redirect(method = "finishUsing(Lnet/minecraft/item/ItemStack;Lnet/minecraft/world/World;Lnet/minecraft/entity/LivingEntity;)Lnet/minecraft/item/ItemStack;",at=@At(value = "INVOKE", target="Lnet/minecraft/entity/LivingEntity;clearStatusEffects()Z"))
    public boolean debugthings$clearStatusEffects(LivingEntity instance) {
        Collection<StatusEffectInstance> effects = instance.getStatusEffects();
        if (instance instanceof PlayerEntity) {
            for (StatusEffectInstance effect : effects) {
                if (!(effect.getEffectType().getRegistryEntry().isIn(DebugEffects.PERSISTENT_EFFECTS))) {
                    instance.removeStatusEffect(effect.getEffectType());
                }
            }
        } else {
            instance.clearStatusEffects();
        }
        return true;
    }
}