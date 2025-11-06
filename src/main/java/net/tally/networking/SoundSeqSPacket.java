package net.tally.networking;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.Sound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.tally.DebugThings;
import net.tally.helpers.SoundSequenceHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class SoundSeqSPacket implements FabricPacket {

    public static void receive(MinecraftServer server, ServerPlayerEntity player, ServerPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Identifier id = buf.readIdentifier();
        String soundString = id + "//" + buf.readFloat() + "//" + buf.readFloat();
        if (player.hasAttached(DebugThings.SOUND_SEQUENCE)) {
            SoundSequenceHelper.setIndexAttachment(player, DebugThings.SOUND_SEQUENCE, soundString, buf.readInt());
        } else {
            player.setAttached(DebugThings.SOUND_SEQUENCE, List.of(soundString));
        }
    }

    public static final PacketType<SoundSeqSPacket> TYPE = PacketType.create(
            Messages.SOUND_SEQ_S,
            SoundSeqSPacket::new
    );

    public final Identifier soundName;
    public final float volume;
    public final float pitch;
    public final int index;

    public SoundSeqSPacket(Identifier name, float volume, float pitch, int index) {
        this.soundName = name;
        this.volume = volume;
        this.pitch = pitch;
        this.index = index;
    }

    public SoundSeqSPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readFloat(), buf.readFloat(), buf.readInt());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeIdentifier(this.soundName);
        buf.writeFloat(this.volume);
        buf.writeFloat(this.pitch);
        buf.writeInt(this.index);
    }
}
