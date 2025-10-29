package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.invertory;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import org.jetbrains.annotations.NotNull;

public class DentisaurusLongirostrisScreen extends AbstractContainerScreen<DentisaurusLongirostrisContainerMenu> {

    private static final ResourceLocation TEXTURE = ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/container/swamp_dragon_container.png");
    private static final ResourceLocation ELITE_TEXTURE = ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/container/swamp_dragon_container_elite.png");

    public DentisaurusLongirostrisScreen(DentisaurusLongirostrisContainerMenu pMenu, Inventory pPlayerInventory, Component pTitle) {
        super(pMenu, pPlayerInventory, pTitle);
    }

    @Override
    protected void renderBg(@NotNull GuiGraphics pGuiGraphics, float pPartialTick, int pMouseX, int pMouseY) {
        if(this.menu.sotek.isElite()){
            pGuiGraphics.blit(ELITE_TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        }else {
            pGuiGraphics.blit(TEXTURE, this.leftPos, this.topPos, 0, 0, this.imageWidth, this.imageHeight);
        }

        this.menu.sotek.setIsInScreen(true);
        InventoryScreen.renderEntityInInventoryFollowsMouse(pGuiGraphics, this.leftPos + 51, this.topPos + 60, 10, (float)(this.leftPos + 51) - pMouseX, (float)(this.topPos + 60 - 50) - pMouseY, this.menu.sotek);
        this.menu.sotek.setIsInScreen(false);
    }
    @Override
    public void render(@NotNull GuiGraphics pGuiGraphics, int pMouseX, int pMouseY, float pPartialTick) {
        this.renderBackground(pGuiGraphics);
        super.render(pGuiGraphics, pMouseX, pMouseY, pPartialTick);
        this.renderTooltip(pGuiGraphics, pMouseX, pMouseY);
    }
    
}