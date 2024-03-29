package org.edible_crystals_mod.abilities_and_effects;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import org.edible_crystals_mod.entity.ModEntities;
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


    public CustomProjectileCrystal(LivingEntity pShooter, Level pLevel) {
        super(ModEntities.BREAKER.get(), pShooter, pShooter.getX(), pShooter.getY(), pShooter.getZ(), pLevel);
        if(pShooter instanceof Player localPlayer) {
            player = localPlayer;
        }
    }

    public CustomProjectileCrystal(double pX, double pY, double pZ, double pOffsetX, double pOffsetY, double pOffsetZ, Level pLevel) {
        super(ModEntities.BREAKER.get(), pX, pY, pZ, pOffsetX, pOffsetY, pOffsetZ, pLevel);
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
        double yOffset = 0.2D;
        double positionX = this.getX() - vec31.x;
        double positionY = this.getY() - vec31.y + yOffset;
        double positionZ = this.getZ() - vec31.z;

        if (this.level().isClientSide) {
            this.level().addParticle(ParticleTypes.SPORE_BLOSSOM_AIR, positionX, positionY, positionZ, 0.0D, 0.0D, 0.0D);
            this.level().addParticle(ParticleTypes.ENCHANT, positionX, positionY, positionZ, 0.0D, 0.0D, 0.0D);
        }
    }

    @Override
    public BlockState getBlockStateOn() {
        return super.getBlockStateOn();
    }

    //Needs block breaking restriction
    @Override
    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        BlockState block = this.level().getBlockState(pResult.getBlockPos());
        float destroySpeed = block.getDestroySpeed(this.level(), pResult.getBlockPos()) ;

        // Finds the side of the block the projectile lands on and then places selected block. Want to replace items like grass etc
        // will need tags
        if(!this.level().isClientSide){
            BlockPos blockPos = pResult.getBlockPos();
            Direction side = pResult.getDirection();
            if(this.level().getBlockState(blockPos.relative(side)).is(Blocks.AIR)){
                this.level().setBlock(blockPos.relative(side), Blocks.LIGHT.defaultBlockState(), 3);
                this.level().addParticle(ParticleTypes.PORTAL, blockPos.getX(), blockPos.getY(), blockPos.getZ(), 10,10,10);
                this.level().playSound(player, blockPos, SoundEvents.FOX_TELEPORT, SoundSource.NEUTRAL, 0.2f,0.4f);
            };
            this.discard();
        }

        // Get the block landed on, destroy and return item. Want to expand on to change to silk touch if selected
//        if (!this.level().isClientSide) {
//
//            if(Range.between(0.0f, 10.0f).contains(destroySpeed)) {
//                this.level().destroyBlock(pResult.getBlockPos(), true, this);
//            }
//            this.discard();
//        }
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
