package net.tally.loot;

import net.fabricmc.fabric.api.loot.v2.LootTableSource;
import net.minecraft.block.Blocks;
import net.minecraft.enchantment.Enchantments;
import net.minecraft.entity.effect.StatusEffects;
import net.minecraft.item.Items;
import net.minecraft.item.map.MapIcon;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.entry.EmptyEntry;
import net.minecraft.loot.entry.ItemEntry;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.number.ConstantLootNumberProvider;
import net.minecraft.loot.provider.number.UniformLootNumberProvider;
import net.minecraft.potion.Potions;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.tag.InstrumentTags;
import net.minecraft.registry.tag.StructureTags;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;

import java.util.List;
import java.util.Optional;

public class ChestLootTableModifiers {
    public static Optional<LootTable> attemptReplace(Identifier key, LootTableSource source, boolean enabled, List<String> excepts) {
        String k = key.toUnderscoreSeparatedString();
        for (String except : excepts) {
            if (k.endsWith(except)) {
                enabled = !enabled; //An exception does the opposite of the predefined enabled
                break;
            }
        }
        if (enabled && source.isBuiltin()) {

            if (LootTables.ABANDONED_MINESHAFT_CHEST.equals(key)) {
                return Optional.of(abandonedMineshaftChest());
            } else if (LootTables.BASTION_BRIDGE_CHEST.equals(key)) {
                return Optional.of(bastionBridgeChest());
            } else if (LootTables.BASTION_HOGLIN_STABLE_CHEST.equals(key)) {
                return Optional.of(bastionHoglinStableChest());
            } else if (LootTables.BASTION_OTHER_CHEST.equals(key)) {
                return Optional.of(bastionOtherChest());
            } else if (LootTables.BASTION_TREASURE_CHEST.equals(key)) {
                return Optional.of(bastionTreasureChest());
            } else if (LootTables.BURIED_TREASURE_CHEST.equals(key)) {
                return Optional.of(buriedTreasureChest());
            } else if (LootTables.ANCIENT_CITY_CHEST.equals(key)) {
                return Optional.of(ancientCityChest());
            } else if (LootTables.ANCIENT_CITY_ICE_BOX_CHEST.equals(key)) {
                return Optional.of(ancientCityIceboxChest());
            } else if (LootTables.DESERT_PYRAMID_CHEST.equals(key)) {
                return Optional.of(desertPyramidChest());
            } else if (LootTables.END_CITY_TREASURE_CHEST.equals(key)) {
                return Optional.of(endCityTreasureChest());
            } else if (LootTables.IGLOO_CHEST_CHEST.equals(key)) {
                return Optional.of(iglooChest());
            } else if (LootTables.JUNGLE_TEMPLE_CHEST.equals(key)) {
                return Optional.of(jungleTempleChest());
            } else if (LootTables.NETHER_BRIDGE_CHEST.equals(key)) {
                return Optional.of(netherBridgeChest());
            } else if (LootTables.PILLAGER_OUTPOST_CHEST.equals(key)) {
                return Optional.of(pillagerOutpostChest());
            } else if (LootTables.SHIPWRECK_MAP_CHEST.equals(key)) {
                return Optional.of(shipwreckMapChest());
            } else if (LootTables.SHIPWRECK_SUPPLY_CHEST.equals(key)) {
                return Optional.of(shipwreckSupplyChest());
            } else if (LootTables.SHIPWRECK_TREASURE_CHEST.equals(key)) {
                return Optional.of(shipwreckTreasureChest());
            } else if (LootTables.SIMPLE_DUNGEON_CHEST.equals(key)) {
                return Optional.of(simpleDungeonChest());
            } else if (LootTables.STRONGHOLD_CORRIDOR_CHEST.equals(key)) {
                return Optional.of(strongholdCorridorChest());
            } else if (LootTables.STRONGHOLD_CROSSING_CHEST.equals(key)) {
                return Optional.of(strongholdCrossingChest());
            } else if (LootTables.STRONGHOLD_LIBRARY_CHEST.equals(key)) {
                return Optional.of(strongholdLibraryChest());
            } else if (LootTables.UNDERWATER_RUIN_BIG_CHEST.equals(key)) {
                return Optional.of(underwaterRuinBigChest());
            } else if (LootTables.UNDERWATER_RUIN_SMALL_CHEST.equals(key)) {
                return Optional.of(underwaterRuinSmallChest());
            } else if (LootTables.VILLAGE_WEAPONSMITH_CHEST.equals(key)) {
                return Optional.of(villageWeaponsmithChest());
            } else if (LootTables.VILLAGE_TOOLSMITH_CHEST.equals(key)) {
                return Optional.of(villageToolsmithChest());
            } else if (LootTables.VILLAGE_CARTOGRAPHER_CHEST.equals(key)) {
                return Optional.of(villageCartographerChest());
            } else if (LootTables.VILLAGE_MASON_CHEST.equals(key)) {
                return Optional.of(villageMasonChest());
            } else if (LootTables.VILLAGE_ARMORER_CHEST.equals(key)) {
                return Optional.of(villageArmorerChest());
            } else if (LootTables.VILLAGE_SHEPARD_CHEST.equals(key)) {
                return Optional.of(villageShepardChest());
            } else if (LootTables.VILLAGE_BUTCHER_CHEST.equals(key)) {
                return Optional.of(villageButcherChest());
            } else if (LootTables.VILLAGE_FLETCHER_CHEST.equals(key)) {
                return Optional.of(villageFletcherChest());
            } else if (LootTables.VILLAGE_FISHER_CHEST.equals(key)) {
                return Optional.of(villageFisherChest());
            } else if (LootTables.VILLAGE_TANNERY_CHEST.equals(key)) {
                return Optional.of(villageTanneryChest());
            } else if (LootTables.VILLAGE_TEMPLE_CHEST.equals(key)) {
                return Optional.of(villageTempleChest());
            } else if (LootTables.VILLAGE_PLAINS_CHEST.equals(key)) {
                return Optional.of(villagePlainsChest());
            } else if (LootTables.VILLAGE_TAIGA_HOUSE_CHEST.equals(key)) {
                return Optional.of(villageTaigaHouseChest());
            } else if (LootTables.VILLAGE_SAVANNA_HOUSE_CHEST.equals(key)) {
                return Optional.of(villageSavannaHouseChest());
            } else if (LootTables.VILLAGE_SNOWY_HOUSE_CHEST.equals(key)) {
                return Optional.of(villageSnowyHouseChest());
            } else if (LootTables.VILLAGE_DESERT_HOUSE_CHEST.equals(key)) {
                return Optional.of(villageDesertHouseChest());
            } else if (LootTables.RUINED_PORTAL_CHEST.equals(key)) {
                return Optional.of(ruinedPortalChest());
            } else if (LootTables.WOODLAND_MANSION_CHEST.equals(key)) {
                return Optional.of(woodlandMansionChest().build());
            }
        }
        return Optional.empty();
    }
    private static LootTable abandonedMineshaftChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(40).quality(3))
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(2).quality(2))
                                .with(ItemEntry.builder(Items.NAME_TAG).weight(60))
                                .with(ItemEntry.builder(Items.BOOK).weight(20).quality(3).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.IRON_PICKAXE).weight(10).quality(-1))
                                .with(EmptyEntry.builder().weight(10).quality(-2))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(20).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(10).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))).quality(2))
                                .with(ItemEntry.builder(Items.REDSTONE).weight(10).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 9.0F))))
                                .with(ItemEntry.builder(Items.LAPIS_LAZULI).weight(10).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 9.0F))))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(6).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(30).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.GLOW_BERRIES).weight(30).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.MELON_SEEDS).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.PUMPKIN_SEEDS).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BEETROOT_SEEDS).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(3.0F))
                                .with(ItemEntry.builder(Blocks.RAIL).weight(20).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 8.0F))))
                                .with(ItemEntry.builder(Blocks.POWERED_RAIL).weight(5).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Blocks.DETECTOR_RAIL).weight(5).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Blocks.ACTIVATOR_RAIL).weight(5).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Blocks.TORCH).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 16.0F))))
                ).build();
    }
    private static LootTable bastionBridgeChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(Blocks.LODESTONE).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 2.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(
                                        ItemEntry.builder(Items.CROSSBOW)
                                                .weight(20)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1F, 0.5F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Items.SPECTRAL_ARROW).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(10.0F, 28.0F))))
                                .with(ItemEntry.builder(Blocks.GILDED_BLACKSTONE).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(8.0F, 12.0F))))
                                .with(ItemEntry.builder(Blocks.CRYING_OBSIDIAN).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 8.0F))))
                                .with(ItemEntry.builder(Blocks.GOLD_BLOCK).weight(20).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(20).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 9.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(20).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 9.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_SWORD).weight(20).quality(1).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_CHESTPLATE)
                                                .weight(20).quality(1)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_HELMET)
                                                .weight(20).quality(1)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_LEGGINGS)
                                                .weight(20).quality(1)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_BOOTS)
                                                .weight(20).quality(1)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_AXE)
                                                .weight(20).quality(1)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.STRING).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.LEATHER).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.ARROW).weight(15).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5.0F, 17.0F))))
                                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(15).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(15).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(10).quality(-1))
                                .with(EmptyEntry.builder().weight(1))
                                .with(ItemEntry.builder(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(4))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(15).quality(-2))
                                .with(EmptyEntry.builder().weight(3))
                                .with(ItemEntry.builder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).weight(2).quality(4))
                ).build();
    }
    private static LootTable bastionHoglinStableChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_SHOVEL)
                                                .weight(30).quality(2)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15F, 0.8F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_PICKAXE)
                                                .weight(24).quality(2)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.15F, 0.95F)))
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Items.NETHERITE_SCRAP).weight(16).quality(3).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.ANCIENT_DEBRIS).weight(24).quality(3).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.ANCIENT_DEBRIS).weight(10).quality(3).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(24).quality(-2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Blocks.GOLD_BLOCK).weight(32).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_CARROT).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(8.0F, 17.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(20).quality(-1).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_AXE)
                                                .weight(20)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Blocks.CRYING_OBSIDIAN).weight(20).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Blocks.GLOWSTONE).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 6.0F))))
                                .with(ItemEntry.builder(Blocks.GILDED_BLACKSTONE).weight(20).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))))
                                .with(ItemEntry.builder(Blocks.SOUL_SAND).weight(20).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Blocks.CRIMSON_NYLIUM).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(20).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.LEATHER).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.ARROW).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5.0F, 17.0F))))
                                .with(ItemEntry.builder(Items.STRING).weight(20).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.PORKCHOP).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.COOKED_PORKCHOP).weight(20).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))))
                                .with(ItemEntry.builder(Blocks.CRIMSON_FUNGUS).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Blocks.CRIMSON_ROOTS).weight(20).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(10).quality(-1))
                                .with(EmptyEntry.builder().weight(1))
                                .with(ItemEntry.builder(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(4))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(15).quality(-2))
                                .with(EmptyEntry.builder().weight(3))
                                .with(ItemEntry.builder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).weight(2).quality(4))
                ).build();
    }
    private static LootTable bastionOtherChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_PICKAXE)
                                                .weight(12).quality(2)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Items.DIAMOND_SHOVEL)
                                        .weight(12).quality(2)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(
                                        ItemEntry.builder(Items.CROSSBOW)
                                                .weight(12).quality(2)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1F, 0.9F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Items.ANCIENT_DEBRIS).weight(24).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.NETHERITE_SCRAP).weight(8).quality(3).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.SPECTRAL_ARROW).weight(20).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(10.0F, 22.0F))))
                                .with(ItemEntry.builder(Items.PIGLIN_BANNER_PATTERN).weight(18).quality(1).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_PIGSTEP).weight(10).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_CARROT).weight(24).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(6.0F, 17.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(18).quality(-1).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.BOOK).weight(20).quality(2).apply(new EnchantRandomlyLootFunction.Builder().add(Enchantments.SOUL_SPEED)))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(2.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(
                                        ItemEntry.builder(Items.IRON_SWORD)
                                                .weight(26).quality(-2)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.1F, 0.9F)))
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Blocks.IRON_BLOCK)
                                        .weight(26).quality(-2)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_BOOTS)
                                                .weight(13)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(new EnchantRandomlyLootFunction.Builder().add(Enchantments.SOUL_SPEED))
                                )
                                .with(
                                        ItemEntry.builder(Items.GOLDEN_AXE)
                                                .weight(13)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Blocks.GOLD_BLOCK).weight(26).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.CROSSBOW).weight(13).quality(-2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(26).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(26).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_SWORD).weight(13).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_CHESTPLATE).weight(13).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_HELMET).weight(13).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_LEGGINGS).weight(13).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_BOOTS).weight(13).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Blocks.CRYING_OBSIDIAN).weight(26).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Blocks.GILDED_BLACKSTONE).weight(26).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Blocks.CHAIN).weight(13).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.MAGMA_CREAM).weight(26).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                                .with(ItemEntry.builder(Blocks.BONE_BLOCK).weight(13).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(13).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 8.0F))))
                                .with(ItemEntry.builder(Blocks.OBSIDIAN).weight(13).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(13).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.STRING).weight(13).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.ARROW).weight(26).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5.0F, 17.0F))))
                                .with(ItemEntry.builder(Items.COOKED_PORKCHOP).weight(13).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(10).quality(-1))
                                .with(EmptyEntry.builder().weight(1))
                                .with(ItemEntry.builder(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(4))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(15).quality(-2))
                                .with(EmptyEntry.builder().weight(3))
                                .with(ItemEntry.builder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).weight(2).quality(4))
                ).build();
    }
    private static LootTable bastionTreasureChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(3.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.17f))
                                .with(ItemEntry.builder(Items.NETHERITE_INGOT).weight(30).quality(3).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Blocks.ANCIENT_DEBRIS).weight(20).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.NETHERITE_SCRAP).weight(16).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Blocks.ANCIENT_DEBRIS).weight(8).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))))
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_SWORD)
                                                .weight(12)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8F, 1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_CHESTPLATE)
                                                .weight(12).quality(2)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8F, 1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_HELMET)
                                                .weight(12).quality(1)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8F, 1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_LEGGINGS)
                                                .weight(12).quality(1)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8F, 1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_BOOTS)
                                                .weight(12)
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8F, 1.0F)))
                                                .apply(EnchantRandomlyLootFunction.builder())
                                )
                                .with(ItemEntry.builder(Items.DIAMOND_SWORD).weight(12).quality(-2))
                                .with(ItemEntry.builder(Items.DIAMOND_CHESTPLATE).weight(10).quality(-1))
                                .with(ItemEntry.builder(Items.DIAMOND_HELMET).weight(10).quality(-2))
                                .with(ItemEntry.builder(Items.DIAMOND_BOOTS).weight(10).quality(-2))
                                .with(ItemEntry.builder(Items.DIAMOND_LEGGINGS).weight(10).quality(-1))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(10).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(4).quality(1).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.SPECTRAL_ARROW).weight(17).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(12.0F, 25.0F))))
                                .with(ItemEntry.builder(Blocks.GOLD_BLOCK).weight(17).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))))
                                .with(ItemEntry.builder(Blocks.IRON_BLOCK).weight(17).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(17).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 9.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(17).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 9.0F))))
                                .with(ItemEntry.builder(Blocks.CRYING_OBSIDIAN).weight(17).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.QUARTZ).weight(17).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(8.0F, 23.0F))))
                                .with(ItemEntry.builder(Blocks.GILDED_BLACKSTONE).weight(17).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5.0F, 15.0F))))
                                .with(ItemEntry.builder(Items.MAGMA_CREAM).weight(17).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 8.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(10).quality(-1))
                                .with(EmptyEntry.builder())
                                .with(ItemEntry.builder(Items.SNOUT_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(4))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE).weight(1).quality(4))
                ).build();
    }
    private static LootTable buriedTreasureChest() {
        return LootTable.builder()
                .pool(LootPool.builder().rolls(ConstantLootNumberProvider.create(1.0F)).with(ItemEntry.builder(Items.HEART_OF_THE_SEA)))
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(5.0F, 8.0F))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(40).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(20).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Blocks.TNT).weight(10).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(ItemEntry.builder(Items.EMERALD).weight(23).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(23).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.PRISMARINE_CRYSTALS).weight(23).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
                                .with(ItemEntry.builder(Items.LEATHER_CHESTPLATE).weight(17).quality(-2))
                                .with(ItemEntry.builder(Items.IRON_SWORD).weight(17))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(2.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.COOKED_COD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.COOKED_SALMON).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(0.0F, 2.0F))
                                .with(ItemEntry.builder(Items.POTION))
                                .apply(SetPotionLootFunction.builder(Potions.WATER_BREATHING))
                ).build();
    }
    private static LootTable ancientCityChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(5.0F, 10.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.5f))
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(6).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_OTHERSIDE).weight(6).quality(1))
                                .with(ItemEntry.builder(Items.COMPASS).weight(12).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.SCULK_CATALYST).weight(12).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.NAME_TAG).weight(12))
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_HOE)
                                                .weight(12).quality(1)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F)))
                                                .apply(SetDamageLootFunction.builder(UniformLootNumberProvider.create(0.8F, 1.0F)))
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(30.0F, 50.0F)).allowTreasureEnchantments())
                                )
                                .with(ItemEntry.builder(Items.LEAD).weight(12).quality(-1).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(12).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(12).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1.0F))))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_13).weight(12).quality(-1))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_CAT).weight(12).quality(-1))
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_LEGGINGS)
                                                .weight(12).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(30.0F, 50.0F)).allowTreasureEnchantments())
                                )
                                .with(ItemEntry.builder(Items.BOOK).weight(18).quality(3).apply(new EnchantRandomlyLootFunction.Builder().add(Enchantments.SWIFT_SNEAK)))
                                .with(ItemEntry.builder(Items.SCULK).weight(18).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.SCULK_SENSOR).weight(18).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.CANDLE).weight(18).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.AMETHYST_SHARD).weight(18).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 15.0F))))
                                .with(ItemEntry.builder(Items.EXPERIENCE_BOTTLE).weight(18).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.GLOW_BERRIES).weight(18).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 15.0F))))
                                .with(
                                        ItemEntry.builder(Items.IRON_LEGGINGS)
                                                .weight(18).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(ItemEntry.builder(Items.ECHO_SHARD).weight(24).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.DISC_FRAGMENT_5).weight(24).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(
                                        ItemEntry.builder(Items.POTION)
                                                .weight(30).quality(-1)
                                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F)))
                                                .apply(SetPotionLootFunction.builder(Potions.STRONG_REGENERATION))
                                )
                                .with(ItemEntry.builder(Items.BOOK).weight(30).quality(3).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.BOOK).weight(30).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.BONE).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 15.0F))))
                                .with(ItemEntry.builder(Items.SOUL_TORCH).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 15.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(42).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(6.0F, 15.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(73).quality(-10))
                                .with(EmptyEntry.builder().weight(2))
                                .with(ItemEntry.builder(Items.WARD_ARMOR_TRIM_SMITHING_TEMPLATE).weight(4).quality(1))
                                .with(ItemEntry.builder(Items.SILENCE_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(4))
                ).build();
    }
    private static LootTable ancientCityIceboxChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(4.0F, 10.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(
                                        ItemEntry.builder(Items.SUSPICIOUS_STEW)
                                                .weight(10).quality(1)
                                                .apply(
                                                        SetStewEffectLootFunction.builder()
                                                                .withEffect(StatusEffects.NIGHT_VISION, UniformLootNumberProvider.create(7.0F, 10.0F))
                                                                .withEffect(StatusEffects.BLINDNESS, UniformLootNumberProvider.create(5.0F, 7.0F))
                                                )
                                                .apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F)))
                                )
                                .with(ItemEntry.builder(Items.GOLDEN_CARROT).weight(10).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.BAKED_POTATO).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.PACKED_ICE).weight(20).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.SNOWBALL).weight(40).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                ).build();
    }
    private static LootTable desertPyramidChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(10).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(30).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(30).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(30).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BONE).weight(50).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.SPIDER_EYE).weight(50).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(50).quality(-5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(40).quality(-4))
                                .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(30).quality(-2))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(20).quality(-1))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(10))
                                .with(ItemEntry.builder(Items.BOOK).weight(40).quality(2).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(40).quality(2))
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(4).quality(1))
                                .with(EmptyEntry.builder().weight(30).quality(-3))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(-0.2f, 0f))
                                .with(ItemEntry.builder(Items.BONE).weight(20).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.GUNPOWDER).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.STRING).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Blocks.SAND).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(22).quality(-3))
                                .with(EmptyEntry.builder().weight(2))
                                .with(
                                        ItemEntry.builder(Items.DUNE_ARMOR_TRIM_SMITHING_TEMPLATE).weight(4)
                                        .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                                        .quality(2)
                                )
                ).build();
    }
    private static LootTable endCityTreasureChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 6.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(20).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(40).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(45).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(6).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.BEETROOT_SEEDS).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(6))
                                .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(3).quality(-1))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(3).quality(-1))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(3))
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_SWORD)
                                                .weight(9).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_BOOTS)
                                                .weight(9).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_CHESTPLATE)
                                                .weight(9).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_LEGGINGS)
                                                .weight(9).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_HELMET)
                                                .weight(9).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_PICKAXE)
                                                .weight(9).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.DIAMOND_SHOVEL)
                                                .weight(9).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.IRON_SWORD)
                                                .weight(9).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.IRON_BOOTS)
                                                .weight(9).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.IRON_CHESTPLATE)
                                                .weight(9).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.IRON_LEGGINGS)
                                                .weight(9).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.IRON_HELMET)
                                                .weight(9).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.IRON_PICKAXE)
                                                .weight(9).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.IRON_SHOVEL)
                                                .weight(9).quality(1)
                                                .apply(EnchantWithLevelsLootFunction.builder(UniformLootNumberProvider.create(20.0F, 39.0F)).allowTreasureEnchantments())
                                )
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(13).quality(-2))
                                .with(EmptyEntry.builder())
                                .with(ItemEntry.builder(Items.SPIRE_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(4))
                ).build();
    }
    private static LootTable iglooChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(ItemEntry.builder(Items.APPLE).weight(30).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(30).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(20).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.STONE_AXE).weight(4))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(20).quality(-2))
                                .with(ItemEntry.builder(Items.EMERALD).quality(2))
                                .with(ItemEntry.builder(Items.EMERALD))
                                .with(ItemEntry.builder(Items.WHEAT).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 3.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE))
                ).build();
    }
    private static LootTable jungleTempleChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 6.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(9).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(30).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(45).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Blocks.BAMBOO).weight(45).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(6).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BONE).weight(60).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(48).quality(-4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(9))
                                .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(3).quality(-1))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(3).quality(-1))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(3))
                                .with(ItemEntry.builder(Items.BOOK).weight(3).quality(1).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0F)).allowTreasureEnchantments()))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(16).quality(-3))
                                .with(EmptyEntry.builder().weight(4))
                                .with(
                                        ItemEntry.builder(Items.WILD_ARMOR_TRIM_SMITHING_TEMPLATE).weight(10).quality(2)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                                )
                ).build();
    }
    private static LootTable netherBridgeChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(15).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(15).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(45).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_SWORD).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.GOLDEN_CHESTPLATE).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.FLINT_AND_STEEL).weight(15).quality(-2))
                                .with(ItemEntry.builder(Items.NETHER_WART).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(30).quality(-1))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(24).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(15).quality(-2))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(13))
                                .with(ItemEntry.builder(Blocks.OBSIDIAN).weight(6).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(14).quality(-2))
                                .with(EmptyEntry.builder().weight(1))
                                .with(ItemEntry.builder(Items.RIB_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(4))
                ).build();
    }
    private static LootTable pillagerOutpostChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(ItemEntry.builder(Items.CROSSBOW)))
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 3.0F))
                                .with(ItemEntry.builder(Items.WHEAT).weight(7).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.POTATO).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.CARROT).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 5.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(ItemEntry.builder(Blocks.DARK_OAK_LOG).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 3.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 3.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.23f))
                                .with(ItemEntry.builder(Items.EXPERIENCE_BOTTLE).weight(21).quality(-2))
                                .with(ItemEntry.builder(Items.STRING).weight(12).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.ARROW).weight(12).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.TRIPWIRE_HOOK).weight(9).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(9).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BOOK).weight(3).quality(2).apply(EnchantRandomlyLootFunction.builder()))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(0.0F, 1.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.1f))
                                .with(ItemEntry.builder(Items.GOAT_HORN))
                                .apply(SetInstrumentLootFunction.builder(InstrumentTags.REGULAR_GOAT_HORNS))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(10).quality(-2))
                                .with(EmptyEntry.builder().weight(2))
                                .with(
                                        ItemEntry.builder(Items.SENTRY_ARMOR_TRIM_SMITHING_TEMPLATE).weight(4).quality(4)
                                                .apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F)))
                                )
                ).build();
    }
    private static LootTable shipwreckMapChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(
                                        ItemEntry.builder(Items.MAP)
                                                .apply(
                                                        ExplorationMapLootFunction.builder()
                                                                .withDestination(StructureTags.ON_TREASURE_MAPS)
                                                                .withDecoration(MapIcon.Type.RED_X)
                                                                .withZoom((byte)1)
                                                                .withSkipExistingChunks(false)
                                                )
                                                .apply(SetNameLootFunction.builder(Text.translatable("filled_map.buried_treasure")))
                                )
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(3.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.COMPASS))
                                .with(ItemEntry.builder(Items.MAP))
                                .with(ItemEntry.builder(Items.CLOCK))
                                .with(ItemEntry.builder(Items.PAPER).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.FEATHER).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.BOOK).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(13).quality(-2))
                                .with(EmptyEntry.builder().weight(2))
                                .with(ItemEntry.builder(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).weight(3).quality(4).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))))
                ).build();
    }
    private static LootTable shipwreckSupplyChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 10.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.PAPER).weight(24).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 12.0F))))
                                .with(ItemEntry.builder(Items.POTATO).weight(21).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.MOSS_BLOCK).weight(21).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.POISONOUS_POTATO).weight(21).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 6.0F))))
                                .with(ItemEntry.builder(Items.CARROT).weight(21).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.WHEAT).weight(21).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(8.0F, 21.0F))))
                                .with(
                                        ItemEntry.builder(Items.SUSPICIOUS_STEW)
                                                .weight(30).quality(2)
                                                .apply(
                                                        SetStewEffectLootFunction.builder()
                                                                .withEffect(StatusEffects.NIGHT_VISION, UniformLootNumberProvider.create(7.0F, 10.0F))
                                                                .withEffect(StatusEffects.JUMP_BOOST, UniformLootNumberProvider.create(7.0F, 10.0F))
                                                                .withEffect(StatusEffects.WEAKNESS, UniformLootNumberProvider.create(6.0F, 8.0F))
                                                                .withEffect(StatusEffects.BLINDNESS, UniformLootNumberProvider.create(5.0F, 7.0F))
                                                                .withEffect(StatusEffects.POISON, UniformLootNumberProvider.create(10.0F, 20.0F))
                                                                .withEffect(StatusEffects.SATURATION, UniformLootNumberProvider.create(7.0F, 10.0F))
                                                )
                                )
                                .with(ItemEntry.builder(Items.COAL).weight(18).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(5.0F, 24.0F))))
                                .with(ItemEntry.builder(Blocks.PUMPKIN).weight(6).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Blocks.BAMBOO).weight(6).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.GUNPOWDER).weight(9).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Blocks.TNT).weight(3).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.LEATHER_HELMET).weight(9).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.LEATHER_CHESTPLATE).weight(9).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.LEATHER_LEGGINGS).weight(9).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.LEATHER_BOOTS).weight(9).apply(EnchantRandomlyLootFunction.builder()))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(13).quality(-2))
                                .with(EmptyEntry.builder().weight(2))
                                .with(ItemEntry.builder(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).weight(3).quality(4).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))))
                ).build();
    }
    private static LootTable shipwreckTreasureChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 6.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.6f))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(90).quality(-4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(10).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(40).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(3).quality(5))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(2))
                                .with(ItemEntry.builder(Items.EXPERIENCE_BOTTLE).weight(5))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.2f))
                                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(50).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(10).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 10.0F))))
                                .with(ItemEntry.builder(Items.LAPIS_LAZULI).weight(20).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 10.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(13).quality(-2))
                                .with(EmptyEntry.builder().weight(2))
                                .with(ItemEntry.builder(Items.COAST_ARMOR_TRIM_SMITHING_TEMPLATE).weight(3).quality(2).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(2.0F))))
                ).build();
    }
    private static LootTable simpleDungeonChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                                .with(ItemEntry.builder(Items.SADDLE).weight(40).quality(-2))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(30).quality(3))
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(4).quality(3))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_OTHERSIDE).weight(4).quality(2))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_13).weight(30).quality(-3))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_CAT).weight(30).quality(-3))
                                .with(ItemEntry.builder(Items.NAME_TAG).weight(40).quality(-4))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(20).quality(-2))
                                .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(30).quality(-4))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(10))
                                .with(ItemEntry.builder(Items.BOOK).weight(20).quality(3).apply(EnchantRandomlyLootFunction.builder()))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(30).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(15).quality(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(60).quality(-6))
                                .with(ItemEntry.builder(Items.WHEAT).weight(60).quality(-6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BUCKET).weight(30).quality(-2))
                                .with(ItemEntry.builder(Items.REDSTONE).weight(45).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(45).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.MELON_SEEDS).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.PUMPKIN_SEEDS).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BEETROOT_SEEDS).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(3.0F))
                                .bonusRolls(UniformLootNumberProvider.create(-0.3f, 0f))
                                .with(ItemEntry.builder(Items.BONE).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.GUNPOWDER).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(20).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.STRING).weight(20).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                ).build();
    }
    private static LootTable strongholdCorridorChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 3.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.ENDER_PEARL).weight(30).quality(1))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(6).quality(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(30).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(15).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.REDSTONE).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 9.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(45).quality(-6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.APPLE).weight(45).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_PICKAXE).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_SWORD).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_CHESTPLATE).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_HELMET).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_LEGGINGS).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_BOOTS).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(3))
                                .with(ItemEntry.builder(Items.SADDLE).weight(3))
                                .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(3))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(3))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(3))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_OTHERSIDE).weight(3).quality(1))
                                .with(ItemEntry.builder(Items.BOOK).weight(2).quality(6).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0F)).allowTreasureEnchantments()))
                                .with(ItemEntry.builder(Items.BOOK).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0F)).allowTreasureEnchantments()))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(14).quality(-2))
                                .with(EmptyEntry.builder().weight(4))
                                .with(ItemEntry.builder(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).weight(2).quality(4))
                ).build();
    }
    private static LootTable strongholdCrossingChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 4.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(30).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(15).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.REDSTONE).weight(15).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 9.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(45).quality(-4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.APPLE).weight(45).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_PICKAXE).weight(3))
                                .with(ItemEntry.builder(Items.BOOK).weight(2).quality(4).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0F)).allowTreasureEnchantments()))
                                .with(ItemEntry.builder(Items.BOOK).apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0F)).allowTreasureEnchantments()))
                ).build();
    }
    private static LootTable strongholdLibraryChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 10.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.25f))
                                .with(ItemEntry.builder(Items.BOOK).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.PAPER).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.MAP))
                                .with(ItemEntry.builder(Items.COMPASS))
                                .with(
                                        ItemEntry.builder(Items.BOOK)
                                                .weight(7).quality(2)
                                                .apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0F)).allowTreasureEnchantments())
                                )
                                .with(
                                        ItemEntry.builder(Items.BOOK)
                                                .weight(3)
                                                .apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(30.0F)).allowTreasureEnchantments())
                                )
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(Items.EYE_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1))
                ).build();
    }
    private static LootTable underwaterRuinBigChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.11f, 0.18f))
                                .with(
                                        ItemEntry.builder(Items.BOOK)
                                                .apply(EnchantWithLevelsLootFunction.builder(ConstantLootNumberProvider.create(25.0F)).allowTreasureEnchantments())
                                )
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.24f))
                                .with(ItemEntry.builder(Items.COAL).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(2))
                                .with(ItemEntry.builder(Items.WHEAT).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 3.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(2).quality(2))
                                .with(ItemEntry.builder(Items.BOOK).weight(10).quality(2).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.LEATHER_CHESTPLATE).weight(2))
                                .with(ItemEntry.builder(Items.GOLDEN_HELMET).weight(2))
                                .with(ItemEntry.builder(Items.FISHING_ROD).weight(10).quality(-1).apply(EnchantRandomlyLootFunction.builder()))
                                .with(
                                        ItemEntry.builder(Items.MAP)
                                                .weight(20).quality(1)
                                                .apply(
                                                        ExplorationMapLootFunction.builder()
                                                                .withDestination(StructureTags.ON_TREASURE_MAPS)
                                                                .withDecoration(MapIcon.Type.RED_X)
                                                                .withZoom((byte)1)
                                                                .withSkipExistingChunks(false)
                                                )
                                                .apply(SetNameLootFunction.builder(Text.translatable("filled_map.buried_treasure")))
                                )
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0f))
                                .with(EmptyEntry.builder().weight(1))
                                .with(EmptyEntry.builder().weight(30).quality(-4))
                ).build();
    }
    private static LootTable underwaterRuinSmallChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.11f, 0.18f))
                                .with(ItemEntry.builder(Items.BOOK).apply(EnchantRandomlyLootFunction.builder()))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(2.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.24f))
                                .with(ItemEntry.builder(Items.COAL).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.STONE_AXE).weight(4))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(10).quality(-1))
                                .with(ItemEntry.builder(Items.EMERALD).quality(2))
                                .with(ItemEntry.builder(Items.EMERALD))
                                .with(ItemEntry.builder(Items.WHEAT).weight(20).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 3.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(ItemEntry.builder(Items.LEATHER_CHESTPLATE))
                                .with(ItemEntry.builder(Items.GOLDEN_HELMET))
                                .with(ItemEntry.builder(Items.FISHING_ROD).weight(5).apply(EnchantRandomlyLootFunction.builder()))
                                .with(
                                        ItemEntry.builder(Items.MAP)
                                                .weight(5).quality(2)
                                                .apply(
                                                        ExplorationMapLootFunction.builder()
                                                                .withDestination(StructureTags.ON_TREASURE_MAPS)
                                                                .withDecoration(MapIcon.Type.RED_X)
                                                                .withZoom((byte)1)
                                                                .withSkipExistingChunks(false)
                                                )
                                                .apply(SetNameLootFunction.builder(Text.translatable("filled_map.buried_treasure")))
                                )
                ).build();
    }
    private static LootTable villageWeaponsmithChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.DIAMOND).weight(9).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(30).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(15).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(45).quality(-6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.APPLE).weight(45).quality(-6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_PICKAXE).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_SWORD).weight(15).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_CHESTPLATE).weight(15))
                                .with(ItemEntry.builder(Items.IRON_HELMET).weight(15))
                                .with(ItemEntry.builder(Items.IRON_LEGGINGS).weight(15))
                                .with(ItemEntry.builder(Items.IRON_BOOTS).weight(15))
                                .with(ItemEntry.builder(Blocks.OBSIDIAN).weight(15).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 7.0F))))
                                .with(ItemEntry.builder(Blocks.OAK_SAPLING).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(9).quality(-1))
                                .with(ItemEntry.builder(Items.IRON_HORSE_ARMOR).weight(3))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(3))
                                .with(ItemEntry.builder(Items.DIAMOND_HORSE_ARMOR).weight(3))
                ).build();
    }
    private static LootTable villageToolsmithChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.DIAMOND).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.DIAMOND).quality(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(10).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_PICKAXE).weight(10))
                                .with(ItemEntry.builder(Items.COAL).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.STICK).weight(40).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.IRON_SHOVEL).weight(10))
                ).build();
    }
    private static LootTable villageCartographerChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.BREAD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(3.0f, 7.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Items.MAP).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.PAPER).weight(15).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.COMPASS).weight(5).quality(2))
                                .with(ItemEntry.builder(Items.BREAD).weight(15).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.STICK).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                ).build();
    }
    private static LootTable villageMasonChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.STONE_BRICKS).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.STONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.BREAD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.SMOOTH_STONE).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Items.CLAY_BALL).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.FLOWER_POT).weight(10).quality(-1))
                                .with(ItemEntry.builder(Blocks.STONE).weight(20))
                                .with(ItemEntry.builder(Blocks.STONE_BRICKS).weight(20))
                                .with(ItemEntry.builder(Items.BREAD).weight(40).quality(-5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.YELLOW_DYE).weight(10).quality(1))
                                .with(ItemEntry.builder(Blocks.SMOOTH_STONE).weight(10))
                                .with(ItemEntry.builder(Items.EMERALD).weight(10).quality(3))
                ).build();
    }
    private static LootTable villageArmorerChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                                .with(ItemEntry.builder(Items.IRON_HELMET).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1f))))
                                .with(ItemEntry.builder(Items.BREAD).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(20).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(40).quality(-5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.IRON_HELMET).weight(10).quality(1))
                                .with(ItemEntry.builder(Items.EMERALD).weight(10).quality(3))
                ).build();
    }
    private static LootTable villageShepardChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.BROWN_WOOL).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.SHEARS).apply(SetCountLootFunction.builder(ConstantLootNumberProvider.create(1f))))
                                .with(ItemEntry.builder(Items.WHEAT).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 3.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Blocks.WHITE_WOOL).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Blocks.BLACK_WOOL).weight(15).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Blocks.GRAY_WOOL).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Blocks.BROWN_WOOL).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Blocks.LIGHT_GRAY_WOOL).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(5).quality(3))
                                .with(ItemEntry.builder(Items.SHEARS).weight(5))
                                .with(ItemEntry.builder(Items.WHEAT).weight(30).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 6.0F))))
                ).build();
    }
    private static LootTable villageButcherChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.BEEF).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.COAL).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.PORKCHOP).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Items.EMERALD).weight(4).quality(3))
                                .with(ItemEntry.builder(Items.PORKCHOP).weight(24).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.WHEAT).weight(24).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BEEF).weight(24).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.MUTTON).weight(24).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(12).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                ).build();
    }
    private static LootTable villageFletcherChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.FLINT).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.ARROW).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.FEATHER).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Items.EMERALD).weight(4).quality(3))
                                .with(ItemEntry.builder(Items.ARROW).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.FEATHER).weight(24).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.EGG).weight(8).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.FLINT).weight(24).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.STICK).weight(24).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                ).build();
    }
    private static LootTable villageFisherChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.SALMON).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.COD).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.COAL).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Items.EMERALD).weight(8).quality(3))
                                .with(ItemEntry.builder(Items.COD).weight(16).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.SALMON).weight(8).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.WATER_BUCKET).weight(8).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.BARREL).weight(8).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.WHEAT_SEEDS).weight(24).quality(-3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(16).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                ).build();
    }
    private static LootTable villageTanneryChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.SADDLE).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.BREAD).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.LEATHER).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 5.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.15f))
                                .with(ItemEntry.builder(Items.LEATHER).weight(8).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.LEATHER_CHESTPLATE).weight(16).quality(-2))
                                .with(ItemEntry.builder(Items.LEATHER_BOOTS).weight(16).quality(-2))
                                .with(ItemEntry.builder(Items.LEATHER_HELMET).weight(16).quality(-2))
                                .with(ItemEntry.builder(Items.BREAD).weight(40).quality(-5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.LEATHER_LEGGINGS).weight(16).quality(-2))
                                .with(ItemEntry.builder(Items.SADDLE).weight(8))
                                .with(ItemEntry.builder(Items.EMERALD).weight(8).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                ).build();
    }
    private static LootTable villageTempleChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.EMERALD).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(0f))
                                .bonusRolls(UniformLootNumberProvider.create(0.1f, 0.18f))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.LAPIS_LAZULI).weight(4).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                                .with(ItemEntry.builder(Items.REDSTONE).weight(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0f, 2.0f))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.REDSTONE).weight(12).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(42).quality(-5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(42).quality(-5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.LAPIS_LAZULI).weight(6).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(6).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(6).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                ).build();
    }
    private static LootTable villagePlainsChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.DANDELION).weight(2).quality(-2))
                                .with(ItemEntry.builder(Items.POPPY).weight(1))
                                .with(ItemEntry.builder(Items.POTATO).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.APPLE).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.BOOK).weight(1).quality(1))
                                .with(ItemEntry.builder(Items.FEATHER).weight(1).quality(-2))
                                .with(ItemEntry.builder(Items.EMERALD).weight(2).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Blocks.OAK_SAPLING).weight(5).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                ).build();
    }
    private static LootTable villageTaigaHouseChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(1).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.FERN).weight(2).quality(-2))
                                .with(ItemEntry.builder(Items.LARGE_FERN).weight(2).quality(-2))
                                .with(ItemEntry.builder(Items.POTATO).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.SWEET_BERRIES).weight(5).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.PUMPKIN_SEEDS).weight(5).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.PUMPKIN_PIE).weight(1).quality(1))
                                .with(ItemEntry.builder(Items.EMERALD).weight(2).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Blocks.SPRUCE_SAPLING).weight(5).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.SPRUCE_SIGN).weight(1))
                                .with(ItemEntry.builder(Items.SPRUCE_LOG).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                ).build();
    }
    private static LootTable villageSavannaHouseChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(1).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.SHORT_GRASS).weight(5).quality(-2))
                                .with(ItemEntry.builder(Items.TALL_GRASS).weight(5).quality(-2))
                                .with(ItemEntry.builder(Items.BREAD).weight(10).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.WHEAT_SEEDS).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(2).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Blocks.ACACIA_SAPLING).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.SADDLE).weight(1))
                                .with(ItemEntry.builder(Blocks.TORCH).weight(1).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.BUCKET).weight(1).quality(-1))
                ).build();
    }
    private static LootTable villageSnowyHouseChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Blocks.BLUE_ICE).weight(1).quality(-1))
                                .with(ItemEntry.builder(Blocks.SNOW_BLOCK).weight(4))
                                .with(ItemEntry.builder(Items.POTATO).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BEETROOT_SEEDS).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 5.0F))))
                                .with(ItemEntry.builder(Items.BEETROOT_SOUP).weight(1))
                                .with(ItemEntry.builder(Items.FURNACE).weight(1))
                                .with(ItemEntry.builder(Items.EMERALD).weight(1).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.SNOWBALL).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(5).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                ).build();
    }
    private static LootTable villageDesertHouseChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(3.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.3f))
                                .with(ItemEntry.builder(Items.CLAY_BALL).weight(1))
                                .with(ItemEntry.builder(Items.GREEN_DYE).weight(1).quality(-1))
                                .with(ItemEntry.builder(Blocks.CACTUS).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.WHEAT).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 7.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(10).quality(-1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BOOK).weight(1).quality(1))
                                .with(ItemEntry.builder(Blocks.DEAD_BUSH).weight(2).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                                .with(ItemEntry.builder(Items.EMERALD).weight(1).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 3.0F))))
                ).build();
    }
    public static LootTable.Builder woodlandMansionChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 3.0F))
                                .with(ItemEntry.builder(Items.LEAD).weight(20).quality(0))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(15).quality(3))
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(2).quality(5))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_13).weight(15).quality(0))
                                .with(ItemEntry.builder(Items.MUSIC_DISC_CAT).weight(15).quality(0))
                                .with(ItemEntry.builder(Items.NAME_TAG).weight(20).quality(1))
                                .with(ItemEntry.builder(Items.CHAINMAIL_CHESTPLATE).weight(10).quality(1))
                                .with(ItemEntry.builder(Items.DIAMOND_HOE).weight(15).quality(2))
                                .with(ItemEntry.builder(Items.DIAMOND_CHESTPLATE).weight(5).quality(2))
                                .with(ItemEntry.builder(Items.BOOK).weight(10).quality(2).apply(EnchantRandomlyLootFunction.builder()))
                )
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(1.0F, 4.0F))
                                .with(ItemEntry.builder(Items.IRON_INGOT).weight(10).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(5).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BREAD).weight(20).quality(1))
                                .with(ItemEntry.builder(Items.WHEAT).weight(20).quality(0).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BUCKET).weight(10).quality(1))
                                .with(ItemEntry.builder(Items.REDSTONE).weight(15).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.COAL).weight(15).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.MELON_SEEDS).weight(10).quality(0).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.PUMPKIN_SEEDS).weight(10).quality(0).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.BEETROOT_SEEDS).weight(10).quality(0).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 4.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(3.0F))
                                .with(ItemEntry.builder(Items.BONE).weight(10).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.GUNPOWDER).weight(10).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.ROTTEN_FLESH).weight(10).quality(0).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.STRING).weight(10).quality(1).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 8.0F))))
                )
                .pool(
                        LootPool.builder()
                                .rolls(ConstantLootNumberProvider.create(1.0F))
                                .with(EmptyEntry.builder().weight(1).quality(0))
                                .with(ItemEntry.builder(Items.VEX_ARMOR_TRIM_SMITHING_TEMPLATE).weight(1).quality(3))
                );
    }
    private static LootTable ruinedPortalChest() {
        return LootTable.builder()
                .pool(
                        LootPool.builder()
                                .rolls(UniformLootNumberProvider.create(4.0F, 8.0F))
                                .bonusRolls(UniformLootNumberProvider.create(0f, 0.5f))
                                .with(ItemEntry.builder(Items.OBSIDIAN).weight(40).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                                .with(ItemEntry.builder(Items.FLINT).weight(40).quality(-6).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 4.0F))))
                                .with(ItemEntry.builder(Items.IRON_NUGGET).weight(40).quality(-5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(9.0F, 18.0F))))
                                .with(ItemEntry.builder(Items.FLINT_AND_STEEL).weight(40).quality(-4))
                                .with(ItemEntry.builder(Items.FIRE_CHARGE).weight(40).quality(-4))
                                .with(ItemEntry.builder(Items.GOLDEN_APPLE).weight(15).quality(1))
                                .with(ItemEntry.builder(Items.GOLD_NUGGET).weight(15).quality(-2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 24.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_SWORD).weight(15).quality(1).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_AXE).weight(15).quality(1).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_HOE).weight(15).quality(-1).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_SHOVEL).weight(15).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_PICKAXE).weight(15).quality(1).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_BOOTS).weight(15).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_CHESTPLATE).weight(15).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_HELMET).weight(15).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GOLDEN_LEGGINGS).weight(15).apply(EnchantRandomlyLootFunction.builder()))
                                .with(ItemEntry.builder(Items.GLISTERING_MELON_SLICE).weight(5).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 12.0F))))
                                .with(ItemEntry.builder(Items.GOLDEN_HORSE_ARMOR).weight(5))
                                .with(ItemEntry.builder(Items.LIGHT_WEIGHTED_PRESSURE_PLATE).weight(5).quality(-2))
                                .with(ItemEntry.builder(Items.GOLDEN_CARROT).weight(5).quality(3).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(4.0F, 12.0F))))
                                .with(ItemEntry.builder(Items.CLOCK).weight(5))
                                .with(ItemEntry.builder(Items.GOLD_INGOT).weight(5).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(2.0F, 8.0F))))
                                .with(ItemEntry.builder(Items.BELL).weight(1))
                                .with(ItemEntry.builder(Items.ENCHANTED_GOLDEN_APPLE).weight(1).quality(4))
                                .with(ItemEntry.builder(Items.GOLD_BLOCK).weight(1).quality(2).apply(SetCountLootFunction.builder(UniformLootNumberProvider.create(1.0F, 2.0F))))
                ).build();
    }
}