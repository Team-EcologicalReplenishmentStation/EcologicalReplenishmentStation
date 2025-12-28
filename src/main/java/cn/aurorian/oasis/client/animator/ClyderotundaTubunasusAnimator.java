package cn.aurorian.oasis.client.animator;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.List;

public class ClyderotundaTubunasusAnimator extends GeneralAnimator<TubunasusClyderotundaEntity> {
    public ClyderotundaTubunasusAnimator(TubunasusClyderotundaEntity entity) {
        super(entity);
    }
    @Override
    public void animate(GeoModel<TubunasusClyderotundaEntity> model, AnimationState<TubunasusClyderotundaEntity> animationState) {
        animTail(model);
        if(!animationState.getAnimatable().getAnimator().isInScreen)
            animHead(model,animationState);
    }

    protected void animHead(GeoModel<TubunasusClyderotundaEntity> model, AnimationState<TubunasusClyderotundaEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames = {"neck2", "neck3"};
        List<GeoBone> bones = getBonesByName(boneNames,model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());

        float[] amplifier = {0.4f, 0.4f};

        for (GeoBone bone : bones) {
            bone.setRotY(netHeadYaw * amplifier[bones.indexOf(bone)]);
        }
    }

    protected void animTail(GeoModel<TubunasusClyderotundaEntity> model){
        String[] tailBoneNames = {"tail","tail2","tail3","tail4"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for(int i = 0; i < tailBones.size(); i++){
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float)(Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 50 * logFactor;
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 1) * 0.5f, -angleLimit, angleLimit);

            tail.setRotY((float) (Math.toRadians(yawOfs)));
        }
    }
}
