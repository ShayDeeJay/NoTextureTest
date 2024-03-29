package org.edible_crystals_mod.block.crystalinfusiontable;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.edible_crystals_mod.EdibleCrystalMod;
import org.edible_crystals_mod.block.crystalinfusiontable.InfusionTableMenu;

public class InfusionTableScreen extends AbstractContainerScreen<InfusionTableMenu> {
    private static final ResourceLocation TEXTURE =
            new ResourceLocation(EdibleCrystalMod.MOD_ID, "textures/gui/infusion_table_gui.png");

    public InfusionTableScreen(InfusionTableMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void init() {
        super.init();
        this.inventoryLabelY = 10000;
        this.titleLabelY = 10000;
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, TEXTURE);
        int x = (width - imageWidth) / 2;
        int y = (height - imageHeight) / 2;

        guiGraphics.blit(TEXTURE, x, y, 0, 0, imageWidth, imageHeight);

        renderProgressBar(guiGraphics, x, y);
    }

    private void renderProgressBar(GuiGraphics guiGraphics, int x, int y) {
        if(menu.isCrafting()) {
            guiGraphics.blit(TEXTURE, x + 54, y + 39, 176, 0, menu.getScaledProgress(25), 4);
            guiGraphics.blit(TEXTURE, x + 122, y + 43, 200, 8, -menu.getScaledProgress(25), -4);
        }
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float delta) {
        renderBackground(guiGraphics);
        super.render(guiGraphics, mouseX, mouseY, delta);
        renderTooltip(guiGraphics, mouseX, mouseY);
    }
}
