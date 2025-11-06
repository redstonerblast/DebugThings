package net.tally.inflictions;

import com.mojang.serialization.Dynamic;
import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.Registries;
import net.minecraft.util.Identifier;
import net.tally.helpers.CustomRegistryHandler;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

public class InflictionInstance {
    private final Infliction type;
    private int duration;
    private int stacks;
    private int maxDuration;
    private String name;
    private int variant;
    private @Nullable Integer source;

    public @Nullable Integer getSource() {
        return source;
    }

    public int getDuration() {
        return duration;
    }

    public int getStacks() {
        return stacks;
    }

    public void setStacks(int stacks) {
        this.stacks = stacks;
    }

    public void setDuration(int duration) {
        this.duration = duration;
    }

    public void setMaxDuration(int maxDuration) {
        this.maxDuration = maxDuration;
    }

    public void setFullDuration(int duration) {
        this.duration = duration;
        this.maxDuration = duration;
    }

    public String getName() {
        return name;
    }

    public Infliction getType() {
        return type;
    }

    public int getMaxDuration() {
        return maxDuration;
    }

    public int variant() {
        return variant;
    }

    public InflictionInstance(
            Infliction type,
            int duration,
            int stacks,
            String name,
            int variant
    ) {
        this(type, duration, stacks, name, variant, duration);
    }

    public InflictionInstance(
            Infliction type,
            int duration,
            int stacks,
            String name,
            int variant,
            @Nullable Integer source
    ) {
        this(type, duration, stacks, name, variant, duration, source);
    }

    public InflictionInstance(
            Infliction type,
            int duration,
            int stacks,
            String name,
            int variant,
            int maxDuration
    ) {
        this(type, duration, stacks, name, variant, duration, null);
    }

    public InflictionInstance(
            Infliction type,
            int duration,
            int stacks,
            String name,
            int variant,
            int maxDuration,
            @Nullable Integer source
    ) {
        this.type = type;
        this.duration = duration;
        this.stacks = stacks;
        this.name = name;
        this.variant = variant;
        this.maxDuration = maxDuration;
        this.source = source;
    }

    public NbtCompound writeNbt(NbtCompound nbt) {
        Identifier identifier = CustomRegistryHandler.REGISTRY_INFLICTION.getId(this.type);
        nbt.putString("id", identifier.toString());
        this.writeTypelessNbt(nbt);
        return nbt;
    }

    public void writeTypelessNbt(NbtCompound nbt) {
        nbt.putInt("stacks", this.getStacks());
        nbt.putInt("duration", this.getDuration());
        nbt.putInt("maxduration", this.getMaxDuration());
        nbt.putString("name", this.getName());
        nbt.putByte("variant", (byte)this.variant());
        if (this.getSource() != null) {
            nbt.putInt("source", this.getSource());
        }
    }

    @Nullable
    public static InflictionInstance fromNbt(NbtCompound nbt) {
        String string = nbt.getString("id");
        Infliction infliction = CustomRegistryHandler.REGISTRY_INFLICTION.get(Identifier.tryParse(string));
        return infliction == null ? null : fromNbt(infliction, nbt);
    }

    public static InflictionInstance fromNbt(Infliction type, NbtCompound nbt) {
        int i = nbt.getInt("stacks");
        int j = nbt.getInt("duration");
        String name = nbt.getString("name");
        int variant1 = nbt.getByte("variant");
        int maxDuration = nbt.getInt("maxduration");
        @Nullable Integer source = nbt.contains("source") ? nbt.getInt("source") : null;

        return new InflictionInstance(type, j, Math.max(i, 0), name, variant1, maxDuration, source);
    }
}
