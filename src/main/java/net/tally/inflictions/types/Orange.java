package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Orange extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:burn", "debugthings:inferno"),
            Map.entry("debugthings:shade", "debugthings:empty")
    );

    public Orange() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        super.applyTick(stacks, entity, source);
        entity.setFireTicks(5);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 5 == 0;
    }
}
