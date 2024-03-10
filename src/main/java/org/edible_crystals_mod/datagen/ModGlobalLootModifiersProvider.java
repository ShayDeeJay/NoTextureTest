package org.edible_crystals_mod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import org.edible_crystals_mod.EdibleCrystalMod;

public class ModGlobalLootModifiersProvider extends GlobalLootModifierProvider {
    public ModGlobalLootModifiersProvider(PackOutput output) {
        super(output, EdibleCrystalMod.MOD_ID);
    }

    @Override
    protected void start() {

    }
}
