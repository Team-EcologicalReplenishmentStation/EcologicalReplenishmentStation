package cn.aurorian.ers.entity.creatures.remipessicarius;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class RemipesSicariusAnimator extends GeneralAnimator<RemipesSicariusEntity> {

    public RemipesSicariusAnimator(RemipesSicariusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(GeoModel<RemipesSicariusEntity> model, AnimationState<RemipesSicariusEntity> animationState) {
        animTail(model);
    }

    protected void animTail(GeoModel<RemipesSicariusEntity> model) {
        String[] tailBoneNames = {"tail", "tail2", "tail3", "tail4", "tail5", "tail6", "tail7"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);

        for (int i = 0; i < tailBones.size(); i++) {
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float) (Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 28 * logFactor;
            int lag = i + 1;
            float pitchOfs = Mth.clamp(pitchTrail.get(partialTicks, 0, lag) * 0.12f, -angleLimit, angleLimit);
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, lag) * 0.12f, -angleLimit, angleLimit);

            float extraFactor = 5.5f - (i * 0.35f);
            pitchOfs *= extraFactor;
            yawOfs *= extraFactor;

            if (i == 0) {
                pitchOfs = -pitchOfs;
                yawOfs = -yawOfs;
                pitchOfs = Mth.clamp(pitchOfs, -6f, 6f);
                yawOfs = Mth.clamp(yawOfs, -6f, 6f);
            }

            tail.setRotX((float) (tail.getRotX() + Math.toRadians(pitchOfs)));
            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }
}
