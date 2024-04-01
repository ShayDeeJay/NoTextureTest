package org.edible_crystals_mod.items.held_items;

import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import org.edible_crystals_mod.abilities_and_effects.custom_effects.movement.LaunchMovement;
import org.edible_crystals_mod.abilities_and_effects.custom_effects.projectile.BlockBreakingProjectile;
import org.edible_crystals_mod.abilities_and_effects.custom_effects.projectile.ItemEffects;
import org.edible_crystals_mod.renderers.item.ItemRenderer;
import org.edible_crystals_mod.sounds.ModSounds;
import org.jetbrains.annotations.Nullable;
import particle.ModParticles;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TabletItem extends Item implements GeoItem {

    public TabletItem(Properties pProperties) {
        super(pProperties);
    }

//    private static final RawAnimation POPUP_ANIM = RawAnimation.begin().thenPlay("use.popup");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private  final String key = "edible_mod_effects";
    private BlockBreakingProjectile fireProjectile;
    boolean modeSelect = true;
    boolean allowNewProjectile = true;

    @Override
    public boolean isFoil(ItemStack pStack) { return pStack.hasTag(); }

    @Override
    public int getMaxStackSize(ItemStack stack) { return 1; }



    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(pInteractionTarget instanceof Mob mob){

            if(!mob.hasEffect(MobEffects.LEVITATION)){
                mob.addEffect(new MobEffectInstance(MobEffects.LEVITATION, 5, 50));
                mob.addEffect(new MobEffectInstance(MobEffects.WITHER, 100, 0));
            }

            return InteractionResult.CONSUME;
        };
        return InteractionResult.FAIL;
    }

    @Override
    public void verifyTagAfterLoad(CompoundTag pTag) {
        super.verifyTagAfterLoad(pTag);
    }


    @Override
    public void inventoryTick(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (pEntity instanceof Player player) {
            boolean hasKey = pStack.getTag() != null && pStack.getTag().contains(key);

            if (player.getOffhandItem() == pStack) {
                if (hasKey) {
                    Arrays.stream(pStack.getTag().getIntArray(key)).boxed().forEach(
                        getEffect -> player.addEffect(
                            new MobEffectInstance(ItemEffects.effectMapItem.get(getEffect), 2, 1, true, true)
                        )
                    );
                }
            }
            if (fireProjectile != null) { fireProjectile.onTickMove(); }
            LaunchMovement.tickEvent(player, this, 4);
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }


    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        ItemStack itemsInHand = player.getItemInHand(pUsedHand);

        if (allowNewProjectile || fireProjectile == null) {
            fireProjectile = new BlockBreakingProjectile(player, pLevel);
        }
        if (player.isShiftKeyDown()) {
            if(!pLevel.isClientSide){
                modeSelect = !modeSelect;
                if (modeSelect) {
                    player.sendSystemMessage(Component.literal("Movement"));
                } else {
                    player.sendSystemMessage(Component.literal("Block"));
                }
            }
        } else {
            if (modeSelect) {
                LaunchMovement.launchPlayerDirection(player, pLevel, 2.0f);
            } else {

                if (allowNewProjectile) {
                    player.playSound(ModSounds.ORB_CREATE.get(), 0.6f,2.0f);


                    double spawnX1 = player.getX() + -Math.sin(Math.toRadians(player.yRotO)) * Math.cos(Math.toRadians(player.xRotO)) * 1.5;
                    double spawnY2 = player.getY() + player.getEyeHeight() + Math.sin(-Math.toRadians(player.xRotO)) * 1.5 - 0.5;
                    double spawnZ3 = player.getZ() + Math.cos(Math.toRadians(player.yRotO)) * Math.cos(Math.toRadians(player.xRotO)) * 1.5;

                    pLevel.addParticle(ModParticles.PROJECTILE_PARTICLES.get(), spawnX1,spawnY2,spawnZ3,1,1,1);
                    fireProjectile.fireProjectile();
                    if(!pLevel.isClientSide){
                        allowNewProjectile = false;
                    }
                } else {
                    fireProjectile.onRelease();
                    if(!pLevel.isClientSide){
                        fireProjectile = null;
                        allowNewProjectile = true;
                    }
                }
            }
        }
        return InteractionResultHolder.fail(itemsInHand);
    }



    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private ItemRenderer renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (this.renderer == null)
                    this.renderer = new ItemRenderer();

                return this.renderer;
            }
        });
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {

    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }


}
