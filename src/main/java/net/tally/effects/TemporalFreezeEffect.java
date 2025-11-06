package net.tally.effects;

import net.minecraft.entity.effect.StatusEffectCategory;

public class TemporalFreezeEffect extends EffectHelper{
    public TemporalFreezeEffect() {
        super(
                StatusEffectCategory.NEUTRAL,
                100,255,100);
    }
}