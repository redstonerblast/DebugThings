package net.tally.helpers;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.tally.DebugThings;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;

public class TempBlock {
    public static final Logger LOGGER = LoggerFactory.getLogger(DebugThings.MOD_ID);
    private int time;
    private BlockState blockState;
    private @Nullable NbtCompound blockNBT;
    private BlockPos location;

    public TempBlock(WorldChunk chunk, BlockPos pos) {
        int index = chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC).indexOf(pos);
        this.location = pos;
        this.time = chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME).get(index);
        this.blockState = chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_STATE).get(index);
        this.blockNBT = chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_NBT).get(index);
    }

    public TempBlock(WorldChunk chunk, BlockPos pos, BlockState state, @Nullable BlockEntity nbt, int time) {
        if (chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC) != null) {
            if (chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC).contains(pos)) {
                TempBlockAttachment.setIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_TIME, chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC).indexOf(pos), time);
            } else {
                TempBlockAttachment.addIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_LOC, pos);
                TempBlockAttachment.addIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_TIME, time);
                TempBlockAttachment.addIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_STATE, state);
                TempBlockAttachment.addIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_NBT, nbt == null ? new NbtCompound() : nbt.createNbtWithId());
            }
        } else {
            chunk.setAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC, Arrays.asList(pos));
            chunk.setAttached(DebugThings.PERSISTENT_TEMP_BLOCK_TIME, Arrays.asList(time));
            chunk.setAttached(DebugThings.PERSISTENT_TEMP_BLOCK_NBT, Arrays.asList(nbt == null ? new NbtCompound() : nbt.createNbtWithId()));
            chunk.setAttached(DebugThings.PERSISTENT_TEMP_BLOCK_STATE, Arrays.asList(state));
        }
    }

    public void remove(WorldChunk chunk) {
        if (this.blockState.hasBlockEntity()) {
            chunk.getWorld().setBlockState(this.location, this.blockState);
            BlockEntity be = BlockEntity.createFromNbt(this.location, this.blockState, this.blockNBT);
            chunk.setBlockEntity(be);
        } else {
            chunk.getWorld().setBlockState(this.location, this.blockState);
        }
        int index = chunk.getAttached(DebugThings.PERSISTENT_TEMP_BLOCK_LOC).indexOf(this.location);
        TempBlockAttachment.removeIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_LOC, index);
        TempBlockAttachment.removeIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_NBT, index);
        TempBlockAttachment.removeIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_STATE, index);
        TempBlockAttachment.removeIndexAttachment(chunk, DebugThings.PERSISTENT_TEMP_BLOCK_TIME, index);
    }
}
