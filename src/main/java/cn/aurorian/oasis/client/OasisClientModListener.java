package cn.aurorian.oasis.client;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.oasis.client.render.PygopodusAnnulatumRender;
import cn.aurorian.oasis.client.render.TubunasusDurovelaRender;
import cn.aurorian.oasis.entity.tubunasusdurovela.invertory.TubunasusDurovelaScreen;
import cn.aurorian.oasis.init.OasisContainers;
import cn.aurorian.oasis.init.OasisEntities;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class OasisClientModListener {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OasisEntities.TUBUNASUS_DUROVELA.get(), TubunasusDurovelaRender::new);
        event.registerEntityRenderer(OasisEntities.PYGOPODUS_ANNULATUM.get(), PygopodusAnnulatumRender::new);
    }

    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event) {
        event.enqueueWork(() -> MenuScreens.register(OasisContainers.TUBUNASUS_CONTAINER.get(), TubunasusDurovelaScreen::new));
    }
}
