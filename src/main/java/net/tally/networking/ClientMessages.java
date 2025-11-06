package net.tally.networking;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;

public class ClientMessages {
    public static final Identifier SOUND_SEQ_C = new Identifier(DebugThings.MOD_ID, "sound_seq_c");
    public static final Identifier INFLICTIONSC = new Identifier(DebugThings.MOD_ID, "inflictions_c");
    public static final Identifier INFLICTIONSRENDER = new Identifier(DebugThings.MOD_ID, "inflictions_render");

    public static void registerS2CPackets() {
        ClientPlayNetworking.registerGlobalReceiver(SOUND_SEQ_C, SoundSeqCPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(INFLICTIONSC, InflictionCPacket::receive);
        ClientPlayNetworking.registerGlobalReceiver(INFLICTIONSRENDER, InflictionUpdateEntityC::receive);
    }
}
