package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import cn.aurorian.ers.molang.ErsMolang;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class AntiquusModel extends ErsModel<EosuchosaurusAntiquusEntity> {
    @Override
    public ResourceLocation getTextureResource(EosuchosaurusAntiquusEntity entity) {
        String base =
                "textures/entity/eosuchosaurus_antiquus/" + entity.getVariant().getName();

        if (!entity.isMature()) {
            base = base + "_baby";
        }

        return EcologicalReplenishmentStation.prefix(base + ".png");
    }

    @Override
    public ResourceLocation getModelResource(EosuchosaurusAntiquusEntity entity) {
        if (!entity.isMature())
            return EcologicalReplenishmentStation.prefix("geo/entity/eosuchosaurus_antiquus_baby.geo.json");
        return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getAnimationResource(EosuchosaurusAntiquusEntity entity) {
        if (!entity.isMature())
            return EcologicalReplenishmentStation.prefix(
                    "animations/entity/eosuchosaurus_antiquus_baby.animation.json");
        return super.getAnimationResource(entity);
    }

    @Override
    public void applyMolangQueries(EosuchosaurusAntiquusEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        if (animatable.getRotDirection().isNone()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
        } else if (animatable.getRotDirection().isRight()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 60);

        } else if (animatable.getRotDirection().isLeft()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> -60);
        }
    }
}
