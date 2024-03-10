package org.edible_crystals_mod.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.network.NetworkHooks;
import org.edible_crystals_mod.block.entity.CrystalInfusionTableEntity;
import org.edible_crystals_mod.block.entity.ModBlockEntities;
import org.edible_crystals_mod.utils.tags.ModTags;
import org.jetbrains.annotations.Nullable;

public class CrystalInfusingTableBlock extends BaseEntityBlock {
    public static final VoxelShape SHAPE = Block.box(0, 0, 0.775, 16, 16, 15.225);
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final BooleanProperty IS_INFUSING = BooleanProperty.create("is_infusing");

    public CrystalInfusingTableBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
                this.stateDefinition.any()
                        .setValue(FACING, Direction.NORTH)
                        .setValue(IS_INFUSING, false)
        );
    }

    @Override
    public void animateTick(BlockState pState, Level pLevel, BlockPos pPos, RandomSource pRandom) {

        double d0 = (double) pPos.getX() + 0.4D + (double) pRandom.nextFloat() * 0.2D;
        double d1 = (double) pPos.getY() + 0.7D + (double) pRandom.nextFloat() * 0.3D;
        double d2 = (double) pPos.getZ() + 0.4D + (double) pRandom.nextFloat() * 0.2D;

        if (pState.getValue(IS_INFUSING)) {
            pLevel.addParticle(ParticleTypes.DRIPPING_DRIPSTONE_LAVA, d0, d1+ 0.8, d2, 0.0D, 0.5D, 0.0D);
            pLevel.playLocalSound(d0, d1, d2, SoundEvents.BLAZE_BURN, SoundSource.BLOCKS, 0.05F, 100.0F, true);
        } else {
            pLevel.addParticle(ParticleTypes.DRAGON_BREATH, d0, d1 + 0.5, d2, 0.0D, 0.01D, 0.01D);
            pLevel.addParticle(ParticleTypes.DRAGON_BREATH, d0, d1 + 0.5, d2, 0.0D, 0.01D, -0.01D);
        }
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(FACING);
        pBuilder.add(IS_INFUSING);
    }

    public BlockState getStateForPlacement(BlockPlaceContext pContext) {
        return this.defaultBlockState().setValue(FACING, pContext.getHorizontalDirection().getOpposite());
    }


    public BlockState rotate(BlockState pState, Rotation pRotation) {
        return pState.setValue(FACING, pRotation.rotate(pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation(pState.getValue(FACING)));
    }

    @Override
    public VoxelShape getShape(BlockState pState, BlockGetter pLevel, BlockPos pPos, CollisionContext pContext) {
        return SHAPE;
    }

    @Override
    public RenderShape getRenderShape(BlockState pState) {
        return RenderShape.MODEL;
    }

    @Override
    public void onRemove(BlockState pState, Level pLevel, BlockPos pPos, BlockState pNewState, boolean pMovedByPiston) {
        if (pState.getBlock() != pNewState.getBlock()) {
            BlockEntity blockEntity = pLevel.getBlockEntity(pPos);
            if (blockEntity instanceof CrystalInfusionTableEntity) {
                ((CrystalInfusionTableEntity) blockEntity).drops();
            }
        }

        super.onRemove(pState, pLevel, pPos, pNewState, pMovedByPiston);
    }

    @Override
    public InteractionResult use(BlockState pState, Level pLevel, BlockPos pPos, Player pPlayer, InteractionHand pHand, BlockHitResult pHit) {
        var stack = pPlayer.getItemInHand(pHand);
        if(!stack.isEmpty() && (stack.is(ModTags.Items.INFUSE) || stack.is(ModTags.Items.INFUSE_FUEL))){
            if(!pLevel.isClientSide){
                BlockEntity entity = pLevel.getBlockEntity(pPos);
                if (entity instanceof CrystalInfusionTableEntity tableEntity) {
                    if (tableEntity.addItemFromHand(pPlayer, pHand)) {
                        return InteractionResult.SUCCESS;
                    }
                }
            } else {
                return InteractionResult.CONSUME;
            }
            return InteractionResult.CONSUME;
        }

        if (!pLevel.isClientSide()) {
            BlockEntity entity = pLevel.getBlockEntity(pPos);
            if(entity instanceof CrystalInfusionTableEntity) {
                NetworkHooks.openScreen(((ServerPlayer)pPlayer), (CrystalInfusionTableEntity)entity, pPos);
                return InteractionResult.SUCCESS;
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }
        return InteractionResult.CONSUME;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new CrystalInfusionTableEntity(pPos,pState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        if(pLevel.isClientSide()) {
            return null;
        }


        return createTickerHelper(pBlockEntityType, ModBlockEntities.CRYSTAL_INFUSION_BE.get(),
                (pLevel1, pPos, pState1, pBlockEntity) -> pBlockEntity.tick(pLevel1, pPos, pState1));
    }
}

