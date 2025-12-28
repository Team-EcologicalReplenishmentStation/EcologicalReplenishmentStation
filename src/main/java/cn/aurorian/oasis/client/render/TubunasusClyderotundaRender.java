package cn.aurorian.oasis.client.render;

import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.ers.client.render.entity.ErsRenderer;
import cn.aurorian.oasis.client.model.TubunasusClyderotundaModel;
import cn.aurorian.oasis.client.render.layer.ClyderoundaHiddenLayer;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.cache.object.GeoBone;

public class TubunasusClyderotundaRender extends ErsRenderer<TubunasusClyderotundaEntity> {
    public TubunasusClyderotundaRender(EntityRendererProvider.Context context) {
        super(context, new TubunasusClyderotundaModel(), 7);
        addRenderLayer(new ClyderoundaHiddenLayer(this));
    }

    @Override
    protected void applyRotations(TubunasusClyderotundaEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        float scale = animatable.getScale();
        poseStack.scale(scale, scale, scale);

        //Extra pitch for baby
        if(animatable.isBaby()){
            poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getAnimator().getModelPitch(partialTick,7)));
        }
    }

    @Override
    public void applyRenderLayersForBone(PoseStack poseStack, TubunasusClyderotundaEntity animatable, GeoBone bone, RenderType renderType, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay) {
        super.applyRenderLayersForBone(poseStack, animatable, bone, renderType, bufferSource, buffer, partialTick, packedLight, packedOverlay);
        if (bone.getName().equals("RealSaddle")) {
            if (Minecraft.getInstance().screen == null || !animatable.isOwnedBy(Minecraft.getInstance().player) && animatable.isVehicle()) {
                animatable.setAnimData(ErsDataTickets.SADDLE_POS, bone.getLocalPosition());
            }
        }
    }
}
