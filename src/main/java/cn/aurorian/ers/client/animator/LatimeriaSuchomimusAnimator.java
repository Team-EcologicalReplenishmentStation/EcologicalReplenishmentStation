package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class LatimeriaSuchomimusAnimator extends GeneralAnimator<LatimeriaSuchomimusEntity> {
    public LatimeriaSuchomimusAnimator(LatimeriaSuchomimusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<LatimeriaSuchomimusEntity> model, AnimationState<LatimeriaSuchomimusEntity> animationState) {
        animTail(model);
    }

    protected void animTail(GeoModel<LatimeriaSuchomimusEntity> model) {
        String[] tailBoneNames = {"head", "tail", "tail1", "tail2", "tail3", "tail4"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for (int i = 0; i < tailBones.size(); i++) {
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float) (Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 160 * logFactor;
            float pitchOfs = Mth.clamp(pitchTrail.get(partialTicks, 0, i + 5) * 0.1f, -angleLimit, angleLimit);
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 5) * 0.1f, -angleLimit, angleLimit);

            if (i < 3) {
                float extraRotationFactor = 6.5f - (i * 0.3f);
                pitchOfs *= extraRotationFactor;
                yawOfs *= extraRotationFactor;
            } else {
                pitchOfs *= 3f;
                yawOfs *= 3f;
            }

            if (i == 0) {
                pitchOfs = -pitchOfs;
                yawOfs = -yawOfs;
                pitchOfs = Mth.clamp(pitchOfs, -5f, 5f);
                yawOfs = Mth.clamp(yawOfs, -5f, 5f);
            }

            tail.setRotX((float) (tail.getRotX() + Math.toRadians(pitchOfs)));
            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }
}
