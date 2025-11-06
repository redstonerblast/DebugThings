package net.tally.inflictions.types;

import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.Inflictions;

import java.util.Map;

public class Frost extends Infliction {
    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:wet", "debugthings:frost"),
            Map.entry("debugthings:burn", "debugthings:frostburn"),
            Map.entry("debugthings:inferno", "debugthings:empty")
    );

    public Frost() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public boolean combineOverrideSelf(Infliction base, Infliction inputA, Infliction inputB) {
        return !(base == Inflictions.FROST);
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