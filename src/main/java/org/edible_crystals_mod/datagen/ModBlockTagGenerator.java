package org.edible_crystals_mod.datagen;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.BlockTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.registers.BlocksRegister;
import org.edible_crystals_mod.utils.tags.ModTags;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ModBlockTagGenerator extends BlockTagsProvider {
    public ModBlockTagGenerator(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, EdibleCrystalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider pProvider) {

        this.tag(BlockTags.MINEABLE_WITH_AXE)
            .add(BlocksRegister.CRYSTAL_INFUSION_TABLE.get());

        this.tag(BlockTags.MINEABLE_WITH_PICKAXE)
            .add(BlocksRegister.FRAGMENTOR.get())
            .add(BlocksRegister.INFUSER.get())
            .add(BlocksRegister.CRYSTAL_ORE.get());;

        this.tag(ModTags.Block.CAN_REPLACE_BLOCK)
            .add(Blocks.AIR)
            .add(Blocks.GRASS);
    }
}
