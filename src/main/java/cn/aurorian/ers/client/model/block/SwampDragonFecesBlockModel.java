package cn.aurorian.ers.client.model.block;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.block.be.SwampDragonFecesBlockEntity;
import cn.aurorian.ers.init.ErsBlockEntities;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntityType;

public class SwampDragonFecesBlockModel extends ErsBlockModel<SwampDragonFecesBlockEntity>{

    BlockEntityType<SwampDragonFecesBlockEntity> blockEntityType;

    public SwampDragonFecesBlockModel(BlockEntityType<SwampDragonFecesBlockEntity> blockEntityType) {
        this.blockEntityType = blockEntityType;
    }

    @Override
    public ResourceLocation getModelResource(SwampDragonFecesBlockEntity animatable) {
        if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_BONE_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_bone_feces.geo.json");
        }else if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_SMALL_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_small_feces.geo.json");
        }else if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_LARGE_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_large_feces.geo.json");
        }else if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_GLASSES_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_glasses_feces.geo.json");
        }else {
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_tel_feces.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(SwampDragonFecesBlockEntity animatable) {
        if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_BONE_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_bone_feces.png");
        }else if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_SMALL_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_small_feces.png");
        }else if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_LARGE_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_large_feces.png");
        }else if(blockEntityType.equals(ErsBlockEntities.SWAMP_DRAGON_GLASSES_FECES_BLOCK_ENTITY.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_glasses_feces.png");
        }else {
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_tel_feces.png");
        }
    }
    
}
