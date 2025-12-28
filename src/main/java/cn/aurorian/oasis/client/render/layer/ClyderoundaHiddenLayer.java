package cn.aurorian.oasis.client.render.layer;

import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ClyderoundaHiddenLayer extends GeoRenderLayer<TubunasusClyderotundaEntity> {
    public ClyderoundaHiddenLayer(GeoRenderer<TubunasusClyderotundaEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(PoseStack poseStack, TubunasusClyderotundaEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        boolean gender = animatable.getGender();

        bakedModel.getBone("bone_male").ifPresent(geoBone -> geoBone.setHidden(!gender));
        bakedModel.getBone("bone_female").ifPresent(geoBone -> geoBone.setHidden(gender));
    }
}
