package cn.aurorian.ers.client.render.entity.layer;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
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

public class SwampDragonHiddenLayer extends GeoRenderLayer<DentisaurusLongirostrisEntity> {

    public SwampDragonHiddenLayer(GeoRenderer<DentisaurusLongirostrisEntity> entityRendererIn) {
        super(entityRendererIn);
    }

    @Override
    public void preRender(
            PoseStack poseStack,
            DentisaurusLongirostrisEntity sotek,
            BakedGeoModel bakedModel,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {

        boolean bloody = sotek.isBloody();
        bakedModel.getBone("blood1").ifPresent(bone -> bone.setHidden(!bloody));
        bakedModel.getBone("blood2").ifPresent(bone -> bone.setHidden(!bloody));

        if (sotek.isMature()) {
            float percent = sotek.getHealth() / sotek.getMaxHealth();
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
        bakedModel.getBone("Saddle").ifPresent(bone -> {
            float scale = sotek.isSaddled() ? 1 : 0;
            if (sotek.getAgeInDays() > 400) scale = 0;
            bone.setScaleX(scale);
            bone.setScaleY(scale);
            bone.setScaleZ(scale);
        });

        boolean hasArmour = sotek.isArmored();
        for (int i = 1; i <= 12; i++) {
            bakedModel.getBone("Armour" + i).ifPresent(bone -> bone.setHidden(!hasArmour));
        }
        bakedModel.getBone("helmet").ifPresent(bone -> bone.setHidden(!hasArmour));

        // 如果没有鞍具，隐藏所有食物
        if (!sotek.isSaddled() || sotek.getAgeInDays() > 400) {
            bakedModel.getBone("Food_A").ifPresent(bone -> bone.setHidden(true));
            bakedModel.getBone("Food_B").ifPresent(bone -> bone.setHidden(true));
            bakedModel.getBone("Food_C").ifPresent(bone -> bone.setHidden(true));
            bakedModel.getBone("Food_E").ifPresent(bone -> bone.setHidden(true));
            bakedModel.getBone("Food_D").ifPresent(bone -> bone.setHidden(true));
            bakedModel.getBone("Food_F").ifPresent(bone -> bone.setHidden(true));
            return;
        }

        int filledSlots = sotek.getFilledFish();

        // 根据填充数量显示对应的食物模型
        bakedModel.getBone("Food_A").ifPresent(bone -> bone.setHidden(filledSlots < 1));
        bakedModel.getBone("Food_B").ifPresent(bone -> bone.setHidden(filledSlots < 1));
        bakedModel.getBone("Food_C").ifPresent(bone -> bone.setHidden(filledSlots < 2));
        bakedModel.getBone("Food_E").ifPresent(bone -> bone.setHidden(filledSlots < 2));
        bakedModel.getBone("Food_D").ifPresent(bone -> bone.setHidden(filledSlots < 3));
        bakedModel.getBone("Food_F").ifPresent(bone -> bone.setHidden(filledSlots < 3));
    }

    @Override
    public void renderForBone(
            PoseStack poseStack,
            DentisaurusLongirostrisEntity animatable,
            GeoBone bone,
            RenderType renderType,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay) {
        super.renderForBone(
                poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
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
            DentisaurusLongirostrisEntity animatable,
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
    protected ResourceLocation getTextureResource(DentisaurusLongirostrisEntity animatable) {
        if (animatable.isElite())
            return EcologicalReplenishmentStation.prefix("textures/entity/dentisaurus_longirostris/elite_layer.png");
        else return EcologicalReplenishmentStation.prefix("textures/entity/dentisaurus_longirostris/base_layer.png");
    }
}
