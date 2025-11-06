package net.tally.inflictions.types;

import net.minecraft.entity.LivingEntity;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.InflictionCategory;
import net.tally.inflictions.Inflictions;
import org.jetbrains.annotations.Nullable;

import java.util.Map;

public class Shade extends Infliction {

    public Shade() {
        super(InflictionCategory.HARMFUL);
    }

    private static final Map<String, String> COMBINES = Map.ofEntries(
            Map.entry("debugthings:red", "debugthings:empty"),
            Map.entry("debugthings:orange", "debugthings:empty"),
            Map.entry("debugthings:yellow", "debugthings:empty"),
            Map.entry("debugthings:lime", "debugthings:empty"),
            Map.entry("debugthings:green", "debugthings:empty"),
            Map.entry("debugthings:cyan", "debugthings:empty"),
            Map.entry("debugthings:light_blue", "debugthings:empty"),
            Map.entry("debugthings:blue", "debugthings:empty"),
            Map.entry("debugthings:purple", "debugthings:empty"),
            Map.entry("debugthings:magenta", "debugthings:empty"),
            Map.entry("debugthings:pink", "debugthings:empty"),
            Map.entry("debugthings:poison", "debugthings:nightshade")
    );

    @Override
    public Map<String, String> getCombines() {
        return COMBINES;
    }

    @Override
    public boolean canApply(long time, LivingEntity livingEntity) {
        return false;
    }
}
