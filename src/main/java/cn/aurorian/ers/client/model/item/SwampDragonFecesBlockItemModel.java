package cn.aurorian.ers.client.model.item;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.item.SwampDragonFecesBlockItem;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.model.GeoModel;

public class SwampDragonFecesBlockItemModel extends GeoModel<SwampDragonFecesBlockItem>{
    Block block;

    public SwampDragonFecesBlockItemModel(Block block) {
        this.block = block;
    }


    @Override
    public ResourceLocation getModelResource(SwampDragonFecesBlockItem animatable) {
        if(block.equals(ErsBlocks.SWAMP_DRAGON_BONE_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_bone_feces.geo.json");
        }else if(block.equals(ErsBlocks.SWAMP_DRAGON_SMALL_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_small_feces.geo.json");
        }else if(block.equals(ErsBlocks.SWAMP_DRAGON_LARGE_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_large_feces.geo.json");
        }else if(block.equals(ErsBlocks.SWAMP_DRAGON_GLASSES_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_glasses_feces.geo.json");
        }else {
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "geo/block/swamp_dragon_tel_feces.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(SwampDragonFecesBlockItem animatable) {
        if(block.equals(ErsBlocks.SWAMP_DRAGON_BONE_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_bone_feces.png");
        }else if(block.equals(ErsBlocks.SWAMP_DRAGON_SMALL_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_small_feces.png");
        }else if(block.equals(ErsBlocks.SWAMP_DRAGON_LARGE_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_large_feces.png");
        }else if(block.equals(ErsBlocks.SWAMP_DRAGON_GLASSES_FECES.get())){
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_glasses_feces.png");
        }else {
            return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "textures/block/swamp_dragon_tel_feces.png");
        }
    }

    @Override
    public ResourceLocation getAnimationResource(SwampDragonFecesBlockItem animatable) {
        return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "animations/block/swamp_dragon_feces.animation.json");
    }
    
}
