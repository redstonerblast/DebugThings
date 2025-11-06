package net.tally.effects;

import net.minecraft.entity.effect.StatusEffectCategory;

public class WoundedEffect extends EffectHelper{
    public WoundedEffect() {
        super(
                StatusEffectCategory.HARMFUL,
                137,70,70);
    }
}
