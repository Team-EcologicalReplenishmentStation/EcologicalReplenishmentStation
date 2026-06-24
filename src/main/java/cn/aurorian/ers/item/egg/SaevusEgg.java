package cn.aurorian.ers.item.egg;

import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.SpawnVariant;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.init.ErsEntities;
import net.minecraft.world.level.Level;

public class SaevusEgg extends ErsEgg {
    public SaevusEgg(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getHatchTime() {
        return 48000;
    }

    @Override
    public ErsTamable<?> born(Level level) {
        TerridensaurusSaevusEntity entity1 =
                ErsEntities.TERRIDENSAURUS_SAEVUS.get().create(level);
        if (entity1 != null) {
            if (level.random.nextFloat() < 0.3f) {
                entity1.setCanBeElite(true);
            }
            if (level.random.nextFloat() < 0.1f) {
                entity1.setVariant(
                        SpawnVariant.getRareSpawnVariant(TerridensaurusSaevusEntity.Variant.values(), level.random));
            } else {
                entity1.setVariant(
                        SpawnVariant.getCommonSpawnVariant(TerridensaurusSaevusEntity.Variant.values(), level.random));
            }
            return entity1;
        }
        return null;
    }
}
