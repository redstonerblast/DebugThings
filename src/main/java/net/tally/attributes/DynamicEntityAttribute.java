package net.tally.attributes;

import net.minecraft.entity.attribute.EntityAttribute;

public class DynamicEntityAttribute extends EntityAttribute {
    public DynamicEntityAttribute(String translationKey) {
        super(translationKey, Double.NaN);
    }
}