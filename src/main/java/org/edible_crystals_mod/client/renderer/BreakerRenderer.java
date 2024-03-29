package org.edible_crystals_mod.client.renderer;

import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.edible_crystals_mod.abilities_and_effects.CustomProjectileCrystal;
import org.edible_crystals_mod.client.model.BreakerModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

import javax.annotation.Nullable;

public class BreakerRenderer extends GeoEntityRenderer<CustomProjectileCrystal> {

    public BreakerRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BreakerModel());
    }
//    @Override
//    public void actuallyRender(PoseStack poseStack, CustomProjectileCrystal animatable, BakedGeoModel model, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, boolean isReRender, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
//
//        poseStack.translate(0.0f, -0.05f, 0.0f);
//        poseStack.scale(1.6F, 1.6F, 1.6F);
//
//        super.actuallyRender(poseStack, animatable, model, renderType, bufferSource, buffer, isReRender, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
//    }

    @Override
    public RenderType getRenderType(CustomProjectileCrystal animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        return RenderType.entityTranslucent(this.getTextureLocation(animatable));
    }
}
