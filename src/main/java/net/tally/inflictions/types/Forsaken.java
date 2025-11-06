package net.tally.inflictions.types;

import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;

import java.util.Map;

public class Forsaken extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:sanctified", "debugthings:empty")
    );

    public Forsaken() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
