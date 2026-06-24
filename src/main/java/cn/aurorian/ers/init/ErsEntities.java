package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.AcanthodesChlamydoselachoidesEntity;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.creatures.argentirhynchusgadiformis.ArgentumniscusAciculabularEntity;
import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.BenthosuchusPlanidensPlanidensEntity;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.echinomorphusconvergens.EchinomorphusConvergensEntity;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import cn.aurorian.ers.entity.creatures.latimeriapercoides.LatimeriaPercoidesEntity;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import cn.aurorian.ers.entity.creatures.magnidiscumyzonsarcopterus.MagnidiscumyzonSarcopterusEntity;
import cn.aurorian.ers.entity.creatures.mandgemarelabium.MandgemareLabiumEntity;
import cn.aurorian.ers.entity.creatures.plesiochelyslongicollis.PlesiochelysLongicollisEntity;
import cn.aurorian.ers.entity.creatures.pterochirusdux.PterochirusDuxEntity;
import cn.aurorian.ers.entity.creatures.remipessicarius.RemipesSicariusEntity;
import cn.aurorian.ers.entity.creatures.tachycarisgustatus.TachycarisGustatusEntity;
import cn.aurorian.ers.entity.creatures.tachypleusgladius.TachypleusGladiusEntity;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.entity.projectile.DragonClawHarpoonEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<EntityType<DentisaurusLongirostrisEntity>> DENTISAURUS_LONGIROSTRIS =
            ENTITIES.register("dentisaurus_longirostris", () -> EntityType.Builder.of(
                            DentisaurusLongirostrisEntity::new, MobCategory.CREATURE)
                    .sized(3.5f, 3.4f)
                    .build(EcologicalReplenishmentStation.prefix("dentisaurus_longirostris")
                            .toString()));

    public static final RegistryObject<EntityType<AquicornisDinosauriformisEntity>> AQUICORNIS_DINOSAURIFORMIS =
            ENTITIES.register("aquicornis_dinosauriformis", () -> EntityType.Builder.of(
                            AquicornisDinosauriformisEntity::new, MobCategory.CREATURE)
                    .sized(1.5f, 1.6f)
                    .build(EcologicalReplenishmentStation.prefix("aquicornis_dinosauriformis")
                            .toString()));

    public static final RegistryObject<EntityType<LatimeriaPercoidesEntity>> LATIMERIA_PERCOIDES =
            ENTITIES.register("latimeria_percoides", () -> EntityType.Builder.of(
                            LatimeriaPercoidesEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1F, 0.9F)
                    .build(EcologicalReplenishmentStation.prefix("latimeria_percoides")
                            .toString()));

    public static final RegistryObject<EntityType<LatimeriaSuchomimusEntity>> LATIMERIA_SUCHOMIMUS =
            ENTITIES.register("latimeria_suchomimus", () -> EntityType.Builder.of(
                            LatimeriaSuchomimusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(2F, 0.95F)
                    .build(EcologicalReplenishmentStation.prefix("latimeria_suchomimus")
                            .toString()));

    public static final RegistryObject<EntityType<TachypleusGladiusEntity>> TACHYPLEUS_GLADIUS = ENTITIES.register(
            "tachypleus_gladius", () -> EntityType.Builder.of(TachypleusGladiusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.2F, 0.8F)
                    .build(EcologicalReplenishmentStation.prefix("tachypleus_gladius")
                            .toString()));

    public static final RegistryObject<EntityType<TachycarisGustatusEntity>> TACHYCARIS_GUSTATUS =
            ENTITIES.register("tachycaris_gustatus", () -> EntityType.Builder.of(
                            TachycarisGustatusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.6F, 0.3F)
                    .build(EcologicalReplenishmentStation.prefix("tachycaris_gustatus")
                            .toString()));

    public static final RegistryObject<EntityType<RemipesSicariusEntity>> REMIPES_SICARIUS = ENTITIES.register(
            "remipes_sicarius", () -> EntityType.Builder.of(RemipesSicariusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.0F, 0.3F)
                    .build(EcologicalReplenishmentStation.prefix("remipes_sicarius")
                            .toString()));

    public static final RegistryObject<EntityType<AcanthodesChlamydoselachoidesEntity>> ACANTHODES_CHLAMYDOSELACHOIDES =
            ENTITIES.register("acanthodes_chlamydoselachoides", () -> EntityType.Builder.of(
                            AcanthodesChlamydoselachoidesEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.6F, 0.9F)
                    .build(EcologicalReplenishmentStation.prefix("acanthodes_chlamydoselachoides")
                            .toString()));

    public static final RegistryObject<EntityType<TerridensaurusSaevusEntity>> TERRIDENSAURUS_SAEVUS =
            ENTITIES.register("terridensaurus_saevus", () -> EntityType.Builder.of(
                            TerridensaurusSaevusEntity::new, MobCategory.CREATURE)
                    .sized(2.75F, 4.35F)
                    .build(EcologicalReplenishmentStation.prefix("terridensaurus_saevus")
                            .toString()));

    public static final RegistryObject<EntityType<DragonClawHarpoonEntity>> DRAGON_CLAW_HARPOON =
            ENTITIES.register("dragon_claw_harpoon", () -> EntityType.Builder.<DragonClawHarpoonEntity>of(
                            DragonClawHarpoonEntity::new, MobCategory.MISC)
                    .sized(0.85F, 0.5F)
                    .clientTrackingRange(4)
                    .updateInterval(20)
                    .build(EcologicalReplenishmentStation.prefix("dragon_claw_harpoon")
                            .toString()));

    public static final RegistryObject<EntityType<MagnidiscumyzonSarcopterusEntity>> MAGNIDISCUMYZON_SARCOPTERUS =
            ENTITIES.register("magnidiscumyzon_sarcopterus", () -> EntityType.Builder.of(
                            MagnidiscumyzonSarcopterusEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.4F, 0.4F)
                    .build(EcologicalReplenishmentStation.prefix("magnidiscumyzon_sarcopterus")
                            .toString()));

    public static final RegistryObject<EntityType<ArgentumniscusAciculabularEntity>> ARGENTUMNISCUS_ACICULAULAR =
            ENTITIES.register("argentumniscus_aciculabular", () -> EntityType.Builder.of(
                            ArgentumniscusAciculabularEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1F, 0.65F)
                    .build(EcologicalReplenishmentStation.prefix("argentumniscus_aciculabular")
                            .toString()));
    public static final RegistryObject<EntityType<MandgemareLabiumEntity>> MANDGEMARE_LABIUM = ENTITIES.register(
            "mandgemare_labium", () -> EntityType.Builder.of(MandgemareLabiumEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.6F, 0.6F)
                    .build(EcologicalReplenishmentStation.prefix("mandgemare_labium")
                            .toString()));

    public static final RegistryObject<EntityType<BenthosuchusPlanidensPlanidensEntity>> BENTHOSUCHUS_PLANIDENS =
            ENTITIES.register("benthosuchus_planidens", () -> EntityType.Builder.of(
                            BenthosuchusPlanidensPlanidensEntity::new, MobCategory.WATER_CREATURE)
                    .sized(2.2F, 0.85F)
                    .build(EcologicalReplenishmentStation.prefix("benthosuchus_planidens")
                            .toString()));

    public static final RegistryObject<EntityType<EosuchosaurusAntiquusEntity>> EOSUCHOSAURUS_ANTIQUUS =
            ENTITIES.register("eosuchosaurus_antiquus", () -> EntityType.Builder.of(
                            EosuchosaurusAntiquusEntity::new, MobCategory.CREATURE)
                    .sized(2F, 2.6F)
                    .build(EcologicalReplenishmentStation.prefix("eosuchosaurus_antiquus")
                            .toString()));

    public static final RegistryObject<EntityType<EchinomorphusConvergensEntity>> ECHINOMORPHUS_CONVERGENS =
            ENTITIES.register("echinomorphus_convergens", () -> EntityType.Builder.of(
                            EchinomorphusConvergensEntity::new, MobCategory.WATER_CREATURE)
                    .sized(0.6F, 0.6F)
                    .build(EcologicalReplenishmentStation.prefix("echinomorphus_convergens")
                            .toString()));

    public static final RegistryObject<EntityType<PterochirusDuxEntity>> PTEROCHIRUS_DUX = ENTITIES.register(
            "pterochirus_dux", () -> EntityType.Builder.of(PterochirusDuxEntity::new, MobCategory.CREATURE)
                    .sized(2.2F, 1.75F)
                    .build(EcologicalReplenishmentStation.prefix("pterochirus_dux")
                            .toString()));

    public static final RegistryObject<EntityType<PlesiochelysLongicollisEntity>> PLESIOCHELYS_LONGICOLLIS =
            ENTITIES.register("plesiochelys_longicollis", () -> EntityType.Builder.of(
                            PlesiochelysLongicollisEntity::new, MobCategory.WATER_CREATURE)
                    .sized(1.2F, 0.6F)
                    .build(EcologicalReplenishmentStation.prefix("plesiochelys_longicollis")
                            .toString()));

    public static final RegistryObject<EntityType<CristatodromeusBrachypterusEntity>> CRISTATODROMEUS_BRACHYPTERUS =
            ENTITIES.register("cristatodromeus_brachypterus", () -> EntityType.Builder.of(
                            CristatodromeusBrachypterusEntity::new, MobCategory.CREATURE)
                    .sized(1.5F, 2.45F)
                    .build(EcologicalReplenishmentStation.prefix("cristatodromeus_brachypterus")
                            .toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
