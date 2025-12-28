package cn.aurorian.oasis.client.model;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.block.SpecimenBlock;
import cn.aurorian.oasis.block.be.DurovelaSpecimenBlockEntity;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;

public class DurovelaSpecimenModel extends OasisBlockModel<DurovelaSpecimenBlockEntity>{
    @Override
    public ResourceLocation getModelResource(DurovelaSpecimenBlockEntity entity) {
        String path = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType()).getPath();
        boolean i = entity.getBlockState().getValue(SpecimenBlock.RANDOM);
        if(i){
            path += "1";
        }
        return ResourceLocation.fromNamespaceAndPath(Oasis.MODID,"geo/block/" + path + ".geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(DurovelaSpecimenBlockEntity entity) {
        String path = BuiltInRegistries.BLOCK_ENTITY_TYPE.getKey(entity.getType()).getPath();
        boolean i = entity.getBlockState().getValue(SpecimenBlock.RANDOM);
        if(i){
            path += "1";
        }
        return ResourceLocation.fromNamespaceAndPath(Oasis.MODID,"textures/block/" + path +".png");
    }
}
