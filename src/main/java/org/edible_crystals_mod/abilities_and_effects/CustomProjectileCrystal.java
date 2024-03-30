package org.edible_crystals_mod.abilities_and_effects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.apache.commons.lang3.Range;
import org.edible_crystals_mod.entity.ModEntities;
import org.edible_crystals_mod.utils.tags.ModTags;
import particle.ModParticles;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class CustomProjectileCrystal extends AbstractSummonProjectile implements GeoEntity {
    public static Player player;

    protected static final RawAnimation FLY_ANIM = RawAnimation.begin().thenLoop("animation.model.new");

    private final AnimatableInstanceCache geoCache = GeckoLibUtil.createInstanceCache(this);



    public CustomProjectileCrystal(double pX, double pY, double pZ, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel, Player localPlayer) {
        super(ModEntities.BREAKER.get(), pX, pY, pZ, pOffsetX, pOffsetY, pOffsetZ, pLevel);
        player = localPlayer;
    }

    public CustomProjectileCrystal(EntityType<? extends Projectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);

    }

    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide) {
            Entity entity = pResult.getEntity();
            if (entity instanceof Mob mob) {
                LightningBolt lightningbolt = EntityType.LIGHTNING_BOLT.create(this.level());
                lightningbolt.moveTo(Vec3.atBottomCenterOf(mob.blockPosition()));
                this.level().addFreshEntity(lightningbolt);
                this.playSound(SoundEvents.BLAZE_BURN, 1.0f, 1.0F);
                this.discard();
            }
        }
    }

    @Override
    protected boolean canHitEntity(Entity p_36842_) {
        return p_36842_ != this.getOwner();
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec31 = this.getDeltaMovement();
        double yOffset = 0.25D;
        double positionX = this.getX() - vec31.x;
        double positionY = this.getY() - vec31.y + yOffset;
        double positionZ = this.getZ() - vec31.z;

        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, positionX, positionY, positionZ, 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ModParticles.PROJECTILE_PARTICLES.get(), positionX, positionY, positionZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public BlockState getBlockStateOn() {
        return super.getBlockStateOn();
    }

    //Needs block breaking restriction
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        BlockState block = this.level().getBlockState(pResult.getBlockPos());
        float destroySpeed = block.getDestroySpeed(this.level(), pResult.getBlockPos());
        BlockBreaker onBlockHit = new BlockBreaker(block, destroySpeed, this.level(), pResult, this, player);

        onBlockHit.placeBlockOnFace();
//        onBlockHit.destroyAndDropBlock(true);

        super.onHitBlock(pResult);
    }


    public static class BlockBreaker {
        private final BlockState block;
        private final float destroySpeed;
        private final Level level;
        private final BlockHitResult blockBreakResults;
        private final AbstractSummonProjectile projectile;
        private final Player player;


        public BlockBreaker(BlockState block, float destroySpeed, Level level, BlockHitResult blockBreakResults, AbstractSummonProjectile projectile, Player player) {
            this.block = block;
            this.destroySpeed = destroySpeed;
            this.level = level;
            this.blockBreakResults = blockBreakResults;
            this.projectile = projectile;
            this.player = player;
        }

        public void destroyAndGetBlockSilk() {
            if (block != null && !level.isClientSide && Range.between(0.0f, 10.0f).contains(destroySpeed)) {
                ItemStack destroyedItem = new ItemStack(block.getBlock().asItem());
                level.destroyBlock(blockBreakResults.getBlockPos(), false, projectile);
                if (player != null && player.addItem(destroyedItem)) {
                    player.playSound(SoundEvents.FOX_TELEPORT);
                } else  {
                    projectile.spawnAtLocation(destroyedItem);
                }
                projectile.discard();
            }
        }

        public void destroyAndDropBlock(boolean isSilk) {
            if (block != null && !level.isClientSide && Range.between(0.0f, 10.0f).contains(destroySpeed)) {
                ItemStack destroyedItem = new ItemStack(block.getBlock().asItem());
                level.destroyBlock(blockBreakResults.getBlockPos(), !isSilk, projectile);
                if(isSilk) { projectile.spawnAtLocation(destroyedItem); }
                projectile.discard();
            }
        }

        public void placeBlockOnFace() {
            // Ideally add an invisible light source but with a hitbox so can be broken
            BlockState replaceBlock = Blocks.LIGHT.defaultBlockState();
            BlockPos blockPos = blockBreakResults.getBlockPos();
            Direction side = blockBreakResults.getDirection();

            if(!level.isClientSide){
                if(level.getBlockState(blockPos.relative(side)).is(ModTags.Block.CAN_REPLACE_BLOCK)){
                    level.setBlock(blockPos.relative(side), replaceBlock, 3);
                };
                projectile.discard();
            } else {
                level.addParticle(ParticleTypes.PORTAL, blockPos.getX(), blockPos.getY() + 1.0F, blockPos.getZ(), 1,1,1);
                level.playSound(player, blockPos, SoundEvents.FOX_TELEPORT, SoundSource.NEUTRAL, 0.2f,0.4f);
            }
        }
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers
            .add(new AnimationController<>(this, "test",0, this::predicate));
    }

    private <T extends GeoAnimatable> PlayState predicate(AnimationState<T> tAnimationState) {
        tAnimationState.getController().setAnimation(FLY_ANIM);
        return PlayState.CONTINUE;
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.geoCache;
    }
}
