package cn.aurorian.oasis.client.model;

import cn.aurorian.ers.molang.ErsMolang;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class TubunasusClyderotundaModel extends OasisModel<TubunasusClyderotundaEntity> {
    @Override
    public ResourceLocation getModelResource(TubunasusClyderotundaEntity entity) {
        if(entity.isBaby()){
            return Oasis.prefix("geo/entity/tubunasus_clyderotunda_baby.geo.json");
        } else
            return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getTextureResource(TubunasusClyderotundaEntity entity) {
        String base = "textures/entity/tubunasus_clyderotunda";

        if(entity.isBaby()){
            base = base + "_baby.png";
            return Oasis.prefix(base);
        }

        if(entity.getGender()){
            base = base + "_male.png";
        }else
            base = base + "_female.png";

        return Oasis.prefix(base);
    }

    @Override
    public ResourceLocation getAnimationResource(TubunasusClyderotundaEntity entity) {
        if(entity.isBaby())
            return Oasis.prefix("animations/entity/tubunasus_clyderotunda_baby.animation.json");
        else
            return super.getAnimationResource(entity);
    }

    @Override
    public void applyMolangQueries(TubunasusClyderotundaEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        if(animatable.getRotDirection().isNone()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
        }else if(animatable.getRotDirection().isRight()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 40);

        }else if(animatable.getRotDirection().isLeft()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> -40);
        }
    }
}
