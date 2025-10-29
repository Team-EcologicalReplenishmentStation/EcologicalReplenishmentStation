package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.model.entity.TerridensaurusSaevusModel;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TerridensaurusSaevusRender extends GeoEntityRenderer<TerridensaurusSaevusEntity> {
    public TerridensaurusSaevusRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TerridensaurusSaevusModel());
    }

    @Override
    protected void applyRotations(TerridensaurusSaevusEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw,
                                  float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        GeneralAnimator<TerridensaurusSaevusEntity> animator = animatable.getAnimator();
//        float scale = animatable.getRenderSize();
//
//        poseStack.scale(scale, scale, scale);
        poseStack.mulPose(Axis.XP.rotationDegrees(animator.getModelPitch(partialTick,7)));
    }
}
