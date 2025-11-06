package net.tally.helpers;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.command.argument.BlockStateArgument;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.state.property.Property;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.WorldChunk;
import net.tally.DebugThings;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.rmi.registry.Registry;
import java.util.*;
import java.util.stream.Collectors;

public class TempBlockAttachment {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebugThings.MOD_ID);

    public static void createTemp(World world, BlockPos pos, BlockState oldState, @Nullable BlockEntity oldEntity, BlockState newState, Integer time) {
        WorldChunk chunk = world.getWorldChunk(pos);
        new TempBlock(chunk, pos, oldState, oldEntity, time);
        world.removeBlockEntity(pos);
        world.setBlockState(pos, newState);
    }

    public static void updateChunk(WorldChunk chunk) {
        if (chunk.hasAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME) && chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME).size() > 0) {
            List<BlockPos> indexes = new ArrayList<>();
            for (int i = 0; i < chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME).size(); i ++) {
                int newTime = chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME).get(i) - 1;
                List<Integer> list = getList(chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME));
                list.set(i, newTime);
                chunk.setAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME, list);
                if (newTime <= 0) {
                    indexes.add(chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC).get(i));
                }
            }
            for (BlockPos index : indexes) {
                TempBlock tickB = new TempBlock(chunk, index);
                tickB.remove(chunk);
            }
        }
    }

    public static <A> void removeIndexAttachment(WorldChunk chunk, AttachmentType<List<A>> attachment, int index) {
        List<A> list = getList(chunk.getAttached(attachment));
        list.remove(index);
        chunk.setAttached(attachment, list);
    }

    public static <A> void addIndexAttachment(WorldChunk chunk, AttachmentType<List<A>> attachment, A obj) {
        List<A> list = getList(chunk.getAttached(attachment));
        list.add(obj);
        chunk.setAttached(attachment, list);
    }

    public static <A> void setIndexAttachment(WorldChunk chunk, AttachmentType<List<A>> attachment, int index, A obj) {
        List<A> list = getList(chunk.getAttached(attachment));
        list.set(index, obj);
        chunk.setAttached(attachment, list);
    }

    public static <A> List<A> getList(List list) {
        return new ArrayList<A>(list);
    }
}
