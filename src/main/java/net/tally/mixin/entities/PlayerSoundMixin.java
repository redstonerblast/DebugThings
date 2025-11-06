package net.tally.mixin.entities;

import com.google.common.collect.Multimap;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.client.sound.SoundLoader;
import net.minecraft.client.sound.SoundSystem;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.tally.networking.Messages;
import net.tally.networking.SoundEndPacket;
import org.slf4j.Logger;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(SoundSystem.class)
public abstract class PlayerSoundMixin {

    @Shadow @Final private static Logger LOGGER;

    @Final
    @Shadow private SoundLoader soundLoader;

    @Redirect(method = "tick()V", at = @At(value = "INVOKE", target="Lcom/google/common/collect/Multimap;remove(Ljava/lang/Object;Ljava/lang/Object;)Z"))
    private boolean debugthings$inject(Multimap instance, Object o, Object od) {
        if (((SoundInstance) od).getCategory() == SoundCategory.MASTER) {
            PacketByteBuf buf = PacketByteBufs.create();
            new SoundEndPacket((SoundInstance) od).write(buf);
            ClientPlayNetworking.send(Messages.SOUND_END, buf);
        }
        return instance.remove(o, od);
    }
}
