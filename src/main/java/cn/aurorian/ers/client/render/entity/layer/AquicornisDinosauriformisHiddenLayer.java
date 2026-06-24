package cn.aurorian.ers.client.render.entity.layer;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class AquicornisDinosauriformisHiddenLayer extends GeoRenderLayer<AquicornisDinosauriformisEntity> {

    public AquicornisDinosauriformisHiddenLayer(GeoRenderer<AquicornisDinosauriformisEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            AquicornisDinosauriformisEntity entity,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {

        if (entity.isMature()) {
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

        bakedModel.getBone("Saddle").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1 : 0;
            if (entity.getAgeInDays() > 400) scale = 0;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        bakedModel.getBone("Saddle2").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1 : 0;
            if (entity.getAgeInDays() > 400) scale = 0;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });
    }

    @Override
    public void render(
            PoseStack poseStack,
            AquicornisDinosauriformisEntity animatable,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        if (!animatable.isMature()) return;
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
    protected ResourceLocation getTextureResource(AquicornisDinosauriformisEntity animatable) {
        ;
        return EcologicalReplenishmentStation.prefix("textures/entity/aquicornis_dinosauriformis/layer.png");
    }
}
