package net.tally.helpers;

import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.packet.s2c.play.PlaySoundS2CPacket;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.chunk.WorldChunk;
import net.tally.networking.ClientMessages;
import net.tally.networking.SoundEndPacket;

import java.util.ArrayList;
import java.util.List;

public class SoundSequenceHelper {
    public static <A> void addIndexAttachment(ServerPlayerEntity player, AttachmentType<List<A>> attachment, A obj) {
        List<A> list = getList(player.getAttached(attachment));
        list.add(obj);
        player.setAttached(attachment, list);
    }

    public static <A> void setIndexAttachment(ServerPlayerEntity player, AttachmentType<List<A>> attachment, A obj, int index) {
        List<A> list = getList(player.getAttached(attachment));
        list.set(index, obj);
        player.setAttached(attachment, list);
    }

    public static <A> List<A> getList(List list) {
        return new ArrayList<A>(list);
    }

    public static <A> void removeFirst(ServerPlayerEntity player, AttachmentType<List<A>> attachment) {
        List<A> list = getList(player.getAttached(attachment));
        list.remove(0);
        player.setAttached(attachment, list);
    }

    public static String packetToString(SoundEndPacket packet) {
        return packet.soundName + "//" + packet.volume + "//" + packet.pitch;
    }

    public static void play(ServerPlayerEntity player, String encodedSound) {
        String[] sound = encodedSound.split("//");
        float volume = Float.parseFloat(sound[1]);
        float pitch = Float.parseFloat(sound[2]);
        PacketByteBuf buf = PacketByteBufs.create();
        buf.writeIdentifier(Identifier.tryParse(sound[0])).writeFloat(volume).writeFloat(pitch).writeBoolean(true).writeInt(0);
        ServerPlayNetworking.send(player, ClientMessages.SOUND_SEQ_C, buf);
    }
}
