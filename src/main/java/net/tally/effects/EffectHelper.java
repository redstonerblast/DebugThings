package net.tally.effects;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.entity.player.PlayerEntity;

import java.util.regex.Pattern;

public class EffectHelper extends StatusEffect {
    public EffectHelper() {
        super(
                StatusEffectCategory.BENEFICIAL, // whether beneficial or harmful for entities
                0x98D982); // color in RGB
    }

    public EffectHelper(StatusEffectCategory category, int red, int green, int blue) {
        super(category, // whether beneficial or harmful for entities
                Math.toIntExact(Long.valueOf(Integer.toHexString(red) + Integer.toHexString(green) + Integer.toHexString(blue), 16)));
    }

    // This method is called every tick to check whether it should apply the status effect or not
    @Override
    public boolean canApplyUpdateEffect(int duration, int amplifier) {
        return duration%tickRate() == 0;
    }

    public int tickRate() {
        return 1;
    }

    // This method is called when it applies the status effect. We implement custom functionality here.
    @Override
    public void applyUpdateEffect(LivingEntity entity, int amplifier) {
    }
}