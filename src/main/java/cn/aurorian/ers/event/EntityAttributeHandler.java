package cn.aurorian.ers.event;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.AcanthodesChlamydoselachoidesEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.latimeriapercoides.LatimeriaPercoidesEntity;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import cn.aurorian.ers.entity.creatures.tachypleusgladius.TachypleusGladiusEntity;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.init.ErsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.level.LevelAccessor;
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
        event.put(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), DentisaurusLongirostrisEntity.createAttributes().build());
        event.put(ErsEntities.LATIMERIA_PERCOIDES.get(), LatimeriaPercoidesEntity.createAttributes().build());
        event.put(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), LatimeriaSuchomimusEntity.createAttributes().build());
        event.put(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), AcanthodesChlamydoselachoidesEntity.createAttributes().build());
        event.put(ErsEntities.TACHYPLEUS_GLADIUS.get(), TachypleusGladiusEntity.createAttributes().build());
        event.put(ErsEntities.TERRIDENSAURUS_SAEVUS.get(), TerridensaurusSaevusEntity.createAttributes().build());
    }


    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event) {
//        event.register(ErsEntities.SWAMP_DRAGON.get(), SpawnPlacements.Type.NO_RESTRICTIONS, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, Animal::checkAnimalSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ErsEntities.LATIMERIA_PERCOIDES.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityAttributeHandler::checkCustomWaterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityAttributeHandler::checkCustomWaterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
        event.register(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), SpawnPlacements.Type.IN_WATER, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES, EntityAttributeHandler::checkCustomWaterSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);
    }

    public static boolean checkCustomWaterSpawnRules(
            EntityType<? extends LivingEntity> type,
            LevelAccessor level,
            MobSpawnType spawnType,
            BlockPos pos,
            RandomSource random) {
        if(rollSpawn(3, random, spawnType)) {
            return checkSurfaceWaterAnimalSpawnRules(level, pos);
        }else {
            return false;
        }
    }

    public static boolean checkSurfaceWaterAnimalSpawnRules(LevelAccessor pLevel, BlockPos pPos) {
        int $$5 = pLevel.getSeaLevel();
        int $$6 = $$5 - 13;
        return pPos.getY() >= $$6 && pPos.getY() <= $$5 && pLevel.getFluidState(pPos.below()).is(FluidTags.WATER) && pLevel.getBlockState(pPos.above()).is(Blocks.WATER);
    }

    public static boolean rollSpawn(int rolls, RandomSource random, MobSpawnType reason) {
        if (reason == MobSpawnType.SPAWNER) {
            return true;
        } else {
            return rolls <= 0 || random.nextInt(rolls) == 0;
        }
    }
}
