package cn.aurorian.oasis.init;


import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import cn.aurorian.oasis.entity.tubunasusdurovela.invertory.TubunasusDurovelaContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, Oasis.MODID);
    public static final RegistryObject<MenuType<TubunasusDurovelaContainerMenu>> TUBUNASUS_CONTAINER =
        CONTAINERS.register("tubunasus_container", () ->
            IForgeMenuType.create((windowId, inv, data) -> {
                int entityId = data.readInt();
                TubunasusDurovelaEntity sotek = (TubunasusDurovelaEntity) inv.player.level().getEntity(entityId);

                return new TubunasusDurovelaContainerMenu(windowId, inv, sotek);
            })
        );

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }
}
