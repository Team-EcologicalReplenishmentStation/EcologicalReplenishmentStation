package cn.aurorian.ers.entity.ai.goal;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.Goal;

import java.util.EnumSet;

public class MobFollowLeaderGoal extends Goal {
    private final ErsTamableVehicle<?> mob;
    private final double speedModifier;
    private final double sprintModifier;
    private final float startDistanceSqr;
    private final float sprintDistanceSqr;
    private final float stopDistanceSqr;
    private boolean shouldSprint;
    private LivingEntity leader;
    private int timeToRecalcPath;

    public MobFollowLeaderGoal(ErsTamableVehicle<?> mob, double sprintModifier, float startDistance, float stopDistance) {
        this.mob = mob;
        this.speedModifier = 1.2;
        this.sprintModifier = sprintModifier;
        this.startDistanceSqr = startDistance * startDistance;
        this.sprintDistanceSqr = startDistanceSqr + 300;
        this.stopDistanceSqr = stopDistance * stopDistance;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        this.mob.updateLeader();
        ErsTamableVehicle<?> currentLeader = this.mob.leader;

        if(this.mob.isVehicle() || this.mob.isBaby())
            return false;
        if (currentLeader == null || currentLeader == this.mob) {
            return false;
        } else if (this.mob.getRandom().nextInt(reducedTickDelay(70)) != 0) {
            return false;
        }
        else if (mob.distanceToSqr(currentLeader) < startDistanceSqr) {
            return false;
        }else
            this.leader = currentLeader;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.getNavigation().isDone()) {
            return false;
        }
        double yDistanceSqr = Math.abs(mob.getY() - leader.getY());
        if(yDistanceSqr > stopDistanceSqr && mob.distanceToSqr(leader) - yDistanceSqr < stopDistanceSqr){
            return false;
        }
        return mob.distanceToSqr(leader) > stopDistanceSqr;
    }

    @Override
    public void start() {
        timeToRecalcPath = 0;
        shouldSprint = mob.distanceToSqr(leader) >= sprintDistanceSqr;
        mob.setSprinting(shouldSprint);
    }

    @Override
    public void stop() {
        mob.getNavigation().stop();
        mob.setSprinting(false);
    }

    @Override
    public void tick() {
        mob.getLookControl().setLookAt(leader, 10, mob.getMaxHeadXRot());
        --timeToRecalcPath;
        if (timeToRecalcPath > 0) {
            return;
        }
        timeToRecalcPath = adjustedTickDelay(100);
        if (mob.distanceToSqr(leader) >= sprintDistanceSqr) {
            shouldSprint = true;
            mob.setSprinting(true);
        }else {
            mob.setSprinting(false);
        }

        mob.getNavigation().moveTo(leader, shouldSprint ? sprintModifier : speedModifier);
    }

}
