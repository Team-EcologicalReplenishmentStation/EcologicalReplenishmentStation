package cn.aurorian.oasis.client.animator;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.List;

public class PygopodusAnnulatumAnimator extends GeneralAnimator<PygopodusAnnulatumEntity> {
    public PygopodusAnnulatumAnimator(PygopodusAnnulatumEntity entity) {
        super(entity);
    }

    @Override
    public void animate(GeoModel<PygopodusAnnulatumEntity> model, AnimationState<PygopodusAnnulatumEntity> animationState) {
        animHead(model,animationState);
    }

    protected void animHead(GeoModel<PygopodusAnnulatumEntity> model, AnimationState<PygopodusAnnulatumEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames = {"neck1","neck2", "neck3","neck4","neck5"};
        List<GeoBone> bones = getBonesByName(boneNames,model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());

        float amplifier = 0.2f;

        for (GeoBone bone : bones) {
            bone.setRotY(netHeadYaw * amplifier);
        }
    }
}
