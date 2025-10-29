package cn.aurorian.ers.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(value = ForgeGui.class,remap = false)
public abstract class MixinForgeGui {
    @Shadow public int rightHeight;
    @Shadow public abstract Minecraft getMinecraft();
    private static final ResourceLocation GUI_ICONS_LOCATION = ResourceLocation.withDefaultNamespace("textures/gui/icons.png");

    /**
     * @author mlus
     * @reason revamp showing way
     */
    @Overwrite
    protected void renderHealthMount(int width, int height, GuiGraphics guiGraphics) {
        Player player = (Player) getMinecraft().getCameraEntity();
        Entity tmp = player.getVehicle();
        if (tmp instanceof LivingEntity) {
            int left_align = width / 2 + 91;
            getMinecraft().getProfiler().popPush("mountHealth");
            RenderSystem.enableBlend();
            LivingEntity mount = (LivingEntity) tmp;

            float health = mount.getHealth();
            float healthMax = mount.getMaxHealth();
            float healthPercent = health / healthMax;

            int top = height - this.rightHeight;

            int BACKGROUND = 52;

            int totalHearts = 10;

            for (int i = 0; i < totalHearts; ++i) {
                int x = left_align - i * 8 - 9;
                guiGraphics.blit(GUI_ICONS_LOCATION, x, top, BACKGROUND, 9, 9, 9);
            }

            int filledHearts = (int) (totalHearts * healthPercent);
            float partialHeart = (totalHearts * healthPercent) - filledHearts;

            for (int i = 0; i < filledHearts; ++i) {
                int x = left_align - i * 8 - 9;
                guiGraphics.blit(GUI_ICONS_LOCATION, x, top, 88, 9, 9, 9); // 完整的心
            }

            if (partialHeart > 0 && filledHearts < totalHearts) {
                int x = left_align - filledHearts * 8 - 9;
                guiGraphics.blit(GUI_ICONS_LOCATION, x, top, 97, 9, 9, 9); // 半颗心
            }

//            String percentText = String.format("%d%%", (int)(healthPercent * 100));
//            int textWidth = getMinecraft().font.width(percentText);
//            int textX = left_align - totalHearts * 8 - textWidth - 5;
//            int textY = top + 1;
//
//            guiGraphics.drawString(getMinecraft().font, percentText, textX, textY, 0xFFFFFF, true);

            this.rightHeight += 10;
            RenderSystem.disableBlend();
        }
    }
}
