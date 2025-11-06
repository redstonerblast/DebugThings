package net.tally.events;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import net.tally.helpers.TempBlockAttachment;
import net.tally.inflictions.InflictionHandler;

import java.util.ArrayList;
import java.util.List;

public class ClientWorldEvents {
    public static void initialize() {
        ClientTickEvents.START_WORLD_TICK.register(ClientWorldEvents::startWorldTick);
    }

    public static void startWorldTick(ClientWorld c) {
    }
}
