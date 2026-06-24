package cn.aurorian.ers.entity.ai;

import java.util.EnumSet;
import javax.annotation.Nullable;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.phys.Vec3;

public class ConstantSwimGoal extends Goal {
    private final PathfinderMob mob;
    private final double speedModifier;
    private final int interval;
    private Vec3 targetPosition;
    private int recalcTicks;
    private int stuckTicks;
    private Vec3 lastPosition = Vec3.ZERO;

    public ConstantSwimGoal(PathfinderMob mob, double speedModifier, int interval) {
        this.mob = mob;
        this.speedModifier = speedModifier;
        this.interval = interval;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        return this.mob.isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        return this.mob.isInWater();
    }

    @Override
    public void start() {
        this.recalcTicks = 0;
        this.stuckTicks = 0;
        this.lastPosition = this.mob.position();
        this.targetPosition = this.findSwimTarget();
        if (this.targetPosition != null) {
            this.mob
                    .getNavigation()
                    .moveTo(this.targetPosition.x, this.targetPosition.y, this.targetPosition.z, this.speedModifier);
        }
    }

    @Override
    public void stop() {
        this.targetPosition = null;
        this.mob.getNavigation().stop();
    }

    @Override
    public void tick() {
        if (!this.mob.isInWater()) return;

        if (this.targetPosition == null
                || this.recalcTicks-- <= 0
                || this.mob.distanceToSqr(this.targetPosition) < 2.0D) {
            this.targetPosition = this.findSwimTarget();
            this.recalcTicks = this.interval + this.mob.getRandom().nextInt(20);
            if (this.targetPosition != null) {
                this.mob
                        .getNavigation()
                        .moveTo(
                                this.targetPosition.x,
                                this.targetPosition.y,
                                this.targetPosition.z,
                                this.speedModifier);
            }
        }

        if (this.mob.distanceToSqr(this.lastPosition) < 0.01D) {
            this.stuckTicks++;
            if (this.stuckTicks > 40) {
                this.targetPosition = this.findSwimTarget();
                this.stuckTicks = 0;
                if (this.targetPosition != null) {
                    this.mob
                            .getNavigation()
                            .moveTo(
                                    this.targetPosition.x,
                                    this.targetPosition.y,
                                    this.targetPosition.z,
                                    this.speedModifier);
                }
            }
        } else {
            this.stuckTicks = 0;
        }
        this.lastPosition = this.mob.position();
    }

    @Nullable
    private Vec3 findSwimTarget() {
        Vec3 target = BehaviorUtils.getRandomSwimmablePos(this.mob, 25, 6);
        if (target == null) {
            target = BehaviorUtils.getRandomSwimmablePos(this.mob, 10, 2);
        }
        return target;
    }
}
