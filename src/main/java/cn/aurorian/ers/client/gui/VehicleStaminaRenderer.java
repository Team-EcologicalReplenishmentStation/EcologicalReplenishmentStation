package cn.aurorian.ers.client.gui;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

public class VehicleStaminaRenderer {
    static Minecraft minecraft = Minecraft.getInstance();
    public static ResourceLocation STAMINA = EcologicalReplenishmentStation.prefix("textures/gui/stamina.png");
    protected final static RandomSource random = RandomSource.create();
    public static IGuiOverlay STAMINA_OVERLAY = (forgeGui, guiGraphics, partialTicks, screenWidth, screenHeight) ->
            render(forgeGui, guiGraphics, screenWidth, screenHeight, minecraft.player);

    @SuppressWarnings("unused")
    public static void render(ForgeGui gui, GuiGraphics guiGraphics, int width, int height, Player player) {
        if (gui.shouldDrawSurvivalElements() && player.getVehicle() instanceof ErsTamableVehicle<?> vehicle) {
            minecraft.getProfiler().push("stamina");
            RenderSystem.enableBlend();

            int left = width / 2 + 91;
            int top = height - gui.rightHeight;
            gui.rightHeight += 10;

            float level = vehicle.getStamina() / 5;

            for (int i = 0; i < 10; ++i)
            {
                int idx = i * 2 + 1;
                int x = left - i * 8 - 9;
                int y = top;

                if (level <= 50 && gui.getGuiTicks() % (level * 3 + 1) == 0)
                {
                    y = top + (random.nextInt(3) - 1);
                }

                guiGraphics.blit(STAMINA, x, y, 0, 0, 9, 9, 25, 9);

                if (idx < level)
                    guiGraphics.blit(STAMINA, x, y, 16, 0, 9, 9, 25, 9);
                else if (idx == level)
                    guiGraphics.blit(STAMINA, x, y, 8, 0, 9, 9, 25, 9);
            }
            RenderSystem.disableBlend();

            minecraft.getProfiler().pop();
        }
    }

    public static void registerStaminaOverlay(RegisterGuiOverlaysEvent event) {
        event.registerAbove(VanillaGuiOverlay.MOUNT_HEALTH.id(), "stamina", STAMINA_OVERLAY);
    }

}