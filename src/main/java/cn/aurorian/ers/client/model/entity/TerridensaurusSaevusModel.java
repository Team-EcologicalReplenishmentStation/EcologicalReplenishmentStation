package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.molang.ErsMolang;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class TerridensaurusSaevusModel extends ErsModel<TerridensaurusSaevusEntity>{
    @Override
    public ResourceLocation getTextureResource(TerridensaurusSaevusEntity entity) {
        return super.getTextureResource(entity);
    }

    @Override
    public void applyMolangQueries(TerridensaurusSaevusEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        if(animatable.getRotDirection().isNone()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
        }else if(animatable.getRotDirection().isRight()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 30);

        }else if(animatable.getRotDirection().isLeft()){
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> -30);
        }
    }
}
