package net.tally.inflictions;

import net.minecraft.util.Formatting;

public enum InflictionCategory {
    BENEFICIAL(Formatting.GREEN),
    HARMFUL(Formatting.RED),
    NEUTRAL(Formatting.GRAY);

    private final Formatting formatting;

    private InflictionCategory(Formatting format) {
        this.formatting = format;
    }

    public Formatting getFormatting() {
        return this.formatting;
    }
}
