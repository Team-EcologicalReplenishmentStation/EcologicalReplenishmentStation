package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.List;

public class TerridensaurusSaevusAnimator extends GeneralAnimator<TerridensaurusSaevusEntity> {
    private float lastStableHeadCompensate = 0.0f;
    public TerridensaurusSaevusAnimator(TerridensaurusSaevusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(GeoModel<TerridensaurusSaevusEntity> model, AnimationState<TerridensaurusSaevusEntity> animationState) {
        animTail(model);
        if(!animationState.getAnimatable().getAnimator().isInScreen){
            animHead(model,animationState);
        }
    }

    protected void animTail(GeoModel<TerridensaurusSaevusEntity> model){
        String[] tailBoneNames;
        tailBoneNames = new String[]{"tail", "tail2", "tail3", "tail4", "tail5", "tail6"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for(int i = 0; i < tailBones.size(); i++){
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float)(Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));
            float angleLimit = 160 * logFactor;
            float pitchOfs = Mth.clamp(pitchTrail.get(partialTicks, 0, i + 1) * 0.13f, -angleLimit, angleLimit);
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 1) * 0.13f, -angleLimit, angleLimit);

            float extraRotationFactor = 3.0f - (i * 0.35f);

            if(i >= 4 && !entity.isSprinting())
            {
                pitchOfs *= 3f;
                yawOfs *=  3f;
            }
            pitchOfs *= extraRotationFactor;
            yawOfs *= extraRotationFactor;
            if(entity.isSprinting()){
                yawOfs *= 0.8f;
            }

            tail.setRotX((float) (tail.getRotX() + Math.toRadians(pitchOfs)));
            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }

    protected void animHead(GeoModel<TerridensaurusSaevusEntity> model, AnimationState<TerridensaurusSaevusEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames;
        boneNames = new String[]{"neck", "neck2", "neck3"};
        List<GeoBone> bones = getBonesByName(boneNames,model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());

        if(animationState.getAnimatable().getAttackState().getType().isTurning()){
            netHeadYaw = Mth.approach(netHeadYaw,0, Math.abs(netHeadYaw) * 0.2f);
            headPitch = Mth.approach(headPitch,0, Math.abs(headPitch) * 0.2f);
        }

        float[] yawFactors = {0.25f, 0.35f, 0.35f};

        for (int i = 0; i < bones.size(); i++) {
            if(bones.get(i).getName().equals("neck")){
                float targetCompensation = (float) Math.toRadians(getModelPitch(animationState.getPartialTick()));
                if(entity.getStableHead()){
                    bones.get(i).setRotX(bones.get(i).getRotX() - targetCompensation);
                    lastStableHeadCompensate = targetCompensation;
                }
                else
                {
                    float currentCompensation = Mth.approach(lastStableHeadCompensate,0, 0.01f);
                    bones.get(i).setRotX(bones.get(i).getRotX() - currentCompensation);
                    lastStableHeadCompensate = currentCompensation;
                }
            }

            if(animationState.getAnimatable().getAttackState().getType().isTurning()){
                yawFactors[i] *= Math.max(0, 1f - (animationState.getAnimatable().getAttackState().getType().getAnimationLength() - animationState.getAnimatable().getAttackState().animatorTick) * 0.025f);
            }

            bones.get(i).setRotY(bones.get(i).getRotY() + netHeadYaw * yawFactors[i]);
            bones.get(i).setRotZ(bones.get(i).getRotZ() + netHeadYaw * yawFactors[i] * -0.6f);
            bones.get(i).setRotX(bones.get(i).getRotX() + headPitch * yawFactors[i]);
        }

    }
}
