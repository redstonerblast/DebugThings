package net.tally.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtCompound;

public interface NbtComponent extends ComponentV3 {
    NbtCompound getValue();
    void setValue(NbtCompound value);
}
