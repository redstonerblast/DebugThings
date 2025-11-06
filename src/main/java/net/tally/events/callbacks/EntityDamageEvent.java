package net.tally.events.callbacks;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;

public interface EntityDamageEvent {

    Event<EntityDamageEvent> EVENT = EventFactory.createArrayBacked(EntityDamageEvent.class,
            (listeners) -> (victim, source, amount) -> {
                for (EntityDamageEvent listener : listeners) {
                    listener.interact(victim, source, amount);
                }

                return ;
            });

    void interact(LivingEntity victim, DamageSource damageSource, float amount);
}