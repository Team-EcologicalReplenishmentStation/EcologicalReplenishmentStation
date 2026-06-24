package cn.aurorian.ers.client.animator;

import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.BenthosuchusPlanidensPlanidensEntity;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.Mth;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class BenthosuchusPlanidensPlanidensAnimator extends GeneralAnimator<BenthosuchusPlanidensPlanidensEntity> {

    public BenthosuchusPlanidensPlanidensAnimator(BenthosuchusPlanidensPlanidensEntity entity) {
        super(entity);
    }

    @Override
    public void animate(
            GeoModel<BenthosuchusPlanidensPlanidensEntity> model,
            AnimationState<BenthosuchusPlanidensPlanidensEntity> animationState) {
        animHead(model);
        animTail(model);
    }

    protected void animHead(GeoModel<BenthosuchusPlanidensPlanidensEntity> model) {
        Optional<GeoBone> headOpt = model.getBone("neck");
        if (headOpt.isEmpty()) return;
        GeoBone head = headOpt.get();

        float pitchOfs = -Mth.clamp(pitchTrail.get(partialTicks, 0, 5) * 0.3f, -40, 40);
        float yawOfs = -Mth.clamp(yawTrail.get(partialTicks, 0, 5) * 0.3f, -40, 40);

        head.setRotX((float) (head.getRotX() + Math.toRadians(pitchOfs)));
        head.setRotY((float) (head.getRotY() + Math.toRadians(yawOfs)));
    }

    protected void animTail(GeoModel<BenthosuchusPlanidensPlanidensEntity> model) {
        String[] tailBoneNames = {"tail", "tail1", "tail2", "tail3", "tail4", "tail5"};
        String[] finBoneNames = {"dorsal_fin1", "dorsal_fin2"};

        List<GeoBone> tailBones = getBonesByName(tailBoneNames, model);
        List<GeoBone> finBones = getBonesByName(finBoneNames, model);

        for (int i = 0; i < tailBones.size(); i++) {
            GeoBone tail = tailBones.get(i);
            float reversedIndex = tailBones.size() - i;
            float logFactor = (float) (Math.log(reversedIndex + 1) / Math.log(tailBones.size() + 1));

            float angleLimit = 150 * logFactor;
            float pitchOfs = Mth.clamp(pitchTrail.get(partialTicks, 0, i + 5) * 0.1f, -angleLimit, angleLimit);
            float yawOfs = Mth.clamp(yawTrail.get(partialTicks, 0, i + 5) * 0.1f, -angleLimit, angleLimit);

            if (i < 2) {
                float extraRotationFactor = 5.5f - (i * 0.3f);
                pitchOfs *= extraRotationFactor;
                yawOfs *= extraRotationFactor;
            } else {
                pitchOfs *= 3f;
                yawOfs *= 3f;
            }

            if (i <= 2 && i > 0) {
                finBones.get(i - 1).setRotX((float) (finBones.get(i - 1).getRotX() + Math.toRadians(pitchOfs)));
                finBones.get(i - 1).setRotY((float) (finBones.get(i - 1).getRotY() + Math.toRadians(yawOfs)));
            }

            tail.setRotX((float) (tail.getRotX() + Math.toRadians(pitchOfs)));
            tail.setRotY((float) (tail.getRotY() + Math.toRadians(yawOfs)));
        }
    }
}
