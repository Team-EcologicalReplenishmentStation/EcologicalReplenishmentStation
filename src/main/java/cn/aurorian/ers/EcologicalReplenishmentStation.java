package cn.aurorian.ers;

import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.ers.client.gui.VehicleStaminaRenderer;
import cn.aurorian.ers.init.*;
import cn.aurorian.oasis.init.*;
import net.minecraft.client.renderer.item.ItemProperties;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import software.bernie.geckolib.GeckoLib;

import java.util.Locale;

@EventBusSubscriber
@Mod(EcologicalReplenishmentStation.MODID)
public class EcologicalReplenishmentStation {

    public static final String MODID = "ers";

    public static boolean AlexsMobsLoaded;
    public static boolean PrehistoricFaunaLoaded;
    public EcologicalReplenishmentStation(FMLJavaModLoadingContext context) {
        GeckoLib.initialize();
        initRegister(context.getModEventBus());
        context.getModEventBus().addListener(this::commonSetup);
        context.getModEventBus().addListener(this::setupClient);

        if (FMLEnvironment.dist.isClient()) {
            context.getModEventBus().addListener(VehicleStaminaRenderer::registerStaminaOverlay);
        }
    }

    private void initRegister(IEventBus eventBus) {
        ErsEntities.register(eventBus);
        ErsContainers.register(eventBus);
        ErsBlocks.register(eventBus);
        ErsBlockEntities.register(eventBus);
        ErsItems.register(eventBus);
        ErsMobEffects.register(eventBus);
        ErsCreativeTab.register(eventBus);
        ErsNetwork.register();
        ErsSerializers.register(eventBus);
        ErsDataTickets.register();
        ErsSounds.SOUND_EVENTS.register(eventBus);
        ErsParticleType.PARTICLE_TYPES.register(eventBus);
        ErsBiomeModifierSerializers.register(eventBus);

        OasisEntities.register(eventBus);
        OasisContainers.register(eventBus);
        OasisItems.register(eventBus);
        OasisCreativeTab.register(eventBus);
        OasisMobEffects.register(eventBus);
        OasisPotion.register(eventBus);
        OasisSounds.SOUND_EVENTS.register(eventBus);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        AlexsMobsLoaded = ModList.get().isLoaded("alexsmobs");
        PrehistoricFaunaLoaded = ModList.get().isLoaded("prehistoricfauna");
        ErsItems.registerComposterBlock();
        OasisPotionRecipe.registerRecipes();
    }

    public void setupClient(FMLClientSetupEvent event) {
        ItemProperties.register(ErsItems.DRAGON_CLAW_HARPOON.get(), prefix("throwing"), (stack, level, entity, p) -> entity != null && entity.isUsingItem() && entity.getUseItem() == stack ? 1.0F : 0.0F);
    }

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));
    }
}