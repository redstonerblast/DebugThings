package net.tally.loot.conditions;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.entity.LivingEntity;
import net.minecraft.loot.condition.LootCondition;
import net.minecraft.loot.condition.LootConditionType;
import net.minecraft.loot.condition.ValueCheckLootCondition;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.context.LootContextParameters;
import net.minecraft.loot.operator.BoundedIntUnaryOperator;
import net.minecraft.loot.provider.number.LootNumberProvider;
import net.minecraft.predicate.NumberRange;
import net.minecraft.util.Identifier;
import net.tally.components.DebugThingsComponents;


public class InflictionLootCondition implements LootCondition {

    public static final Codec<InflictionLootCondition> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Identifier.CODEC.fieldOf("infliction").forGetter(InflictionLootCondition::getInfliction),
            BoundedIntUnaryOperator.CODEC.fieldOf("amount").forGetter(InflictionLootCondition::getRange)
    ).apply(instance, InflictionLootCondition::new));
    public static final LootConditionType TYPE = new LootConditionType(CODEC);

    private final Identifier infliction;
    private final BoundedIntUnaryOperator range;

    public InflictionLootCondition(Identifier origin, BoundedIntUnaryOperator range) {
        this.infliction = origin;
        this.range = range;
    }

    @Override
    public LootConditionType getType() {
        return TYPE;
    }

    @Override
    public boolean test(LootContext lootContext) {

        if (!(lootContext.get(LootContextParameters.THIS_ENTITY) instanceof LivingEntity livingEntity)) {
            return false;
        }

        int amount = DebugThingsComponents.getInflictRender(livingEntity).getInt(infliction.toString());

        return range.test(lootContext, amount);
    }

    public Identifier getInfliction() {
        return infliction;
    }

    public BoundedIntUnaryOperator getRange() {
        return range;
    }

    public static LootCondition.Builder builder(Identifier value, BoundedIntUnaryOperator range) {
        return () -> new InflictionLootCondition(value, range);
    }
}