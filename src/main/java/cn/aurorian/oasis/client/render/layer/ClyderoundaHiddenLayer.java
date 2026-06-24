package cn.aurorian.oasis.client.render.layer;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class ClyderoundaHiddenLayer extends GeoRenderLayer<TubunasusClyderotundaEntity> {
    public ClyderoundaHiddenLayer(GeoRenderer<TubunasusClyderotundaEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            TubunasusClyderotundaEntity animatable,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        boolean gender = animatable.getGender();

        bakedModel.getBone("bone_male").ifPresent(geoBone -> geoBone.setHidden(!gender));
        bakedModel.getBone("bone_female").ifPresent(geoBone -> geoBone.setHidden(gender));

        if (!animatable.isBaby()) {
            float percent = animatable.getHealth() / animatable.getMaxHealth();
            bakedModel.getBone("scar1").ifPresent(bone -> bone.setHidden(percent > 0.8));
            bakedModel.getBone("scar9").ifPresent(bone -> bone.setHidden(percent > 0.8));
            bakedModel.getBone("scar2").ifPresent(bone -> bone.setHidden(percent > 0.6));
            bakedModel.getBone("scar3").ifPresent(bone -> bone.setHidden(percent > 0.6));
            bakedModel.getBone("scar7").ifPresent(bone -> bone.setHidden(percent > 0.6));
            bakedModel.getBone("scar8").ifPresent(bone -> bone.setHidden(percent > 0.6));
            bakedModel.getBone("scar4").ifPresent(bone -> bone.setHidden(percent > 0.4));
            bakedModel.getBone("scar5").ifPresent(bone -> bone.setHidden(percent > 0.4));
            bakedModel.getBone("scar6").ifPresent(bone -> bone.setHidden(percent > 0.4));
        }
    }

    @Override
    public void render(
            PoseStack poseStack,
            TubunasusClyderotundaEntity animatable,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        if (animatable.isBaby()) return;
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getTextureResource(animatable));
        this.getRenderer()
                .reRender(
                        this.getDefaultBakedModel(animatable),
                        poseStack,
                        bufferSource,
                        animatable,
                        armorRenderType,
                        bufferSource.getBuffer(armorRenderType),
                        partialTick,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F);
    }

    @Override
    protected ResourceLocation getTextureResource(TubunasusClyderotundaEntity animatable) {
        return Oasis.prefix("textures/entity/tubunasus_clyderotunda_layer.png");
    }
}
