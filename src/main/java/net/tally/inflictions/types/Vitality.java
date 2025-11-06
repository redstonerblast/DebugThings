package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Vitality extends Infliction {

    public Vitality() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        super.applyTick(stacks, entity, source);
        entity.heal(entity.getMaxHealth() * (stacks / 20));
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 40 == 0;
    }
}
