package net.tally.loot.modifiers;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.enchantment.Enchantment;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.enchantment.EnchantmentLevelEntry;
import net.minecraft.item.EnchantedBookItem;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.function.*;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.loot.provider.number.LootNumberProviderType;
import net.minecraft.loot.provider.number.LootNumberProviderTypes;
import net.minecraft.registry.Registries;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.dynamic.Codecs;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.function.BiFunction;

public class RollsFunction extends ConditionalLootFunction {
    public static final Codec<RollsFunction> CODEC = RecordCodecBuilder.create((instance) -> {
        return addConditionsField(instance).and(instance.group(LootNumberProviderTypes.CODEC.fieldOf("rolls").forGetter((function) -> {
            return function.rolls;
        }), LootNumberProviderTypes.CODEC.fieldOf("bonus_rolls").forGetter((function) -> {
            return function.bonusRolls;
        }), LootFunctionTypes.CODEC.fieldOf("reference").forGetter((function) -> {
            return function.lootFunction;
        }))).apply(instance, RollsFunction::new);
    });

    public static final LootFunctionType TYPE = new LootFunctionType(CODEC);

    private final LootNumberProvider rolls;
    private final LootNumberProvider bonusRolls;
    private final LootFunction lootFunction;

    public RollsFunction(List<LootCondition> conditions, LootNumberProvider rolls, LootNumberProvider bonusRolls, LootFunction lootFunction) {
        super(conditions);
        this.rolls = rolls;
        this.bonusRolls = bonusRolls;
        this.lootFunction = lootFunction;
    }

    public LootFunctionType getType() {
        return TYPE;
    }

    public ItemStack process(ItemStack stack, LootContext context) {
        for (int i = 0; i < this.rolls.nextInt(context)+this.bonusRolls.nextInt(context); i++) {
            lootFunction.apply(stack, context);
        }
        return stack;
    }
}
