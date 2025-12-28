package cn.aurorian.ers.client.model.item;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.item.FecesBlockItem;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class FecesBlockItemModel extends GeoModel<FecesBlockItem>{
    Block block;

    public FecesBlockItemModel(Block block) {
        this.block = block;
    }


    @Override
    public ResourceLocation getModelResource(FecesBlockItem animatable) {
        return EcologicalReplenishmentStation.prefix("geo/block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + "_block_entity.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FecesBlockItem animatable) {
        return EcologicalReplenishmentStation.prefix("textures/block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() +"_block_entity.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FecesBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "animations/block/empty.animation.json");
    }
    
}
