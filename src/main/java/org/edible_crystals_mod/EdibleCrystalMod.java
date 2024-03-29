package org.edible_crystals_mod;


import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.edible_crystals_mod.registers.ModEffectsRegister;
import org.edible_crystals_mod.abilities_and_effects.effects.EffectMaps;
import org.edible_crystals_mod.block.crystalinfusiontable.InfusionTableScreen;
import org.edible_crystals_mod.entity.ModEntities;
import org.edible_crystals_mod.loot.ModLootModifiers;
import org.edible_crystals_mod.registers.BlockEntitiesRegister;
import org.edible_crystals_mod.registers.BlocksRegister;
import org.edible_crystals_mod.registers.ItemsRegister;
import org.edible_crystals_mod.registers.ModMenusRegister;
import org.edible_crystals_mod.sounds.ModSounds;
import org.edible_crystals_mod.utils.ModCreativeModTabs;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(EdibleCrystalMod.MOD_ID)
public class EdibleCrystalMod {

    // Define mod id in a common place for everything to reference
    public static final String MOD_ID = "ediblecrystals";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "untitled" namespace

    public EdibleCrystalMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        //Here is where re register the item and pass the i eventBuss above which will register the Mod
        ModCreativeModTabs.register(modEventBus);

        ItemsRegister.register(modEventBus);
        BlocksRegister.register(modEventBus);
        EffectMaps.register(modEventBus);
        ModLootModifiers.register(modEventBus);

        BlockEntitiesRegister.register(modEventBus);
        ModMenusRegister.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEffectsRegister.register(modEventBus);

        ModSounds.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game event we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        /*CrystalItems.setup(event);*/
    }

    // Add the example blockstates item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
//        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
//            event.accept(ItemsRegister.EDIBLE_CRYSTAL_FRAGMENT);
//        }
    }
    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents {

        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            MenuScreens.register(ModMenusRegister.CRYSTAL_INFUSION_MENU.get(), InfusionTableScreen::new);

        }
    }
}
