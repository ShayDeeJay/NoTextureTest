package org.edible_crystals_mod.utils;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.abilities_and_effects.effects.EffectMaps;
import org.edible_crystals_mod.registers.BlocksRegister;
import org.edible_crystals_mod.registers.ItemsRegister;

import java.util.List;

public class ModCreativeModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EdibleCrystalMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> EXAMPLE_TAB = CREATIVE_MODE_TAB.register(
            "edible_crystals_tab", () -> CreativeModeTab.builder().icon(() -> new ItemStack(EffectMaps.EDIBLE_CRYSTAL.get()))
                    .title(Component.translatable("creativetab.edible_crystals_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(BlocksRegister.CRYSTAL_ORE.get());
                        pOutput.accept(EffectMaps.EDIBLE_CRYSTAL_FRAGMENT.get());
                        pOutput.accept(EffectMaps.EDIBLE_CRYSTAL.get());

                        EffectMaps.effectMap.values().forEach(
                            data -> {
                                ItemStack blankCrystal = new ItemStack(EffectMaps.EDIBLE_CRYSTAL.get());
                                blankCrystal.getOrCreateTag().putFloat("CustomModelData", data.modelID());
                                PotionUtils.setCustomEffects(blankCrystal, List.of(data.createEffectInstance(1000,0)));
                                pOutput.accept(blankCrystal);
                            }
                        );

                        pOutput.accept(ItemsRegister.MOD_BOOK.get());
                        pOutput.accept(ItemsRegister.TABLET_OF_REX.get());
                        pOutput.accept(BlocksRegister.CRYSTAL_INFUSION_TABLE.get());
                        pOutput.accept(BlocksRegister.FRAGMENTOR.get());
                        pOutput.accept(BlocksRegister.INFUSER.get());
                   })
                    .build()
    );

    public static void register(IEventBus eventBus) {
        CREATIVE_MODE_TAB.register(eventBus);
    }
}
