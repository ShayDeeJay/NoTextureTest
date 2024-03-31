package org.edible_crystals_mod.registers;

import net.minecraft.util.valueproviders.UniformInt;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.DropExperienceBlock;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.block.crystalinfusiontable.CrystalInfusingTableBlock;
import org.edible_crystals_mod.block.fragmentor.FragmentorBlock;
import org.edible_crystals_mod.block.infuser.InfuserBlock;

import java.util.function.Supplier;

public class BlocksRegister {

    public static final DeferredRegister<Block> BLOCK =
            DeferredRegister.create(ForgeRegistries.BLOCKS, EdibleCrystalMod.MOD_ID);


    public static RegistryObject<Block> CRYSTAL_INFUSION_TABLE = registerBlock("infusion_table",
            () -> new CrystalInfusingTableBlock(
                BlockBehaviour
                    .Properties.copy(Blocks.CRAFTING_TABLE)
                    .noOcclusion()
            )
    );

    public static RegistryObject<Block> TANK = registerBlock("tank",
            () -> new FragmentorBlock(
                BlockBehaviour
                    .Properties.copy(Blocks.IRON_BLOCK)
                    .noOcclusion()
            )
    );

    public static RegistryObject<Block> INFUSER = registerBlock("infuser",
            () -> new InfuserBlock(
                BlockBehaviour
                    .Properties.copy(Blocks.CRAFTING_TABLE)
                    .noOcclusion()
            )
    );

    public static RegistryObject<Block> CRYSTAL_ORE = registerBlock("crystal_ore",
        () -> new DropExperienceBlock(
            BlockBehaviour.Properties.copy(Blocks.STONE)
                .strength(2f)
                .lightLevel((p_187431_) -> 1)
                .requiresCorrectToolForDrops(),
            UniformInt.of(3, 6)
        )
    );

    public static RegistryObject<Block> LIGHTING = registerBlock("lighting",
        () -> new Block(
            BlockBehaviour.Properties.copy(Blocks.TORCH)
                .lightLevel((p_187431_) -> 15)
                .noCollission()
                .instabreak()
        )
    );

    private static <T extends Block> RegistryObject<T> registerBlock(String name, Supplier<T> block) {
        RegistryObject<T> toReturn = BLOCK.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }

    private static <T extends Block>RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block){
            return ItemsRegister.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    };

    public static void register(IEventBus eventBus) {
        BLOCK.register(eventBus);
    }

}
