package org.edible_crystals_mod.block.entity;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.block.ModBlocks;

public class ModBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EdibleCrystalMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrystalInfusionTableEntity>> CRYSTAL_INFUSION_BE =
            BLOCK_ENTITIES.register("infusion_table_be", () -> BlockEntityType.Builder.of(CrystalInfusionTableEntity::new, ModBlocks.CRYSTAL_INFUSION_TABLE.get()).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

