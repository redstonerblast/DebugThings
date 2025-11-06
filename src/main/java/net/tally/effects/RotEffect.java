package net.tally.effects;

import net.minecraft.entity.attribute.EntityAttribute;
import net.minecraft.entity.attribute.EntityAttributeModifier;
import net.minecraft.entity.attribute.EntityAttributes;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;

public class RotEffect extends EffectHelper{
    public RotEffect() {
        super(
                StatusEffectCategory.HARMFUL,
                137,30,90);
    }
}