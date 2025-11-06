package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.registry.RegistryKeys;
import net.tally.holders.DebugDamageTypes;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.InflictionHandler;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Holy extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:unholy", "debugthings:empty")
    );

    public Holy() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        if (stacks > 30) {
            InflictionHandler.clearInflictions(entity, Inflictions.HOLY, stacks - 30);
        }
        super.applyTick(stacks, entity, source);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}

