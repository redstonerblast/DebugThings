package net.tally.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.util.ActionResult;

public interface EntityDamageModifyEvent {

    Event<EntityDamageModifyEvent> EVENT = EventFactory.createArrayBacked(EntityDamageModifyEvent.class,
            (listeners) -> (victim, source, amount) -> {
                for (EntityDamageModifyEvent listener : listeners) {
                    float result = listener.interact(victim, source, amount);

                    if(result != amount) {
                        return result;
                    }
                }

                return amount;
            });

    float interact(LivingEntity victim, DamageSource damageSource, float amount);
}