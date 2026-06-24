package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import net.minecraft.resources.ResourceLocation;

public class CristatodromeusBrachypterusModel extends ErsModel<CristatodromeusBrachypterusEntity> {
    @Override
    public ResourceLocation getTextureResource(CristatodromeusBrachypterusEntity entity) {
        return EcologicalReplenishmentStation.prefix("textures/entity/cristatodromeus_brachypterus/"
                + entity.getVariant().getName() + ".png");
    }
}
