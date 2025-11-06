package net.tally.mixin;

import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ServerWorld;
import net.tally.attributes.DebugAttributes;
import net.tally.helpers.IEntityDataSaver;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(ServerWorld.class)
public abstract class ServerWorldMixin {
    @Unique
    private final ServerWorld instanceS = (ServerWorld) (Object) this;

    @Redirect(method = "tickEntity(Lnet/minecraft/entity/Entity;)V", at = @At(value="INVOKE", target = "Lnet/minecraft/entity/Entity;tick()V"))
    public void debugthings$inject(Entity instance) {
        long value = 1;
        if (instance instanceof LivingEntity lE) {
            value = (long) lE.getAttributeValue(DebugAttributes.TICK_RATE);
        }
        if (value == 1) {
            instance.tick();
        } else if (value >= 0) {
            if (instanceS.getTime() % (value == 0 ? 1 : value) == 0) {
                instance.tick();
            }
        } else {
            for(; value < 1; value++) {
                instance.tick();
            }
        }
    }
}
