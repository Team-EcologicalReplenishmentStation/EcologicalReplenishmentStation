package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.plesiochelyslongicollis.PlesiochelysLongicollisEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PlesiochelysLongicollisAnimator extends GeneralAnimator<PlesiochelysLongicollisEntity> {
    private float[] tailSmoothedYaw = new float[0];
    private static final int TAIL_DELAY_BASE = 2;
    private static final int TAIL_DELAY_STEP = 2;
    private static final float TAIL_SWING_FACTOR = 0.16f;
    private static final float TAIL_ROOT_WEIGHT = 1.1f;
    private static final float TAIL_TIP_WEIGHT = 1.55f;
    private static final float TAIL_ROOT_LIMIT = 7.0f;
    private static final float TAIL_TIP_LIMIT = 24.0f;
    private static final float TAIL_BEND_RESPONSE = 0.35f;
    private static final float TAIL_RECENTER_RESPONSE = 0.08f;

    public PlesiochelysLongicollisAnimator(PlesiochelysLongicollisEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<PlesiochelysLongicollisEntity> model,
            AnimationState<PlesiochelysLongicollisEntity> animationState) {
        animTail(model);
        if (!animationState.getAnimatable().getAnimator().isInScreen) {
            animHead(model, animationState);
        }
    }

    protected void animHead(
            GeoModel<PlesiochelysLongicollisEntity> model,
            AnimationState<PlesiochelysLongicollisEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames = new String[] {"neck1", "neck2", "neck3", "head"};
        List<GeoBone> bones = getBonesByName(boneNames, model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());
        float[] yawFactors = {0.2f, 0.28f, 0.34f, 0.2f};
        float[] pitchFactors = {0.12f, 0.18f, 0.22f, 0.14f};

        for (int i = 0; i < bones.size(); i++) {
            GeoBone bone = bones.get(i);
            bone.setRotY(bone.getRotY() + netHeadYaw * yawFactors[i]);
            bone.setRotX(bone.getRotX() + headPitch * pitchFactors[i]);
        }

        if (entity.isBasking()) {
            return;
        }

        for (int i = 0; i < bones.size(); i++) {
            GeoBone bone = bones.get(i);
            float yawOfs = -Mth.clamp(yawTrail.get(partialTicks, 0, i + 1), -120f, 120f) * 1.2f;
            bone.setRotY((float) (bone.getRotY() + Math.toRadians(yawOfs) * yawFactors[i]));
        }
    }

    protected void animTail(GeoModel<PlesiochelysLongicollisEntity> model) {
        String[] tailBoneNames = new String[] {"tail", "tail2", "tail3", "tail4"};
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
}
