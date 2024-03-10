package org.edible_crystals_mod.items;


import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.itemproperties.CrystalItem;

public class CrystalItems {

    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EdibleCrystalMod.MOD_ID);
    public static final RegistryObject<Item> EDIBLE_CRYSTAL_FRAGMENT =
            ITEMS.register(CrystalItem.UN_FED_FRAGMENT, () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EDIBLE_CRYSTAL =
            ITEMS.register(CrystalItem.UN_FED.getA(), () -> new Item(new Item.Properties().food(CrystalItem.UN_FED.getB())));
    public static final RegistryObject<Item> EDIBLE_CRYSTAL_CARROT =
            ITEMS.register(CrystalItem.CARROT.getA(), () -> new Item(new Item.Properties().food(CrystalItem.GOLDEN_APPLE.getB())));
    public static final RegistryObject<Item> EDIBLE_CRYSTAL_BEET =
            ITEMS.register(CrystalItem.BEET.getA(), () -> new Item(new Item.Properties().food(CrystalItem.GOLDEN_APPLE.getB())));
    public static final RegistryObject<Item> EDIBLE_CRYSTAL_GOLDEN_APPLE =
            ITEMS.register(CrystalItem.GOLDEN_APPLE.getA(), () -> new Item(new Item.Properties().food(CrystalItem.GOLDEN_APPLE.getB())));
    public static final RegistryObject<Item> EDIBLE_CRYSTAL_GOLDEN_APPLE_JUICED =
            ITEMS.register(CrystalItem.GOLDEN_APPLE_JUICED.getA(), () -> new CrystalItem(new Item.Properties().food(CrystalItem.GOLDEN_APPLE_JUICED.getB())));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
