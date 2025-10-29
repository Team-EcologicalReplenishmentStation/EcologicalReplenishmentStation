package cn.aurorian.ers.init;


import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.invertory.DentisaurusLongirostrisContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS = DeferredRegister.create(ForgeRegistries.MENU_TYPES, EcologicalReplenishmentStation.MODID);
    public static final RegistryObject<MenuType<DentisaurusLongirostrisContainerMenu>> SWAMP_DRAGON_CONTAINER =
        CONTAINERS.register("swamp_dragon_container", () ->
            IForgeMenuType.create((windowId, inv, data) -> {
                int entityId = data.readInt();
                DentisaurusLongirostrisEntity sotek = (DentisaurusLongirostrisEntity) inv.player.level().getEntity(entityId);

                return new DentisaurusLongirostrisContainerMenu(windowId, inv, sotek);
            })
        );

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }
}
