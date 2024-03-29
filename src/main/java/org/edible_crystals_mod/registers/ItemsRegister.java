package org.edible_crystals_mod.registers;


import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.items.held_items.TabletItem;

public class ItemsRegister {

    public static MobEffect effects;

    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EdibleCrystalMod.MOD_ID);

    public static final RegistryObject<Item> MOD_BOOK =
            ITEMS.register("mod_book", () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> TABLET_OF_REX =
            ITEMS.register("tablet_of_rex", () -> new TabletItem(new Item.Properties()));


    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
