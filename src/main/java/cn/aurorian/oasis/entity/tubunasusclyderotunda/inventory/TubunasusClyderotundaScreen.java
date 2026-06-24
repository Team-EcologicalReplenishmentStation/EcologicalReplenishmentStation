package cn.aurorian.oasis.entity.tubunasusclyderotunda.inventory;

import cn.aurorian.oasis.Oasis;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class TubunasusClyderotundaScreen extends AbstractContainerScreen<TubunasusClyderotundaContainerMenu> {

    private static final ResourceLocation MALE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Oasis.MODID, "textures/container/clyderotunda_male_container.png");
    private static final ResourceLocation FEMALE_TEXTURE =
            ResourceLocation.fromNamespaceAndPath(Oasis.MODID, "textures/container/clyderotunda_female_container.png");

    public TubunasusClyderotundaScreen(
            TubunasusClyderotundaContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        var texture = FEMALE_TEXTURE;
        if (this.menu.entity.getGender()) {
            texture = MALE_TEXTURE;
        }

        pGuiGraphics.blit(texture, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);

        this.menu.entity.getAnimator().isInScreen = true;
        InventoryScreen.renderEntityInInventoryFollowsMouse(
                pGuiGraphics,
                this.leftPos + 51,
                this.topPos + 60,
                10,
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
