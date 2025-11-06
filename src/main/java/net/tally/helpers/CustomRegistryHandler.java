package net.tally.helpers;

import com.mojang.serialization.Lifecycle;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;
import net.tally.DebugThings;
import net.tally.inflictions.Infliction;
import net.tally.inflictions.Inflictions;

public class CustomRegistryHandler {
    public static final RegistryKey<Registry<Infliction>> INFLICTION = of("infliction");

    public static final Registry<Infliction> REGISTRY_INFLICTION = Registries.create(
            INFLICTION,
            registry -> Inflictions.BLEED);


    private static <T> RegistryKey<Registry<T>> of(String id) {
        return RegistryKey.ofRegistry(new Identifier("debugthings", id));
    }

    public static void init() {
        DebugThings.LOGGER.info("Loaded Registry Handler");
    }
}
