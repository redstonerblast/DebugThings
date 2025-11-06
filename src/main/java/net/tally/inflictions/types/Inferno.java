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

public class Inferno extends Infliction {

    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:wet", "debugthings:empty"),
            Map.entry("debugthings:entangled", "debugthings:empty"),
            Map.entry("debugthings:rooted", "debugthings:empty"),
            Map.entry("debugthings:frost", "debugthings:empty")
    );

    public Inferno() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }

    @Override
    public boolean combineOverrideSelf(Infliction base, Infliction inputA, Infliction inputB) {
        return !(inputB == Inflictions.WET || inputB == Inflictions.FROST || inputB == Inflictions.ROOTED);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        super.applyTick(stacks, entity, source);
        entity.setFireTicks(20);
        DamageSource damageSource;
        if (source != null) {
            damageSource = new DamageSource(
                    entity.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE)
                            .entryOf(DebugDamageTypes.BURN), source);
        } else {
            damageSource = new DamageSource(
                    entity.getWorld().getRegistryManager()
                            .get(RegistryKeys.DAMAGE_TYPE)
                            .entryOf(DebugDamageTypes.BURN_NAT));
        }
        entity.damage(damageSource, 2);
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 20 == 0;
    }
}