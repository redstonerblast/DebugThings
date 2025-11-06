package net.tally;

import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlockEntities {
    public static final BlockEntityType<FakeBlock> FAKE_BLOCK_ENTITY = Registry.register(
            Registries.BLOCK_ENTITY_TYPE,
            new Identifier(DebugThings.MOD_ID, "fake_block_entity"),
            BlockEntityType.Builder.create(FakeBlock::new, DebugThings.SOLID_WATER).build()
    );

    public static void registerBlockEntities() {
        DebugThings.LOGGER.info("Registering Block Entities for " + DebugThings.MOD_ID);
    }
}
