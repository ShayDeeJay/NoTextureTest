package org.edible_crystals_mod.utils.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import org.edible_crystals_mod.EdibleCrystalMod;

public class ModTags {
//    public static class Blocks {
//        public static final TagKey<Block> METAL_DETECTOR_VALUABLES = tag("metal_detector_valuables");
//        public static final TagKey<Block> NEEDS_SAPPHIRE_TOOL = tag("needs_sapphire_tool");
//
//
//        private static TagKey<Block> tag(String name) {
//            return BlockTags.create(new ResourceLocation(TutorialMod.MOD_ID, name));
//        }
//    }

    public static class Items {
        public static final TagKey<Item> INFUSE = tag("infuse");

        public static final TagKey<Item> INFUSE_FUEL = tag("infuse_fuel");

        public static final TagKey<Item> INFUSED_JUICER = tag("infuse_juicer");
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(EdibleCrystalMod.MOD_ID, name));
        }
    }
}