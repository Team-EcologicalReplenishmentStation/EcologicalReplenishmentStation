package cn.aurorian.oasis.event;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import cn.aurorian.oasis.init.OasisEntities;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OasisEntityAttributeHandler {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                OasisEntities.TUBUNASUS_DUROVELA.get(),
                TubunasusDurovelaEntity.createAttributes().build());
        event.put(
                OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(),
                TubunasusClyderotundaEntity.createAttributes().build());
        event.put(
                OasisEntities.PYGOPODUS_ANNULATUM.get(),
                PygopodusAnnulatumEntity.createAttributes().build());
        event.put(
                OasisEntities.IMPERIOVENATOR_REGIUS.get(),
                ImperiovenatorRegiusEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                OasisEntities.PYGOPODUS_ANNULATUM.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                OasisEntities.IMPERIOVENATOR_REGIUS.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }
}
