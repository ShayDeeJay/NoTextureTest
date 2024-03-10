package org.edible_crystals_mod.block;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.block.custom.CrystalInfusingTableBlock;
import org.edible_crystals_mod.items.CrystalItems;

import java.util.function.Supplier;

public class ModBlocks {

    public static final DeferredRegister<Block> BLOCK =
            DeferredRegister.create(ForgeRegistries.BLOCKS, EdibleCrystalMod.MOD_ID);


    public static RegistryObject<Block> CRYSTAL_INFUSION_TABLE = registerBlock("infusion_table",
            () -> new CrystalInfusingTableBlock(
                    BlockBehaviour
                    .Properties.copy(Blocks.CRAFTING_TABLE)
                    .noOcclusion()
            )
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCK.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
            return CrystalItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    };

    public static void register(IEventBus eventBus) {
        BLOCK.register(eventBus);
    }

}
