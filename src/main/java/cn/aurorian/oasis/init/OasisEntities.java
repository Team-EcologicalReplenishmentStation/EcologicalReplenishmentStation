package cn.aurorian.oasis.init;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisEntities {
    public static final DeferredRegister<EntityType<?>> ENTITIES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, Oasis.MODID);

    public static final RegistryObject<EntityType<TubunasusDurovelaEntity>> TUBUNASUS_DUROVELA = ENTITIES.register(
            "tubunasus_durovela", () -> EntityType.Builder.of(TubunasusDurovelaEntity::new, MobCategory.CREATURE)
                    .sized(3, 3.2f)
                    .build(ResourceLocation.fromNamespaceAndPath(Oasis.MODID, "tubunasus_durovela")
                            .toString()));

    public static final RegistryObject<EntityType<TubunasusClyderotundaEntity>> TUBUNASUS_CLYDEROTUNDA =
            ENTITIES.register("tubunasus_clyderotunda", () -> EntityType.Builder.of(
                            TubunasusClyderotundaEntity::new, MobCategory.CREATURE)
                    .sized(2.85f, 2.9f)
                    .build(ResourceLocation.fromNamespaceAndPath(Oasis.MODID, "tubunasus_clyderotunda")
                            .toString()));

    public static final RegistryObject<EntityType<PygopodusAnnulatumEntity>> PYGOPODUS_ANNULATUM = ENTITIES.register(
            "pygopodus_annulatum", () -> EntityType.Builder.of(PygopodusAnnulatumEntity::new, MobCategory.CREATURE)
                    .sized(0.6f, 1.3f)
                    .build(ResourceLocation.fromNamespaceAndPath(Oasis.MODID, "pygopodus_annulatum")
                            .toString()));
    public static final RegistryObject<EntityType<ImperiovenatorRegiusEntity>> IMPERIOVENATOR_REGIUS =
            ENTITIES.register("imperiovenator_regius", () -> EntityType.Builder.of(
                            ImperiovenatorRegiusEntity::new, MobCategory.CREATURE)
                    .sized(2.5f, 3f)
                    .build(ResourceLocation.fromNamespaceAndPath(Oasis.MODID, "imperiovenator_regius")
                            .toString()));

    public static void register(IEventBus eventBus) {
        ENTITIES.register(eventBus);
    }
}
