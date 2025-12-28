package cn.aurorian.ers.client.model.block;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.block.be.FecesBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class FecesBlockModel extends ErsBlockModel<FecesBlockEntity>{
    BlockEntityType<FecesBlockEntity> blockEntityType;

    public FecesBlockModel(BlockEntityType<FecesBlockEntity> blockEntityType) {
        this.blockEntityType = blockEntityType;
    }

    @Override
    public ResourceLocation getModelResource(FecesBlockEntity animatable) {
        return EcologicalReplenishmentStation.prefix("geo/block/" + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType).getPath() + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FecesBlockEntity animatable) {
        return EcologicalReplenishmentStation.prefix("textures/block/" + BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(blockEntityType).getPath() +".png");
    }
    
}
