package net.tally.items;

import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.minecraft.item.FoodComponent;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class DebugItems {
    private DebugItems() {}

    public static final FoodComponent DUMMY_FOOD_COMPONENT = new FoodComponent.Builder()
            .build();

    public static final Item DUMMY_ITEM = register("dummy_item", new DummyItem(new FabricItemSettings()));
    public static final Item DUMMY_FOOD = register("dummy_food", new DummyFood(new FabricItemSettings().food(DUMMY_FOOD_COMPONENT)));

    public static <T extends Item> T register(String path, T item) {
        return Registry.register(Registries.ITEM, new Identifier("debugthings", path), item);
    }

    public static void initialize() {
    }
}