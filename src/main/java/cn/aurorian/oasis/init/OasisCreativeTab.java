package cn.aurorian.oasis.init;

import cn.aurorian.oasis.Oasis;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class OasisCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, Oasis.MODID);
    public static RegistryObject<CreativeModeTab> ERS_TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.oasis_group"))
            .icon(() -> OasisItems.TUBUNASUS_DUROVELA_SPAWN_EGG.get().getDefaultInstance())
            .displayItems((par, output) -> {
                output.accept(OasisItems.TUBUNASUS_DUROVELA_SPAWN_EGG.get());
                output.accept(OasisItems.PYGOPODUS_ANNULATUM_SPAWN_EGG.get());
                output.accept(OasisItems.TUBUNASUS_DUROVELA_LARGE_BUCKET.get());
                output.accept(OasisItems.TUBUNASUS_SADDLE.get());
                output.accept(OasisItems.HORSESHOE.get());
                output.accept(OasisItems.BONE.get());
                output.accept(OasisItems.HEART.get());
                output.accept(OasisItems.INTESTINES.get());
                output.accept(OasisItems.LEATHER.get());
                output.accept(OasisItems.LIVER.get());
                output.accept(OasisItems.LUNG.get());
                output.accept(OasisItems.EMBRYO.get());
                output.accept(OasisItems.COOKED_LUNG.get());
                output.accept(OasisItems.KIDNEY.get());
                output.accept(OasisItems.COOKED_KIDNEY.get());
                output.accept(OasisItems.ANNULATUM.get());
                output.accept(OasisItems.COOKED_ANNULATUM.get());
                output.accept(OasisItems.TEASELGOURD.get());
            })
            .build());
    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
