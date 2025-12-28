package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.molang.ErsMolang;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class TerridensaurusSaevusModel extends ErsModel<TerridensaurusSaevusEntity>{
    @Override
    public ResourceLocation getTextureResource(TerridensaurusSaevusEntity entity) {
        String base = "textures/entity/terridensaurus_saevus/";
        if(entity.isElite()){
            base = base + "elite";
            if(entity.hasCustomName()) {
                if (entity.getCustomName().getString().equals("Icarian"))
                    return EcologicalReplenishmentStation.prefix(base + "_icarian.png");
                if (entity.getCustomName().getString().equals("Halgus_DEVIL"))
                    return EcologicalReplenishmentStation.prefix(base + "_halgusdevil.png");
                if (entity.getCustomName().getString().equals("remake"))
                    return EcologicalReplenishmentStation.prefix(base + "_remake.png");
            }
        }else if (!entity.isMature()){
            base = base + "baby";
        }else {
            base = base + "base";
        }

        switch (entity.getVariant().getId()) {
            case 0 -> base = base + ".png";
            case 1 -> base = base + "_black.png";
            case 2 -> base = base + "_lackyellow.png";
            case 3 -> base = base + "_white.png";
        }
        return EcologicalReplenishmentStation.prefix(base);
    }

    @Override
    public ResourceLocation getModelResource(TerridensaurusSaevusEntity entity) {
        if(entity.isElite()){
            return EcologicalReplenishmentStation.prefix("geo/entity/terridensaurus_saevus_elite.geo.json");
        }
        if(!entity.isMature())
            return EcologicalReplenishmentStation.prefix("geo/entity/terridensaurus_saevus_baby.geo.json");
        return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getAnimationResource(TerridensaurusSaevusEntity entity) {
        if(!entity.isMature())
            return EcologicalReplenishmentStation.prefix("animations/entity/terridensaurus_saevus_baby.animation.json");
        return super.getAnimationResource(entity);
    }

    @Override
    public void applyMolangQueries(TerridensaurusSaevusEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        if(animatable.getRotDirection().isNone()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
        }else if(animatable.getRotDirection().isRight()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 60);

        }else if(animatable.getRotDirection().isLeft()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> -60);
        }
    }
}
