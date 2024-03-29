package org.edible_crystals_mod.datagen;

import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.abilities_and_effects.effects.EffectMaps;

import java.util.List;

public class ModItemModelProvider extends ItemModelProvider {

    private static final ResourceLocation modelData = new ResourceLocation("custom_model_data");
    public ModItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, EdibleCrystalMod.MOD_ID, existingFileHelper);
    }

    public  ModelFile modelFile(String location) {
        return new ModelFile.ExistingModelFile(location(location),this.existingFileHelper);
    }

    @Override
    protected void registerModels() {
        List<String> files = List.of(
            "edible_crystal_carrot",
            "edible_crystal_golden_apple",
            "edible_crystal_golden_carrot",
            "edible_crystal_glistening_melon",
            "edible_crystal_cooked_porkchop",
            "edible_crystal_cooked_beef",
            "edible_crystal_cooked_rabbit",
            "edible_crystal_apple",
            "edible_crystal_cooked_salmon",
            "edible_crystal_bread",
            "edible_crystal_pumpkin_pie",
            "edible_crystal_glow_berries",
            "edible_crystal_dried_kelp",
            "edible_crystal_honey_bottle",
            "edible_crystal_cookie",
            "edible_crystal_cooked_cod",
            "edible_crystal_puffer_fish"
        );

        files.forEach(this::simpleItemModel);

        simpleItem(EffectMaps.EDIBLE_CRYSTAL_FRAGMENT);
        files.forEach( overrider ->
            simpleItem(EffectMaps.EDIBLE_CRYSTAL)
                .override()
                .predicate(modelData, files.indexOf(overrider) + 1)
                .model(modelFile("item/"+overrider)).end()
        );
    }


    private ItemModelBuilder simpleItemModel(String item) {
        return withExistingParent(item,
            new ResourceLocation("item/generated")).texture("layer0",
            new ResourceLocation(EdibleCrystalMod.MOD_ID,"item/crystals/" + item));
    }

    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {
        return withExistingParent(item.getId().getPath(),
                new ResourceLocation("item/generated")).texture("layer0",
                new ResourceLocation(EdibleCrystalMod.MOD_ID,"item/crystals/" + item.getId().getPath()));
    }

    private ResourceLocation location(String location) {
        return new ResourceLocation(EdibleCrystalMod.MOD_ID, location);
    }
}
