package net.tally.blocks;

import net.minecraft.block.BlockState;
import net.minecraft.fluid.Fluid;
import net.minecraft.fluid.FluidState;
import net.minecraft.item.Item;
import net.minecraft.item.Items;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tally.DebugThings;
import net.tally.GeneralFluid;

public abstract class SolidWater extends GeneralFluid {
    @Override
    public Fluid getStill() {
        return DebugThings.STILL_SOLID_WATER;
    }

    @Override
    public Fluid getFlowing() {
        return DebugThings.FLOWING_SOLID_WATER;
    }

    @Override
    public Item getBucketItem() {
        return Items.WATER_BUCKET;
    }

    @Override
    protected BlockState toBlockState(FluidState fluidState) {
        return DebugThings.SOLID_WATER.getDefaultState().with(Properties.LEVEL_15, getBlockStateLevel(fluidState));
    }

    @Override
    public void onScheduledTick(World world, BlockPos pos, FluidState state) {
    }

    public static class Flowing extends SolidWater {
        @Override
        protected void appendProperties(StateManager.Builder<Fluid, FluidState> builder) {
            super.appendProperties(builder);
            builder.add(LEVEL);
        }

        @Override
        public int getLevel(FluidState fluidState) {
            return fluidState.get(LEVEL);
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return false;
        }
    }

    public static class Still extends SolidWater {
        @Override
        public int getLevel(FluidState fluidState) {
            return 8;
        }

        @Override
        public boolean isStill(FluidState fluidState) {
            return true;
        }
    }
}
