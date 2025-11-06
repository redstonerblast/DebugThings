package net.tally.loot.conditions;

import com.google.common.collect.ImmutableSet;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.LootConditionTypes;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameter;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.function.LootFunctionType;
import net.minecraft.predicate.item.ItemPredicate;
import net.minecraft.util.dynamic.Codecs;

import java.util.Optional;
import java.util.Set;

public record MatchItemCondition(Optional<ItemPredicate> predicate) implements LootCondition {
    public static final Codec<MatchItemCondition> CODEC = RecordCodecBuilder.create((instance) -> {
        return instance.group(Codecs.createStrictOptionalFieldCodec(ItemPredicate.CODEC, "predicate").forGetter(MatchItemCondition::predicate)).apply(instance, MatchItemCondition::new);
    });

    public MatchItemCondition(Optional<ItemPredicate> predicate) {
        this.predicate = predicate;
    }

    public static final LootConditionType TYPE = new LootConditionType(CODEC);

    public LootConditionType getType() {
        return LootConditionTypes.MATCH_TOOL;
    }

    public Set<LootContextParameter<?>> getRequiredParameters() {
        return ImmutableSet.of(LootContextParameters.TOOL);
    }

    public boolean test(LootContext lootContext) {
        ItemStack itemStack = (ItemStack)lootContext.get(LootContextParameters.TOOL);
        return itemStack != null && (this.predicate.isEmpty() || ((ItemPredicate)this.predicate.get()).test(itemStack));
    }

    public static Builder builder(net.minecraft.predicate.item.ItemPredicate.Builder predicate) {
        return () -> {
            return new MatchItemCondition(Optional.of(predicate.build()));
        };
    }

    public Optional<ItemPredicate> predicate() {
        return this.predicate;
    }
}
