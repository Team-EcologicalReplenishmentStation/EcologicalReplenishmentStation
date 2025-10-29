package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.molang.ErsMolang;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

import java.util.HashMap;
import java.util.Map;


public class SwampDragonModel extends ErsModel<DentisaurusLongirostrisEntity> {

    @Override
    public ResourceLocation getModelResource(DentisaurusLongirostrisEntity entity) {
        if(!entity.isMature()){
            return EcologicalReplenishmentStation.prefix("geo/entity/dentisaurus_longirostris_baby.geo.json");
        } else if(entity.isElite()){
            return EcologicalReplenishmentStation.prefix("geo/entity/dentisaurus_longirostris_elite.geo.json");
        }
        return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getTextureResource(DentisaurusLongirostrisEntity animatable) {
        String base = "textures/entity/dentisaurus_longirostris";

        if(animatable.isElite()){
            base = base + "_elite";
            if(animatable.hasCustomName()){
                if(animatable.getCustomName().getString().equals("Ladon"))
                    return EcologicalReplenishmentStation.prefix(base + "_ladon.png");
                if(animatable.getCustomName().getString().equals("Forsaken"))
                    return EcologicalReplenishmentStation.prefix(base + "_forsaken.png");
                if(animatable.getCustomName().getString().equals("Acheron_Pollux"))
                    return EcologicalReplenishmentStation.prefix(base + "_acheron_pollux.png");
            }

        }else if(!animatable.isMature()) {
            base = base + "_baby";
        }else{
            if(animatable.hasCustomName()){
                if(animatable.getCustomName().getString().equals("profound"))
                    return EcologicalReplenishmentStation.prefix(base + "_profound.png");
            }
        }

        switch (animatable.getVariant().getId()) {
            case 3,4,5 -> base = base + "_lackyellow";
            case 6,7,8 -> base = base + "_black";
            case 9,10,11 -> base = base + "_white";
            case 0,1,2 ->{}
        }

        if(animatable.isMature()){
            switch (animatable.getVariant().getId())  {
                case 0,3,6,9 -> base = base + ".png";
                case 1,4,7,10 -> base = base + "_light.png";
                case 2,5,8,11 -> base = base + "_deep.png";
            }
        }else {
            base = base + ".png";
        }

        return EcologicalReplenishmentStation.prefix(base);
    }

    @Override
    public ResourceLocation getAnimationResource(DentisaurusLongirostrisEntity entity) {
        if(!entity.isMature())
            return EcologicalReplenishmentStation.prefix("animations/entity/dentisaurus_longirostris_baby.animation.json");
        else
            return super.getAnimationResource(entity);
    }

    private static final Map<Integer, Integer> angleCache = new HashMap<>();

    @Override
    public void applyMolangQueries(DentisaurusLongirostrisEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        angleCache.putIfAbsent(animatable.getId(), 0);
        if(animatable.isVehicle())
            parser.setMemoizedValue(ErsMolang.MOVE_TURN, () -> angleCache.get(animatable.getId()));
        Integer angle = angleCache.get(animatable.getId());

        if(animatable.getRotDirection().isNone()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> 0);
            if(angle > 0)
                angle --;
            else if(angle < 0)
                angle ++;
        }else if(animatable.getRotDirection().isRight()){
            if(animatable.isMature())
                parser.setMemoizedValue(ErsMolang.V_TURN, () -> -40);
            else
                parser.setMemoizedValue(ErsMolang.V_TURN, () -> 40);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> -100);

            angle = Math.max(-30, --angle);

        }else if(animatable.getRotDirection().isLeft()){
            if(animatable.isMature())
                parser.setMemoizedValue(ErsMolang.V_TURN, () -> 40);
            else
                parser.setMemoizedValue(ErsMolang.V_TURN, () -> -40);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> 100);
            angle = Math.min(30, ++ angle);
        }

        angleCache.put(animatable.getId(), angle);
    }
}
