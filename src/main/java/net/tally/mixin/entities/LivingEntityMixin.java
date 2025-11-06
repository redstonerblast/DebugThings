package net.tally.mixin.entities;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.attribute.DefaultAttributeContainer;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.damage.DamageTypes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.ActionResult;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.tally.DebugThings;
import net.tally.attributes.DebugAttributes;
import net.tally.components.DebugThingsComponents;
import net.tally.effects.DebugEffects;
import net.tally.events.callbacks.EntityDamageEvent;
import net.tally.events.callbacks.EntityDamageModifyEvent;
import net.tally.events.callbacks.EntityGainEffectEvent;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.*;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> type, World world) {
        super(type, world);
    }

    @Unique
    private final LivingEntity instance = (LivingEntity) (Object) this;

    @Inject(method="tickMovement", at = @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;getFrozenTicks()I"))
    public void debugthings$tickInject(CallbackInfo ci) {
        if (InflictionHandler.hasInfliction(Inflictions.FROST, instance) || InflictionHandler.hasInfliction(Inflictions.FROSTBURN, instance)) {
            instance.inPowderSnow = true;
        }
    }

    @Inject(method = "applyFluidMovingSpeed(DZLnet/minecraft/util/math/Vec3d;)Lnet/minecraft/util/math/Vec3d;", at = @At("RETURN"), cancellable = true)
    public void debugthings$blueMove(double gravity, boolean falling, Vec3d motion, CallbackInfoReturnable<Vec3d> cir) {
        if(InflictionHandler.hasInfliction(Inflictions.BLUE, instance)) {
            if (Math.abs(motion.y - gravity / 16.0D) < 0.025D) {
                cir.setReturnValue(new Vec3d(motion.x, 0, motion.z));
            }
        }
    }

    @Redirect(method="travel", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;getSlipperiness()F"))
    private float debugthings$slippy(Block block) {
        if(InflictionHandler.hasInfliction(Inflictions.LIGHT_BLUE, instance)) {
            return 0.98f;
        }
        return block.getSlipperiness();
    }

    @Inject(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), cancellable = true)
    private void debugthings$onAddEffect(StatusEffectInstance effect, Entity source, CallbackInfoReturnable<Boolean> cir) {
        ActionResult result = EntityGainEffectEvent.EVENT.invoker().interact(effect, instance, source);

        if(result == ActionResult.FAIL) {
            cir.setReturnValue(false);
        }
    }

    @ModifyVariable(method = "addStatusEffect(Lnet/minecraft/entity/effect/StatusEffectInstance;Lnet/minecraft/entity/Entity;)Z", at = @At("HEAD"), argsOnly = true)
    private StatusEffectInstance debugthings$statusEffect(StatusEffectInstance effect) {
        if (effect.getDuration() == -1) {
            return effect;
        }
        if (effect.getEffectType().isBeneficial()) {
            return new StatusEffectInstance(effect.getEffectType(), (int) (effect.getDuration() * instance.getAttributeValue(DebugAttributes.POSITIVE_POT_DUR)), effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon());
        } else if (effect.getEffectType().getCategory() == StatusEffectCategory.HARMFUL) {
            return new StatusEffectInstance(effect.getEffectType(), (int) (effect.getDuration() * instance.getAttributeValue(DebugAttributes.NEGATIVE_POT_DUR)), effect.getAmplifier(), effect.isAmbient(), effect.shouldShowParticles(), effect.shouldShowIcon());
        }
        return effect;
    }

    @ModifyVariable(at = @At(value = "INVOKE", target = "Lnet/minecraft/world/World;getFluidState(Lnet/minecraft/util/math/BlockPos;)Lnet/minecraft/fluid/FluidState;"), method = "travel", name = "d", ordinal = 0)
    public double debugthings$modifyFallingVelocity(double in) {
        if(this.getVelocity().y > 0D) {
            return in;
        }
        if(InflictionHandler.hasInfliction(Inflictions.LIME, instance)) {
            this.fallDistance = 0;
            return 0.01;
        }
        return in;
    }

    @Inject(method = "heal(F)V", at = @At("HEAD"), cancellable = true)
    private void debugthings$onHeal(float amount, CallbackInfo ci){
        if(instance.hasStatusEffect(DebugEffects.WOUNDED) || instance.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE) || InflictionHandler.hasInfliction(Inflictions.GREEN, instance)) {
            ci.cancel();
        }
    }

    @Inject(method = "createLivingAttributes()Lnet/minecraft/entity/attribute/DefaultAttributeContainer$Builder;", at = @At("RETURN"))
    private static void debugthings$injectAtCreateLivingAttributes(
            CallbackInfoReturnable<DefaultAttributeContainer.Builder> cir
    ) {
        cir.getReturnValue()
                .add(DebugAttributes.DODGE_CHANCE)
                .add(DebugAttributes.PARRY_CHANCE)
                .add(DebugAttributes.TICK_RATE, 1.0)
                .add(DebugAttributes.NEGATIVE_POT_DUR, 1.0)
                .add(DebugAttributes.POSITIVE_POT_DUR, 1.0)
                .add(DebugAttributes.INVUL_ATTACK, 1.0)
                .add(DebugAttributes.INVUL_SELF, 1.0)
                .add(DebugAttributes.AIR_SPEED, 1.0);
    }

    @ModifyConstant(method = "getOffGroundSpeed", constant = @Constant(floatValue = 0.02F))
    private float debugthings$modifyFlySpeed(float constant) {
        return constant * (float) instance.getAttributeValue(DebugAttributes.AIR_SPEED);
    }

    @ModifyConstant(method = "getOffGroundSpeed", constant = @Constant(floatValue = 0.1F))
    private float debugthings$modifyFlySpeedb(float constant) {
        return constant * (float) instance.getAttributeValue(DebugAttributes.AIR_SPEED);
    }

    @Redirect(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at= @At(value = "FIELD", target = "Lnet/minecraft/entity/LivingEntity;timeUntilRegen:I", opcode = Opcodes.PUTFIELD))
    public void debugthings$modifyInvul(LivingEntity instance, int value, @Local(argsOnly = true) DamageSource source) {
        if (!source.isIn(DebugThings.NO_INVUL)) {
            instance.timeUntilRegen = (int) (instance.getAttributeValue(DebugAttributes.INVUL_SELF) * 10d);
            if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity from) {
                instance.timeUntilRegen = (int) (from.getAttributeValue(DebugAttributes.INVUL_ATTACK) * instance.timeUntilRegen);
            }
            instance.timeUntilRegen += 10;
        }
    }

    @Inject(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at= @At(value = "INVOKE", target = "Lnet/minecraft/entity/LivingEntity;applyDamage(Lnet/minecraft/entity/damage/DamageSource;F)V", ordinal = 1), cancellable = true)
    public void debugthings$onDmg(DamageSource source, float amount, CallbackInfoReturnable<Boolean> cir) {
        if (source.getAttacker() != null) {
            float luckMult = instance instanceof PlayerEntity player ? 1.0F + (player.getLuck() / 10.0f) : 1.0F;
            if (source.isIn(DebugThings.MELEE) && instance.getMainHandStack().isIn(DebugThings.PARRY) && instance.getRandom().nextFloat() <= instance.getAttributeValue(DebugAttributes.PARRY_CHANCE) * luckMult) {
                instance.getWorld().playSound(null, instance.getBlockPos(), SoundEvents.BLOCK_ANVIL_PLACE, SoundCategory.NEUTRAL, 0.3F, 1.8F + instance.getWorld().random.nextFloat() * 0.2F);
                source.getAttacker().damage(new DamageSource(instance.getWorld().getRegistryManager().get(RegistryKeys.DAMAGE_TYPE).entryOf(DamageTypes.THORNS)),amount * 0.5f);
                cir.setReturnValue(false);
            }
            if (instance.getRandom().nextFloat() <= instance.getAttributeValue(DebugAttributes.DODGE_CHANCE) * luckMult) {
                instance.getWorld().playSound(null, instance.getBlockPos(), SoundEvents.ENTITY_BAT_TAKEOFF, SoundCategory.NEUTRAL, 0.3F, 1.2F + instance.getWorld().random.nextFloat() * 0.4F);
                cir.setReturnValue(false);
            }
        }

        EntityDamageEvent.EVENT.invoker().interact((LivingEntity) (Object) this, source, amount);
    }

    @Inject(method = "hasStatusEffect", at = @At("HEAD"), cancellable = true)
    public void debugthings$hasStatusEffect(StatusEffect effect, CallbackInfoReturnable<Boolean> cir) {
        if(effect == StatusEffects.BLINDNESS && InflictionHandler.hasInfliction(Inflictions.SHADE, instance) && !instance.hasStatusEffect(StatusEffects.NIGHT_VISION) && !DebugThingsComponents.IMMUNITIES.get(instance).hasValue("shade_darkness")) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method="getStatusEffect", at=@At("HEAD"), cancellable = true)
    public void debugthings$getEffect(StatusEffect effect, CallbackInfoReturnable<StatusEffectInstance> cir) {
        if(effect == StatusEffects.BLINDNESS && InflictionHandler.hasInfliction(Inflictions.SHADE, instance) && !instance.hasStatusEffect(StatusEffects.NIGHT_VISION) && !DebugThingsComponents.IMMUNITIES.get(instance).hasValue("shade_darkness")) {
            cir.setReturnValue(new StatusEffectInstance(StatusEffects.BLINDNESS, 200));
        }
    }

    @ModifyVariable(method = "damage(Lnet/minecraft/entity/damage/DamageSource;F)Z", at = @At("HEAD"), ordinal = 0, argsOnly = true)
    private float debugthings$injected(float amount, DamageSource source) {
        amount = EntityDamageModifyEvent.EVENT.invoker().interact((LivingEntity) (Object) this, source, amount);

        if(source.getAttacker() != null && source.isIn(DebugThings.CRITICAL) && source.getAttacker() instanceof PlayerEntity player) {
            float luckMult = 1.0F + ((float) (player.getLuck() / 10.0f));
            double crit = player.getAttributeValue(DebugAttributes.CRIT_CHANCE);
            double critMult = player.getAttributeValue(DebugAttributes.CRIT_MULT);
            if (crit >= 1d) {
                float newAmount = amount;
                for(; crit >= 1d; crit -= 1d) {
                    newAmount *= (float) critMult;
                }
                if (instance.getRandom().nextFloat() <= crit * luckMult) {
                    player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.BLOCK_BONE_BLOCK_BREAK, SoundCategory.NEUTRAL, 0.3F, 0.9F + player.getWorld().random.nextFloat() * 0.2F);
                    newAmount *= (float) critMult;
                }
                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.NEUTRAL, 0.3F, 0.9F + player.getWorld().random.nextFloat() * 0.2F);
                return newAmount;
            }
            if (instance.getRandom().nextFloat() <= crit * luckMult) {
                player.getWorld().playSound(null, player.getBlockPos(), SoundEvents.ENTITY_PLAYER_ATTACK_CRIT, SoundCategory.NEUTRAL, 0.3F, 0.9F + player.getWorld().random.nextFloat() * 0.2F);
                return (float) (amount * critMult);
            }
            return amount;
        }
        return amount;
    }
}