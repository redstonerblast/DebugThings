package net.tally;

import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.minecraft.client.color.world.BiomeColors;
import net.minecraft.fluid.FluidState;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockRenderView;
import org.jetbrains.annotations.Nullable;

public class WaterLikeFluidRenderHandler extends SimpleFluidRenderHandler {
    public WaterLikeFluidRenderHandler(Identifier stillTexture, Identifier flowingTexture, Identifier overlayTexture) {
        super(stillTexture, flowingTexture, overlayTexture);
    }

    public WaterLikeFluidRenderHandler(Identifier stillTexture, Identifier flowingTexture) {
        super(stillTexture, flowingTexture);
    }

    @Override
    public int getFluidColor(@Nullable BlockRenderView view, @Nullable BlockPos pos, FluidState state) {
        if (view != null && pos != null) {
            return BiomeColors.getWaterColor(view, pos);
        } else {
            return 0x3f76e4;
        }
    }
}
