package cn.aurorian.oasis.client.model;

import cn.aurorian.ers.client.model.entity.ErsModel;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.oasis.Oasis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public class OasisModel<T extends Mob & GeoAnimatable & ErsEntity<T>> extends ErsModel<T> {
    @Override
    public ResourceLocation getModelResource(T entity) {
        return Oasis.prefix("geo/entity/"
                + BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath()
                + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return Oasis.prefix("textures/entity/"
                + BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath()
                + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return Oasis.prefix("animations/entity/"
                + BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).getPath()
                + ".animation.json");
    }
}
