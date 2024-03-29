package org.edible_crystals_mod.block.crystalinfusiontable;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
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
import org.edible_crystals_mod.abilities_and_effects.effects.EffectMaps;
import org.edible_crystals_mod.registers.BlockEntitiesRegister;
import org.edible_crystals_mod.utils.tags.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CrystalInfusionTableEntity extends BlockEntity implements MenuProvider {

    private final ItemStackHandler itemHandler = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        }
    };

    private final ItemStackHandler itemHandlerOutput = new ItemStackHandler(1);

    private final ItemStackHandler itemHandlerBook = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        }
    };
    private static final int INFUSE_SLOT = 0;
    private static final int UNFED_SLOT = 1;
    private static final int INFUSED_OUTPUT_SLOT = 0;
    private static final int BOOK_SLOT = 0;


    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandlerOutput = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandlerBook= LazyOptional.empty();


    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 200;


    public CrystalInfusionTableEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegister.CRYSTAL_INFUSION_BE.get(), pPos, pBlockState);

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
            lazyItemHandlerBook.cast();
        }

        return super.getCapability(cap, side);
    }


    public ItemStack getRenderer() {
        return this.itemHandlerBook.getStackInSlot(0);
    }


    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
        lazyItemHandlerOutput = LazyOptional.of(() -> itemHandlerOutput);
        lazyItemHandlerBook = LazyOptional.of(() -> itemHandlerBook);
    }

    public ContainerData getData() {
        return data;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyItemHandlerOutput.invalidate();
        lazyItemHandlerBook.invalidate();
    }

    public void drops() {

        SimpleContainer inventory = new SimpleContainer(4);
        inventory.setItem(0,itemHandler.getStackInSlot(0));
        inventory.setItem(1,itemHandler.getStackInSlot(1));
        inventory.setItem(2, itemHandlerOutput.getStackInSlot(0));
        inventory.setItem(3, itemHandlerBook.getStackInSlot(0));
        Containers.dropContents(this.level, this.worldPosition, inventory);

    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("inventory", itemHandler.serializeNBT());
        pTag.put("output", itemHandlerOutput.serializeNBT());
        pTag.put("book", itemHandlerBook.serializeNBT());
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
        itemHandlerOutput.deserializeNBT(pTag.getCompound("output"));
        itemHandlerBook.deserializeNBT(pTag.getCompound("book"));
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

    private boolean stackHandlerBook(ItemStack itemStack, TagKey<Item> tagType, int inputSlotNumber, Player player, InteractionHand pHand, Level pLevel, BlockPos position, BlockState blockState) {
        ItemStack inputSlot = this.itemHandlerBook.getStackInSlot(inputSlotNumber);
        SoundEvent bookPut = SoundEvents.BOOK_PUT;
        SoundEvent bookPut2 = SoundEvents.BOOK_PAGE_TURN;
        SoundEvent bookTake = SoundEvents.BEACON_DEACTIVATE;
        if (!itemStack.isEmpty()) {
            if (inputSlot.isEmpty() && itemStack.is(tagType)) {
                ItemStack itemStackCopy = itemStack.copyWithCount(1);
                if (!player.getAbilities().instabuild) {
                    itemStack.shrink(1);
                }

                pLevel.playSound(null, position.getX(), position.getY(), position.getZ(), bookPut, SoundSource.BLOCKS, 1F, 1F);
                pLevel.playSound(null, position.getX(), position.getY(), position.getZ(), bookPut2, SoundSource.BLOCKS, 1F, 1F);
                this.itemHandlerBook.insertItem(inputSlotNumber, itemStackCopy, false);
                return true;
            }
        } else {
            if(!inputSlot.isEmpty()) {
                ItemStack book = this.itemHandlerBook.getStackInSlot(inputSlotNumber);
                pLevel.playSound(null, position.getX(), position.getY(), position.getZ(), bookTake, SoundSource.BLOCKS, 1F, 0.1F);
                this.itemHandlerBook.extractItem(inputSlotNumber, 1, false);
                player.setItemInHand(pHand, book);
                return true;
            }
        }
        System.out.println("7");
        return false;
    }

    public boolean addItemFromHand(Player player, InteractionHand pHand, Level pLevel, BlockPos position, BlockState blockState) {
        ItemStack itemStack = player.getItemInHand(pHand);

        if (stackHandler(itemStack, ModTags.Items.INFUSE, INFUSE_SLOT, player)) {
            System.out.println("here");

            return true;
        }

        if(stackHandlerBook(itemStack, ModTags.Items.MOD_BOOK, BOOK_SLOT, player, pHand, pLevel, position, blockState)) {
            System.out.println("there");

            return true;
        }
        System.out.println("non");

        return stackHandler(itemStack, ModTags.Items.INFUSE_FUEL, UNFED_SLOT, player);
    }

    private void craftItem() {
        // Create a new ItemStack for the result of crafting
        ItemStack result = new ItemStack(EffectMaps.EDIBLE_CRYSTAL.get(), 1);

        // Extract one item from the input slots
        this.itemHandler.extractItem(INFUSE_SLOT, 1, false);
        this.itemHandler.extractItem(UNFED_SLOT, 1, false);

        // Get the current count of items in the output slot and add the count of the new result
        int newCount = this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).getCount() + result.getCount();

        // Set the new ItemStack with the updated count in the output slot
        this.itemHandlerOutput.setStackInSlot(INFUSED_OUTPUT_SLOT, new ItemStack(result.getItem(), newCount));
    }

    private boolean hasRecipe() {
        boolean hasCraftingItem = this.itemHandler.getStackInSlot(INFUSE_SLOT).is(ModTags.Items.INFUSE);
        boolean hasSecondCraftingItem = this.itemHandler.getStackInSlot(UNFED_SLOT).getItem() == EffectMaps.EDIBLE_CRYSTAL.get();
        ItemStack result = new ItemStack(EffectMaps.EDIBLE_CRYSTAL.get());

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
//        int maxStackSize = this.itemHandlerOutput.getStackInSlot(INFUSED_OUTPUT_SLOT).getMaxStackSize();
        int maxStackSize = 1;
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

