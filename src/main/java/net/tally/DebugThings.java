package net.tally;

import com.mojang.serialization.Codec;
import com.mojang.serialization.JsonOps;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerChunkEvents;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerTickEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.FluidBlock;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.fluid.FlowableFluid;
import net.minecraft.fluid.Fluid;
import net.minecraft.item.Item;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.registry.tag.TagKey;
import net.minecraft.server.world.ChunkLevelType;
import net.minecraft.util.Identifier;
import net.minecraft.util.dynamic.Codecs;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.chunk.WorldChunk;
import net.tally.attributes.DebugAttributes;
import net.tally.blocks.SolidWater;
import net.tally.effects.*;
import net.tally.entities.MimicBlock;
import net.tally.events.EntityEvents;
import net.tally.events.PlayerEvents;
import net.tally.events.WorldEvents;
import net.tally.helpers.CustomRegistryHandler;
import net.tally.helpers.TempBlockAttachment;
import net.tally.holders.CommandHolder;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.Inflictions;
import net.tally.items.DebugItems;
import net.tally.loot.conditions.InflictionLootCondition;
import net.tally.loot.modifiers.RollsFunction;
import net.tally.loot.modifiers.WeightedListFunction;
import net.tally.networking.Messages;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

public class DebugThings implements ModInitializer {
	public static final String MOD_ID = "debugthings";

	public static final AttachmentType<List<BlockPos>> PERSISTENT_TEMP_BLOCK_LOC = AttachmentRegistry.createPersistent(
			Identifier.of(MOD_ID, "persistent_temp_block_pos"),
			BlockPos.CODEC.listOf()
	);

	public static final AttachmentType<List<BlockState>> PERSISTENT_TEMP_BLOCK_STATE = AttachmentRegistry.createPersistent(
			Identifier.of(MOD_ID, "persistent_temp_block_state"),
			BlockState.CODEC.listOf()
	);

	public static final AttachmentType<List<NbtCompound>> PERSISTENT_TEMP_BLOCK_NBT = AttachmentRegistry.createPersistent(
			Identifier.of(MOD_ID, "persistent_temp_block_nbt"),
			NbtCompound.CODEC.listOf()
	);

	public static final AttachmentType<List<Integer>> PERSISTENT_TEMP_BLOCK_TIME = AttachmentRegistry.createPersistent(
			Identifier.of(MOD_ID, "persistent_temp_block_time"),
			Codec.INT.listOf()
	);

	public static final AttachmentType<List<String>> SOUND_SEQUENCE = AttachmentRegistry.create(
			Identifier.of(MOD_ID, "sound_sequence")
	);

	public static FlowableFluid STILL_SOLID_WATER;
	public static FlowableFluid FLOWING_SOLID_WATER;
	public static Block SOLID_WATER;

	public static final TagKey<Fluid> EMPTY = ofFluid("empty");

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static final TagKey<Item> PARRY = i_of("parry");
	public static final TagKey<DamageType> MELEE = of("melee");
	public static final TagKey<DamageType> CRITICAL = of("critical");
	public static final TagKey<DamageType> HOLY = of("holy");
	public static final TagKey<DamageType> NO_INVUL = of("no_invul");

	public static final EntityType<MimicBlock> MIMIC_BLOCK_ENTITY_TYPE = Registry.register(
			Registries.ENTITY_TYPE,
			Identifier.of("debugthings", "mimic_block"),
			EntityType.Builder.create(MimicBlock::new, SpawnGroup.MISC).setDimensions(1,1).build("mimic_block")
	);

	private static TagKey<Fluid> ofFluid(String id) {
		return TagKey.of(RegistryKeys.FLUID, new Identifier("debugthings", id));
	}

	private static TagKey<DamageType> of(String id) {
		return TagKey.of(RegistryKeys.DAMAGE_TYPE, new Identifier("debugthings", id));
	}

	private static TagKey<Item> i_of(String id) {
		return TagKey.of(RegistryKeys.ITEM, new Identifier("debugthings", id));
	}

	private static List<Infliction> beneficials;
	private static List<Infliction> harmfuls;
	private static List<Infliction> neutrals;

	public static void addToCategory(InflictionCategory category, Infliction infliction) {
		if (category == InflictionCategory.BENEFICIAL) {
			beneficials.add(infliction);
		} else if (category == InflictionCategory.HARMFUL) {
			harmfuls.add(infliction);
		} else {
			neutrals.add(infliction);
		}
	}

	public static List<Infliction> getCategory(InflictionCategory category) {
		if (category == InflictionCategory.BENEFICIAL) {
			return beneficials;
		} else if (category == InflictionCategory.HARMFUL) {
			return harmfuls;
		} else {
			return neutrals;
		}
	}

	@Override
	public void onInitialize() {

		Messages.registerC2SPackets();

		DebugDamageTypes.init();
		CustomRegistryHandler.init();

		DebugEffects.init();

		beneficials = new ArrayList<>();
		harmfuls = new ArrayList<>();
		neutrals = new ArrayList<>();

		Inflictions.init();

		CommandHolder.register();
		DebugAttributes.setup();

		Registry.register(Registries.LOOT_FUNCTION_TYPE, new Identifier("debugthings", "weighted_list"), WeightedListFunction.TYPE);
		Registry.register(Registries.LOOT_FUNCTION_TYPE, new Identifier("debugthings", "roll"), RollsFunction.TYPE);
		Registry.register(Registries.LOOT_CONDITION_TYPE, new Identifier("debugthings", "infliction"), InflictionLootCondition.TYPE);

		var wlf = new InflictionLootCondition(Identifier.of("debugthings", "bleed"), BoundedIntUnaryOperator.createMin(1));
		LOGGER.info("condition - {}", InflictionLootCondition.CODEC.encode(wlf, JsonOps.INSTANCE, null));

		DebugItems.initialize();

		PlayerEvents.initialize();
		WorldEvents.initialize();
		EntityEvents.initialize();


		/*LootTableEvents.REPLACE.register(((resourceManager, lootManager, id, original, source) -> ChestLootTableModifiers.attemptReplace(id, source, true, new ArrayList<>()).orElse(original)));

		LootTableEvents.ALL_LOADED.register((resourceManager, lootManager) -> {
			Gson gson = new GsonBuilder().setPrettyPrinting().create();
            for (Identifier id : lootManager.getIds(LootDataType.LOOT_TABLES)) {
				if (id.toString().contains(":chest")) {
					File dataFile = new File("datagen\\" + id.getNamespace() + "\\loot_tables\\" + id.getPath().replace("/", "\\") + ".json");
					try {
						List<String> strings = new ArrayList<>(Arrays.stream(id.getPath().split("/")).toList());
						strings.remove(strings.size() - 1);
						new File("datagen\\" + id.getNamespace() + "\\loot_tables\\" + String.join("\\", strings)).mkdirs();
						dataFile.createNewFile();
						DebugThings.LOGGER.info("encode - {}", LootTable.CODEC.encodeStart(JsonOps.INSTANCE, lootManager.getLootTable(id)).get().left());
						Files.write(dataFile.toPath(), gson.toJson(LootTable.CODEC.encodeStart(JsonOps.INSTANCE, lootManager.getLootTable(id)).get().left().orElse(null)).getBytes(StandardCharsets.UTF_8));
					} catch (IOException e) {
						DebugThings.LOGGER.warn("File failed");
					}
				}
            }
		});*/

		STILL_SOLID_WATER = Registry.register(Registries.FLUID, new Identifier("debugthings", "still_solid_water"), new SolidWater.Still());
		FLOWING_SOLID_WATER = Registry.register(Registries.FLUID, new Identifier("debugthings", "solid_water_flowing"), new SolidWater.Flowing());
		SOLID_WATER = Registry.register(Registries.BLOCK, new Identifier(MOD_ID, "solid_water"), new FluidBlock(STILL_SOLID_WATER, FabricBlockSettings.copy(Blocks.WATER)){});
	}
}