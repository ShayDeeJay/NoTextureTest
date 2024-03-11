package org.edible_crystals_mod.screen;

import net.minecraft.core.Direction;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import org.edible_crystals_mod.block.ModBlocks;
import org.edible_crystals_mod.block.entity.CrystalInfusionTableEntity;
import org.edible_crystals_mod.utils.inventory_slots.InfuseSlot;
import org.edible_crystals_mod.utils.inventory_slots.OutputSlot;
import org.edible_crystals_mod.utils.inventory_slots.UnFedSlot;

public class InfusionTableMenu extends AbstractContainerMenu {
    public final CrystalInfusionTableEntity blockEntity;
    private final Level level;
    private final ContainerData data;

    public InfusionTableMenu(int pContainerId, Inventory inv, FriendlyByteBuf extraData) {
        this(pContainerId, inv, inv.player.level().getBlockEntity(extraData.readBlockPos()), new SimpleContainerData(3));
    }

    public InfusionTableMenu(int pContainerId, Inventory inv, BlockEntity entity, ContainerData data) {
        // Call superclass constructor with the specified container type and container ID
        super(ModMenuTypes.CRYSTAL_INFUSION_MENU.get(), pContainerId);

        // Check if the inventory size is as expected (in this case, 3 slots)
        checkContainerSize(inv, 4);

        // Cast the BlockEntity to CrystalInfusionTableEntity and assign it to the blockEntity field
        blockEntity = ((CrystalInfusionTableEntity) entity);

        // Get the level (world) associated with the player's inventory
        this.level = inv.player.level();

        // Store the ContainerData object
        this.data = data;

        // player's 3 inventory rows
        for (int playerInvY = 0; playerInvY < 3; playerInvY++) {

            for (int playerInvX = 0; playerInvX < 9; playerInvX++) {

                this.addSlot(new Slot(inv, playerInvX + playerInvY * 9 + 9, 8 + playerInvX * 18, 84 + playerInvY * 18));
            }
        }

        // Add hot-bar slots.
        for (int hotbarX = 0; hotbarX < 9; hotbarX++) {
            this.addSlot(new Slot(inv, hotbarX, 8 + hotbarX * 18, 142));
        }

        // Retrieve the IItemHandler capability from the blockEntity
        this.blockEntity
            .getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.NORTH)
            .ifPresent(
                iItemHandler -> {
                    this.addSlot(new InfuseSlot(iItemHandler,0,36,33));
                    this.addSlot(new UnFedSlot(iItemHandler,1, 124, 33));
                }
            );
        this.blockEntity
                .getCapability(ForgeCapabilities.ITEM_HANDLER, Direction.DOWN)
                .ifPresent(
                        iItemHandler -> {
                            this.addSlot(new OutputSlot(iItemHandler,0, 80, 33));
                        }
                );
        // Add additional data slots if needed
        addDataSlots(data);
    }

    public boolean isCrafting() {
        return data.get(0) > 0;
    }

    public int getScaledProgress(int progressBar) {
        int progress = this.data.get(0);
        int maxProgress = this.data.get(1);  // Max Progress
        int progressArrowSize = progressBar; // This is the width in pixels loader

        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }
    private static final int HOTBAR_SLOT_COUNT = 9;
    private static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    private static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    private static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    private static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    private static final int VANILLA_FIRST_SLOT_INDEX = 0;
    private static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;

    // THIS YOU HAVE TO DEFINE!
    private static final int TE_INVENTORY_SLOT_COUNT = 3;  // must be the number of slots you have!
    @Override
    public ItemStack quickMoveStack(Player playerIn, int pIndex) {
        Slot sourceSlot = slots.get(pIndex);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (pIndex < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + TE_INVENTORY_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (pIndex < TE_INVENTORY_FIRST_SLOT_INDEX + TE_INVENTORY_SLOT_COUNT) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + pIndex);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }


    @Override
    public boolean stillValid(Player pPlayer) {
        // Check if the container is still valid using the ContainerLevelAccess.create method
        // This method requires the level (world) and the position of the block entity
        // It also checks if the player is still interacting with the container
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                pPlayer, ModBlocks.CRYSTAL_INFUSION_TABLE.get());
    }
}
