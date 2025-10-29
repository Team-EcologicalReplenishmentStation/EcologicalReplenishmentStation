package cn.aurorian.ers.client.render.block.layer;

import cn.aurorian.ers.block.be.SwampDragonArtificialNestBlockEntity;
import cn.aurorian.ers.client.render.block.SwampDragonNestRender;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class SwampDragonEggLayer extends GeoRenderLayer<SwampDragonArtificialNestBlockEntity> {
    public SwampDragonEggLayer(SwampDragonNestRender entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(PoseStack poseStack, SwampDragonArtificialNestBlockEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        bakedModel.getBone("egg").ifPresent(bone -> bone.setHidden(true));
        bakedModel.getBone("egg").ifPresent(bone -> bone.setHidden(!animatable.hasEgg()));
    }
}
