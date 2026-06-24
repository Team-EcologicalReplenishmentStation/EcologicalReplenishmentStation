package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.inventory.AquicornisDinosauriformisContainerMenu;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.inventory.CristatodromeusBrachypterusContainerMenu;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.inventory.DentisaurusLongirostrisContainerMenu;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.inventory.EosuchosaurusAntiquusContainerMenu;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.inventory.TerridensaurusSaevusContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.common.extensions.IForgeMenuType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsContainers {
    public static final DeferredRegister<MenuType<?>> CONTAINERS =
            DeferredRegister.create(ForgeRegistries.MENU_TYPES, EcologicalReplenishmentStation.MODID);
    public static final RegistryObject<MenuType<DentisaurusLongirostrisContainerMenu>> SWAMP_DRAGON_CONTAINER =
            CONTAINERS.register(
                    "swamp_dragon_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        DentisaurusLongirostrisEntity sotek = (DentisaurusLongirostrisEntity)
                                inv.player.level().getEntity(entityId);

                        return new DentisaurusLongirostrisContainerMenu(windowId, inv, sotek);
                    }));

    public static final RegistryObject<MenuType<AquicornisDinosauriformisContainerMenu>>
            AQUICORNIS_DINOSAURIFORMIS_CONTAINER = CONTAINERS.register(
                    "aquicornis_dinosauriformis_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        AquicornisDinosauriformisEntity aquicornis = (AquicornisDinosauriformisEntity)
                                inv.player.level().getEntity(entityId);

                        return new AquicornisDinosauriformisContainerMenu(windowId, inv, aquicornis);
                    }));

    public static final RegistryObject<MenuType<TerridensaurusSaevusContainerMenu>> TERRIDENS_SAEVUS_CONTAINER =
            CONTAINERS.register(
                    "terridens_saevus_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        TerridensaurusSaevusEntity saevi =
                                (TerridensaurusSaevusEntity) inv.player.level().getEntity(entityId);

                        return new TerridensaurusSaevusContainerMenu(windowId, inv, saevi);
                    }));

    public static final RegistryObject<MenuType<EosuchosaurusAntiquusContainerMenu>> EOSUCHOSAURUS_ANTIQUUS_CONTAINER =
            CONTAINERS.register(
                    "eosuchosaurus_antiquus_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        EosuchosaurusAntiquusEntity antiquus =
                                (EosuchosaurusAntiquusEntity) inv.player.level().getEntity(entityId);
                        return new EosuchosaurusAntiquusContainerMenu(windowId, inv, antiquus);
                    }));

    public static final RegistryObject<MenuType<CristatodromeusBrachypterusContainerMenu>>
            CRISTATODROMEUS_BRACHYPTERUS_CONTAINER = CONTAINERS.register(
                    "cristatodromeus_brachypterus_container",
                    () -> IForgeMenuType.create((windowId, inv, data) -> {
                        int entityId = data.readInt();
                        CristatodromeusBrachypterusEntity brachypterus = (CristatodromeusBrachypterusEntity)
                                inv.player.level().getEntity(entityId);
                        return new CristatodromeusBrachypterusContainerMenu(windowId, inv, brachypterus);
                    }));

    public static void register(IEventBus bus) {
        CONTAINERS.register(bus);
    }
}
