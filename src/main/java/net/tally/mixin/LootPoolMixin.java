package net.tally.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import com.llamalad7.mixinextras.sugar.Share;
import com.llamalad7.mixinextras.sugar.ref.LocalFloatRef;
import com.llamalad7.mixinextras.sugar.ref.LocalIntRef;
import com.llamalad7.mixinextras.sugar.ref.LocalRef;
import com.mojang.datafixers.util.Either;
import com.mojang.serialization.Codec;
import com.mojang.serialization.DataResult;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.LootChoice;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.context.LootContext;
import net.minecraft.loot.entry.LootPoolEntry;
import net.tally.helpers.HyperLootPoolDuck;
import org.apache.commons.lang3.mutable.MutableInt;
import org.spongepowered.asm.mixin.Debug;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Iterator;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;

/*                                  ====================
                                    HOW THIS THING WORKS
                                    ====================

    This implementation of the loot pool uses the concept of expected value in order
    to change the probabilities of entries in the pool to match a given luck amount.
    Each entry has an integer quality that determines how good is that entry in relation
    to the rest of the pool. Then the expected value of the pool would be calculated
    as the sum of the product of the quality and the probability of each entry:

                            E0 = q0 * p0 + q1 * p1 + ... + qn * pn;

    Which can be thought as the average value of the pool.
    When the luck value tends to -inf then the expected quality of the pool tends to
    the minimum quality in the pool. When the luck value tends to inf then the expected
    value tends to the maximum amount of the pool. The expected value is thus a function
    of the luck of the player. A sigmoid function was chosen for this:

                                             Q  -  q
                    E(luck) = q +  ----------------------------
                                   /Q  - q     \   -luck / 5
                                  | ------  - 1 | e          + 1
                                   \E0 - q     /

    Where q is the minimum quality, Q the maximum quality, E0 the expected value without
    luck (when luck = 0). One can check that as luck -> inf then the function goes to
    the maximum and when luck -> -inf then the function goes towards the minimum luck.

    In order to obtain the maximum quality, only the entries with the maximum quality
    should have a probability and the same is true for the minimum (with the worse entries).
    In other words, the rest of the pool should have probability 0. Therefore, we can
    think that as luck increases the maximum quality entries increase their probability
    while the rest decreases their probability and when luck decreases the minimum quality
    entries increase their probability and the rest decreases. We can imagine the hyperplane
    where the initial probabilities live:

                            p0 + p1 + p2 + p3 + ... + pn = 1

    Then, as luck increases the vector P = (p0, p1, ..., pn) would tend to the vector
    S = (0, 0, 1/n, 0, ..., 0, 1/n, ...) where only the n entries that have maximum
    quality would survive with an equal probability 1/n each. Something similar would
    happen when luck decreases but only the entries with minimum probability surviving.
    Then in each of these cases the vector S - P encodes a direction in which the
    probability would need to move in order to reach either the maximum value or the
    minimum. We'll call this vector Delta.

    Now we only need to figure out how much along Delta we need to move in order to
    reach a wanted expected value, so we solve for k in the equation:

                            E = Qual . (P0 + k * Delta)

    Here . is the canonical dot product, Qual is the vector of qualities of the pool,
    P0 is the initial probabilities vector and k is the scaling factor needed in order
    to change the probabilities the right amount to reach E. We get

                                  E - Qual . P0
                            k =  ----------------
                                   Qual . Delta

    Which will have a solution as long as Qual and Delta are not orthogonal.
    Therefore, the new probabilities would need to be P + k * Delta. We can then
    multiply them by 100 and floor to obtain a configuration of weights. To calculate
    the initial probability vector we can just divide each weight by the sum of weights.
    Thus, Qual . P0 = Qual . W0 / WeightSum, where W0 is the vector of initial weights.

    In the class you will find these values:
    positiveDelta = Delta (when luck is positive)
    negativeDelta = Delta (when luck is negative)
    qualDotDeltaPositive = Qual . Delta (when luck is positive)
    qualDotDeltaNegative = Qual . Delta (when luck is negative)
    qualDotWeight = Qual . W0
    weightSum, maxQuality, minQuality (self-explanatory)

    The values are calculated when the loot pool is being built in the Builder class.
    Here they are just used to calculate the new weights.
 */

@Mixin(LootPool.class)
public abstract class LootPoolMixin implements HyperLootPoolDuck {
    @Unique
    public FloatArrayList positiveDelta = FloatArrayList.of();
    @Unique
    public FloatArrayList negativeDelta = FloatArrayList.of();
    @Unique
    public float qualDotDeltaPositive;
    @Unique
    public float qualDotDeltaNegative;
    @Unique
    public int qualDotWeight;
    @Unique
    public int weightSum;
    @Unique
    public int maxQuality;
    @Unique
    public int minQuality;
    @ModifyExpressionValue(
            method = "<clinit>",
            at = @At(
                    value = "INVOKE",
                    target = "Lcom/mojang/serialization/codecs/RecordCodecBuilder;create(Ljava/util/function/Function;)Lcom/mojang/serialization/Codec;",
                    remap = false
            )
    )
    private static Codec<LootPool> modifyCodec(Codec<LootPool> original) {
        return Codec.either(
            original,
            RecordCodecBuilder.<LootPool>create(
                instance -> instance.group(
                        original.fieldOf("luck").forGetter(Function.identity()),
                    Codec.FLOAT.listOf().fieldOf("delta+").forGetter(pool -> ((HyperLootPoolDuck) pool).getPositiveDelta()),
                    Codec.FLOAT.listOf().fieldOf("delta-").forGetter(pool -> ((HyperLootPoolDuck) pool).getNegativeDelta()),
                    Codec.FLOAT.fieldOf("qual.delta+").forGetter(pool -> ((HyperLootPoolDuck) pool).getQualDotDeltaPositive()),
                    Codec.FLOAT.fieldOf("qual.delta-").forGetter(pool -> ((HyperLootPoolDuck) pool).getQualDotDeltaPositive()),
                    Codec.INT.fieldOf("qual.weight").forGetter(pool -> ((HyperLootPoolDuck) pool).getQualDotWeight()),
                    Codec.INT.fieldOf("weight_sum").forGetter(pool -> ((HyperLootPoolDuck) pool).getWeightSum()),
                    Codec.INT.fieldOf("max_quality").forGetter(pool -> ((HyperLootPoolDuck) pool).getMaxQuality()),
                    Codec.INT.fieldOf("min_quality").forGetter(pool -> ((HyperLootPoolDuck) pool).getMinQuality())
                ).apply(instance, (o, pd, nd, m, n, w, s, x, u) -> o)
            )
        ).comapFlatMap(
            either -> either.left()
                .or(either::right)
                .map(DataResult::success)
                .orElseGet(
                    () -> DataResult.error(() -> "No Codec found for LootPool")
                ),
            Either::left
        );
    }
    @Unique
    @Override
    public FloatArrayList getPositiveDelta() {
        return positiveDelta;
    }
    @Unique
    @Override
    public void setPositiveDelta(FloatArrayList positiveDelta) {
        this.positiveDelta = positiveDelta;
    }
    @Unique
    @Override
    public FloatArrayList getNegativeDelta() {
        return negativeDelta;
    }
    @Unique
    @Override
    public void setNegativeDelta(FloatArrayList negativeDelta) {
        this.negativeDelta = negativeDelta;
    }
    @Unique
    @Override
    public int getQualDotWeight() {
        return qualDotWeight;
    }
    @Unique
    @Override
    public void setQualDotWeight(int qualDotWeight) {
        this.qualDotWeight = qualDotWeight;
    }
    @Unique
    @Override
    public float getQualDotDeltaPositive() {
        return qualDotDeltaPositive;
    }
    @Unique
    @Override
    public void setQualDotDeltaPositive(float qualDotDeltaPositive) {
        this.qualDotDeltaPositive = qualDotDeltaPositive;
    }
    @Unique
    @Override
    public float getQualDotDeltaNegative() {
        return qualDotDeltaNegative;
    }
    @Unique
    @Override
    public void setQualDotDeltaNegative(float qualDotDeltaNegative) {
        this.qualDotDeltaNegative = qualDotDeltaNegative;
    }
    @Unique
    @Override
    public int getWeightSum() {
        return weightSum;
    }
    @Unique
    @Override
    public void setWeightSum(int weightSum) {
        this.weightSum = weightSum;
    }
    @Unique
    @Override
    public int getMaxQuality() {
        return maxQuality;
    }
    @Unique
    @Override
    public void setMaxQuality(int maxQuality) {
        this.maxQuality = maxQuality;
    }
    @Unique
    @Override
    public int getMinQuality() {
        return minQuality;
    }
    @Unique
    @Override
    public void setMinQuality(int minQuality) {
        this.minQuality = minQuality;
    }
    @Unique
    public float luckExpectedValue(float luck) {
        float expectedValue = weightSum != 0 ? (float) qualDotWeight / weightSum : 0;
        if (luck == 0) return expectedValue;
        double expLuck = Math.exp((double) (-1 * luck) / 5);
        double Q = maxQuality - minQuality; double E = expectedValue - minQuality;
        double C = Q / E - 1;
        double val1 = 1 / (1 + C * expLuck);
        return (float) (Q * val1) + minQuality;
    }
    @Unique
    public float kScalingFactor(float luckExpectedValue, float luck) {
        float v1 = weightSum != 0 ? (float) qualDotWeight / weightSum : 0;
        float dotProduct = luck > 0 ? qualDotDeltaPositive : ((luck < 0) ? qualDotDeltaNegative : 0);
        return dotProduct != 0 ? (luckExpectedValue - v1) / dotProduct : 0;
    }
    @Unique
    private int calculateNewWeight(float luck, int prevWeight, int counter, float kFactor) {
        if (luck != 0 && positiveDelta != null && negativeDelta != null) {
            float delta = 0;
            if (luck > 0 && positiveDelta.size() > counter) {
                delta = positiveDelta.getFloat(counter);
            } else if (luck < 0 && negativeDelta.size() > counter) {
                delta = negativeDelta.getFloat(counter);
            }
            double newProb = weightSum != 0 ? (double) prevWeight / weightSum + kFactor * delta : 0;
            newProb = Math.max((Math.min(newProb, 1d)),0d);
            return (int) Math.floor(newProb * 100);
        }
        return prevWeight;
    }
    @Inject(
            method = "supplyOnce(Ljava/util/function/Consumer;Lnet/minecraft/loot/context/LootContext;)V",
            at = @At("HEAD")
    )
    private void initializeVariablesForCalculations(Consumer<ItemStack> consumer, LootContext context, CallbackInfo ci,
                                              @Share("kFactor") LocalFloatRef kFactorRef,
                                              @Share("counterForWeightCalculation") LocalIntRef counterRef,
                                              @Share("counterForLootGeneration") LocalIntRef generationCounterRef,
                                              @Share("weightsListForLootGeneration") LocalRef<IntArrayList> weightsListRef) {
        float luck = context.getLuck() / 4f;
        float eV = luckExpectedValue(luck);
        kFactorRef.set(kScalingFactor(eV, luck));
        counterRef.set(-1);
        generationCounterRef.set(-1);
        weightsListRef.set(IntArrayList.of());
    }
    @ModifyVariable(
            method = "supplyOnce(Ljava/util/function/Consumer;Lnet/minecraft/loot/context/LootContext;)V",
            at = @At(value = "LOAD", ordinal = 0),
            allow = 1
    )
    private Iterator<List<LootPoolEntry>> countAndSaveLuckDelta(Iterator<List<LootPoolEntry>> iterator,
                                                                @Share("counterForWeightCalculation") LocalIntRef counterRef) {
        counterRef.set(counterRef.get() + 1);
        return iterator;
    }
    @WrapOperation(
            method = "supplyOnce(Ljava/util/function/Consumer;Lnet/minecraft/loot/context/LootContext;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/entry/LootPoolEntry;expand(Lnet/minecraft/loot/context/LootContext;Ljava/util/function/Consumer;)Z")
    )
    private boolean modifyExpand(LootPoolEntry instance, LootContext context, Consumer<LootChoice> lootConsumer, Operation<Boolean> original,
                                 @Share("kFactor") LocalFloatRef kFactorRef,
                                 @Share("counterForWeightCalculation") LocalIntRef counterRef,
                                 @Share("weightsListForLootGeneration") LocalRef<IntArrayList> weightsListRef,
                                 @Local List<LootChoice> list,
                                 @Local MutableInt mutableInt) {
        return instance.expand(context, choice -> {
            int newWeight = calculateNewWeight(context.getLuck(), choice.getWeight(0), counterRef.get(), kFactorRef.get());
            if (newWeight > 0) {
                list.add(choice);
                mutableInt.add(newWeight);
                weightsListRef.get().add(newWeight);
            }
        });
    }
    @ModifyVariable(
            method = "supplyOnce(Ljava/util/function/Consumer;Lnet/minecraft/loot/context/LootContext;)V",
            at = @At(value = "LOAD", ordinal = 2),
            allow = 1
    )
    private Iterator<List<LootChoice>> countForLootGeneration(Iterator<List<LootChoice>> iterator,
                                                                 @Share("counterForLootGeneration") LocalIntRef generationCounterRef) {
        generationCounterRef.set(generationCounterRef.get() + 1);
        return iterator;
    }
    @WrapOperation(
            method = "supplyOnce(Ljava/util/function/Consumer;Lnet/minecraft/loot/context/LootContext;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/loot/LootChoice;getWeight(F)I")
    )
    private int modifyWeight(LootChoice lootChoice, float luck, Operation<Integer> original,
                             @Share("kFactor") LocalFloatRef kFactorRef,
                             @Share("counterForLootGeneration") LocalIntRef counterRef,
                             @Share("weightsListForLootGeneration") LocalRef<IntArrayList> weightsListRef) {
        IntArrayList weightsList = weightsListRef.get();
        int i = counterRef.get();
        return weightsList.size() > i ? weightsList.getInt(i) : original.call(lootChoice, 0F);
    }
}