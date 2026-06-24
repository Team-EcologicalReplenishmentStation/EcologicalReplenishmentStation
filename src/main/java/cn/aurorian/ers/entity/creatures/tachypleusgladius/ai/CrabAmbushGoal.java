package cn.aurorian.ers.entity.creatures.tachypleusgladius.ai;

import cn.aurorian.ers.entity.creatures.tachypleusgladius.TachypleusGladiusEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

public class CrabAmbushGoal extends Goal {

    private final TachypleusGladiusEntity mob;

    int countdown = 0;

    public CrabAmbushGoal(TachypleusGladiusEntity tachypleusGladiusEntity) {
        this.mob = tachypleusGladiusEntity;
    }

    @Override
    public boolean canUse() {
        if (mob.getCooldown() > 0) return false;
        if (!mob.isInWater()) return false;
        if (mob.isWaiting()) return false;
        if (mob.getTarget() != null) return false;
        return canAmbush();
    }

    @Override
    public void start() {
        this.mob.getNavigation().stop();
        this.mob.setCooldown(200);
        this.mob.triggerAnim("extra", "ambush");
    }

    private boolean canAmbush() {
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 3; j++) {
                if (mob.level()
                                .getBlockState(mob.getOnPos().offset(i - 1, 0, j - 1))
                                .is(Blocks.WATER)
                        || mob.level()
                                .getBlockState(mob.getOnPos().offset(i - 1, 0, j - 1))
                                .is(Blocks.AIR)) {
                    return false;
                }
            }
        }
        return mob.getBlockStateOn().is(Blocks.SAND)
                || mob.getBlockStateOn().is(Blocks.GRAVEL)
                || mob.getBlockStateOn().is(Blocks.MUD);
    }

    @Override
    public boolean canContinueToUse() {
        return !mob.isWaiting() && mob.getLastHurtByMob() == null;
    }

    @Override
    public void tick() {
        countdown++;
        this.mob.getNavigation().stop();
        if (countdown > 30) {
            mob.setWaiting(true);
            countdown = 0;
        } else if (this.mob.level() instanceof ServerLevel serverLevel) {
            BlockParticleOption particleOptions =
                    new BlockParticleOption(ParticleTypes.BLOCK, this.mob.getBlockStateOn());
            serverLevel.sendParticles(
                    particleOptions, this.mob.getX(), this.mob.getY(), this.mob.getZ(), 40, 0.3, 0.3, 0.3, 0.5);
        }
    }
}
