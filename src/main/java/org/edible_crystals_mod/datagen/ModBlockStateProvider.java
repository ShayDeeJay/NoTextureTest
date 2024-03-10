package org.edible_crystals_mod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.block.ModBlocks;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EdibleCrystalMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        simpleBlockWithItem(ModBlocks.CRYSTAL_INFUSION_TABLE.get(),
            new ModelFile.UncheckedModelFile(modLoc("block/infusion_table")));
    }

}
