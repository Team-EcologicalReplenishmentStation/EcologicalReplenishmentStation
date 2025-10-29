package cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.navigation;

import com.google.common.collect.ImmutableSet;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class AcanthodesNavigation extends AmphibiousPathNavigation {
    public AcanthodesNavigation(Mob pMob, Level pLevel) {
        super(pMob, pLevel);
    }
    // Distance to continue beyond the target (in blocks)
    private static final int OVERSHOOT_DISTANCE = 30;
    @Nullable
    @Override
    public Path createPath(Entity pEntity, int pAccuracy) {
        // Get the target's current position
        BlockPos targetPos = pEntity.blockPosition();

        // Calculate direction from mob to target

        Vec3 mobPos = this.mob.position();
        Vec3 direction = new Vec3(
                targetPos.getX() - mobPos.x,
                targetPos.getY() - mobPos.y,
                targetPos.getZ() - mobPos.z
        ).normalize();


        // Calculate overshoot position (extending beyond the target)
        BlockPos overshootPos = new BlockPos(
                (int)(targetPos.getX() + direction.x * OVERSHOOT_DISTANCE),
                (int)(targetPos.getY() + direction.y * OVERSHOOT_DISTANCE / 3),
                (int)(targetPos.getZ() + direction.z * OVERSHOOT_DISTANCE)
        );

        // Create path to the overshoot position
        return this.createPath(ImmutableSet.of(overshootPos), 16, true, pAccuracy);
    }

    public void alterCreatePath(Entity pEntity, int pAccuracy, int i) {
        BlockPos targetPos = pEntity.blockPosition();

        Vec3 mobPos = this.mob.position();
        Vec3 direction = new Vec3(
                targetPos.getX() - mobPos.x,
                targetPos.getY() - mobPos.y,
                targetPos.getZ() - mobPos.z
        ).normalize().multiply(i,i,i);

        BlockPos overshootPos = new BlockPos(
                (int)(targetPos.getX() + direction.x * OVERSHOOT_DISTANCE),
                (int)(targetPos.getY() + direction.y * OVERSHOOT_DISTANCE / 3),
                (int)(targetPos.getZ() + direction.z * OVERSHOOT_DISTANCE)
        );

        this.createPath(ImmutableSet.of(overshootPos), 16, true, pAccuracy);
    }
}
