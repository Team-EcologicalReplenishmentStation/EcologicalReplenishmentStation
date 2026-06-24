package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class AquicornisDinosauriformisAnimator extends GeneralAnimator<AquicornisDinosauriformisEntity> {
    private float lastStableHeadCompensate = 0.0f;
    private static final int TAIL_DELAY_BASE = 2;
    private static final int TAIL_DELAY_STEP = 2;
    private static final float TAIL_SWING_FACTOR = 0.12f;
    private static final float TAIL_ROOT_WEIGHT = 1.65f;
    private static final float TAIL_TIP_WEIGHT = 1.25f;
    private static final float TAIL_ROOT_LIMIT = 8.0f;
    private static final float TAIL_TIP_LIMIT = 28.0f;
    private static final int TAIL_FRONT_BOOST_SEGMENTS = 3;
    private static final float TAIL_FRONT_BOOST_MAX = 1.45f;

    public AquicornisDinosauriformisAnimator(AquicornisDinosauriformisEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<AquicornisDinosauriformisEntity> model,
            AnimationState<AquicornisDinosauriformisEntity> animationState) {
        animTail(model, animationState);
        if (!animationState.getAnimatable().getAnimator().isInScreen) {
            animHead(model, animationState);
        }
    }

    protected void animTail(
            GeoModel<AquicornisDinosauriformisEntity> model,
            AnimationState<AquicornisDinosauriformisEntity> animationState) {
        String[] tailBoneNames;

        tailBoneNames = new String[] {"Tail_1", "Tail_2", "Tail_3", "Tail_4", "Tail_5"};

        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        if (tailBones.isEmpty()) {
            return;
        }

        int tailCount = tailBones.size();
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
            float yawOfs = Mth.clamp(yawDelta * TAIL_SWING_FACTOR * swingWeight, -segmentLimit, segmentLimit);

            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }

    protected void animHead(
            GeoModel<AquicornisDinosauriformisEntity> model,
            AnimationState<AquicornisDinosauriformisEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames;
        boneNames = new String[] {"Neck_1", "Neck_2", "Neck_3", "Head"};

        List<GeoBone> bones = getBonesByName(boneNames, model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());

        float[] yawFactors = {0.15f, 0.15f, 0.25f, 0.15f};

        for (int i = 0; i < bones.size(); i++) {
            if (bones.get(i).getName().equals("Neck_1")) {
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
            bones.get(i).setRotY(bones.get(i).getRotY() + netHeadYaw * yawFactors[i]);
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
