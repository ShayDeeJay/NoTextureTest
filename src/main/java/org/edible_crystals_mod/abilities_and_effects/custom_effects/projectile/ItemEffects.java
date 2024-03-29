package org.edible_crystals_mod.abilities_and_effects.custom_effects.projectile;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import org.edible_crystals_mod.registers.ModEffectsRegister;

import java.util.Map;

public class ItemEffects {
    public static Map<Integer, MobEffect> effectMapItem = Map.ofEntries(
        Map.entry(1, MobEffects.NIGHT_VISION),
        Map.entry(2,MobEffects.REGENERATION),
        Map.entry(3,MobEffects.DAMAGE_BOOST),
        Map.entry(4,MobEffects.HEAL),
        Map.entry(5,MobEffects.MOVEMENT_SPEED),
        Map.entry(6,MobEffects.DIG_SPEED),
        Map.entry(7,MobEffects.JUMP),
        Map.entry(8,MobEffects.DAMAGE_RESISTANCE),
        Map.entry(9,MobEffects.WATER_BREATHING),
        Map.entry(10,MobEffects.GLOWING),
        Map.entry(11,MobEffects.INVISIBILITY),
        Map.entry(12,MobEffects.FIRE_RESISTANCE),
        Map.entry(13,MobEffects.SLOW_FALLING),
        Map.entry(14,MobEffects.ABSORPTION),
        Map.entry(15,MobEffects.HEALTH_BOOST),
        Map.entry(16,MobEffects.DOLPHINS_GRACE),
        Map.entry(17,MobEffects.CONDUIT_POWER),
        Map.entry(18, ModEffectsRegister.AMPLIFY_REACH.get()),
        Map.entry(19, ModEffectsRegister.STEP_BOOST.get())
    );
}
