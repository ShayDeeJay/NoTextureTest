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
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LightLayer;
import org.edible_crystals_mod.block.crystalinfusiontable.CrystalInfusingTableBlock;
import org.edible_crystals_mod.block.crystalinfusiontable.CrystalInfusionTableEntity;

public class CrystalInfusionTableRenderer implements BlockEntityRenderer<CrystalInfusionTableEntity> {


    public CrystalInfusionTableRenderer(BlockEntityRendererProvider.Context context) {

    }

    private int direction(CrystalInfusionTableEntity blockEntity){
        Direction direction = blockEntity.getBlockState().getValue(CrystalInfusingTableBlock.FACING);

        if(direction == Direction.NORTH ) {
            return 0;
        }

        if(direction == Direction.WEST ) {
            return 90;
        }

        if(direction == Direction.SOUTH ) {
            return 180;
        }

        return 270;
    }

    @Override
    public void render(
        CrystalInfusionTableEntity pBlockEntity,
        float pPartialTick,
        PoseStack pPoseStack,
        MultiBufferSource pBuffer,
        int pPackedLight,
        int pPackedOverlay
    ){
        ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();
        ItemStack itemStack = pBlockEntity.getRenderer();

        pPoseStack.pushPose();

        pPoseStack.translate(0.5f, 1.52f, 0.5f);
        pPoseStack.scale(0.8f,0.8f,0.8f);

        pPoseStack.mulPose(Axis.YP.rotationDegrees(direction(pBlockEntity)));
        pPoseStack.mulPose(Axis.XP.rotationDegrees(22));
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

    private int getLightLevel(Level level, BlockPos blockPos) {
        int bLight = level.getBrightness(LightLayer.BLOCK, blockPos);
        int sLight = level.getBrightness(LightLayer.SKY, blockPos);
        return LightTexture.pack(bLight, sLight);
    }
}