package org.edible_crystals_mod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.abilities_and_effects.effects.EffectMaps;
import org.edible_crystals_mod.registers.ItemsRegister;
import org.edible_crystals_mod.utils.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModItemTagGenerator extends ItemTagsProvider {
    public ModItemTagGenerator(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_,
                               CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, EdibleCrystalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {
        this.tag(ModTags.Items.INFUSE);
//                .add(EffectMaps.EDIBLE_CRYSTAL_GOLDEN_APPLE.get())
//                .add(EffectMaps.EDIBLE_CRYSTAL_CARROT.get());

        this.tag(ModTags.Items.INFUSE_FUEL)
                .add(EffectMaps.EDIBLE_CRYSTAL.get());

        this.tag(ModTags.Items.INFUSED_JUICER);
        this.tag(ModTags.Items.MOD_BOOK)
                .add(ItemsRegister.MOD_BOOK.get())
                .add(ItemsRegister.TABLET_OF_REX.get());

        this.tag(ModTags.Items.INFUSE_EFFECT)
                .add(Items.CARROT)
                .add(Items.GOLDEN_APPLE)
                .add(Items.GOLDEN_CARROT)
                .add(Items.GLISTERING_MELON_SLICE)
                .add(Items.COOKED_PORKCHOP)
                .add(Items.COOKED_BEEF)
                .add(Items.COOKED_RABBIT)
                .add(Items.APPLE)
                .add(Items.COOKED_SALMON)
                .add(Items.BREAD)
                .add(Items.PUMPKIN_PIE)
                .add(Items.GLOW_BERRIES)
                .add(Items.DRIED_KELP)
                .add(Items.HONEY_BOTTLE)
                .add(Items.COOKIE)
                .add(Items.COOKED_COD)
                .add(Items.PUFFERFISH);
    }
}
