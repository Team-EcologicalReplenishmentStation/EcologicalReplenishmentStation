package cn.aurorian.ers.client.render.entity.layer;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class CristatodromeusBrachypterusHiddenLayer extends GeoRenderLayer<CristatodromeusBrachypterusEntity> {
    public CristatodromeusBrachypterusHiddenLayer(GeoRenderer<CristatodromeusBrachypterusEntity> entityRenderer) {
        super(entityRenderer);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            CristatodromeusBrachypterusEntity entity,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        float scale = entity.isSaddled() ? 1.0F : 0.0F;
        bakedModel.getBone("Saddle").ifPresent(bone -> {
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });
        bakedModel.getBone("RealSaddle").ifPresent(bone -> {
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        boolean bloody = entity.isBloody();
        bakedModel.getBone("blood1").ifPresent(bone -> bone.setHidden(!bloody));
        bakedModel.getBone("blood2").ifPresent(bone -> bone.setHidden(!bloody));

        float percent = entity.getHealth() / entity.getMaxHealth();
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

    @Override
    public void render(
            PoseStack poseStack,
            CristatodromeusBrachypterusEntity animatable,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {

        RenderType saddleRenderType = RenderType.armorCutoutNoCull(getTextureResource(animatable));
        this.getRenderer()
                .reRender(
                        this.getDefaultBakedModel(animatable),
                        poseStack,
                        bufferSource,
                        animatable,
                        saddleRenderType,
                        bufferSource.getBuffer(saddleRenderType),
                        partialTick,
                        packedLight,
                        OverlayTexture.NO_OVERLAY,
                        1.0F,
                        1.0F,
                        1.0F,
                        1.0F);
    }

    @Override
    protected ResourceLocation getTextureResource(CristatodromeusBrachypterusEntity animatable) {
        return EcologicalReplenishmentStation.prefix("textures/entity/cristatodromeus_brachypterus/layer.png");
    }
}
