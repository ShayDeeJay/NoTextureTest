package org.edible_crystals_mod.event;

import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.block.entity.ModBlockEntities;
import org.edible_crystals_mod.block.entity.renderer.CrystalInfusionTableRenderer;

@Mod.EventBusSubscriber(modid = EdibleCrystalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(ModBlockEntities.CRYSTAL_INFUSION_BE.get(), CrystalInfusionTableRenderer::new);
    }
}
