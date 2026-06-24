package cn.aurorian.oasis.client;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.client.render.entity.ErsRenderer;
import cn.aurorian.oasis.client.model.DurovelaSpecimenModel;
import cn.aurorian.oasis.client.model.PygopodusAnnulatumModel;
import cn.aurorian.oasis.client.render.ImperiovenatorRegiusRender;
import cn.aurorian.oasis.client.render.TubunasusClyderotundaRender;
import cn.aurorian.oasis.client.render.TubunasusDurovelaRender;
import cn.aurorian.oasis.entity.imperiovenatorregius.inventory.ImperiovenatorRegiusScreen;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.inventory.TubunasusClyderotundaScreen;
import cn.aurorian.oasis.entity.tubunasusdurovela.inventory.TubunasusDurovelaScreen;
import cn.aurorian.oasis.init.OasisBlockEntities;
import cn.aurorian.oasis.init.OasisContainers;
import cn.aurorian.oasis.init.OasisEntities;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

@Mod.EventBusSubscriber(
        modid = EcologicalReplenishmentStation.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class OasisClientModListener {
    @SubscribeEvent
    public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(OasisEntities.TUBUNASUS_DUROVELA.get(), TubunasusDurovelaRender::new);
        event.registerEntityRenderer(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), TubunasusClyderotundaRender::new);
        event.registerEntityRenderer(
                OasisEntities.PYGOPODUS_ANNULATUM.get(),
                (EntityRendererProvider.Context context) ->
                        new ErsRenderer<>(context, new PygopodusAnnulatumModel(), 0));
        event.registerEntityRenderer(OasisEntities.IMPERIOVENATOR_REGIUS.get(), ImperiovenatorRegiusRender::new);

        event.registerBlockEntityRenderer(
                OasisBlockEntities.DUROVELA_SPECIMEN_BLOCK_ENTITY.get(),
                (BlockEntityRendererProvider.Context context) -> new GeoBlockRenderer<>(new DurovelaSpecimenModel()));
    }

    @SubscribeEvent
    public static void registerRenderers(final FMLClientSetupEvent event) {
        event.enqueueWork(
                () -> MenuScreens.register(OasisContainers.DUROVELA_CONTAINER.get(), TubunasusDurovelaScreen::new));
        event.enqueueWork(() ->
                MenuScreens.register(OasisContainers.CLYDEROTUNDA_CONTAINER.get(), TubunasusClyderotundaScreen::new));
        event.enqueueWork(
                () -> MenuScreens.register(OasisContainers.REGIUS_CONTAINER.get(), ImperiovenatorRegiusScreen::new));
    }
}
