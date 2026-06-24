package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import net.minecraft.resources.ResourceLocation;

public class SuchomimusModel extends ErsModel<LatimeriaSuchomimusEntity> {
    @Override
    public ResourceLocation getTextureResource(LatimeriaSuchomimusEntity entity) {
        if (entity.hasCustomName()) {
            String base = "textures/entity/latimeria_suchomimus";

            String suffix =
                    switch (entity.getCustomName().getString()) {
                        case "Fosforo" -> "_fosforo";
                        case "Lemonade" -> "_lemonade";
                        default -> null;
                    };
            if (suffix != null) return EcologicalReplenishmentStation.prefix(base + suffix + ".png");
        }
        return super.getTextureResource(entity);
    }
}
