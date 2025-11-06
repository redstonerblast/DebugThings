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

public class Unholy extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:holy", "debugthings:empty")
    );

    public Unholy() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public void applyTick(int stacks, LivingEntity entity, @Nullable LivingEntity source) {
        if (stacks >= 15) {
            DamageSource damageSource;
            float dmg = ((InflictionHandler.getInflictions(Inflictions.SHADE, entity) * 0.25f) + 1.25f) * stacks;
            if (source != null) {
                damageSource = new DamageSource(
                        entity.getWorld().getRegistryManager()
                                .get(RegistryKeys.DAMAGE_TYPE)
                                .entryOf(DebugDamageTypes.UNHOLY), source);
            } else {
                damageSource = new DamageSource(
                        entity.getWorld().getRegistryManager()
                                .get(RegistryKeys.DAMAGE_TYPE)
                                .entryOf(DebugDamageTypes.UNHOLY_NAT));
            }
            entity.damage(damageSource, dmg);
            InflictionHandler.clearInfliction(entity, Inflictions.UNHOLY);
        }
        super.applyTick(stacks, entity, source);
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return time % 20 == 0;
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}

