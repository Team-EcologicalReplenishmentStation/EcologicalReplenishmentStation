package cn.aurorian.ers.client.animator;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;

public class GeneralAnimator<T extends Mob & GeoAnimatable> {
    protected final CircularBuffer yawTrail = new CircularBuffer(64); // 记录身体旋转的buffer
    protected final CircularBuffer pitchTrail = new CircularBuffer(16);
    protected final CircularBuffer yTrail = new CircularBuffer(8);
    public boolean isInScreen;

    protected T entity;
    protected float partialTicks;
    private boolean init;

    public GeneralAnimator(T entity) {
        this.entity = entity;
        init = false;
    }

    /*
     * entity.yBodyRot获得的是当前的旋转角度，在Minecraft中，以Z轴的方向为0度，顺时针为正方向，逆时针为负方向
     * 可能是由于tick存在的上限，yawDiff最大值为4，最小值为-4
     */

    public void tick() {
        if (!init) {
            init = true;
            yawTrail.fill(-entity.yBodyRot);
            yTrail.fill((float) entity.getY());
            pitchTrail.fill(getModelPitch(partialTicks));
        }

        if (entity.isPassenger()) return;

        yawTrail.update(-entity.yBodyRot);
        yTrail.update((float) entity.getY());
        pitchTrail.update(getModelPitch(partialTicks));
    }

    /*
     * geckolib的get和set函数操作的值是基于模型的，而不是基于Minecraft的坐标系
     * Geckolib会在每一帧结束之后重新把这些值修改成默认值，因此想要打印出值，必须要在操作之后再打印
     */
    public void animate(GeoModel<T> model, AnimationState<T> animationState) {}

    public float getModelPitch(float pt, float amp) {
        float pitchMovingMax = 90;
        return Mth.clamp(yTrail.get(pt, 5, 0) * amp, -pitchMovingMax, pitchMovingMax);
    }

    public float getModelPitch(float pt) {
        return getModelPitch(pt, 10);
    }

    public List<GeoBone> getBonesByName(String[] boneNames, GeoModel<T> model) {
        List<GeoBone> bones = new ArrayList<>();
        for (String boneName : boneNames) {
            Optional<GeoBone> bone = model.getBone(boneName);
            bone.ifPresent(bones::add);
        }
        return bones;
    }

    public void setPartialTick(float partialTicks) {
        this.partialTicks = partialTicks;
    }

    public float getPartialTick() {
        return partialTicks;
    }
}
