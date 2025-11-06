package net.tally.inflictions.types;

import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;

import java.util.Map;

public class Fortify extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:bless", "debugthings:sanctified"),
            Map.entry("debugthings:brittle", "debugthings:empty")
    );

    public Fortify() {
        super(InflictionCategory.BENEFICIAL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
