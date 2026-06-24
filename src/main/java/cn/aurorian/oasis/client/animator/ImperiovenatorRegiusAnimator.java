package cn.aurorian.oasis.client.animator;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class ImperiovenatorRegiusAnimator extends GeneralAnimator<ImperiovenatorRegiusEntity> {
    public ImperiovenatorRegiusAnimator(ImperiovenatorRegiusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<ImperiovenatorRegiusEntity> model, AnimationState<ImperiovenatorRegiusEntity> animationState) {
        animTail(model);
        if (!animationState.getAnimatable().getAnimator().isInScreen) animHead(model, animationState);
    }

    protected void animHead(
            GeoModel<ImperiovenatorRegiusEntity> model, AnimationState<ImperiovenatorRegiusEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames = {"neck2", "neck3"};
        List<GeoBone> bones = getBonesByName(boneNames, model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());

        float[] amplifier = {0.5f, 0.6f};

        for (GeoBone bone : bones) {
            bone.setRotY(netHeadYaw * amplifier[bones.indexOf(bone)]);
        }

        if (entity.isVehicle()) return;

        for (int i = 0; i < bones.size(); i++) {
            GeoBone head = bones.get(i);
            float angleLimit = 160;
            float yawOfs = -Mth.clamp(yawTrail.get(partialTicks, 0, i + 1), -angleLimit, angleLimit) * 2f;

            if (!entity.isMoving()) yawOfs *= 1.5f;

            head.setRotY((float) (head.getRotY() + Math.toRadians(yawOfs) * 0.4f));
        }
    }

    protected void animTail(GeoModel<ImperiovenatorRegiusEntity> model) {
        String[] tailBoneNames = {"tail", "tail2", "tail3", "tail4"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for (int i = 0; i < tailBones.size(); i++) {
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float) (Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 80 * logFactor;
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 1) * 0.7f, -angleLimit, angleLimit);

            tail.setRotY((float) (Math.toRadians(yawOfs)));
        }
    }
}
