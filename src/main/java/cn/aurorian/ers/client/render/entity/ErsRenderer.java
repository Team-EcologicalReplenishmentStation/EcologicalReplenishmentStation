package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.model.entity.ErsModel;
import cn.aurorian.ers.entity.ErsEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class ErsRenderer<T extends Mob & ErsEntity<T> & GeoAnimatable> extends GeoEntityRenderer<T> {
    public ErsRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new ErsModel<>());
        this.XRotDegree = 25;
    }

    public ErsRenderer(EntityRendererProvider.Context renderManager, float XRotDegree) {
        super(renderManager, new ErsModel<>());
        this.XRotDegree = XRotDegree;
    }

    public ErsRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model) {
        super(renderManager, model);
        this.XRotDegree = 25;
    }

    public ErsRenderer(EntityRendererProvider.Context renderManager, GeoModel<T> model, float XRotDegree) {
        super(renderManager, model);
        this.XRotDegree = XRotDegree;
    }

    float XRotDegree;

    @Override
    protected void applyRotations(T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        GeneralAnimator<? extends T> animator = animatable.getAnimator();

        poseStack.mulPose(Axis.XP.rotationDegrees(animator.getModelPitch(partialTick, XRotDegree)));
    }
}
