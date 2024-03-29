package org.edible_crystals_mod.datagen.loot;

import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.functions.ApplyBonusCount;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.UniformGenerator;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.abilities_and_effects.effects.EffectMaps;
import org.edible_crystals_mod.registers.BlocksRegister;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    public ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }

    @Override
    protected void generate() {

        this.dropSelf(BlocksRegister.CRYSTAL_INFUSION_TABLE.get());
        this.dropSelf(BlocksRegister.FRAGMENTOR.get());
        this.dropSelf(BlocksRegister.INFUSER.get());

        this.add(BlocksRegister.CRYSTAL_ORE.get(),
            block -> createCopperLikeOreDrops(BlocksRegister.CRYSTAL_ORE.get(), EffectMaps.EDIBLE_CRYSTAL_FRAGMENT.get()));
    }

    @Override
    protected Iterable<Block> getKnownBlocks() {
        return BlocksRegister.BLOCK.getEntries().stream().map(RegistryObject::get)::iterator;
    }

    protected LootTable.Builder createCopperLikeOreDrops(Block pBlock, Item item) {
        return createSilkTouchDispatchTable(pBlock,
            this.applyExplosionDecay(pBlock,
                LootItem.lootTableItem(item)
                    .apply(SetItemCountFunction.setCount(UniformGenerator.between(1.0F, 3.0F)))
                    .apply(ApplyBonusCount.addOreBonusCount(Enchantments.BLOCK_FORTUNE))
            )
        );
    }
}
