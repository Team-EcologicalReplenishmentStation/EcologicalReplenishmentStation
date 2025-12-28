package cn.aurorian.ers.client.render.entity.layer;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.mandgemarelabium.MandgemareLabiumEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class MandgemareLabiumMarkLayer extends GeoRenderLayer<MandgemareLabiumEntity> {
    public MandgemareLabiumMarkLayer(GeoRenderer<MandgemareLabiumEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void render(PoseStack poseStack, MandgemareLabiumEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getTextureResource(animatable));
        this.getRenderer().reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType, bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    public void preRender(PoseStack poseStack, MandgemareLabiumEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        boolean loser = animatable.getComplete() != 0;
        bakedModel.getBone("fin_silk").ifPresent(bone -> bone.setHidden(loser));
        bakedModel.getBone("fin_silk2").ifPresent(bone -> bone.setHidden(loser));
        bakedModel.getBone("Left_fin_silk").ifPresent(bone -> bone.setHidden(loser));
        bakedModel.getBone("Right_fin_silk").ifPresent(bone -> bone.setHidden(loser));
    }

    @Override
    protected ResourceLocation getTextureResource(MandgemareLabiumEntity animatable) {
        if(animatable.getGender()){
            String base = "textures/entity/mandgemare_labium/";

            switch (animatable.getFigure().getId()){
                case 0 -> base = base + "spot/";
                case 1 -> base = base + "stripe/";
            }

            switch (animatable.getTail().getId()) {
                case 0 -> base = base + "basic";
                case 1 -> base = base + "lion";
                case 2 -> base = base + "round";
            }
            switch (animatable.getMarking().getId()) {
                case 0 -> base = base + "_white.png";
                case 1 -> base = base + "_black.png";
                case 2 -> base = base + "_yellow.png";
                case 3 -> base = base + "_blue.png";
                case 4 -> base = base + "_red.png";
                case 5 -> base = base + "_cyan.png";
                case 6 -> base = base + "_green.png";
            }
            return EcologicalReplenishmentStation.prefix(base);
        }
        return super.getTextureResource(animatable);
    }
}
