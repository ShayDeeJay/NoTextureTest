package org.edible_crystals_mod.itemproperties;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.edible_crystals_mod.items.CrystalItems;
import org.jetbrains.annotations.NotNull;
import oshi.util.tuples.Pair;

public class CrystalItem extends Item {
    public static final String UN_FED_FRAGMENT = "edible_crystal_fragment";

    public static final Pair<String, FoodProperties> UN_FED = new Pair<>(
            "edible_crystal",
            customFoodProperties(0,0.0f, MobEffects.JUMP,1000,30,1.0f)
    );
    public static final Pair<String, FoodProperties> CARROT = new Pair<>(
            "edible_crystal_carrot",
            customFoodProperties(5,0.5f, MobEffects.NIGHT_VISION,1000,0,1.0f)
    );
    public static final Pair<String, FoodProperties> BEET = new Pair<>(
            "edible_crystal_beet",
            customFoodProperties(5,0.5f, MobEffects.MOVEMENT_SPEED,1000,0,1.0f)
    );
    public static final Pair<String, FoodProperties> GOLDEN_APPLE = new Pair<>(
            "edible_crystal_golden_apple",
            customFoodProperties(5,0.5f, MobEffects.REGENERATION,1000,0,1.0f)
    );
    public static final Pair<String, FoodProperties> GOLDEN_APPLE_JUICED = new Pair<>(
            "edible_crystal_golden_apple_juiced",
            customFoodProperties(5, 0.5f, MobEffects.REGENERATION, 1000, 1, 1.0f)
    );

    public CrystalItem(Item.Properties pProperties) {
        super(pProperties);
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return super.getUseAnimation(pStack);
    }

    @Override
    public ItemStack finishUsingItem(ItemStack pStack, Level pLevel, LivingEntity pEntityLiving) {
        super.finishUsingItem(pStack, pLevel, pEntityLiving);

        if (pEntityLiving instanceof ServerPlayer serverplayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger(serverplayer, pStack);
            serverplayer.awardStat(Stats.ITEM_USED.get(this));
            serverplayer.getFoodData().setSaturation(0);
            serverplayer.getFoodData().setFoodLevel(0);
        }

        if (pStack.isEmpty()) {
            return new ItemStack(CrystalItems.EDIBLE_CRYSTAL.get());
        } else {
            if (pEntityLiving instanceof Player && !((Player)pEntityLiving).getAbilities().instabuild) {
                ItemStack itemstack = new ItemStack(CrystalItems.EDIBLE_CRYSTAL.get());
                Player player = (Player)pEntityLiving;
                if (!player.getInventory().add(itemstack)) {
                    player.drop(itemstack, false);
                }
            }

            return pStack;
        }
    }

    public FoodProperties getFoodProperties() {
        return UN_FED.getB();
    }
    public @NotNull SoundEvent getEatingSound() {
        return SoundEvents.AMETHYST_CLUSTER_BREAK;
    }

    public static FoodProperties customFoodProperties(
            Integer nutrition, Float saturation, MobEffect mobEffect, Integer duration, Integer amplifier, Float probability
    ){
        return new FoodProperties
                .Builder()
                .nutrition(nutrition)
                .fast()
                .saturationMod(saturation)
                .effect(() -> new MobEffectInstance(mobEffect, duration, amplifier),probability)
                .effect(() -> new MobEffectInstance(MobEffects.MOVEMENT_SPEED,  1000, 3),probability)
                .effect(() -> new MobEffectInstance(MobEffects.JUMP,  1000, 3),probability)
                .alwaysEat()
                .build();
    }

}
