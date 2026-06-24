package cn.aurorian.ers.event;

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
import cn.aurorian.ers.init.ErsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityAttributeHandler {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(
                ErsEntities.DENTISAURUS_LONGIROSTRIS.get(),
                DentisaurusLongirostrisEntity.createAttributes().build());
        event.put(
                ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(),
                AquicornisDinosauriformisEntity.createAttributes().build());
        event.put(
                ErsEntities.LATIMERIA_PERCOIDES.get(),
                LatimeriaPercoidesEntity.createAttributes().build());
        event.put(
                ErsEntities.LATIMERIA_SUCHOMIMUS.get(),
                LatimeriaSuchomimusEntity.createAttributes().build());
        event.put(
                ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(),
                AcanthodesChlamydoselachoidesEntity.createAttributes().build());
        event.put(
                ErsEntities.TACHYPLEUS_GLADIUS.get(),
                TachypleusGladiusEntity.createAttributes().build());
        event.put(
                ErsEntities.TACHYCARIS_GUSTATUS.get(),
                TachycarisGustatusEntity.createAttributes().build());
        event.put(
                ErsEntities.TERRIDENSAURUS_SAEVUS.get(),
                TerridensaurusSaevusEntity.createAttributes().build());
        event.put(
                ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(),
                MagnidiscumyzonSarcopterusEntity.createAttributes().build());
        event.put(
                ErsEntities.ARGENTUMNISCUS_ACICULAULAR.get(),
                ArgentumniscusAciculabularEntity.createAttributes().build());
        event.put(
                ErsEntities.MANDGEMARE_LABIUM.get(),
                MandgemareLabiumEntity.createAttributes().build());
        event.put(
                ErsEntities.BENTHOSUCHUS_PLANIDENS.get(),
                BenthosuchusPlanidensPlanidensEntity.createAttributes().build());
        event.put(
                ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(),
                EosuchosaurusAntiquusEntity.createAttributes().build());
        event.put(
                ErsEntities.ECHINOMORPHUS_CONVERGENS.get(),
                EchinomorphusConvergensEntity.createAttributes().build());
        event.put(
                ErsEntities.PTEROCHIRUS_DUX.get(),
                PterochirusDuxEntity.createAttributes().build());
        event.put(
                ErsEntities.REMIPES_SICARIUS.get(),
                RemipesSicariusEntity.createAttributes().build());
        event.put(
                ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(),
                PlesiochelysLongicollisEntity.createAttributes().build());
        event.put(
                ErsEntities.CRISTATODROMEUS_BRACHYPTERUS.get(),
                CristatodromeusBrachypterusEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
        event.register(
                ErsEntities.LATIMERIA_PERCOIDES.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkCustomWaterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.TACHYPLEUS_GLADIUS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                EntityAttributeHandler::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.TACHYCARIS_GUSTATUS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                EntityAttributeHandler::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkCustomWaterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.LATIMERIA_SUCHOMIMUS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkCustomWaterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.ARGENTUMNISCUS_ACICULAULAR.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.TERRIDENSAURUS_SAEVUS.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.MANDGEMARE_LABIUM.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.BENTHOSUCHUS_PLANIDENS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkCustomWaterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.ECHINOMORPHUS_CONVERGENS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                EntityAttributeHandler::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.PTEROCHIRUS_DUX.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.REMIPES_SICARIUS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.OCEAN_FLOOR,
                EntityAttributeHandler::checkSurfaceWaterAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(),
                SpawnPlacements.Type.IN_WATER,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EntityAttributeHandler::checkCustomWaterSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(
                ErsEntities.CRISTATODROMEUS_BRACHYPTERUS.get(),
                SpawnPlacements.Type.ON_GROUND,
                Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                Animal::checkAnimalSpawnRules,
                SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    public static boolean checkCustomWaterSpawnRules(
            EntityType<? extends LivingEntity> type,
            LevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {
        if (rollSpawn(3, random, spawnType)) {
            return checkSurfaceWaterAnimalSpawnRules(type, level, spawnType, pos, random);
        } else {
            return false;
        }
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(
            EntityType<? extends LivingEntity> type,
            LevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {
        int $$5 = level.getSeaLevel();
        int $$6 = $$5 - 19;
        return pos.getY() >= $$6
                && pos.getY() <= $$5
                && level.getFluidState(pos.below()).is(FluidTags.WATER)
                && level.getBlockState(pos.above()).is(Blocks.WATER);
    }

    public static boolean rollSpawn(int rolls, RandomSource random, MobSpawnType reason) {
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        } else {
            return rolls <= 0 || random.nextInt(rolls) == 0;
        }
    }

    public static boolean checkWarmOceanSpawnRules(
            EntityType<? extends Mob> type,
            LevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {
        if (!level.getBiome(pos).is(Biomes.WARM_OCEAN)
                && !level.getBiome(pos).is(Biomes.LUKEWARM_OCEAN)
                && !level.getBiome(pos).is(Biomes.DEEP_LUKEWARM_OCEAN)
                && !level.getBiome(pos).is(Biomes.RIVER)) {
            return false;
        }
        return checkSurfaceWaterAnimalSpawnRules(type, level, spawnType, pos, random);
    }
}
