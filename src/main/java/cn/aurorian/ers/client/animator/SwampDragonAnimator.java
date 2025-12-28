package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

import java.util.List;

public class SwampDragonAnimator extends GeneralAnimator<DentisaurusLongirostrisEntity>{
    private float lastStableHeadCompensate = 0.0f;

    public SwampDragonAnimator(DentisaurusLongirostrisEntity entity) {
        super(entity);
    }

    @Override
    public void animate(GeoModel<DentisaurusLongirostrisEntity> model, AnimationState<DentisaurusLongirostrisEntity> animationState) {
        animTail(model,animationState);
        if(!animationState.getAnimatable().getAnimator().isInScreen){
            animHead(model,animationState);
        }
    }

    protected void animTail(GeoModel<DentisaurusLongirostrisEntity> model, AnimationState<DentisaurusLongirostrisEntity> animationState){
        String[] tailBoneNames;
        if(entity.isMature()){
            tailBoneNames = new String[]{"Tail_A", "Tail_B", "Tail_C", "Tail_D", "Tail_E", "Tail_F", "Tail_G", "Tail_H", "Tail_I", "Tail_J", "Tail_K", "Tail_L", "Tail_M"};
        }else {
            tailBoneNames = new String[]{"Tail", "Tail_2", "Tail_3", "Tail_4", "Tail_5", "Tail_6", "Tail_7", "Tail_8", "Tail_9", "Tail_10", "Tail_11"};
        }
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for(int i = 0; i < tailBones.size(); i++){
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float)(Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));
            // 应用对数渐变到角度限制
            float angleLimit = 160 * logFactor;
            float pitchOfs = Mth.clamp(pitchTrail.get(partialTicks, 0, i + 1) * 0.13f, -angleLimit, angleLimit);
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 1) * 0.13f, -angleLimit, angleLimit);
            // 为前三段尾巴骨骼增加额外旋转
            if(i>=5 && i<=7 && !entity.isSprinting())
            {
                pitchOfs *= 3.5f;
                yawOfs *=  3.5f;
            }
            if (i < 3) {
                float extraRotationFactor = 5.5f - (i * 0.3f); // 第一段最大，逐渐减小
                pitchOfs *= extraRotationFactor;
                yawOfs *= extraRotationFactor;
                if(entity.isSprinting()){
                    yawOfs *= 0.8f;
                }
            }

            tail.setRotX((float) (tail.getRotX() + Math.toRadians(pitchOfs)));
            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }

    protected void animHead(GeoModel<DentisaurusLongirostrisEntity> model, AnimationState<DentisaurusLongirostrisEntity> animationState) {
        EntityModelData modelData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);
        String[] boneNames;
        if(entity.isMature())
            boneNames = new String[]{"Neck_A", "Neck_B", "Neck_C", "Neck_D", "Neck_E", "Head"};
        else {
            boneNames = new String[]{"Neck", "Neck_2", "Neck_3", "Neck_4", "Neck_5", "Head"};
        }
        List<GeoBone> bones = getBonesByName(boneNames,model);

        float netHeadYaw = (float) Math.toRadians(modelData.netHeadYaw());
        float headPitch = (float) Math.toRadians(modelData.headPitch());

        float[] yawFactors = {0.15f, 0.15f, 0.15f, 0.2f, 0.25f, 0.15f};

        for (int i = 0; i < bones.size(); i++) {
            if(bones.get(i).getName().equals("Neck_A") || bones.get(i).getName().equals("Neck")){
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
            bones.get(i).setRotY(bones.get(i).getRotY() + netHeadYaw * yawFactors[i]);
            bones.get(i).setRotX(bones.get(i).getRotX() + headPitch * yawFactors[i]);
        }

    }
}
