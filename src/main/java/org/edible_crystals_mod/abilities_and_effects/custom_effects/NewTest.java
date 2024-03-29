package org.edible_crystals_mod.abilities_and_effects.custom_effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;

import java.util.Optional;

public class NewTest extends MobEffect {


    public NewTest(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {

        if(pLivingEntity instanceof Player player) {

        }

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
