package org.edible_crystals_mod.untitled;

import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.util.Unit;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;
import java.util.stream.Stream;


/**
 * This class is going to hold all the items we want to register to Minecraft under this mod
 * */
public class CrystalItems {

    /**
     * This is a register to allow MC know that this is an items being added to the game.
     * A Deferred register is essentially just a list of items in game, this is adding a item to that list.
     * This allows forge to load it in to the game at the correct time?
     * */
    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EdibleCrystalMod.MOD_ID);

    public static final String carrot = "edible_crystal_carrot";
    public static final String beet = "edible_crystal_beet";
    public static final String goldenApple = "edible_crystal_golden_apple";
    public static final String goldenAppleJuiced = "edible_crystal_golden_apple_juiced";
    public static final String unFed = "edible_crystal";
    public static final String unFedFragment = "edible_crystal_fragment";

    public static FoodProperties customFoodProperties(
            Integer nutrition, Float saturation, MobEffect mobEffect, Integer duration, Integer amplifier, Float probability
    ){
        return new FoodProperties
                .Builder()
                .nutrition(nutrition)
                .fast()
                .saturationMod(saturation)
                .effect(() -> new MobEffectInstance(mobEffect, duration, amplifier),probability)
                .alwaysEat()
                .build();
    }

    public static FoodProperties edibleCrystal(String type) {
        List<Pair<String, FoodProperties>> foods = List.of(
            new Pair<>(carrot, customFoodProperties(5,0.5f, MobEffects.JUMP,1000,0,1.0f)),
            new Pair<>(beet, customFoodProperties(5, 0.5f, MobEffects.MOVEMENT_SPEED, 1000, 0, 1.0f)),
            new Pair<>(goldenApple, customFoodProperties(5, 0.5f, MobEffects.REGENERATION, 1000, 0, 1.0f)),
            new Pair<>(goldenAppleJuiced, customFoodProperties(5,0.5f, MobEffects.JUMP,1000,0,1.0f))
        );
        return foods.stream()
                .filter(item -> item.getA().equals(type))
                .findFirst().orElseThrow().getB();
    }

    /**
     * This now an item that is added to the game. This alone means its currently in the code,
     * but will not be displayed anywhere. This will need to added to menus such as the creative menu
     * */

    public static final RegistryObject<Item> EDIBLE_CRYSTAL_FRAGMENT =
            ITEMS.register(unFedFragment, () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EDIBLE_CRYSTAL =
            ITEMS.register(unFed, () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> EDIBLE_CRYSTAL_CARROT =
            ITEMS.register(carrot, () -> new Item(new Item.Properties().food(edibleCrystal(carrot))));

    public static final RegistryObject<Item> EDIBLE_CRYSTAL_BEET =
            ITEMS.register(beet, () -> new Item(new Item.Properties().food(edibleCrystal(beet))));

    public static final RegistryObject<Item> EDIBLE_CRYSTAL_GOLDEN_APPLE =
            ITEMS.register(goldenApple, () -> new Item(new Item.Properties().food(edibleCrystal(goldenApple))));
    public static final RegistryObject<Item> EDIBLE_CRYSTAL_GOLDEN_APPLE_JUICED =
            ITEMS.register(goldenAppleJuiced, () -> new Item(new Item.Properties().food(edibleCrystal(goldenApple))));


    /** The DeferredRegister also needs to be registered*/
    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

}
