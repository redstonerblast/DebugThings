package net.tally.components;

import dev.onyxstudios.cca.api.v3.component.ComponentV3;
import net.minecraft.nbt.NbtList;

import java.util.List;

public interface NbtListComponent extends ComponentV3 {
    NbtList getValue();
    void setValue(NbtList value);
    void addValue(String val);
    void removeValue(String val);
    boolean hasValue(String val);
}
