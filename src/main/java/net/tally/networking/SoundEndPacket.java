package net.tally.networking;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;
import net.tally.helpers.SoundSequenceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class SoundEndPacket implements FabricPacket {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebugThings.MOD_ID);

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        if (player.hasAttached(DebugThings.SOUND_SEQUENCE)) {
            SoundEndPacket read = new SoundEndPacket(buf);
            String readVal = SoundSequenceHelper.packetToString(read);
            List<String> attachment = player.getAttached(DebugThings.SOUND_SEQUENCE);
            if (Objects.equals(attachment.get(0).split("//")[0], readVal.split("//")[0])) {
                SoundSequenceHelper.removeFirst(player, DebugThings.SOUND_SEQUENCE);
                if (attachment.size() > 1) {
                    SoundSequenceHelper.play(player, attachment.get(1));
                } else {
                    player.removeAttached(DebugThings.SOUND_SEQUENCE);
                }
            }
        }
    }

    public static final PacketType<SoundEndPacket> TYPE = PacketType.create(
            Messages.SOUND_END,
            SoundEndPacket::new
    );

    public final String soundName;
    public final float volume;
    public final float pitch;

    public SoundEndPacket(String name, float volume, float pitch) {
        this.soundName = name;
        this.volume = volume;
        this.pitch = pitch;
    }

    public SoundEndPacket(SoundInstance sound) {
        this.soundName = sound.getSound().getIdentifier().toString();
        this.volume = sound.getVolume();
        this.pitch = sound.getPitch();
    }

    public SoundEndPacket(PacketByteBuf buf) {
        this(buf.readString(), buf.readFloat(), buf.readFloat());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeString(this.soundName);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
    }
}