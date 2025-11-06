package net.tally.inflictions.types;

import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.Inflictions;

import java.util.Map;

public class Rooted extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:wet", "debugthings:entangled"),
            Map.entry("debugthings:inferno", "debugthings:empty"),
            Map.entry("debugthings:burn", "debugthings:empty")
    );

    public Rooted() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public boolean combineOverrideOther(Infliction base, Infliction inputA, Infliction inputB) {
        return !(inputB == Inflictions.INFERNO);
    }

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }
}
