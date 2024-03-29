package org.edible_crystals_mod.block.infuser;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.tags.TagKey;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Player;
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
import org.edible_crystals_mod.registers.ItemsRegister;
import org.edible_crystals_mod.utils.tags.ModTags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class InfuserBlockEntity extends BlockEntity {

    public InfuserBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(BlockEntitiesRegister.INFUSER_BE.get(), pPos, pBlockState);

        this.data = new ContainerData() {
            @Override
            public int get(int pIndex) {
                return switch (pIndex) {
                    case 0 -> InfuserBlockEntity.this.progress;
                    case 1 -> InfuserBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int pIndex, int pValue) {
                switch (pIndex) {
                    case 0 -> InfuserBlockEntity.this.progress = pValue;
                    case 1 -> InfuserBlockEntity.this.maxProgress = pValue;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    private final ItemStackHandler itemHandler = new ItemStackHandler(2){
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            assert level != null;
            level.sendBlockUpdated(getBlockPos(),getBlockState(),getBlockState(),3);
        }
    };

    private final ItemStackHandler itemHandlerOutput = new ItemStackHandler(1);

    private static final int INPUT_CRYSTAL = 0;
    private static final int INPUT_FOOD = 1;

    private static final int OUTPUT = 0;

    public int tickCount;


    private LazyOptional<IItemHandler> lazyItemHandlerInput = LazyOptional.empty();

    private LazyOptional<IItemHandler> lazyItemHandlerOutput = LazyOptional.empty();




    protected final ContainerData data;
    private int progress = 0;
    private int maxProgress = 1;


    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            if(side == Direction.UP) {
                return lazyItemHandlerInput.cast();
            }
            if(side == Direction.DOWN) {
                return lazyItemHandlerOutput.cast();
            }
        }

        return super.getCapability(cap, side);
    }


    public ItemStack getRenderer() {
        return this.itemHandler.getStackInSlot(0);
    }


    @Override
    public void onLoad() {
        super.onLoad();
        lazyItemHandlerInput = LazyOptional.of(() -> itemHandler);
        lazyItemHandlerOutput = LazyOptional.of(() -> itemHandlerOutput);
    }

    public ContainerData getData() {
        return data;
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandlerInput.invalidate();
        lazyItemHandlerOutput.invalidate();
    }

    public void drops() {

        if(this.level != null){
            SimpleContainer inventory = new SimpleContainer(3);
            inventory.setItem(0, itemHandler.getStackInSlot(0));
            inventory.setItem(1, itemHandler.getStackInSlot(1));
            inventory.setItem(2, itemHandlerOutput.getStackInSlot(0));
            Containers.dropContents(this.level, this.worldPosition, inventory);
        }

    }

    @Override
    protected void saveAdditional(CompoundTag pTag) {
        pTag.put("input", itemHandler.serializeNBT());
        pTag.put("output", itemHandlerOutput.serializeNBT());
        pTag.putInt("infuser.progress", progress);

        super.saveAdditional(pTag);
    }


    @Override
    public void load(CompoundTag pTag) {
        super.load(pTag);
        itemHandler.deserializeNBT(pTag.getCompound("input"));
        itemHandlerOutput.deserializeNBT(pTag.getCompound("output"));
        progress = pTag.getInt("infuser.progress");
    }


    public void tick(Level pLevel, BlockPos pPos, BlockState pState) {
        if (hasRecipe()) {
            tickCount++;
            System.out.println(this.itemHandler.getStackInSlot(0));
            System.out.println(this.itemHandler.getStackInSlot(1));
            System.out.println(this.itemHandlerOutput.getStackInSlot(0));
            progress++;
            if (!pState.getValue(InfuserBlock.IS_INFUSING)) {
                pState = pState.setValue(InfuserBlock.IS_INFUSING, true);
                pLevel.setBlock(pPos, pState, Block.UPDATE_ALL);
            }

            setChanged(pLevel, pPos, pState);
            if (hasProgressFinished()) {
                craftItem();
                resetProgress();
            }
        } else {
            if (pState.getValue(InfuserBlock.IS_INFUSING)) {
                pState = pState.setValue(InfuserBlock.IS_INFUSING, false);
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
                        if (itemStack.getCount() == 1) {
                            itemStack.copyAndClear();
                        }

                        itemStack.shrink(amountToAdd);
                    }

                    this.itemHandler.insertItem(inputSlotNumber, itemStackCopy, false);
                    return true;
                }
            }
        }
        return false;
    }

//    public boolean addItemFromHand(Player player, InteractionHand pHand) {
//        ItemStack itemStack = player.getItemInHand(pHand);
//
//        if (stackHandler(itemStack, ModTags.Items.INFUSE_FUEL, INPUT_CRYSTAL, player)) {
//            System.out.println("here");
//            return true;
//        }
//
//        return stackHandler(itemStack, ModTags.Items.INFUSE_EFFECT, INPUT_FOOD, player);
//    }

    public boolean addItemFromHand(Player player, InteractionHand pHand) {
        ItemStack itemStack = player.getItemInHand(pHand);

        if (stackHandler(itemStack, ModTags.Items.MOD_BOOK, INPUT_CRYSTAL, player)) {
            System.out.println("here");
            return true;
        }

        return stackHandler(itemStack, ModTags.Items.INFUSE_EFFECT, INPUT_FOOD, player);
    }

    private void craftItem() {
        EffectMaps.CrystalData localEffect = EffectMaps.effectMap.get(this.itemHandler.getStackInSlot(INPUT_FOOD).getItem());
        // Create a new ItemStack for the result of crafting
        ItemStack result = new ItemStack(ItemsRegister.TABLET_OF_REX.get());

//        result.getOrCreateTag().putFloat("CustomModelData", localEffect.modelID());
//
//        List<MobEffectInstance> localEffects = new ArrayList<>();
//
//        localEffects.add(localEffect.createEffectInstance(1000,1));
//
//        PotionUtils.setCustomEffects(result, localEffects);

        result.getOrCreateTag().putIntArray("edible_mod_effects", List.of(1,5,3,6,18,19,7));
//        result.getOrCreateTagElement("edible_mod_effects").putIntArray("edible_mod_effects", List.of(1,5,3,6,18,19,7));

        result.copyWithCount(1);


        this.itemHandler.extractItem(INPUT_CRYSTAL, 1, false);
        this.itemHandler.extractItem(INPUT_FOOD, 1, false);

        // Get the current count of items in the output slot and add the count of the new result
//        int newCount = this.itemHandlerOutput.getStackInSlot(OUTPUT).getCount() + result.getCount();

        // Set the new ItemStack with the updated count in the output slot
        this.itemHandlerOutput.insertItem(OUTPUT, result, false);
    }

//    private void craftItem() {
//        EffectMaps.CrystalData localEffect = EffectMaps.effectMap.get(this.itemHandler.getStackInSlot(INPUT_FOOD).getItem());
//        // Create a new ItemStack for the result of crafting
//        ItemStack result = new ItemStack(EffectMaps.EDIBLE_CRYSTAL.get());
//
//        result.getOrCreateTag().putFloat("CustomModelData", localEffect.modelID());
//
//        List<MobEffectInstance> localEffects = new ArrayList<>();
//
//        localEffects.add(localEffect.createEffectInstance(1000,1));
//
//        PotionUtils.setCustomEffects(result, localEffects);
//        result.setCount(1);
//
//        this.itemHandler.extractItem(INPUT_CRYSTAL, 1, false);
//        this.itemHandler.extractItem(INPUT_FOOD, 1, false);
//
//        // Get the current count of items in the output slot and add the count of the new result
////        int newCount = this.itemHandlerOutput.getStackInSlot(OUTPUT).getCount() + result.getCount();
//
//        // Set the new ItemStack with the updated count in the output slot
//        this.itemHandlerOutput.insertItem(OUTPUT, result, false);
//    }

//    private boolean hasRecipe() {
//        boolean hasCraftingItem = this.itemHandler.getStackInSlot(INPUT_CRYSTAL).is(ModTags.Items.INFUSE_FUEL);
//        boolean hasInfusingItem = this.itemHandler.getStackInSlot(INPUT_FOOD).is(ModTags.Items.INFUSE_EFFECT);
//        ItemStack result = new ItemStack(EffectMaps.EDIBLE_CRYSTAL.get());
//
//        return hasCraftingItem && hasInfusingItem && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
//    }
private boolean hasRecipe() {
    boolean hasCraftingItem = this.itemHandler.getStackInSlot(INPUT_CRYSTAL).is(ModTags.Items.MOD_BOOK);
    boolean hasInfusingItem = this.itemHandler.getStackInSlot(INPUT_FOOD).is(ModTags.Items.INFUSE_EFFECT);
    ItemStack result = new ItemStack(EffectMaps.EDIBLE_CRYSTAL.get());

    return hasCraftingItem && hasInfusingItem && canInsertAmountIntoOutputSlot(result.getCount()) && canInsertItemIntoOutputSlot(result.getItem());
}

    // Method to check if the provided item can be inserted into the output slot
    private boolean canInsertItemIntoOutputSlot(Item item) {
        // Check if the output slot is empty or if it contains the same item as the one being inserted
        return this.itemHandlerOutput.getStackInSlot(OUTPUT).isEmpty() ||
                this.itemHandlerOutput.getStackInSlot(OUTPUT).is(item);
    }

    // Method to check if the specified number of items can be inserted into the output slot
    private boolean canInsertAmountIntoOutputSlot(int count) {
        // Get the current count of items in the output slot
        int currentCount = this.itemHandlerOutput.getStackInSlot(OUTPUT).getCount();
        // Get the maximum stack size allowed for items in the output slot
        int maxStackSize = 64;
        // Check if inserting the specified number of items would exceed the maximum stack size
        return currentCount + count <= maxStackSize;
    }

    private boolean hasProgressFinished() {
        return progress >= maxProgress;
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

