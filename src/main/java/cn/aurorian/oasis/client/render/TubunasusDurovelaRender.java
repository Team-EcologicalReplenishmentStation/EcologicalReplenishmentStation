package cn.aurorian.oasis.client.render;

import cn.aurorian.ers.client.render.entity.ErsRenderer;
import cn.aurorian.oasis.client.model.DurovelaModel;
import cn.aurorian.oasis.client.render.layer.DurovelaHiddenLayer;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TubunasusDurovelaRender extends ErsRenderer<TubunasusDurovelaEntity> {
    public TubunasusDurovelaRender(EntityRendererProvider.Context context) {
        super(context, new DurovelaModel(), 7);
        addRenderLayer(new DurovelaHiddenLayer(this));
    }

    @Override
    protected void applyRotations(
            TubunasusDurovelaEntity animatable,
            PoseStack poseStack,
            float ageInTicks,
            float rotationYaw,
            float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        float scale = animatable.getScale();
        poseStack.scale(scale, scale, scale);

        // Extra pitch for baby
        if (animatable.isBaby()) {
            poseStack.mulPose(Axis.XP.rotationDegrees(animatable.getAnimator().getModelPitch(partialTick, 7)));
        }
    }
}
