package cn.aurorian.ers.entity.ai.goal;

import cn.aurorian.ers.entity.ErsTamable;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.LeavesBlock;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.NodeEvaluator;

/**
 * Custom Implementation of {@link FollowOwnerGoal} which starts even if the pet is sitting or
 * sleeping and uses a different teleport check for swimming or flying dinos.
 */
public class MobFollowOwnerGoal extends Goal {
    private final ErsTamable<?> mob;
    private final double speedModifier;
    private final double sprintModifier;
    private final float startDistanceSqr;
    private final float sprintDistanceSqr;
    private final float stopDistanceSqr;
    private final float teleportDistanceSqr;
    private final boolean canFly;
    private boolean shouldSprint;
    private LivingEntity owner;
    private int timeToRecalcPath;

    public MobFollowOwnerGoal(
            ErsTamable<?> mob, double sprintModifier, float startDistance, float stopDistance, boolean canFly) {
        this(mob, sprintModifier, startDistance, stopDistance, 18, canFly);
    }

    public MobFollowOwnerGoal(
            ErsTamable<?> mob,
            double sprintModifier,
            float startDistance,
            float stopDistance,
            float teleportDistance,
            boolean canFly) {
        this.mob = mob;
        this.speedModifier = 1.2;
        this.sprintModifier = sprintModifier;
        this.startDistanceSqr = startDistance * startDistance;
        this.sprintDistanceSqr = startDistanceSqr + 120;
        this.stopDistanceSqr = stopDistance * stopDistance;
        this.teleportDistanceSqr = teleportDistance * teleportDistance;
        this.canFly = canFly;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        LivingEntity currentOwner = mob.getOwner();
        if (currentOwner == null || currentOwner.isSpectator()) {
            return false;
        } else if (mob.getCommand() != 2) {
            return false;
        } else if (mob.distanceToSqr(currentOwner) < startDistanceSqr) {
            return false;
        } else this.owner = currentOwner;
        return true;
    }

    @Override
    public boolean canContinueToUse() {
        if (mob.getNavigation().isDone()) {
            return false;
        }
        double yDistanceSqr = Math.abs(mob.getY() - owner.getY());
        if (yDistanceSqr > stopDistanceSqr && mob.distanceToSqr(owner) - yDistanceSqr < stopDistanceSqr) {
            return false;
        }
        return mob.distanceToSqr(owner) > stopDistanceSqr;
    }

    @Override
    public void start() {
        timeToRecalcPath = 0;
        shouldSprint = mob.distanceToSqr(owner) >= sprintDistanceSqr;
        mob.setSprinting(shouldSprint);
    }

    @Override
    public void stop() {
        owner = null;
        mob.getNavigation().stop();
        mob.setSprinting(false);
    }

    @Override
    public void tick() {
        mob.getLookControl().setLookAt(owner, 10, mob.getMaxHeadXRot());
        --timeToRecalcPath;
        if (mob.distanceToSqr(owner) >= sprintDistanceSqr) {
            shouldSprint = true;
            mob.setSprinting(true);
        } else {
            mob.setSprinting(false);
        }
        if (!mob.isLeashed() && mob.distanceToSqr(owner) >= teleportDistanceSqr) {
            teleportToOwner();
            return;
        }

        if (timeToRecalcPath <= 0) {
            timeToRecalcPath = 10;
            mob.getNavigation().moveTo(owner, mob.isSprinting() ? sprintModifier : speedModifier);
        }
    }

    private void teleportToOwner() {
        BlockPos ownerBlockPos = owner.blockPosition();
        for (int i = 0; i < 10; i++) {
            int x = ownerBlockPos.getX() + randomIntInclusive(-3, 3);
            int y = ownerBlockPos.getY() + randomIntInclusive(-1, 1);
            int z = ownerBlockPos.getZ() + randomIntInclusive(-3, 3);
            if (!maybeTeleportTo(x, y, z)) continue;
            return;
        }
    }

    private boolean maybeTeleportTo(int x, int y, int z) {
        if (Math.abs(x - owner.getX()) < 2 && Math.abs(z - owner.getZ()) < 2) {
            return false;
        }
        if (!canTeleportTo(new BlockPos(x, y, z))) {
            return false;
        }
        mob.moveTo(x + 0.5, y, z + 0.5, mob.getYRot(), mob.getXRot());
        mob.getNavigation().stop();
        return true;
    }

    private boolean canTeleportTo(BlockPos teleportPos) {
        Level level = mob.level();
        NodeEvaluator nodeEvaluator = mob.getNavigation().getNodeEvaluator();
        BlockPathTypes type =
                nodeEvaluator.getBlockPathType(level, teleportPos.getX(), teleportPos.getY(), teleportPos.getZ());
        //        if (type == BlockPathTypes.WATER && dino.getPathfindingMalus(type) == 0) {
        //            return level.noCollision(dino,
        // dino.getBoundingBox().move(teleportPos.subtract(dino.blockPosition())));
        //        }
        if (type != BlockPathTypes.WALKABLE) {
            return false;
        }
        if (!canFly && level.getBlockState(teleportPos).getBlock() instanceof LeavesBlock) {
            return false;
        }
        return level.noCollision(mob, mob.getBoundingBox().move(teleportPos.subtract(mob.blockPosition())));
    }

    private int randomIntInclusive(int min, int max) {
        return mob.getRandom().nextInt(max - min + 1) + min;
    }
}
