package cn.aurorian.oasis.client.render.layer;

import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.GeoRenderLayer;

public class RegiusHiddenLayer extends GeoRenderLayer<ImperiovenatorRegiusEntity> {
    public RegiusHiddenLayer(GeoRenderer<ImperiovenatorRegiusEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            ImperiovenatorRegiusEntity animatable,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        boolean bloody = animatable.isBloody();
        bakedModel.getBone("moth1_blood").ifPresent(bone -> bone.setHidden(!bloody));
        bakedModel.getBone("moth3_blood").ifPresent(bone -> bone.setHidden(!bloody));
        bakedModel.getBone("face_blood").ifPresent(bone -> bone.setHidden(!bloody));

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
    public void renderForBone(
            PoseStack poseStack,
            ImperiovenatorRegiusEntity animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        if (bone.getName().equals("RealSaddle")) {
            if (Minecraft.getInstance().screen == null
                    || !animatable.isOwnedBy(Minecraft.getInstance().player) && animatable.isVehicle()) {
                animatable.setAnimData(ErsDataTickets.SADDLE_POS, bone.getLocalPosition());
            }
        }
    }

    @Override
    public void render(
            PoseStack poseStack,
            ImperiovenatorRegiusEntity animatable,
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
    protected ResourceLocation getTextureResource(ImperiovenatorRegiusEntity animatable) {
        return Oasis.prefix("textures/entity/imperiovenator_regius_layer.png");
    }
}
