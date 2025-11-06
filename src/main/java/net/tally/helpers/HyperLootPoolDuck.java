package net.tally.helpers;

import it.unimi.dsi.fastutil.floats.FloatArrayList;
import it.unimi.dsi.fastutil.ints.IntArrayList;
import org.spongepowered.asm.mixin.Unique;

public interface HyperLootPoolDuck {
    FloatArrayList getPositiveDelta();
    @Unique
    void setPositiveDelta(FloatArrayList positiveDelta);

    FloatArrayList getNegativeDelta();
    @Unique
    void setNegativeDelta(FloatArrayList negativeDelta);

    int getQualDotWeight();
    @Unique
    void setQualDotWeight(int qualDotWeight);

    float getQualDotDeltaPositive();
    @Unique
    void setQualDotDeltaPositive(float qualDotDeltaPositive);

    float getQualDotDeltaNegative();
    @Unique
    void setQualDotDeltaNegative(float qualDotDeltaNegative);

    int getWeightSum();
    @Unique
    void setWeightSum(int weightSum);

    int getMaxQuality();
    @Unique
    void setMaxQuality(int maxQuality);

    int getMinQuality();
    @Unique
    void setMinQuality(int minQuality);
}