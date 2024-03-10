package org.edible_crystals_mod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.items.CrystalItems;
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
        this.tag(ModTags.Items.INFUSE)
                .add(CrystalItems.EDIBLE_CRYSTAL_BEET.get())
                .add(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE.get())
                .add(CrystalItems.EDIBLE_CRYSTAL_CARROT.get());

        this.tag(ModTags.Items.INFUSE_FUEL)
                .add(CrystalItems.EDIBLE_CRYSTAL.get());

        this.tag(ModTags.Items.INFUSED_JUICER)
                .add(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE_JUICED.get());
    }
}
