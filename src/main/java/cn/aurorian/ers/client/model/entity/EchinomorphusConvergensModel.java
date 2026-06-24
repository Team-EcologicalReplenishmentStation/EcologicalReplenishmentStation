package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.echinomorphusconvergens.EchinomorphusConvergensEntity;
import net.minecraft.resources.ResourceLocation;

public class EchinomorphusConvergensModel extends ErsModel<EchinomorphusConvergensEntity> {
    @Override
    public ResourceLocation getTextureResource(EchinomorphusConvergensEntity entity) {
        String variantName = entity.getVariant().getName();
        return EcologicalReplenishmentStation.prefix(
                "textures/entity/echinomorphus_convergens_" + variantName + ".png");
    }
}
