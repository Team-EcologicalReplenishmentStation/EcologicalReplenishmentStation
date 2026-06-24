package cn.aurorian.oasis.client.model;

import cn.aurorian.ers.molang.ErsMolang;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class RegiusModel extends OasisModel<ImperiovenatorRegiusEntity> {
    @Override
    public ResourceLocation getAnimationResource(ImperiovenatorRegiusEntity entity) {
        if (entity.isBaby()) {
            return Oasis.prefix("animations/entity/imperiovenator_regius_baby.animation.json");
        }

        return super.getAnimationResource(entity);
    }

    @Override
    public ResourceLocation getModelResource(ImperiovenatorRegiusEntity entity) {
        if (entity.isBaby()) {
            return Oasis.prefix("geo/entity/imperiovenator_regius_baby.geo.json");
        }

        return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getTextureResource(ImperiovenatorRegiusEntity entity) {
        String base = "textures/entity/imperiovenator_regius";

        if (entity.isBaby()) {
            base = base + "_baby.png";
        } else {
            if (entity.hasCustomName()) {
                String suffix =
                        switch (entity.getCustomName().getString()) {
                            case "Joker" -> "_joker";
                            case "Mon3tr" -> "_mon3tr";
                            case "Brass_gear" -> "_brass_gear";
                            default -> null;
                        };
                if (suffix != null) return Oasis.prefix(base + suffix + ".png");
            }

            if (entity.getVariant().getId() == 1) {
                base = base + "_grey.png";
            } else {
                base = base + ".png";
            }
        }

        return Oasis.prefix(base);
    }

    @Override
    public void applyMolangQueries(ImperiovenatorRegiusEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        if (animatable.getRotDirection().isNone()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
        } else if (animatable.getRotDirection().isRight()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 70);

        } else if (animatable.getRotDirection().isLeft()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> -70);
        }
    }
}
