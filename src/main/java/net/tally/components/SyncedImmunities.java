package net.tally.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.nbt.NbtString;

import java.util.List;

class SyncedImmunities implements NbtListComponent, AutoSyncedComponent {
    private NbtList value;
    private final Entity provider;

    public SyncedImmunities(Entity provider) { this.provider = provider; }

    public void setValue(NbtList value) {
        this.value = value;
        DebugThingsComponents.IMMUNITIES.sync(this.provider);
    }

    @Override
    public NbtList getValue() {
        return value;
    }

    @Override
    public void addValue(String val) {
        this.value.add(NbtString.of(val));
        DebugThingsComponents.IMMUNITIES.sync(this.provider);
    }

    @Override
    public void removeValue(String val) {
        this.value.remove(NbtString.of(val));
        DebugThingsComponents.IMMUNITIES.sync(this.provider);
    }

    @Override
    public boolean hasValue(String val) {
        return this.value.contains(NbtString.of(val));
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.value = nbtCompound.getList("value", NbtElement.STRING_TYPE);
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.put("value", this.value != null ? this.value : new NbtList());
    }
}
