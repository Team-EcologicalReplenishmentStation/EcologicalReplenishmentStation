package cn.aurorian.oasis.client.model;

import cn.aurorian.ers.molang.ErsMolang;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class TubunasusDurovelaModel extends OasisModel<TubunasusDurovelaEntity> {
    @Override
    public ResourceLocation getModelResource(TubunasusDurovelaEntity entity) {
        if(entity.isBaby()){
            return Oasis.prefix("geo/entity/tubunasus_durovela_baby.geo.json");
        }
        if(entity.getDeadProgress() != 0){
            switch (entity.getDeadProgress()){
                case 1 ->  {
                    return Oasis.prefix("geo/entity/tubunasus_durovela_dead.geo.json");
                }
                case 2,3,4,5,6 -> {
                    return Oasis.prefix("geo/entity/tubunasus_durovela_body.geo.json");
                }
                default -> {
                    return Oasis.prefix("geo/entity/tubunasus_durovela_skeleton.geo.json");
                }
            }
        }else
            return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getTextureResource(TubunasusDurovelaEntity entity) {
        String base = "textures/entity/tubunasus_durovela";

        if(entity.getDeadProgress() != 0 && !entity.isBaby()){
            switch (entity.getDeadProgress()){
                case 1 -> base = base + "_dead.png";
                case 2,3,4,5,6 -> base = base + "_body.png";
                default -> base = base + "_skeleton.png";
            }
            return Oasis.prefix(base);
        }

        if(entity.isBaby()){
            base = base + "_baby";
        }

        switch (entity.getVariant().getId()) {
            case 1 -> base = base + "_blue.png";
            case 2 -> base = base + "_green.png";
            case 3 -> base = base + "_white.png";
            default -> base = base + ".png";
        }

        return Oasis.prefix(base);
    }

    @Override
    public ResourceLocation getAnimationResource(TubunasusDurovelaEntity entity) {
        if(entity.isBaby())
            return Oasis.prefix("animations/entity/tubunasus_durovela_baby.animation.json");
        else
            return super.getAnimationResource(entity);
    }

    @Override
    public void applyMolangQueries(TubunasusDurovelaEntity animatable, double animTime) {
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
