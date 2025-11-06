package net.tally.inflictions.types;

import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;

import java.util.Map;

public class Magenta extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:shade", "debugthings:empty")
    );

    public Magenta() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
