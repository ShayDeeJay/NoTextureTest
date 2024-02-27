package org.edible_crystals_mod.untitled;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.block.ModBlocks;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EdibleCrystalMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TAB.register(
            "edible_crystals_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(CrystalItems.EDIBLE_CRYSTAL.get()))
                    .title(Component.translatable("creativetab.edible_crystals_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(CrystalItems.EDIBLE_CRYSTAL_FRAGMENT.get());
                        pOutput.accept(CrystalItems.EDIBLE_CRYSTAL.get());
                        pOutput.accept(CrystalItems.EDIBLE_CRYSTAL_CARROT.get());
                        pOutput.accept(CrystalItems.EDIBLE_CRYSTAL_BEET.get());
                        pOutput.accept(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE.get());
                        pOutput.accept(ModBlocks.CRYSTAL_INFUSION_TABLE.get());
                   })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
