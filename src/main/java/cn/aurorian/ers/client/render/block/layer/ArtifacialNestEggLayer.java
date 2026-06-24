package cn.aurorian.ers.client.render.block.layer;

import cn.aurorian.ers.block.be.ArtificialNestBlockEntity;
import cn.aurorian.ers.client.render.block.ArtifacialNestRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.registries.BuiltInRegistries;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ArtifacialNestEggLayer extends GeoRenderLayer<ArtificialNestBlockEntity> {
    public ArtifacialNestEggLayer(ArtifacialNestRender entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            ArtificialNestBlockEntity animatable,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        bakedModel.getBone("swamp_dragon_egg").ifPresent(bone -> bone.setHidden(true));
        bakedModel.getBone("saevus_egg").ifPresent(bone -> bone.setHidden(true));
        bakedModel.getBone("dinosauriformis_egg").ifPresent(bone -> bone.setHidden(true));
        bakedModel.getBone("antiquus_egg").ifPresent(bone -> bone.setHidden(true));
        bakedModel
                .getBone(BuiltInRegistries.ITEM
                        .getKey(animatable.getEgg().getItem())
                        .getPath())
                .ifPresent(bone -> bone.setHidden(false));
    }
}
