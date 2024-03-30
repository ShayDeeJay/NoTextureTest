package org.edible_crystals_mod.utils.tags;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.edible_crystals_mod.EdibleCrystalMod;

import java.util.Arrays;
import java.util.List;

public class ModTags {

    public static List<Integer> checkNBTIntArray(ItemStack itemStack, String key) {
        assert itemStack.getTag() != null;
        return Arrays.stream(itemStack.getTag().getIntArray(key)).boxed().toList();
    }

    public static class Items {
        public static final TagKey<Item> INFUSE = tag("infuse");

        public static final TagKey<Item> INFUSE_FUEL = tag("infuse_fuel");

        public static final TagKey<Item> INFUSED_JUICER = tag("infuse_juicer");

        public static final TagKey<Item> MOD_BOOK = tag("mod_book");

        public static final TagKey<Item> INFUSE_EFFECT = tag("infuse");
        private static TagKey<Item> tag(String name) {
            return ItemTags.create(new ResourceLocation(EdibleCrystalMod.MOD_ID, name));
        }
    }

    public static class Block {
        public static final TagKey<net.minecraft.world.level.block.Block> CAN_REPLACE_BLOCK = tag("replace");

        private static TagKey<net.minecraft.world.level.block.Block> tag(String name) {
            return BlockTags.create(new ResourceLocation(EdibleCrystalMod.MOD_ID, name));
        }
    }
}