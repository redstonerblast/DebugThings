package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Yellow extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:shade", "debugthings:empty")
    );

    public Yellow() {
        super(InflictionCategory.BENEFICIAL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
