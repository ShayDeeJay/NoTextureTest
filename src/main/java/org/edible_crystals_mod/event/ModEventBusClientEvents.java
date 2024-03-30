package org.edible_crystals_mod.event;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.client.renderer.BreakerRenderer;
import org.edible_crystals_mod.entity.ModEntities;
import org.edible_crystals_mod.items.held_items.TabletItem;
import org.edible_crystals_mod.registers.BlockEntitiesRegister;
import org.edible_crystals_mod.renderers.blocks.CrystalInfusionTableRenderer;
import org.edible_crystals_mod.renderers.blocks.FragmentorRenderer;
import org.edible_crystals_mod.renderers.blocks.InfusionRenderer;
import particle.ModParticles;
import particle.custom.ProjectileParticles;


@Mod.EventBusSubscriber(modid = EdibleCrystalMod.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerBER(EntityRenderersEvent.RegisterRenderers event) {
        event.registerBlockEntityRenderer(BlockEntitiesRegister.CRYSTAL_INFUSION_BE.get(), CrystalInfusionTableRenderer::new);
        event.registerBlockEntityRenderer(BlockEntitiesRegister.FRAGMENTOR_BE.get(), FragmentorRenderer::new);
        event.registerBlockEntityRenderer(BlockEntitiesRegister.INFUSER_BE.get(), InfusionRenderer::new);
        event.registerEntityRenderer(ModEntities.BREAKER.get(), BreakerRenderer::new);
//        event.registerEntityRenderer(ModEntities.BREAKER.get(), ProjectileRenderer::new);
    }

    @SubscribeEvent
    public void register (RegisterEvent event) {
        event.register(ForgeRegistries.Keys.ITEMS,
            helper -> helper.register(new ResourceLocation(EdibleCrystalMod.MOD_ID, "tablet_of_rex"), new TabletItem(new Item.Properties()))
        );
    }

    @SubscribeEvent
    public void registerParticleFactories(final RegisterParticleProvidersEvent event) {

        event.registerSpriteSet(ModParticles.PROJECTILE_PARTICLES.get(), ProjectileParticles.Provider::new);

//        Minecraft.getInstance().particleEngine.register(ModParticles.PROJECTILE_PARTICLES.get(), ProjectileParticles.Provider::new);
    }

}
