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
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;

public class TabletItem extends Item implements GeoItem {

//    private static final RawAnimation POPUP_ANIM = RawAnimation.begin().thenPlay("use.popup");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private static final String key = "edible_mod_effects";

    public TabletItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public boolean isFoil(ItemStack pStack) { return pStack.hasTag(); }

    @Override
    public int getMaxStackSize(ItemStack stack) { return 1; }


    @Override
    public InteractionResult interactLivingEntity(ItemStack pStack, Player pPlayer, LivingEntity pInteractionTarget, InteractionHand pUsedHand) {
        if(pInteractionTarget instanceof Mob mob){

            mob.addEffect(new MobEffectInstance(MobEffects.LEVITATION,5,50));
            mob.addEffect(new MobEffectInstance(MobEffects.WITHER,100,0));

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
        if(pEntity instanceof Player player ){
            boolean hasKey = pStack.getTag() != null && pStack.getTag().contains(key);
            if (player.getOffhandItem() == pStack) {
                LaunchMovement.tickEvent(player, this, 10);

                if (hasKey) {
                    Arrays.stream(pStack.getTag().getIntArray(key)).boxed().forEach(
                        getEffect -> player.addEffect(
                            new MobEffectInstance(
                                ItemEffects.effectMapItem.get(getEffect),
                                2,
                                1,
                                true,
                                true
                            )
                        )
                    );
                }
            }
        }
        super.inventoryTick(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player player, InteractionHand pUsedHand) {
        ItemStack itemsInHand = player.getItemInHand(pUsedHand);
//        LaunchMovement.launchPlayerDirection(player, pLevel,2.0f);
        BlockBreakingProjectile.fireProjectile(player, pLevel);
        return InteractionResultHolder.pass(itemsInHand);
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
