package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.AcanthodesChlamydoselachoidesEntity;
import net.minecraft.resources.ResourceLocation;

public class ChlamydoselachoidesModel extends ErsModel<AcanthodesChlamydoselachoidesEntity> {
    @Override
    public ResourceLocation getTextureResource(AcanthodesChlamydoselachoidesEntity entity) {
        if (entity.hasCustomName()) {
            String base = "textures/entity/acanthodes_chlamydoselachoides";

            String suffix =
                    switch (entity.getCustomName().getString()) {
                        case "Flanker" -> "_flanker";
                        default -> null;
                    };
            if (suffix != null) return EcologicalReplenishmentStation.prefix(base + suffix + ".png");
        }
        return super.getTextureResource(entity);
    }
}
