package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Regen extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:red", "debugthings:vitality")
    );

    public Regen() {
        super(InflictionCategory.BENEFICIAL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        super.applyTick(stacks, entity, source);
        entity.heal(stacks);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 20 == 0;
    }
}
