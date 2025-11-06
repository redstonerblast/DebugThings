package net.tally.inflictions.types;

import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;

import java.util.Map;

public class Sanctified extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:forsaken", "debugthings:empty")
    );

    public Sanctified() {
        super(InflictionCategory.BENEFICIAL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
