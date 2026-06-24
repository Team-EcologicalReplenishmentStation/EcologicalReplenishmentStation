package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.pterochirusdux.PterochirusDuxEntity;
import java.util.Optional;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class PterochirusDuxAnimator extends GeneralAnimator<PterochirusDuxEntity> {
    public PterochirusDuxAnimator(PterochirusDuxEntity entity) {
        super(entity);
    }

    @Override
    public void animate(GeoModel<PterochirusDuxEntity> model, AnimationState<PterochirusDuxEntity> animationState) {
        animBody(model, animationState);
    }

    protected void animBody(GeoModel<PterochirusDuxEntity> model, AnimationState<PterochirusDuxEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        Optional<GeoBone> bone = model.getBone("root");

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());

        if (animationState.getAnimatable().isFlying()) {
            bone.ifPresent(geoBone -> geoBone.setRotZ(geoBone.getRotZ() + netHeadYaw * 3));
        }
    }
}
