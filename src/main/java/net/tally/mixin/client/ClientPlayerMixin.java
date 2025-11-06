package net.tally.mixin.client;

import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerAbilities;
import net.tally.attributes.DebugAttributes;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerMixin {
    @Unique
    private final ClientPlayerEntity instance = (ClientPlayerEntity) (Object) this;

    @Inject(method= "isSubmergedInWater", at = @At("HEAD"), cancellable = true)
    public void debugthings$isSubmergedInWater(CallbackInfoReturnable<Boolean> cir) {
        if (InflictionHandler.hasInfliction(Inflictions.BLUE, instance)) {
            cir.setReturnValue(true);
        }
    }
}
