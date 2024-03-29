package org.edible_crystals_mod.renderers.blocks;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LightTexture;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.edible_crystals_mod.block.infuser.InfuserBlockEntity;

public class InfusionRenderer implements BlockEntityRenderer<InfuserBlockEntity>{

    public InfusionRenderer(BlockEntityRendererProvider.Context context) {

    }


    private int getLightLevel(Level level, BlockPos blockPos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, blockPos);
        int sLight = level.getBrightness(LightLayer.SKY, blockPos);
        return LightTexture.pack(bLight, sLight);
    }

    @Override
    public void render(InfuserBlockEntity pBlockEntity, float pPartialTick, PoseStack pPoseStack, MultiBufferSource pBuffer, int pPackedLight, int pPackedOverlay) {
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderer();

        pPoseStack.pushPose();
        pPoseStack.translate(0.5f, 0.5f, 0.5f);
        pPoseStack.scale(0.5f,0.5f,0.5f);
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.sin(2 * 10000.1F) * 180.0F));
        itemRenderer.renderStatic(
                itemStack,
                ItemDisplayContext.FIXED,
                getLightLevel(pBlockEntity.getLevel(), pBlockEntity.getBlockPos()),
                OverlayTexture.NO_OVERLAY,
                pPoseStack,
                pBuffer,
                pBlockEntity.getLevel(),
                1
        );
        pPoseStack.popPose();
    }
}