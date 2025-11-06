package net.tally.components;

import dev.onyxstudios.cca.api.v3.component.sync.AutoSyncedComponent;
import net.minecraft.entity.Entity;
import net.minecraft.nbt.NbtCompound;
import net.tally.DebugThings;

class SyncedRenderInflicts implements NbtComponent, AutoSyncedComponent {
    private NbtCompound value;
    private final Entity provider;

    public SyncedRenderInflicts(Entity provider) { this.provider = provider; }

    public void setValue(NbtCompound value) {
        this.value = value;
        DebugThingsComponents.INFLICT_RENDER.sync(this.provider);
    }

    @Override
    public NbtCompound getValue() {
        return value;
    }

    @Override
    public void readFromNbt(NbtCompound nbtCompound) {
        this.value = nbtCompound.getCompound("value");
    }

    @Override
    public void writeToNbt(NbtCompound nbtCompound) {
        nbtCompound.put("value", this.value != null ? this.value : new NbtCompound());
    }
}
