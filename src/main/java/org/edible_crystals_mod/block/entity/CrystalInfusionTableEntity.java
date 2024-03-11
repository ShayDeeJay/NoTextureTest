package org.edible_crystals_mod.block.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.edible_crystals_mod.block.custom.CrystalInfusingTableBlock;
import org.edible_crystals_mod.items.CrystalItems;
import org.edible_crystals_mod.screen.InfusionTableMenu;
import org.edible_crystals_mod.utils.tags.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrystalInfusionTableEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2);
    private final ItemStackHandler itemHandlerOutput = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if(!level.isClientSide) {
                level.sendBlockUpdated(getBlockPos(), getBlockState(), getBlockState(), 3);
            }
        }
    };
    private static final int INFUSE_SLOT = 0;
    private static final int UNFED_SLOT = 1;
    private static final int INFUSED_OUTPUT_SLOT = 0;
//    private static final int INFUSED_OUTPUT_SLOT = EM

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandlerOutput = LazyOptional.empty();

    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;


    public CrystalInfusionTableEntity(BlockPos pPos, BlockState pBlockState) {
        super(ModBlockEntities.CRYSTAL_INFUSION_BE.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> CrystalInfusionTableEntity.this.progress;
                    case 1 -> CrystalInfusionTableEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> CrystalInfusionTableEntity.this.progress = pValue;
                    case 1 -> CrystalInfusionTableEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == Direction.DOWN) {
                return lazyItemHandlerOutput.cast();
            }
            if(side == Direction.NORTH) {
                return lazyItemHandler.cast();
            }
        }

        return super.getCapability(cap, side);
    }


    public ItemStack getRenderer() {
        if(itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).isEmpty()) {
            return itemHandler.getStackInSlot(INFUSE_SLOT);
        } else {
            return itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT);
        }
    }


    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyItemHandlerOutput = LazyOptional.of(() -> itemHandlerOutput);
    }

    public ContainerData getData() {
        return data;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyItemHandlerOutput.invalidate();
    }

    public void drops() {
        SimpleContainer inventory = new SimpleContainer(itemHandler.getSlots());
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            inventory.setItem(i, itemHandler.getStackInSlot(i));
        }
        Containers.dropContents(this.level, this.worldPosition, inventory);
    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.putInt("crystal_infusing_table.progress", progress);

        super.saveAdditional(pTag);
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.ediblecrystals.infusion_table");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, Inventory pPlayerInventory, Player pPlayer) {
        return new InfusionTableMenu(pContainerId, pPlayerInventory, this, this.data);
    }


    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("inventory"));
        progress = pTag.getInt("crystal_infusing_table.progress");
    }


    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            increaseCraftingProgress();
            if (!pState.getValue(CrystalInfusingTableBlock.IS_INFUSING)) {
                pState = pState.setValue(CrystalInfusingTableBlock.IS_INFUSING, true);
                pLevel.setBlock(pPos, pState, Block.UPDATE_ALL);
            }

            setChanged(pLevel, pPos, pState);
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            if (pState.getValue(CrystalInfusingTableBlock.IS_INFUSING)) {
                pState = pState.setValue(CrystalInfusingTableBlock.IS_INFUSING, false);
                pLevel.setBlock(pPos, pState, Block.UPDATE_ALL);
            }
            resetProgress();
        }
    }

    private void resetProgress() {
        progress = 0;
    }

    private boolean stackHandler(ItemStack itemStack, TagKey<Item> tagType, int inputSlotNumber, Player player) {
        ItemStack inputSlot = this.itemHandler.getStackInSlot(inputSlotNumber);

        if (!itemStack.isEmpty() && itemStack.is(tagType)) {
            if (inputSlot.isEmpty() || itemStack.is(inputSlot.getItem())) {
                int remainingSpace = inputSlot.getMaxStackSize() - inputSlot.getCount();
                if (remainingSpace > 0) {
                    int amountToAdd = Math.min(remainingSpace, itemStack.getCount());
                    ItemStack itemStackCopy = itemStack.copyWithCount(amountToAdd);

                    if (!player.getAbilities().instabuild) {
                        itemStack.shrink(amountToAdd);
                    }

                    this.itemHandler.insertItem(inputSlotNumber, itemStackCopy, false);
                    return true;
                }
            }
        }
        return false;
    }

    public boolean addItemFromHand(Player player, InteractionHand pHand) {
        ItemStack itemStack = player.getItemInHand(pHand);

        if (stackHandler(itemStack, ModTags.Items.INFUSE, INFUSE_SLOT, player)) {
            return true;
        }

        return stackHandler(itemStack, ModTags.Items.INFUSE_FUEL, UNFED_SLOT, player);
    }

    private void craftItem() {
        // Create a new ItemStack for the result of crafting
        ItemStack result = new ItemStack(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE_JUICED.get(), 1);

        // Extract one item from the input slots
        this.itemHandler.extractItem(INFUSE_SLOT, 1, false);
        this.itemHandler.extractItem(UNFED_SLOT, 1, false);

        // Get the current count of items in the output slot and add the count of the new result
//        int newCount = this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).getCount() + result.getCount();

        // Set the new ItemStack with the updated count in the output slot
        this.itemHandlerOutput.setStackInSlot(INFUSED_OUTPUT_SLOT, new ItemStack(result.getItem(),
                this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).getCount() + result.getCount()));
    }

    private boolean hasRecipe() {
        boolean hasCraftingItem = this.itemHandler.getStackInSlot(INFUSE_SLOT).is(ModTags.Items.INFUSE);
        boolean hasSecondCraftingItem = this.itemHandler.getStackInSlot(UNFED_SLOT).getItem() == CrystalItems.EDIBLE_CRYSTAL.get();
        ItemStack result = new ItemStack(CrystalItems.EDIBLE_CRYSTAL_GOLDEN_APPLE_JUICED.get());

        return hasCraftingItem && hasSecondCraftingItem && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
    }

    // Method to check if the provided item can be inserted into the output slot
    private boolean canInsertItemIntoOutputSlot(Item item) {
        // Check if the output slot is empty or if it contains the same item as the one being inserted
        return this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).isEmpty() ||
                this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).is(item);
    }

    // Method to check if the specified number of items can be inserted into the output slot
    private boolean canInsertAmountIntoOutputSlot(int count) {
        // Get the current count of items in the output slot
        int currentCount = this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).getCount();
        // Get the maximum stack size allowed for items in the output slot
        int maxStackSize = this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).getMaxStackSize();

        // Check if inserting the specified number of items would exceed the maximum stack size
        return currentCount + count <= maxStackSize;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
    }

    private void increaseCraftingProgress() {
        progress++;
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }
}

