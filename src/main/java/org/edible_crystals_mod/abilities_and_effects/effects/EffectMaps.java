package org.edible_crystals_mod.abilities_and_effects.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.items.crystals.CrystalItemBase;

import java.util.Map;


public class EffectMaps {
    public static final DeferredRegister<Item>
            ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, EdibleCrystalMod.MOD_ID);

    //Unassigned
    //public static final MobEffect SATURATION = MobEffects.SATURATION;
    //public static final MobEffect LUCK = MobEffects.LUCK;
    //public static final MobEffect HERO_OF_THE_VILLAGE = MobEffects.HERO_OF_THE_VILLAGE;

    // Harmful Effects
    public static final MobEffect SLOWNESS = MobEffects.MOVEMENT_SLOWDOWN;
    public static final MobEffect MINING_FATIGUE = MobEffects.DIG_SLOWDOWN;
    public static final MobEffect WEAKNESS = MobEffects.WEAKNESS;
    public static final MobEffect POISON = MobEffects.POISON;
    public static final MobEffect WITHER = MobEffects.WITHER;
    public static final MobEffect BLINDNESS = MobEffects.BLINDNESS;
    public static final MobEffect HUNGER = MobEffects.HUNGER;
    public static final MobEffect NAUSEA = MobEffects.CONFUSION;
    public static final MobEffect LEVITATION = MobEffects.LEVITATION;
    public static final MobEffect UN_LUCK = MobEffects.UNLUCK;
    public static final MobEffect BAD_OMEN = MobEffects.BAD_OMEN;
    public static final MobEffect DARKNESS = MobEffects.DARKNESS;

    public static final RegistryObject<Item> EDIBLE_CRYSTAL_FRAGMENT =
            ITEMS.register("edible_crystal_fragment", CrystalItemBase::new);
    public static final RegistryObject<Item> EDIBLE_CRYSTAL =
            ITEMS.register("edible_crystal", CrystalItemBase::new);


    public static Map<Item, CrystalData> effectMap = Map.ofEntries(
        Map.entry(Items.CARROT,new CrystalData(MobEffects.NIGHT_VISION, 1f)),
        Map.entry(Items.GOLDEN_APPLE, new CrystalData(MobEffects.REGENERATION, 2f)),
        Map.entry(Items.GOLDEN_CARROT, new CrystalData(MobEffects.DAMAGE_BOOST, 3f)),
        Map.entry(Items.GLISTERING_MELON_SLICE, new CrystalData(MobEffects.HEAL, 4f)),
        Map.entry(Items.COOKED_PORKCHOP, new CrystalData(MobEffects.MOVEMENT_SPEED, 5f)),
        Map.entry(Items.COOKED_BEEF, new CrystalData(MobEffects.DIG_SPEED, 6f)),
        Map.entry(Items.COOKED_RABBIT, new CrystalData(MobEffects.JUMP, 7f)),
        Map.entry(Items.APPLE, new CrystalData(MobEffects.DAMAGE_RESISTANCE, 8f)),
        Map.entry(Items.COOKED_SALMON, new CrystalData(MobEffects.WATER_BREATHING, 9f)),
        Map.entry(Items.BREAD, new CrystalData(MobEffects.GLOWING, 10.0f)),
        Map.entry(Items.PUMPKIN_PIE, new CrystalData(MobEffects.INVISIBILITY, 11.0f)),
        Map.entry(Items.GLOW_BERRIES, new CrystalData(MobEffects.FIRE_RESISTANCE, 12f)),
        Map.entry(Items.DRIED_KELP, new CrystalData(MobEffects.SLOW_FALLING, 13f)),
        Map.entry(Items.HONEY_BOTTLE, new CrystalData(MobEffects.ABSORPTION, 14f)),
        Map.entry(Items.COOKIE, new CrystalData(MobEffects.HEALTH_BOOST, 15f)),
        Map.entry(Items.COOKED_COD, new CrystalData(MobEffects.DOLPHINS_GRACE, 16f)),
        Map.entry(Items.PUFFERFISH, new CrystalData(MobEffects.CONDUIT_POWER, 17f))
    );


    public record CrystalData(
        MobEffect effect,
        float modelID
    ){
        public MobEffectInstance createEffectInstance(int duration, int amplifier) {
            return new MobEffectInstance(effect, duration, amplifier,false,false,true);
        }
    }

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
