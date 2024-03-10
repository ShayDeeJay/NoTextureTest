package org.edible_crystals_mod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.items.CrystalItems;

public class ModItemModelProvider extends ItemModelProvider {

    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EdibleCrystalMod.MOD_ID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(CrystalItems.EDIBLE_CRYSTAL_CARROT);
        simpleItem(CrystalItems.EDIBLE_CRYSTAL);
        simpleItem(CrystalItems.EDIBLE_CRYSTAL_FRAGMENT);
        simpleItem(CrystalItems.EDIBLE_CRYSTAL_BEET);
        simpleItem(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE);
        simpleItem(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE_JUICED);
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(EdibleCrystalMod.MOD_ID,"item/" + item.getId().getPath()));
    }
}
