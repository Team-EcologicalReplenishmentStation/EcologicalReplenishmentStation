package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.tachypleusgladius.TachypleusGladiusEntity;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.List;

public class KnifeHorseshoeCrabAnimator extends GeneralAnimator<TachypleusGladiusEntity>{

    public KnifeHorseshoeCrabAnimator(TachypleusGladiusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(GeoModel<TachypleusGladiusEntity> model, AnimationState<TachypleusGladiusEntity> animationState) {
        if(!animationState.getAnimatable().getAnimator().isInScreen){
            animHead(model,animationState);
        }
    }

    protected void animHead(GeoModel<TachypleusGladiusEntity> model, AnimationState<TachypleusGladiusEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames = {"Left_beard", "Right_beard", "left_up_beard", "right_up_beard"};
        List<GeoBone> bones = getBonesByName(boneNames,model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());

        float yawFactors = 0.7f;

        for (GeoBone bone : bones) {
            bone.setRotY(bone.getRotY() + netHeadYaw * yawFactors);
            bone.setRotX(bone.getRotX() + headPitch * yawFactors);
        }

    }
}
