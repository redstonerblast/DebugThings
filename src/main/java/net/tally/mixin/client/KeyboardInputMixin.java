package net.tally.mixin.client;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.input.KeyboardInput;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(KeyboardInput.class)
public class KeyboardInputMixin {

    @Inject(method = "getMovementMultiplier(ZZ)F", at = @At(value = "RETURN", ordinal = 1), cancellable = true)
    private static void debugthings$flipDirection(boolean positive, boolean negative, CallbackInfoReturnable<Float> cir) {
        if (InflictionHandler.hasInfliction(Inflictions.CONFUSION, MinecraftClient.getInstance().player)) {
            cir.setReturnValue(negative ? 1.0F : -1.0F);
        }
    }
}
