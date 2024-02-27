package org.edible_crystals_mod.untitled;


import com.mojang.logging.LogUtils;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.world.item.CreativeModeTabs;
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
import org.edible_crystals_mod.block.ModBlocks;
import org.edible_crystals_mod.block.entity.ModBlockEntities;
import org.edible_crystals_mod.loot.ModLootModifiers;
import org.edible_crystals_mod.screen.InfusionTableScreen;
import org.edible_crystals_mod.screen.ModMenuTypes;
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

        CrystalItems.register(modEventBus);
        ModBlocks.register(modEventBus);

        ModLootModifiers.register(modEventBus);

        ModBlockEntities.register(modEventBus);
        ModMenuTypes.register(modEventBus);

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        /*CrystalItems.setup(event);*/
    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS) {
            event.accept(CrystalItems.EDIBLE_CRYSTAL_FRAGMENT);
            event.accept(CrystalItems.EDIBLE_CRYSTAL);
        }

        if(event.getTabKey() == CreativeModeTabs.FOOD_AND_DRINKS) {
            event.accept(CrystalItems.EDIBLE_CRYSTAL_BEET);
            event.accept(CrystalItems.EDIBLE_CRYSTAL_CARROT);
            event.accept(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE);
            event.accept(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE_JUICED);
        }
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
            MenuScreens.register(ModMenuTypes.CRYSTAL_INFUSION_TABLE.get(), InfusionTableScreen::new);
        }
    }
}
