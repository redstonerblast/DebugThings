package net.tally.inflictions.types;

import net.tally.DebugThings;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;

import java.util.Map;
import java.util.stream.Collectors;

public class Bless extends Infliction {
    public Bless() {
        super(InflictionCategory.BENEFICIAL);
    }

    @Override
    public Map<String, String> getCombines() {
        Map<String, String> COMBINES = DebugThings.getCategory(InflictionCategory.HARMFUL).stream().collect(Collectors.toMap(Infliction::getId, i -> "debugthings:empty"));
        COMBINES.put("debugthings:fortify", "debugthings:sanctified");
        return COMBINES;
    }
}
