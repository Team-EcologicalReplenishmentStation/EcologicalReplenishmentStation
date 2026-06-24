package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.model.entity.ErsModel;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.packet.ErsTamableTrackFoodPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
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
    protected void applyRotations(
            T animatable, PoseStack poseStack, float ageInTicks, float rotationYaw, float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        GeneralAnimator<? extends T> animator = animatable.getAnimator();

        if (animatable instanceof ErsTamableVehicle<?> vehicle) {
            float scale = vehicle.getRenderSize();
            poseStack.scale(scale, scale, scale);
        }

        if (!animatable.getAnimator().isInScreen) {
            poseStack.mulPose(Axis.XP.rotationDegrees(animator.getModelPitch(partialTick, XRotDegree)));
        }
    }

    @Override
    public void renderFinal(
            PoseStack poseStack,
            T animatable,
            BakedGeoModel model,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha) {
        super.renderFinal(
                poseStack,
                animatable,
                model,
                bufferSource,
                buffer,
                partialTick,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha);
        if (animatable instanceof ErsTamable<?> tamable) {
            model.getBone("food").ifPresent(bone -> {
                if (!tamable.getAttackState().isEmpty()) {
                    ErsNetwork.INSTANCE.sendToServer(new ErsTamableTrackFoodPacket(
                            animatable.getId(),
                            new Vector3f((float) bone.getLocalPosition().x, (float) bone.getLocalPosition().y, (float)
                                    bone.getLocalPosition().z)));
                }
            });

            if (animatable instanceof ErsTamableVehicle<?> vehicle) {
                model.getBone("RealSaddle").ifPresent(bone -> {
                    if (Minecraft.getInstance().screen == null
                            || !vehicle.isOwnedBy(Minecraft.getInstance().player) && animatable.isVehicle()) {
                        vehicle.setAnimData(ErsDataTickets.SADDLE_POS, bone.getLocalPosition());
                    }
                });
            }
        }
    }
}
