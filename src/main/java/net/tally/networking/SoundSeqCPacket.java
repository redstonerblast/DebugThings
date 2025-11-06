package net.tally.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.Sound;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

public class SoundSeqCPacket implements FabricPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        Random l = Random.create(0);
        Identifier id = buf.readIdentifier();
        float volume = buf.readFloat();
        float pitch = buf.readFloat();
        boolean play = buf.readBoolean();
        int index = buf.readInt();
        Vec3d vec3d = client.player.getPos();
        PositionedSoundInstance positionedSoundInstance = new PositionedSoundInstance(id, SoundCategory.MASTER, volume, pitch, l, false, 0, SoundInstance.AttenuationType.NONE, vec3d.getX(), vec3d.getY(), vec3d.getZ(), false);
        if (play) {
            client.getSoundManager().play(positionedSoundInstance);
            ClientPlayNetworking.send(new SoundSeqSPacket(positionedSoundInstance.getSound().getIdentifier(), volume, pitch, index));
        } else {
            ClientPlayNetworking.send(new SoundSeqSPacket(id, volume, pitch, index));
        }
    }

    public static final PacketType<SoundSeqCPacket> TYPE = PacketType.create(
            ClientMessages.SOUND_SEQ_C,
            SoundSeqCPacket::new
    );

    public final Identifier soundName;
    public final float volume;
    public final float pitch;
    public final boolean play;
    public final int index;

    public SoundSeqCPacket(Identifier name, float volume, float pitch, boolean play, int index) {
        this.soundName = name;
        this.volume = volume;
        this.pitch = pitch;
        this.play = play;
        this.index = index;
    }

    public SoundSeqCPacket(PacketByteBuf buf) {
        this(buf.readIdentifier(), buf.readFloat(), buf.readFloat(), buf.readBoolean(),buf.readInt());
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
        buf.writeBoolean(this.play);
        buf.writeInt(this.index);
    }
}