package cn.aurorian.ers.item.egg;

import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.SpawnVariant;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import cn.aurorian.ers.init.ErsEntities;
import net.minecraft.world.level.Level;

public class AntiquusEgg extends ErsEgg {
    public AntiquusEgg(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public int getHatchTime() {
        return 24000;
    }

    @Override
    public ErsTamable<?> born(Level level) {
        EosuchosaurusAntiquusEntity entity1 =
                ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get().create(level);
        if (entity1 != null) {
            entity1.setVariant(
                    SpawnVariant.getCommonSpawnVariant(EosuchosaurusAntiquusEntity.Variant.values(), level.random));
            return entity1;
        }
        return null;
    }
}
