package net.tally;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.client.render.fluid.v1.SimpleFluidRenderHandler;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.util.Identifier;
import net.tally.entities.renderer.MimicBlockRenderer;
import net.tally.events.ClientWorldEvents;
import net.tally.events.RenderEvents;
import net.tally.networking.ClientMessages;

import java.util.List;

public class DebugThingsClient implements ClientModInitializer {
    private static NbtCompound infliction;

    public static NbtCompound getInfliction() {
        return infliction;
    }

    public static void setInfliction(NbtCompound infliction) {
        DebugThingsClient.infliction = infliction;
    }

    @Override
    public void onInitializeClient() {
        RenderEvents.initialize();
        ClientWorldEvents.initialize();

        BlockRenderLayerMap.INSTANCE.putFluids(RenderLayer.getTranslucent(), DebugThings.STILL_SOLID_WATER, DebugThings.FLOWING_SOLID_WATER);

        FluidRenderHandlerRegistry.INSTANCE.register(DebugThings.STILL_SOLID_WATER, DebugThings.FLOWING_SOLID_WATER, new WaterLikeFluidRenderHandler(
                new Identifier("minecraft:block/water_still"),
                new Identifier("minecraft:block/water_flow")
        ));

        EntityRendererRegistry.register(DebugThings.MIMIC_BLOCK_ENTITY_TYPE, MimicBlockRenderer::new);

        ClientMessages.registerS2CPackets();
    }
}
