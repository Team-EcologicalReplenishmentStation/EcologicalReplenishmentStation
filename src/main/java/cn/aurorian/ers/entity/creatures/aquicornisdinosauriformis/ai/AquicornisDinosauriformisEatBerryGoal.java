package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.ai;

import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SweetBerryBushBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.phys.Vec3;

public class AquicornisDinosauriformisEatBerryGoal extends Goal {
    private final AquicornisDinosauriformisEntity dinosaur;
    private final int searchRange;

    private BlockPos targetPos;
    private int eatProgress;
    private static final int EAT_DURATION = 30;

    public AquicornisDinosauriformisEatBerryGoal(AquicornisDinosauriformisEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.searchRange = 16;
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (dinosaur.getControllingPassenger() != null) {
            return false;
        }

        if (dinosaur.getCommand() == 1) {
            return false;
        }

        // Check if hunger is below 80
        if (dinosaur.getHunger() >= 80) {
            return false;
        }

        // Search for mature sweet berry bushes
        targetPos = findNearbyMatureBerryBush();
        return targetPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        // Stop if hunger reaches 80 or above
        if (dinosaur.getHunger() >= 80) {
            return false;
        }

        return targetPos != null
                && isMatureBerryBush(dinosaur.level().getBlockState(targetPos))
                && dinosaur.distanceToSqr(Vec3.atCenterOf(targetPos)) < searchRange * searchRange;
    }

    @Override
    public void start() {
        eatProgress = 0;
    }

    @Override
    public void tick() {
        if (targetPos == null) return;

        Level level = dinosaur.level();
        double distanceSqr = dinosaur.distanceToSqr(Vec3.atCenterOf(targetPos));

        if (distanceSqr > 4.0) {
            // Move towards target
            dinosaur.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
        } else {
            // At target, start eating
            dinosaur.getLookControl().setLookAt(targetPos.getX() + 0.5, targetPos.getY() + 0.5, targetPos.getZ() + 0.5);
            eatProgress++;

            if (eatProgress == 1) {
                dinosaur.triggerAnim("attack", "attack");
            }

            if (eatProgress >= EAT_DURATION) {
                // Complete eating
                performEat(level, targetPos);
                this.stop();
            }
        }
    }

    @Override
    public void stop() {
        targetPos = null;
        eatProgress = 0;
        dinosaur.getNavigation().stop();
    }

    private BlockPos findNearbyMatureBerryBush() {
        BlockPos centerPos = dinosaur.blockPosition();
        Level level = dinosaur.level();

        for (int x = centerPos.getX() - searchRange; x <= centerPos.getX() + searchRange; x++) {
            for (int z = centerPos.getZ() - searchRange; z <= centerPos.getZ() + searchRange; z++) {
                for (int y = centerPos.getY() - 2; y <= centerPos.getY() + 2; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (isMatureBerryBush(state)) {
                        return pos;
                    }
                }
            }
        }

        return null;
    }

    private boolean isMatureBerryBush(BlockState state) {
        if (!state.is(Blocks.SWEET_BERRY_BUSH)) {
            return false;
        }
        // Check if age is 3 (mature)
        int age = state.getValue(BlockStateProperties.AGE_3);
        return age >= 3;
    }

    private void performEat(Level level, BlockPos pos) {
        if (level.isClientSide) return;

        BlockState state = level.getBlockState(pos);
        if (!(state.getBlock() instanceof SweetBerryBushBlock)) return;

        dinosaur.triggerAnim("attack", "attack");
        // Reset bush age to 0 (harvested)
        level.setBlock(pos, state.setValue(BlockStateProperties.AGE_3, 0), 2);

        // Play sound
        level.playSound(null, pos, SoundEvents.SWEET_BERRY_BUSH_PICK_BERRIES, dinosaur.getSoundSource(), 1.0F, 1.0F);

        // Restore hunger (15 points) and heal (2 hearts = 4 health)
        dinosaur.feed(5);
        dinosaur.heal(5.0F);
    }
}
