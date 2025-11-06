package net.tally.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;

public class Messages {

    public static final Identifier SOUND_END = new Identifier(DebugThings.MOD_ID, "sound_end");
    public static final Identifier SOUND_SEQ_S = new Identifier(DebugThings.MOD_ID, "sound_seq_s");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SOUND_END, SoundEndPacket::receive);
        ServerPlayNetworking.registerGlobalReceiver(SOUND_SEQ_S, SoundSeqSPacket::receive);
    }
}
