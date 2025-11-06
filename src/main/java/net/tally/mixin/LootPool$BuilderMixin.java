package net.tally.mixin;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.entry.LeafEntry;
import net.minecraft.loot.entry.LootPoolEntry;
import net.tally.helpers.HyperLootPoolDuck;
import org.spongepowered.asm.mixin.*;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(LootPool.Builder.class)
public abstract class LootPool$BuilderMixin {
    @Unique
    private final FloatArrayList qualities = FloatArrayList.of();
    @Unique
    final FloatArrayList positiveDelta = FloatArrayList.of();
    @Unique
    final FloatArrayList negativeDelta = FloatArrayList.of();
    @Unique
    float qualDotDeltaPositive;
    @Unique
    float qualDotDeltaNegative;
    @Unique
    private int maxQuality = Integer.MIN_VALUE;
    @Unique
    private int minQuality = Integer.MAX_VALUE;
    @Unique
    private int qualDotWeight;
    @Unique
    private int weightSum;
    @Unique
    private boolean nonZeroQualityExists = false;
    @Inject(
            method = "with",
            at = @At("RETURN")
    )
    private void withCalculation(LootPoolEntry.Builder<?> entry, CallbackInfoReturnable<LootPool.Builder> cir) {
        if (entry.build() instanceof LeafEntry leafEntry) {
            int quality = ((LeafEntryAccessor)leafEntry).getQuality();
            int weight = ((LeafEntryAccessor)leafEntry).getWeight();
            float negWeight = -1 * weight;
            qualities.add(quality);
            negativeDelta.add(negWeight);
            positiveDelta.add(negWeight);
            weightSum += weight;
            qualDotWeight += weight * quality;
            qualDotDeltaPositive += negWeight * quality;
            qualDotDeltaNegative = qualDotDeltaPositive;
            maxQuality = Math.max(maxQuality, quality);
            minQuality = Math.min(minQuality, quality);
            if (quality != 0) {
                nonZeroQualityExists = true;
            }
        } else {
            qualities.add(0);
        }
    }
    @ModifyReturnValue(
            method = "build",
            at = @At("RETURN")
    )
    private LootPool modifyBuild(LootPool original) {
        if (nonZeroQualityExists) {
            qualDotDeltaPositive /= weightSum; qualDotDeltaNegative = qualDotDeltaPositive;
            int maxCount = 0, minCount = 0;
            IntArrayList positiveIndices = IntArrayList.of(), negativeIndices = IntArrayList.of();
            for (int i = 0; i < qualities.size(); i++) {
                positiveDelta.set(i, positiveDelta.getFloat(i) / weightSum);
                negativeDelta.set(i, negativeDelta.getFloat(i) / weightSum);
                float q = qualities.getFloat(i);
                if (q == maxQuality) {
                    positiveIndices.add(i);
                    maxCount++;
                }
                if (q == minQuality) {
                    negativeIndices.add(i);
                    minCount++;
                }
            }
            float probMax = (float) 1 / maxCount, probMin = (float) 1 / minCount;
            for (Integer positiveIndex : positiveIndices) {
                float q = qualities.getFloat(positiveIndex);
                positiveDelta.set(positiveIndex.intValue(), positiveDelta.getFloat(positiveIndex) + probMax);
                qualDotDeltaPositive += q * probMax;
            }
            for (Integer negativeIndex : negativeIndices) {
                float q = qualities.getFloat(negativeIndex);
                negativeDelta.set(negativeIndex.intValue(), negativeDelta.getFloat(negativeIndex) + probMin);
                qualDotDeltaNegative += q * probMin;
            }
            ((HyperLootPoolDuck) original).setMaxQuality(maxQuality);
            ((HyperLootPoolDuck) original).setMinQuality(minQuality);
            ((HyperLootPoolDuck) original).setPositiveDelta(positiveDelta);
            ((HyperLootPoolDuck) original).setNegativeDelta(negativeDelta);
            ((HyperLootPoolDuck) original).setQualDotDeltaPositive(qualDotDeltaPositive);
            ((HyperLootPoolDuck) original).setQualDotDeltaNegative(qualDotDeltaNegative);
            ((HyperLootPoolDuck) original).setQualDotWeight(qualDotWeight);
            ((HyperLootPoolDuck) original).setWeightSum(weightSum);
        }
        return original;
    }
}