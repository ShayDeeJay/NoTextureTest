package org.edible_crystals_mod.worldgen;

import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.level.levelgen.feature.ConfiguredFeature;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;
import net.minecraft.world.level.levelgen.feature.configurations.OreConfiguration;
import net.minecraft.world.level.levelgen.structure.templatesystem.RuleTest;
import net.minecraft.world.level.levelgen.structure.templatesystem.TagMatchTest;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.registers.BlocksRegister;

import java.util.List;

public class ModConfiguredFeatures {
    public static final ResourceKey<ConfiguredFeature<?, ?>> OVERWORLD_CRYSTAL_ORE_KEY = registerKey("crystal_ore");


    public static void bootstrap(BootstapContext<ConfiguredFeature<?, ?>> context) {
        RuleTest stoneReplaceable = new TagMatchTest(BlockTags.STONE_ORE_REPLACEABLES);
        RuleTest deepslateReplaceables = new TagMatchTest(BlockTags.DEEPSLATE_ORE_REPLACEABLES);


        List<OreConfiguration.TargetBlockState> overworldCrystalOres = List.of(
            OreConfiguration.target(stoneReplaceable, BlocksRegister.CRYSTAL_ORE.get().defaultBlockState()),
            OreConfiguration.target(deepslateReplaceables, BlocksRegister.CRYSTAL_ORE.get().defaultBlockState())
        );

        register(context, OVERWORLD_CRYSTAL_ORE_KEY, Feature.ORE, new OreConfiguration(overworldCrystalOres, 9));
    }


    public static ResourceKey<ConfiguredFeature<?, ?>> registerKey(String name) {
        return ResourceKey.create(Registries.CONFIGURED_FEATURE, new ResourceLocation(EdibleCrystalMod.MOD_ID, name));
    }
    private static <FC extends FeatureConfiguration, F extends Feature<FC>> void register(BootstapContext<ConfiguredFeature<?, ?>> context,
                                                                                          ResourceKey<ConfiguredFeature<?, ?>> key, F feature, FC configuration) {
        context.register(key, new ConfiguredFeature<>(feature, configuration));
    }
}
