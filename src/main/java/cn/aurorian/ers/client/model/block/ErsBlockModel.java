package cn.aurorian.ers.client.model.block;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;

public class ErsBlockModel<T extends BlockEntity & GeoAnimatable> extends GeoModel<T> {
    @Override
    public ResourceLocation getModelResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(
                EcologicalReplenishmentStation.MODID,
                "geo/block/"
                        + BuiltInRegistries.BLOCK_ENTITY_TYPE
                                .getKey(entity.getType())
                                .getPath()
                        + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(
                EcologicalReplenishmentStation.MODID,
                "textures/block/"
                        + BuiltInRegistries.BLOCK_ENTITY_TYPE
                                .getKey(entity.getType())
                                .getPath()
                        + ".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(
                EcologicalReplenishmentStation.MODID, "animations/block/empty.animation.json");
    }
}
