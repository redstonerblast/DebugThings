package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.ai.TargetPredicate;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.nbt.NbtCompound;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.InflictionInstance;
import org.jetbrains.annotations.Nullable;

import java.util.List;
import java.util.Map;

public class Confusion extends Infliction {

    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:confusion", "debugthings:rabid")
    );

    public Confusion() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
