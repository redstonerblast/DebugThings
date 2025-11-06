package net.tally.loot.modifiers;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootTableReporter;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.function.*;
import net.minecraft.util.collection.DataPool;
import net.minecraft.util.collection.WeightedList;

import java.util.List;
import java.util.function.BiFunction;

public class WeightedListFunction implements LootFunction {
    public static final Codec<WeightedListFunction> CODEC = RecordCodecBuilder.create(builder -> builder.group(
                    DataPool.createCodec(LootFunctionTypes.CODEC).fieldOf("terms").forGetter(WeightedListFunction::getTerms)
            ).apply(builder, WeightedListFunction::new)
    );
    public static final LootFunctionType TYPE = new LootFunctionType(CODEC);
    private final DataPool<LootFunction> terms;

    private WeightedListFunction(DataPool<LootFunction> terms) {
        this.terms = terms;
    }

    public DataPool<LootFunction> getTerms() {
        return terms;
    }

    public ItemStack apply(ItemStack itemStack, LootContext lootContext) {
        return (ItemStack)this.terms.getDataOrEmpty(lootContext.getRandom()).get().apply(itemStack, lootContext);
    }

    public void validate(LootTableReporter reporter) {
        LootFunction.super.validate(reporter);

        //for(int i = 0; i < this.terms.getEntries().size(); ++i) {
            //((LootFunction)this.terms.getEntries().get(i)).validate(reporter.makeChild(".function[" + i + "]"));
        //}

    }

    public LootFunctionType getType() {
        return TYPE;
    }
}
