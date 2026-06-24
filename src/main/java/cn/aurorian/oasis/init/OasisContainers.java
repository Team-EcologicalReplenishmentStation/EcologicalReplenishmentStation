package cn.aurorian.oasis.init;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import cn.aurorian.oasis.entity.imperiovenatorregius.inventory.ImperiovenatorRegiusContainerMenu;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.inventory.TubunasusClyderotundaContainerMenu;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import cn.aurorian.oasis.entity.tubunasusdurovela.inventory.TubunasusDurovelaContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, Oasis.MODID);
    public static final RegistryObject<MenuType<TubunasusDurovelaContainerMenu>> DUROVELA_CONTAINER =
            CONTAINERS.register(
                    "durovela_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        TubunasusDurovelaEntity entity =
                                (TubunasusDurovelaEntity) inv.player.level().getEntity(entityId);

                        return new TubunasusDurovelaContainerMenu(windowId, inv, entity);
                    }));

    public static final RegistryObject<MenuType<TubunasusClyderotundaContainerMenu>> CLYDEROTUNDA_CONTAINER =
            CONTAINERS.register(
                    "tubunasus_clyderotunda_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        TubunasusClyderotundaEntity entity =
                                (TubunasusClyderotundaEntity) inv.player.level().getEntity(entityId);

                        return new TubunasusClyderotundaContainerMenu(windowId, inv, entity);
                    }));

    public static final RegistryObject<MenuType<ImperiovenatorRegiusContainerMenu>> REGIUS_CONTAINER =
            CONTAINERS.register(
                    "imperiovenator_regius_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        ImperiovenatorRegiusEntity entity =
                                (ImperiovenatorRegiusEntity) inv.player.level().getEntity(entityId);

                        return new ImperiovenatorRegiusContainerMenu(windowId, inv, entity);
                    }));

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }
}
