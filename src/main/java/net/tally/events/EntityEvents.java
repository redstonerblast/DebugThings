package net.tally.events;

import net.fabricmc.fabric.api.entity.event.v1.ServerLivingEntityEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.function.CommandFunction;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;
import net.tally.effects.DebugEffects;
import net.tally.events.callbacks.EntityDamageEvent;
import net.tally.events.callbacks.EntityDamageModifyEvent;
import net.tally.events.callbacks.EntityGainEffectEvent;
import net.tally.helpers.FunctionTags;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.InflictionInstance;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;

public class EntityEvents {
    private static final Identifier ENTITY_DEATH_TAG_ID = new Identifier("debugthings", "entity_die");

    public static void initialize() {
        ServerLivingEntityEvents.ALLOW_DAMAGE.register(EntityEvents::allowDamage);
        EntityDamageEvent.EVENT.register(EntityEvents::onDamage);
        EntityDamageModifyEvent.EVENT.register(EntityEvents::modDamage);
        EntityGainEffectEvent.EVENT.register(EntityEvents::gainEffect);
        ServerLivingEntityEvents.ALLOW_DEATH.register(EntityEvents::allowDeath);
    }

    private static boolean allowDeath(LivingEntity livingEntity, DamageSource damageSource, float v) {
        if (!livingEntity.getWorld().isClient) {
            Collection<CommandFunction<ServerCommandSource>> collection = livingEntity.getServer().getCommandFunctionManager().getTag(ENTITY_DEATH_TAG_ID);
            FunctionTags.executeAllEntity(livingEntity.getServer(), collection, ENTITY_DEATH_TAG_ID, livingEntity);
        }
        return true;
    }

    private static float modDamage(LivingEntity livingEntity, DamageSource damageSource, float amount) {
        if (damageSource.isIn(DebugThings.HOLY)) {
            amount *= (Math.min(InflictionHandler.getInflictions(Inflictions.HOLY, livingEntity), 30) * 0.5f) + 1.0f;
        }
        return amount;
    }

    private static ActionResult gainEffect(StatusEffectInstance effectInstance, LivingEntity livingEntity, @Nullable Entity entity) {
        if (InflictionHandler.hasInfliction(Inflictions.PURPLE, livingEntity)) {
            if (!(effectInstance.getEffectType().getRegistryEntry().isIn(DebugEffects.PERSISTENT_EFFECTS))) {
                return ActionResult.FAIL;
            }
        }
        return ActionResult.PASS;
    }

    private static void onDamage(LivingEntity entity, DamageSource damageSource, float v) {
        if (v > 0.1) {
            if (damageSource.getAttacker() instanceof LivingEntity livingEntity) {
                if (InflictionHandler.hasInfliction(Inflictions.RED, livingEntity)) {
                    livingEntity.heal(InflictionHandler.getInflictions(Inflictions.RED, livingEntity));
                }
                if (!entity.getWorld().isClient) {
                    if (InflictionHandler.hasInfliction(Inflictions.RABID, livingEntity)) {
                        InflictionHandler.addInfliction(entity, new InflictionInstance(Inflictions.RABID, 100, 1, "rabid_infection", 0), "highest", "replace", (ServerWorld) entity.getWorld());
                    }
                }
            }
            if (damageSource.getSource() instanceof LivingEntity livingEntity) {
                if (InflictionHandler.hasInfliction(Inflictions.RED, livingEntity)) {
                    livingEntity.heal(InflictionHandler.getInflictions(Inflictions.RED, livingEntity));
                }
                if (!entity.getWorld().isClient) {
                    if (InflictionHandler.hasInfliction(Inflictions.RABID, livingEntity)) {
                        InflictionHandler.addInfliction(entity, new InflictionInstance(Inflictions.RABID, 100, 1, "rabid_infection", 0), "highest", "replace", (ServerWorld) entity.getWorld());
                    }
                }
            }
        }
    }

    public static boolean allowDamage(LivingEntity livingEntity, DamageSource source, float amount) {
        if (livingEntity.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE) || InflictionHandler.hasInfliction(Inflictions.PINK, livingEntity)) {
            return false;
        }
        if (source.getAttacker() != null && source.getAttacker() instanceof LivingEntity from) {
            return !from.hasStatusEffect(DebugEffects.TEMPORAL_FREEZE);
        }
        return true;
    }
}
