package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class TerridensaurusSaevusAnimator extends GeneralAnimator<TerridensaurusSaevusEntity> {
    private float lastStableHeadCompensate = 0.0f;
    private float[] tailSmoothedYaw = new float[0];
    private static final int TAIL_DELAY_BASE = 2;
    private static final int TAIL_DELAY_STEP = 2;
    private static final float TAIL_SWING_FACTOR = 0.12f;
    private static final float TAIL_ROOT_WEIGHT = 1.55f;
    private static final float TAIL_TIP_WEIGHT = 1.55f;
    private static final float TAIL_ROOT_LIMIT = 8.0f;
    private static final float TAIL_TIP_LIMIT = 28.0f;
    private static final int TAIL_FRONT_BOOST_SEGMENTS = 2;
    private static final float TAIL_FRONT_BOOST_MAX = 1.2f;
    private static final float TAIL_BEND_RESPONSE = 0.35f;
    private static final float TAIL_RECENTER_RESPONSE = 0.08f;

    public TerridensaurusSaevusAnimator(TerridensaurusSaevusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<TerridensaurusSaevusEntity> model, AnimationState<TerridensaurusSaevusEntity> animationState) {
        animTail(model);
        if (!animationState.getAnimatable().getAnimator().isInScreen) {
            animHead(model, animationState);
        }
    }

    protected void animTail(GeoModel<TerridensaurusSaevusEntity> model) {
        String[] tailBoneNames;
        tailBoneNames = new String[] {"tail", "tail2", "tail3", "tail4", "tail5", "tail6"};
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

            // Sample older yaw deltas for later segments so the turn visibly travels toward the tip.
            float yawDelta = Mth.wrapDegrees(yawTrail.get(partialTicks, 0, delay));
            float swingWeight = Mth.lerp(segmentProgress, TAIL_ROOT_WEIGHT, TAIL_TIP_WEIGHT);
            if (i < TAIL_FRONT_BOOST_SEGMENTS) {
                float frontBoostProgress =
                        TAIL_FRONT_BOOST_SEGMENTS == 1 ? 0.0f : (float) i / (TAIL_FRONT_BOOST_SEGMENTS - 1);
                float frontBoost = Mth.lerp(frontBoostProgress, TAIL_FRONT_BOOST_MAX, 1.0f);
                swingWeight *= frontBoost;
            }
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
            GeoModel<TerridensaurusSaevusEntity> model, AnimationState<TerridensaurusSaevusEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames;
        boneNames = new String[] {"neck", "neck2", "neck3"};
        List<GeoBone> bones = getBonesByName(boneNames, model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());

        if (animationState.getAnimatable().getAttackState().getType().isTurning()) {
            netHeadYaw = Mth.approach(netHeadYaw, 0, Math.abs(netHeadYaw) * 0.2f);
            headPitch = Mth.approach(headPitch, 0, Math.abs(headPitch) * 0.2f);
        }

        float[] yawFactors = {0.25f, 0.35f, 0.35f};

        for (int i = 0; i < bones.size(); i++) {
            if (bones.get(i).getName().equals("neck")) {
                float targetCompensation = (float) Math.toRadians(getModelPitch(animationState.getPartialTick()));
                if (entity.getStableHead()) {
                    bones.get(i).setRotX(bones.get(i).getRotX() - targetCompensation);
                    lastStableHeadCompensate = targetCompensation;
                } else {
                    float currentCompensation = Mth.approach(lastStableHeadCompensate, 0, 0.01f);
                    bones.get(i).setRotX(bones.get(i).getRotX() - currentCompensation);
                    lastStableHeadCompensate = currentCompensation;
                }
            }

            if (animationState.getAnimatable().getAttackState().getType().isTurning()) {
                yawFactors[i] *= Math.max(
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

            bones.get(i).setRotY(bones.get(i).getRotY() + netHeadYaw * yawFactors[i]);
            bones.get(i).setRotZ(bones.get(i).getRotZ() + netHeadYaw * yawFactors[i] * -0.6f);
            bones.get(i).setRotX(bones.get(i).getRotX() + headPitch * yawFactors[i]);
        }

        if (entity.isVehicle() || entity.mightBeSleeping()) return;

        for (int i = 0; i < bones.size(); i++) {
            GeoBone head = bones.get(i);
            float angleLimit = 160;
            float yawOfs = -Mth.clamp(yawTrail.get(partialTicks, 0, i + 1), -angleLimit, angleLimit) * 2f;

            if (!entity.isMoving()) yawOfs *= 1.5f;

            head.setRotY((float) (head.getRotY() + Math.toRadians(yawOfs) * yawFactors[i]));
        }
    }
}
