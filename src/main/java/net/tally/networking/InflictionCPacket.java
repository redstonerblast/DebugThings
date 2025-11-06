package net.tally.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.client.sound.PositionedSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.tally.DebugThingsClient;

public class InflictionCPacket implements FabricPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        DebugThingsClient.setInfliction(buf.readNbt());
    }

    public static final PacketType<InflictionCPacket> TYPE = PacketType.create(
            ClientMessages.INFLICTIONSC,
            InflictionCPacket::new
    );

    public final NbtCompound inflictions;

    public InflictionCPacket(NbtCompound nbtCompound) {
        this.inflictions = nbtCompound;
    }

    public InflictionCPacket(PacketByteBuf buf) {
        this(buf.readNbt());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeNbt(this.inflictions);
    }
}