package org.edible_crystals_mod.registers;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.block.crystalinfusiontable.CrystalInfusionTableEntity;
import org.edible_crystals_mod.block.fragmentor.FragmentorBlockEntity;
import org.edible_crystals_mod.block.infuser.InfuserBlockEntity;

public class BlockEntitiesRegister {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EdibleCrystalMod.MOD_ID);

    public static final RegistryObject<BlockEntityType<CrystalInfusionTableEntity>> CRYSTAL_INFUSION_BE =
            BLOCK_ENTITIES.register(
                    "infusion_table_be",
                    () -> BlockEntityType.Builder.of(
                            CrystalInfusionTableEntity::new,
                            BlocksRegister.CRYSTAL_INFUSION_TABLE.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<FragmentorBlockEntity>> FRAGMENTOR_BE =
            BLOCK_ENTITIES.register(
                    "fragmentor_be",
                    () -> BlockEntityType.Builder.of(
                            FragmentorBlockEntity::new,
                            BlocksRegister.TANK.get()
                    ).build(null)
            );

    public static final RegistryObject<BlockEntityType<InfuserBlockEntity>> INFUSER_BE =
            BLOCK_ENTITIES.register(
                    "infuser_be",
                    () -> BlockEntityType.Builder.of(
                            InfuserBlockEntity::new,
                            BlocksRegister.INFUSER.get()
                    ).build(null)
            );

    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}

