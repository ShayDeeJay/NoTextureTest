package org.edible_crystals_mod.sounds;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EdibleCrystalMod.MOD_ID);

    public static final RegistryObject<SoundEvent> PROJECTILE_LAUNCH_SOUND = registerSoundEvents("metal_detector_found_ore");

//    public static final RegistryObject<SoundEvent> SOUND_BLOCK_BREAK = registerSoundEvents("sound_block_break");
//    public static final RegistryObject<SoundEvent> SOUND_BLOCK_STEP = registerSoundEvents("sound_block_step");
//    public static final RegistryObject<SoundEvent> SOUND_BLOCK_FALL = registerSoundEvents("sound_block_fall");
//    public static final RegistryObject<SoundEvent> SOUND_BLOCK_PLACE = registerSoundEvents("sound_block_place");
//    public static final RegistryObject<SoundEvent> SOUND_BLOCK_HIT = registerSoundEvents("sound_block_hit");



//    public static final ForgeSoundType SOUND_BLOCK_SOUNDS = new ForgeSoundType(1f, 1f,
//            ModSounds.SOUND_BLOCK_BREAK, ModSounds.SOUND_BLOCK_STEP, ModSounds.SOUND_BLOCK_PLACE,
//            ModSounds.SOUND_BLOCK_HIT, ModSounds.SOUND_BLOCK_FALL);


    private static RegistryObject<SoundEvent> registerSoundEvents(String name) {
        return SOUND_EVENTS.register(name, () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(EdibleCrystalMod.MOD_ID, name)));
    }

    public static void register(IEventBus eventBus) {
        SOUND_EVENTS.register(eventBus);
    }
}
