package org.edible_crystals_mod.items.crystals;

import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class CrystalItemBase extends Item {
    private static final int BASE_NUTRITION = 1;
    private static final Float BASE_SATURATION = 0.1f;

    @Override
    public int getUseDuration(ItemStack pStack) {
        return 32;
    }

    public CrystalItemBase() {
        super(new Item.Properties().food(customFoodProperties(BASE_NUTRITION, BASE_SATURATION)));
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
        if (!PotionUtils.getMobEffects(itemstack).isEmpty()) {
            pPlayer.startUsingItem(pUsedHand);
            return InteractionResultHolder.consume(itemstack);
        }
        return InteractionResultHolder.pass(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public SoundEvent getEatingSound() {
        return super.getEatingSound();
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pLivingEntity) {
        PotionUtils.getMobEffects(pStack).forEach(pLivingEntity::addEffect);

        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }

    public String getDescriptionId(ItemStack pStack) {
        return pStack.getItem().getDescriptionId();
    }

    @Override
    public void appendHoverText(ItemStack pStack, @Nullable Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
    }

    public static FoodProperties customFoodProperties(
            Integer nutrition, Float saturation
    ){
        return new FoodProperties
                .Builder()
                .nutrition(nutrition)
                .saturationMod(saturation)
                .alwaysEat()
                .fast()
                .build();
    }
}
