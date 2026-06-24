package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.inventory;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class AquicornisDinosauriformisScreen extends AbstractContainerScreen<AquicornisDinosauriformisContainerMenu> {

    private static final ResourceLocation TEXTURE =
            EcologicalReplenishmentStation.prefix("textures/container/aquicornis_dinosauriformis_container.png");

    public AquicornisDinosauriformisScreen(
            AquicornisDinosauriformisContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        this.menu.entity.getAnimator().isInScreen = true;
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                pGuiGraphics,
                this.leftPos + 51,
                this.topPos + 60,
                20,
                (float) (this.leftPos + 51) - pMouseX,
                (float) (this.topPos + 60 - 50) - pMouseY,
                this.menu.entity);
        this.menu.entity.getAnimator().isInScreen = false;
    }

    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
}
