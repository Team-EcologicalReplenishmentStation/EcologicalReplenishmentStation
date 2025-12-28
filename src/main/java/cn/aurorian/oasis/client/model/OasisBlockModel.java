package cn.aurorian.oasis.client.model;

import cn.aurorian.ers.client.model.block.ErsBlockModel;
import cn.aurorian.oasis.Oasis;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public class OasisBlockModel<T extends BlockEntity & GeoAnimatable> extends ErsBlockModel<T> {
    @Override
    public ResourceLocation getModelResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(Oasis.MODID,"geo/block/" + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType()).getPath() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(Oasis.MODID,"textures/block/" + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType()).getPath() +".png");
    }

    @Override
    public ResourceLocation getAnimationResource(T entity) {
        return ResourceLocation.fromNamespaceAndPath(Oasis.MODID, "animations/block/empty.animation.json");
    }
}
