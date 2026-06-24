package cn.aurorian.oasis.client.render.layer;

import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
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

public class DurovelaHiddenLayer extends GeoRenderLayer<TubunasusDurovelaEntity> {

    public DurovelaHiddenLayer(GeoRenderer<TubunasusDurovelaEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            TubunasusDurovelaEntity entity,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        bakedModel.getBone("saddleA").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1.0f : 0.0f;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        bakedModel.getBone("saddleB").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1.0f : 0.0f;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        bakedModel.getBone("Saddle").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1.0f : 0.0f;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        if (!entity.isBaby()) {
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
    }

    @Override
    public void renderForBone(
            PoseStack poseStack,
            TubunasusDurovelaEntity animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        if (bone.getName().equals("Saddle")) {
            if (Minecraft.getInstance().screen == null
                    || !animatable.isOwnedBy(Minecraft.getInstance().player) && animatable.isVehicle()) {
                animatable.setAnimData(ErsDataTickets.SADDLE_POS, bone.getLocalPosition());
            }
        }
    }

    @Override
    public void render(
            PoseStack poseStack,
            TubunasusDurovelaEntity animatable,
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
    protected ResourceLocation getTextureResource(TubunasusDurovelaEntity animatable) {
        if (animatable.hasCustomName() && animatable.getCustomName().getString().equals("D3WOJDIWLANLAND")) {
            return Oasis.prefix("textures/entity/tubunasus_durovela/base_D3WOJDIWLANLAND.png");
        }
        return Oasis.prefix("textures/entity/tubunasus_durovela/layer.png");
    }
}
