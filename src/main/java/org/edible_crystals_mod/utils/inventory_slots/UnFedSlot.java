package org.edible_crystals_mod.utils.inventory_slots;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import org.edible_crystals_mod.utils.tags.ModTags;
import org.jetbrains.annotations.NotNull;

public class UnFedSlot extends SlotItemHandler {
    public UnFedSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(@NotNull ItemStack stack) {
        return stack.is(ModTags.Items.INFUSE_FUEL);
    }
}
