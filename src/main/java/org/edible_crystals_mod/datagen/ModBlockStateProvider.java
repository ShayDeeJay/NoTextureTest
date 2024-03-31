package org.edible_crystals_mod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.registers.BlocksRegister;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EdibleCrystalMod.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        blockWithItem(BlocksRegister.CRYSTAL_ORE);
        simpleBlockWithItem(BlocksRegister.CRYSTAL_INFUSION_TABLE.get(),
            new ModelFile.UncheckedModelFile(modLoc("block/infusion_table")));
        simpleBlockWithItem(BlocksRegister.TANK.get(),
            new ModelFile.UncheckedModelFile(modLoc("block/tank")));
        simpleBlockWithItem(BlocksRegister.INFUSER.get(),
            new ModelFile.UncheckedModelFile(modLoc("block/infuser")));
        simpleBlockWithItem(BlocksRegister.LIGHTING.get(),
            new ModelFile.UncheckedModelFile(modLoc("block/lighting")));
    }

    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }

}
