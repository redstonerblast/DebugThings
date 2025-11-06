package net.tally.inflictions.types;

import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;

import java.util.Map;

public class Purple extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:shade", "debugthings:empty")
    );

    public Purple() {
        super(InflictionCategory.NEUTRAL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}

