package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.magnidiscumyzonsarcopterus.MagnidiscumyzonSarcopterusEntity;
import java.util.List;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class MagnidiscumyzonSarcopterusAnimator extends GeneralAnimator<MagnidiscumyzonSarcopterusEntity> {
    public MagnidiscumyzonSarcopterusAnimator(MagnidiscumyzonSarcopterusEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<MagnidiscumyzonSarcopterusEntity> model,
            AnimationState<MagnidiscumyzonSarcopterusEntity> animationState) {
        animTail(model);
    }

    protected void animTail(GeoModel<MagnidiscumyzonSarcopterusEntity> model) {
        String[] tailBoneNames = {"tail", "tail_2", "tail_3"};
        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        for (int i = 0; i < tailBones.size(); i++) {
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float) (Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 140 * logFactor;
            float pitchOfs = Mth.clamp(pitchTrail.get(partialTicks, 0, i + 1) * 0.13f, -angleLimit, angleLimit);
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 1) * 0.13f, -angleLimit, angleLimit);

            float extraRotationFactor = 5.5f - (i * 0.3f);
            pitchOfs *= extraRotationFactor;
            yawOfs *= extraRotationFactor;

            tail.setRotX((float) (tail.getRotX() + Math.toRadians(pitchOfs)));
            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }
}
