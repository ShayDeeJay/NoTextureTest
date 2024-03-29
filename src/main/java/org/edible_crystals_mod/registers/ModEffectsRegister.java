package org.edible_crystals_mod.registers;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.abilities_and_effects.custom_effects.ModEffectBeneficial;

public class ModEffectsRegister {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
        DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EdibleCrystalMod.MOD_ID);


    public static final RegistryObject<MobEffect> STEP_BOOST = MOB_EFFECTS.register("step_boost",
        () -> new ModEffectBeneficial(MobEffectCategory.BENEFICIAL, 3434234).addAttributeModifier(ForgeMod.STEP_HEIGHT_ADDITION.get(), "75ab037d-34d9-4cb9-a918-b11b454eb738",1.0D, AttributeModifier.Operation.ADDITION ));


    public static final RegistryObject<MobEffect> AMPLIFY_REACH = MOB_EFFECTS.register("amplify_reach",
        () -> new ModEffectBeneficial(MobEffectCategory.BENEFICIAL, 3436524).addAttributeModifier(ForgeMod.BLOCK_REACH.get(), "2e121062-10de-4f66-ba73-91a43f4325cb",1.0D, AttributeModifier.Operation.ADDITION ));
    public static final RegistryObject<MobEffect> NO_FALL_DAMAGE= MOB_EFFECTS.register("no_fall_damage",
        () -> new ModEffectBeneficial(MobEffectCategory.BENEFICIAL, 3436524).addAttributeModifier(Attributes.FOLLOW_RANGE, "2e121062-10de-4f66-ba73-91a43f4325cb",1.0D, AttributeModifier.Operation.ADDITION ));
    public static void register (IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }

}
