package org.edible_crystals_mod.renderers.entity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.ShulkerBulletModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.abilities_and_effects.CustomProjectileCrystal;

@OnlyIn(Dist.CLIENT)
public class ProjectileRenderer extends EntityRenderer<CustomProjectileCrystal> {

    private static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(EdibleCrystalMod.MOD_ID,"textures/entity/spark.png");
    private static final RenderType RENDER_TYPE = RenderType.entityTranslucentEmissive(TEXTURE_LOCATION, true);
    private final ShulkerBulletModel<CustomProjectileCrystal> model;

    public ProjectileRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new ShulkerBulletModel<>(pContext.bakeLayer(ModelLayers.SHULKER_BULLET));
    }

    @Override
    public ResourceLocation getTextureLocation(CustomProjectileCrystal pEntity) {
        return TEXTURE_LOCATION;
    }

    @Override
    protected int getBlockLightLevel(CustomProjectileCrystal pEntity, BlockPos pPos) {
        return 15;
    }

    public void render(CustomProjectileCrystal pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        float f = Mth.rotLerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot());
        float f1 = Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot());
        float f2 = (float)pEntity.tickCount + pPartialTicks;


        pPoseStack.translate(0.0f, 0.2f, 0.0f );
//        pPoseStack.translate(pe, f, 0);
        pPoseStack.scale(0.3F, 0.3F, 0.3F);

        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * -90.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * -90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.10F) * -90.0F));

        this.model.setupAnim(pEntity, 0.0F, 0.0F, 0.0F, f, f1);
        VertexConsumer vertexconsumer = pBuffer.getBuffer(this.model.renderType(TEXTURE_LOCATION));
        this.model.renderToBuffer(pPoseStack, vertexconsumer, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 0.6F, 1.0F, 1.0F);

        pPoseStack.scale(4.5F, 4.5F, 4.5F);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(Mth.cos(f2 * 0.1F) * 180.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.sin(f2 * 0.10F) * 180.0F));

        VertexConsumer vertexconsumer1 = pBuffer.getBuffer(RENDER_TYPE);
        this.model.renderToBuffer(pPoseStack, vertexconsumer1, pPackedLight, OverlayTexture.NO_OVERLAY, 0.0F, 1.0F, 1.0F, 2.15F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }


}
