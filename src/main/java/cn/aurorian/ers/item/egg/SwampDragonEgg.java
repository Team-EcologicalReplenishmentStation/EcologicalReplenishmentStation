package cn.aurorian.ers.item.egg;

import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.SpawnVariant;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.init.ErsEntities;
import net.minecraft.world.level.Level;

public class SwampDragonEgg extends ErsEgg {
    public SwampDragonEgg(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getHatchTime() {
        return 48000;
    }

    @Override
    public ErsTamable<?> born(Level level) {
        DentisaurusLongirostrisEntity entity1 =
                ErsEntities.DENTISAURUS_LONGIROSTRIS.get().create(level);
        if (entity1 != null) {
            if (level.random.nextFloat() < 0.05f) {
                entity1.setCanBeElite(true);
            }
            if (level.random.nextFloat() < 0.1f) {
                entity1.setVariant(
                        SpawnVariant.getRareSpawnVariant(DentisaurusLongirostrisEntity.Variant.values(), level.random));
            } else {
                entity1.setVariant(SpawnVariant.getCommonSpawnVariant(
                        DentisaurusLongirostrisEntity.Variant.values(), level.random));
            }
            return entity1;
        }
        return null;
    }
}
