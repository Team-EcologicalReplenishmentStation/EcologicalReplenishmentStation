package cn.aurorian.ers.client.render.entity.layer;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
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

public class SaevusHiddenLayer extends GeoRenderLayer<TerridensaurusSaevusEntity> {
    public SaevusHiddenLayer(GeoRenderer<TerridensaurusSaevusEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(PoseStack poseStack, TerridensaurusSaevusEntity entity, BakedGeoModel bakedModel, RenderType renderType,
                          MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick,
                          int packedLight, int packedOverlay) {

        boolean bloody = entity.isBloody();
        bakedModel.getBone("blood1").ifPresent(bone -> bone.setHidden(!bloody));
        bakedModel.getBone("blood2").ifPresent(bone -> bone.setHidden(!bloody));

        if(entity.isMature()){
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

        // 设置鞍具缩放
        bakedModel.getBone("Saddle1").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1 : 0;
            if(entity.getAgeInDays() > 400) scale = 0;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });
        bakedModel.getBone("Saddle2").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1 : 0;
            if(entity.getAgeInDays() > 400) scale = 0;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });
        bakedModel.getBone("Saddle3").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1 : 0;
            if(entity.getAgeInDays() > 400) scale = 0;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        bakedModel.getBone("RealSaddle").ifPresent(bone -> {
            float scale = entity.isSaddled() ? 1 : 0;
            if(entity.getAgeInDays() > 400) scale = 0;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        boolean hasArmour = entity.isArmored();
        for (int i = 1; i <= 12; i++) {
            bakedModel.getBone("armor" + i).ifPresent(bone -> bone.setHidden(!hasArmour));
        }
    }

    @Override
    public void renderForBone(PoseStack poseStack, TerridensaurusSaevusEntity animatable, GeoBone bone, RenderType renderType,
                              MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight,
                              int packedOverlay) {
        super.renderForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight,
                packedOverlay);
        if (bone.getName().equals("RealSaddle")) {
            if (Minecraft.getInstance().screen == null || !animatable.isOwnedBy(Minecraft.getInstance().player) && animatable.isVehicle()) {
                animatable.setAnimData(ErsDataTickets.SADDLE_POS, bone.getLocalPosition());
            }
        }
    }

    @Override
    public void render(PoseStack poseStack, TerridensaurusSaevusEntity animatable, BakedGeoModel bakedModel, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        if(!animatable.isMature())
            return;
        RenderType armorRenderType = RenderType.armorCutoutNoCull(getTextureResource(animatable));
        this.getRenderer().reRender(this.getDefaultBakedModel(animatable), poseStack, bufferSource, animatable, armorRenderType, bufferSource.getBuffer(armorRenderType), partialTick, packedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
    }

    @Override
    protected ResourceLocation getTextureResource(TerridensaurusSaevusEntity animatable) {
        if(animatable.isElite())
            return EcologicalReplenishmentStation.prefix("textures/entity/terridensaurus_saevus/elite_layer.png");
        else
            return EcologicalReplenishmentStation.prefix("textures/entity/terridensaurus_saevus/base_layer.png");
    }
}
