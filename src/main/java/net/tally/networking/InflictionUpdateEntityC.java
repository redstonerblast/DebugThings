package net.tally.networking;

import net.fabricmc.fabric.api.networking.v1.FabricPacket;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.fabricmc.fabric.api.networking.v1.PacketType;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.PacketByteBuf;
import net.tally.DebugThings;
import net.tally.DebugThingsClient;
import net.tally.helpers.IEntityDataSaver;

public class InflictionUpdateEntityC implements FabricPacket {

    public static void receive(MinecraftClient client, ClientPlayNetworkHandler handler, PacketByteBuf buf, PacketSender responseSender) {
        NbtCompound update = buf.readNbt();
        int id = buf.readInt();
        Entity entity = client.world.getEntityById(id);
        NbtCompound eData = ((IEntityDataSaver)entity).debugged_things_1_20_4$getPersistentData();

        DebugThings.LOGGER.info("data - {}", entity);

        eData.put("inflictions", update);
    }

    public static final PacketType<InflictionUpdateEntityC> TYPE = PacketType.create(
            ClientMessages.INFLICTIONSRENDER,
            InflictionUpdateEntityC::new
    );

    private final NbtCompound inflictions;
    private final int id;

    public InflictionUpdateEntityC(NbtCompound nbtCompound, int id) {
        this.inflictions = nbtCompound;
        this.id = id;
    }

    public InflictionUpdateEntityC(PacketByteBuf buf) {
        this(buf.readNbt(), buf.readInt());
    }

    @Override
    public PacketType<?> getType() {
        return TYPE;
    }

    @Override
    public void write(PacketByteBuf buf) {
        buf.writeNbt(this.inflictions);
        buf.writeInt(this.id);
    }
}