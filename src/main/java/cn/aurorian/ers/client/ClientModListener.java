package cn.aurorian.ers.client;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.client.model.entity.DragonClawHarpoonModel;
import cn.aurorian.ers.client.model.layer.ErsLayers;
import cn.aurorian.ers.client.render.block.ErsBlockRenderer;
import cn.aurorian.ers.client.render.block.SwampDragonFecesRender;
import cn.aurorian.ers.client.render.block.SwampDragonNestRender;
import cn.aurorian.ers.client.render.entity.DragonClawHarpoonRender;
import cn.aurorian.ers.client.render.entity.ErsRenderer;
import cn.aurorian.ers.client.render.entity.SwampDragonRender;
import cn.aurorian.ers.client.render.entity.TerridensaurusSaevusRender;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.invertory.DentisaurusLongirostrisScreen;
import cn.aurorian.ers.init.ErsBlockEntities;
import cn.aurorian.ers.init.ErsContainers;
import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.ers.init.ErsKeyBindings;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value =Dist.CLIENT)
public final class ClientModListener {
	@SubscribeEvent
	public static void registerRenderers(final EntityRenderersEvent.RegisterRenderers event) {
		event.registerEntityRenderer(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), SwampDragonRender::new);
		event.registerEntityRenderer(ErsEntities.LATIMERIA_PERCOIDES.get(), ErsRenderer::new);
		event.registerEntityRenderer(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), (EntityRendererProvider.Context context) -> new ErsRenderer<>(context, 10));
		event.registerEntityRenderer(ErsEntities.TACHYPLEUS_GLADIUS.get(), (EntityRendererProvider.Context context) -> new ErsRenderer<>(context, 15));
		event.registerEntityRenderer(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), (EntityRendererProvider.Context context) -> new ErsRenderer<>(context, 45));
		event.registerEntityRenderer(ErsEntities.TERRIDENSAURUS_SAEVUS.get(), TerridensaurusSaevusRender::new);
		event.registerBlockEntityRenderer(ErsBlockEntities.SWAMP_DRAGON_BONE_FECES_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context context) ->new SwampDragonFecesRender(ErsBlockEntities.SWAMP_DRAGON_BONE_FECES_BLOCK_ENTITY.get()));
		event.registerBlockEntityRenderer(ErsBlockEntities.SWAMP_DRAGON_SMALL_FECES_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context context) -> new SwampDragonFecesRender(ErsBlockEntities.SWAMP_DRAGON_SMALL_FECES_BLOCK_ENTITY.get()));
		event.registerBlockEntityRenderer(ErsBlockEntities.SWAMP_DRAGON_LARGE_FECES_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context context) -> new SwampDragonFecesRender(ErsBlockEntities.SWAMP_DRAGON_LARGE_FECES_BLOCK_ENTITY.get()));
		event.registerBlockEntityRenderer(ErsBlockEntities.SWAMP_DRAGON_GLASSES_FECES_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context context) -> new SwampDragonFecesRender(ErsBlockEntities.SWAMP_DRAGON_GLASSES_FECES_BLOCK_ENTITY.get()));
		event.registerBlockEntityRenderer(ErsBlockEntities.SWAMP_DRAGON_TEL_FECES_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context context) -> new SwampDragonFecesRender(ErsBlockEntities.SWAMP_DRAGON_TEL_FECES_BLOCK_ENTITY.get()));
		event.registerBlockEntityRenderer(ErsBlockEntities.SWAMP_DRAGON_NEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context context) -> new ErsBlockRenderer<>().withScale(2));
		event.registerBlockEntityRenderer(ErsBlockEntities.SWAMP_DRAGON_ARTIFICIAL_NEST_BLOCK_ENTITY.get(), (BlockEntityRendererProvider.Context context) -> new SwampDragonNestRender().withScale(2));

		event.registerEntityRenderer(ErsEntities.DRAGON_CLAW_HARPOON.get(), DragonClawHarpoonRender::new);
	}

	@SubscribeEvent
	public static void registerLayerDefinitions(EntityRenderersEvent.RegisterLayerDefinitions event) {
		event.registerLayerDefinition(ErsLayers.DRAGON_CLAW_HARPOON, DragonClawHarpoonModel::createBodyLayer);
	}

	@SubscribeEvent
	public static void registerRenderers(final FMLClientSetupEvent event) {
		//注册GUI
		event.enqueueWork(() -> MenuScreens.register(ErsContainers.SWAMP_DRAGON_CONTAINER.get(), DentisaurusLongirostrisScreen::new));
	}
	@SubscribeEvent
	public static void registerKeys(RegisterKeyMappingsEvent event) {
		event.register(ErsKeyBindings.DIVE_KEY);
		event.register(ErsKeyBindings.ATTACK_KEY);
        event.register(ErsKeyBindings.ATTACK2_KEY);
		event.register(ErsKeyBindings.ATTACK3_KEY);
		event.register(ErsKeyBindings.ATTACK4_KEY);
    }

}

