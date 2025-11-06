package net.tally.inflictions.types;

import net.tally.DebugThings;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;

import java.util.Map;
import java.util.stream.Collectors;

public class Curse extends Infliction {

    public Curse() {
        super(InflictionCategory.HARMFUL);
    }

    @Override
    public Map<String, String> getCombines() {
        Map<String, String> COMBINES = DebugThings.getCategory(InflictionCategory.BENEFICIAL).stream().collect(Collectors.toMap(Infliction::getId, i -> "debugthings:empty"));
        COMBINES.put("debugthings:brittle", "debugthings:forsaken");
        return COMBINES;
    }
}
