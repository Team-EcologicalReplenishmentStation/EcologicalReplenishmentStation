package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.tachycarisgustatus.TachycarisGustatusEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class GustatusAnimator extends GeneralAnimator<TachycarisGustatusEntity> {

    public GustatusAnimator(TachycarisGustatusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<TachycarisGustatusEntity> model, AnimationState<TachycarisGustatusEntity> animationState) {
        animTail(model);
    }

    protected void animTail(GeoModel<TachycarisGustatusEntity> model) {
        String[] tailBoneNames = {"tail", "tail2", "tail3", "tail4", "tail5", "tail6"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for (int i = 0; i < tailBones.size(); i++) {
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float) (Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 50 * logFactor;
            float pitchOfs = Mth.clamp(pitchTrail.get(partialTicks, 0, i + 1) * 0.1f, -angleLimit, angleLimit);
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 1) * 0.1f, -angleLimit, angleLimit);

            float extraRotationFactor = 2.6f - (i * 0.3f);
            pitchOfs *= extraRotationFactor;
            yawOfs *= extraRotationFactor;

            tail.setRotX((float) (tail.getRotX() + Math.toRadians(pitchOfs)));
            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }
}
