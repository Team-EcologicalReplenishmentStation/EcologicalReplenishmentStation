package cn.aurorian.ers.entity.ai.goal;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;

public class MobAvodingEntityGoal<T extends LivingEntity> extends AvoidEntityGoal<T> {
    public MobAvodingEntityGoal(
            PathfinderMob pMob,
            Class<T> pEntityClassToAvoid,
            float pMaxDistance,
            double walkSpeedModifier,
            double sprintSpeedModifier) {
        super(pMob, pEntityClassToAvoid, pMaxDistance, walkSpeedModifier, sprintSpeedModifier);
    }

    @Override
    public void tick() {
        super.tick();
        this.mob.setSprinting(this.mob.distanceToSqr(this.toAvoid) < 64.0);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setSprinting(false);
    }
}
