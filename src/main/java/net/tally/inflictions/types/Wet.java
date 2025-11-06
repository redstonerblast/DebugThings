package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Wet extends Infliction {

    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:burn", "debugthings:empty"),
            Map.entry("debugthings:inferno", "debugthings:empty"),
            Map.entry("debugthings:frost", "debugthings:frost")
    );

    public Wet() {
        super(InflictionCategory.NEUTRAL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        super.applyTick(stacks, entity, source);
        entity.extinguish();
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }

    @Override
    public boolean combineOverrideOther(Infliction base, Infliction inputA, Infliction inputB) {
        return !(base == Inflictions.FROST || inputB == Inflictions.INFERNO);
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return false;
    }
}
