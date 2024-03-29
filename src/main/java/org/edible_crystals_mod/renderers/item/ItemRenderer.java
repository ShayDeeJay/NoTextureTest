package org.edible_crystals_mod.renderers.item;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.items.held_items.TabletItem;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class ItemRenderer extends GeoItemRenderer<TabletItem> {

    public ItemRenderer() {
        super(new DefaultedItemGeoModel<>(new ResourceLocation(EdibleCrystalMod.MOD_ID, "tablet_of_rex")));
    }


    @Override
    public void defaultRender(PoseStack poseStack, TabletItem animatable, MultiBufferSource bufferSource, @Nullable RenderType renderType, @Nullable VertexConsumer buffer, float yaw, float partialTick, int packedLight) {
        super.defaultRender(poseStack, animatable, bufferSource, renderType, buffer, yaw, partialTick, packedLight);
    }
}