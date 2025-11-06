package net.tally.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.util.ActionResult;
import org.jetbrains.annotations.Nullable;

public interface EntityGainEffectEvent {
    Event<EntityGainEffectEvent> EVENT = EventFactory.createArrayBacked(EntityGainEffectEvent.class,
            (listeners) -> (effectInstance, entity, source) -> {
                for (EntityGainEffectEvent listener : listeners) {
                    ActionResult result = listener.interact(effectInstance, entity, source);

                    if(result != ActionResult.PASS) {
                        return result;
                    }
                }

                return ActionResult.PASS;
            });

    ActionResult interact(StatusEffectInstance effect, LivingEntity entity, @Nullable Entity source);
}
