package cn.aurorian.ers.item.egg;

import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.SpawnVariant;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.init.ErsEntities;
import net.minecraft.world.level.Level;

public class DinosauriformisEgg extends ErsEgg {
    public DinosauriformisEgg(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getHatchTime() {
        return 24000;
    }

    @Override
    public ErsTamable<?> born(Level level) {
        AquicornisDinosauriformisEntity entity1 =
                ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get().create(level);
        if (entity1 != null) {
            entity1.setVariant(
                    SpawnVariant.getCommonSpawnVariant(AquicornisDinosauriformisEntity.Variant.values(), level.random));
            return entity1;
        }
        return null;
    }
}
