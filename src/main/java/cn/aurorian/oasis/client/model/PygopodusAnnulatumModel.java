package cn.aurorian.oasis.client.model;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import net.minecraft.resources.ResourceLocation;

public class PygopodusAnnulatumModel extends OasisModel<PygopodusAnnulatumEntity> {
    @Override
    public ResourceLocation getModelResource(PygopodusAnnulatumEntity entity) {
        if (entity.isBaby()) return Oasis.prefix("geo/entity/pygopodus_annulatum_baby.geo.json");
        return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getTextureResource(PygopodusAnnulatumEntity entity) {
        if (entity.isBaby()) return Oasis.prefix("textures/entity/pygopodus_annulatum_baby.png");
        return super.getTextureResource(entity);
    }

    @Override
    public ResourceLocation getAnimationResource(PygopodusAnnulatumEntity entity) {
        if (entity.isBaby()) return Oasis.prefix("animations/entity/pygopodus_annulatum_baby.animation.json");
        return super.getAnimationResource(entity);
    }
}
