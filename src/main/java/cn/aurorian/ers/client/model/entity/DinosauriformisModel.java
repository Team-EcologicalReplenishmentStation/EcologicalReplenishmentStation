package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.molang.ErsMolang;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class DinosauriformisModel extends ErsModel<AquicornisDinosauriformisEntity> {

    @Override
    public ResourceLocation getModelResource(AquicornisDinosauriformisEntity entity) {
        if (!entity.isMature()) {
            return EcologicalReplenishmentStation.prefix("geo/entity/aquicornis_dinosauriformis_baby.geo.json");
        }
        return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getTextureResource(AquicornisDinosauriformisEntity animatable) {
        String base = "textures/entity/aquicornis_dinosauriformis/"
                + animatable.getVariant().getName();
        ;

        if (!animatable.isMature()) {
            base = base + "_baby";
        }

        return EcologicalReplenishmentStation.prefix(base + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(AquicornisDinosauriformisEntity entity) {
        if (!entity.isMature())
            return EcologicalReplenishmentStation.prefix(
                    "animations/entity/aquicornis_dinosauriformis_baby.animation.json");
        return super.getAnimationResource(entity);
    }

    @Override
    public void applyMolangQueries(AquicornisDinosauriformisEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;

        if (animatable.getRotDirection().isNone()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> 0);
        } else if (animatable.getRotDirection().isRight()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 50);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> 70);

        } else if (animatable.getRotDirection().isLeft()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> -50);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> -70);
        }
    }
}
