package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CristatodromeusBrachypterusAnimator extends GeneralAnimator<CristatodromeusBrachypterusEntity> {
    private float lastStableHeadCompensate = 0.0f;
    private float[] tailSmoothedYaw = new float[0];
    private static final int TAIL_DELAY_BASE = 2;
    private static final int TAIL_DELAY_STEP = 2;
    private static final float TAIL_SWING_FACTOR = 0.12f;
    private static final float TAIL_ROOT_WEIGHT = 1.5f;
    private static final float TAIL_TIP_WEIGHT = 1.35f;
    private static final float TAIL_ROOT_LIMIT = 7.0f;
    private static final float TAIL_TIP_LIMIT = 25.0f;
    private static final float TAIL_BEND_RESPONSE = 0.35f;
    private static final float TAIL_RECENTER_RESPONSE = 0.08f;

    public CristatodromeusBrachypterusAnimator(CristatodromeusBrachypterusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<CristatodromeusBrachypterusEntity> model,
            AnimationState<CristatodromeusBrachypterusEntity> animationState) {
        animTail(model);
        if (!animationState.getAnimatable().getAnimator().isInScreen) {
            animHead(model, animationState);
        }
    }

    protected void animTail(GeoModel<CristatodromeusBrachypterusEntity> model) {
        String[] tailBoneNames = new String[] {"tail_1", "tail_2", "tail_3", "tail_4", "tail_5", "tail_6"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        if (tailBones.isEmpty()) {
            return;
        }

        int tailCount = tailBones.size();
        if (tailSmoothedYaw.length != tailCount) {
            tailSmoothedYaw = new float[tailCount];
        }

        for (int i = 0; i < tailCount; i++) {
            GeoBone tail = tailBones.get(i);
            float segmentProgress = tailCount == 1 ? 1.0f : (float) i / (tailCount - 1);
            int delay = TAIL_DELAY_BASE + i * TAIL_DELAY_STEP;

            float yawDelta = Mth.wrapDegrees(yawTrail.get(partialTicks, 0, delay));
            float swingWeight = Mth.lerp(segmentProgress, TAIL_ROOT_WEIGHT, TAIL_TIP_WEIGHT);
            float segmentLimit = Mth.lerp(segmentProgress, TAIL_ROOT_LIMIT, TAIL_TIP_LIMIT);
            float targetYawOfs = Mth.clamp(yawDelta * TAIL_SWING_FACTOR * swingWeight, -segmentLimit, segmentLimit);
            float currentYawOfs = tailSmoothedYaw[i];
            float response =
                    Math.abs(targetYawOfs) < Math.abs(currentYawOfs) ? TAIL_RECENTER_RESPONSE : TAIL_BEND_RESPONSE;
            currentYawOfs = Mth.lerp(response, currentYawOfs, targetYawOfs);
            tailSmoothedYaw[i] = currentYawOfs;

            tail.setRotY((float) (tail.getRotY() + Math.toRadians(currentYawOfs)));
        }
    }

    protected void animHead(
            GeoModel<CristatodromeusBrachypterusEntity> model,
            AnimationState<CristatodromeusBrachypterusEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames = new String[] {"chest", "neck", "neck4", "neck2", "neck3", "head"};
        List<GeoBone> bones = getBonesByName(boneNames, model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());

        if (animationState.getAnimatable().getAttackState().getType().isTurning()) {
            netHeadYaw = Mth.approach(netHeadYaw, 0, Math.abs(netHeadYaw) * 0.2f);
            headPitch = Mth.approach(headPitch, 0, Math.abs(headPitch) * 0.2f);
        }

        float[] yawFactors = {0.08f, 0.18f, 0.2f, 0.24f, 0.28f, 0.22f};
        float[] zFactors = {0.0f, -0.15f, -0.12f, -0.08f, -0.04f, 0.0f};

        for (int i = 0; i < bones.size(); i++) {
            GeoBone bone = bones.get(i);
            if (bone.getName().equals("neck")) {
                float targetCompensation = (float) Math.toRadians(getModelPitch(animationState.getPartialTick()));
                if (entity.getStableHead()) {
                    bone.setRotX(bone.getRotX() - targetCompensation);
                    lastStableHeadCompensate = targetCompensation;
                } else {
                    float currentCompensation = Mth.approach(lastStableHeadCompensate, 0, 0.01f);
                    bone.setRotX(bone.getRotX() - currentCompensation);
                    lastStableHeadCompensate = currentCompensation;
                }
            }

            float yawFactor = yawFactors[i];
            if (animationState.getAnimatable().getAttackState().getType().isTurning()) {
                yawFactor *= Math.max(
                        0,
                        1f
                                - (animationState
                                                        .getAnimatable()
                                                        .getAttackState()
                                                        .getType()
                                                        .getAnimationLength()
                                                - animationState.getAnimatable().getAttackState().animatorTick)
                                        * 0.025f);
            }

            bone.setRotY(bone.getRotY() + netHeadYaw * yawFactor);
            bone.setRotZ(bone.getRotZ() + netHeadYaw * yawFactor * zFactors[i]);
            bone.setRotX(bone.getRotX() + headPitch * yawFactor);
        }

        if (entity.isVehicle() || entity.mightBeSleeping()) {
            return;
        }

        for (int i = 0; i < bones.size(); i++) {
            GeoBone bone = bones.get(i);
            float angleLimit = 160;
            float yawOfs = -Mth.clamp(yawTrail.get(partialTicks, 0, i + 1), -angleLimit, angleLimit) * 2f;

            if (!entity.isMoving()) {
                yawOfs *= 1.5f;
            }

            bone.setRotY((float) (bone.getRotY() + Math.toRadians(yawOfs) * yawFactors[i]));
        }
    }
}
