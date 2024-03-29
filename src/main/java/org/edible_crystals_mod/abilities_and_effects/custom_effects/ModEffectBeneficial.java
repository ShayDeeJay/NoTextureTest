package org.edible_crystals_mod.abilities_and_effects.custom_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;

import java.util.Optional;

public class ModEffectBeneficial extends MobEffect {


    public ModEffectBeneficial(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

    @Override
    public MobEffectCategory getCategory() {
        return MobEffectCategory.BENEFICIAL;
    }

    @Override
    public Optional<MobEffectInstance.FactorData> createFactorData() {
        return super.createFactorData();
    }

    @Override
    public boolean isBeneficial() {
        return true;
    }
}
