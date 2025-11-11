package net.tally.events;

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.minecraft.entity.LivingEntity;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.world.chunk.WorldChunk;
import net.tally.helpers.TempBlockAttachment;
import net.tally.inflictions.InflictionHandler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldEvents {
    public static List<WorldChunk> loadedChunks = new ArrayList<>();

    public static void initialize() {

        ServerTickEvents.START_WORLD_TICK.register(WorldEvents::startWorldTick);
        ServerChunkEvents.CHUNK_LOAD.register(WorldEvents::chunkLoad);
        ServerChunkEvents.CHUNK_UNLOAD.register(WorldEvents::chunkUnload);
    }

    public static void startWorldTick(ServerWorld s) {
        List<WorldChunk> processChunks = new ArrayList<>(loadedChunks);
        for (WorldChunk chunk : processChunks) {
            if (s == chunk.getWorld()) {
                if (chunk != null && chunk.getLevelType().isAfter(ChunkLevelType.BLOCK_TICKING) && s.isChunkLoaded(chunk.getPos().toLong())) {
                    TempBlockAttachment.updateChunk(chunk);
                }
            }
        }

        long time = s.getTime();

        if (!s.isClient()) {
            s.iterateEntities().forEach(entity -> {
                if (entity instanceof LivingEntity livingEntity) {
                    InflictionHandler.processInflictions(livingEntity, time, s);
                }
            });
        }
    }

    public static void chunkLoad(ServerWorld world, WorldChunk chunk) {
        loadedChunks.removeAll(Collections.singleton(chunk));
        loadedChunks.add(chunk);
    }

    public static void chunkUnload(ServerWorld world, WorldChunk chunk) {
        loadedChunks.removeAll(Collections.singleton(chunk));
    }
}
