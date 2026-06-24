package cn.aurorian.oasis.client.animator;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class TubunasusDurovelaAnimator extends GeneralAnimator<TubunasusDurovelaEntity> {
    public TubunasusDurovelaAnimator(TubunasusDurovelaEntity entity) {
        super(entity);
    }

    private float lastStableHeadCompensate = 0.0f;

    @Override
    public void animate(
            GeoModel<TubunasusDurovelaEntity> model, AnimationState<TubunasusDurovelaEntity> animationState) {
        animTail(model);
        if (!animationState.getAnimatable().getAnimator().isInScreen) animHead(model, animationState);
    }

    protected void animHead(
            GeoModel<TubunasusDurovelaEntity> model, AnimationState<TubunasusDurovelaEntity> animationState) {
        if (entity.isBaby()) return;

        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames = {"neck2", "neck3"};
        List<GeoBone> bones = getBonesByName(boneNames, model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());

        float[] amplifier = {0.5f, 0.6f};

        for (GeoBone bone : bones) {
            if (bone.getName().equals("neck2")) {
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
            bone.setRotY(netHeadYaw * amplifier[bones.indexOf(bone)]);
            bone.setRotX(headPitch * 0.5f);
        }
    }

    protected void animTail(GeoModel<TubunasusDurovelaEntity> model) {
        String[] tailBoneNames = {"tail", "tail2", "tail3", "tail4"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for (int i = 0; i < tailBones.size(); i++) {
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float) (Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 50 * logFactor;
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 1) * 0.5f, -angleLimit, angleLimit);

            tail.setRotY((float) (Math.toRadians(yawOfs)));
        }
    }
}
