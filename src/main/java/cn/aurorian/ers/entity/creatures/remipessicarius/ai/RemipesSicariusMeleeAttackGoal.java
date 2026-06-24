package cn.aurorian.ers.entity.creatures.remipessicarius.ai;

import cn.aurorian.ers.entity.creatures.remipessicarius.RemipesSicariusEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class RemipesSicariusMeleeAttackGoal extends MeleeAttackGoal {
    private final RemipesSicariusEntity mob;

    public RemipesSicariusMeleeAttackGoal(
            RemipesSicariusEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.mob = pMob;
    }

    @Override
    public void start() {
        super.start();
        this.mob.setSprinting(true);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setSprinting(false);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity enemy, double distanceToEnemySqr) {
        double reach = this.getAttackReachSqr(enemy);
        if (distanceToEnemySqr <= reach && this.isTimeToAttack() && !this.mob.isAlliedTo(enemy)) {
            this.resetAttackCooldown();
            this.mob.triggerAnim("attack", "attack");
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(enemy);
        }

        if (distanceToEnemySqr > reach - 2) {
            this.mob.setSprinting(true);
        }
    }
}
