package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.AcanthodesChlamydoselachoidesEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.latimeriapercoides.LatimeriaPercoidesEntity;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
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
    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES,
            EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<EntityType<DentisaurusLongirostrisEntity>> DENTISAURUS_LONGIROSTRIS = ENTITIES.register("dentisaurus_longirostris",
    () -> EntityType.Builder.of(DentisaurusLongirostrisEntity::new, MobCategory.CREATURE)
            .sized(DentisaurusLongirostrisEntity.BASE_BOUNDING_BOX_WIDTH, DentisaurusLongirostrisEntity.BASE_BOUNDING_BOX_HEIGHT)
            .build(EcologicalReplenishmentStation.prefix( "dentisaurus_longirostris").toString()));

    public static final RegistryObject<EntityType<LatimeriaPercoidesEntity>> LATIMERIA_PERCOIDES = ENTITIES.register("latimeria_percoides",
    () -> EntityType.Builder.of(LatimeriaPercoidesEntity::new, MobCategory.WATER_CREATURE)
            .sized(1F, 1F)
            .build(EcologicalReplenishmentStation.prefix( "latimeria_percoides").toString()));

    public static final RegistryObject<EntityType<LatimeriaSuchomimusEntity>> LATIMERIA_SUCHOMIMUS = ENTITIES.register("latimeria_suchomimus",
    () -> EntityType.Builder.of(LatimeriaSuchomimusEntity::new, MobCategory.WATER_CREATURE)
            .sized(1F, 1F)
            .build(EcologicalReplenishmentStation.prefix( "latimeria_suchomimus").toString()));

    public static final RegistryObject<EntityType<TachypleusGladiusEntity>> TACHYPLEUS_GLADIUS = ENTITIES.register("tachypleus_gladius",
    () -> EntityType.Builder.of(TachypleusGladiusEntity::new, MobCategory.WATER_CREATURE)
            .sized(1.2F, 0.8F)
            .build(EcologicalReplenishmentStation.prefix( "tachypleus_gladius").toString()));

    public static final RegistryObject<EntityType<AcanthodesChlamydoselachoidesEntity>> ACANTHODES_CHLAMYDOSELACHOIDES = ENTITIES.register("acanthodes_chlamydoselachoides",
    () -> EntityType.Builder.of(AcanthodesChlamydoselachoidesEntity::new, MobCategory.WATER_CREATURE)
            .sized(1.6F, 0.9F)
            .build(EcologicalReplenishmentStation.prefix( "acanthodes_chlamydoselachoides").toString()));

    public static final RegistryObject<EntityType<TerridensaurusSaevusEntity>> TERRIDENSAURUS_SAEVUS = ENTITIES.register("terridensaurus_saevus",
    () -> EntityType.Builder.of(TerridensaurusSaevusEntity::new, MobCategory.CREATURE)
            .sized(2.5F,4.2F)
            .build(EcologicalReplenishmentStation.prefix("terridensaurus_saevus").toString()));

    public static final RegistryObject<EntityType<DragonClawHarpoonEntity>> DRAGON_CLAW_HARPOON = ENTITIES.register("dragon_claw_harpoon",
    () -> EntityType.Builder.<DragonClawHarpoonEntity>of(DragonClawHarpoonEntity::new, MobCategory.MISC)
            .sized(0.85F, 0.5F)
            .clientTrackingRange(4)
            .updateInterval(20)
            .build(EcologicalReplenishmentStation.prefix( "dragon_claw_harpoon").toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
