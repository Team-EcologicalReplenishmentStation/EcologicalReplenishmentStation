package cn.aurorian.oasis.entity.imperiovenatorregius.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.util.TickHelper;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class ImperiovenatorRegiusAttackGoal extends MeleeAttackGoal {
    ImperiovenatorRegiusEntity entity;

    public ImperiovenatorRegiusAttackGoal(
            ImperiovenatorRegiusEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse()
                && !entity.isBaby()
                && entity.getControllingPassenger() == null
                && entity.getCommand() != 1
                && entity.getSatisfiedTime() <= 0;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && entity.getSatisfiedTime() <= 0;
    }

    @Override
    public void stop() {
        if (entity.getTarget() != null && entity.getTarget().isDeadOrDying()) {
            entity.heal(50);
        }
        entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
        entity.setSprinting(false);

        super.stop();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);

        if (pDistToEnemySqr <= d0 && isTimeToAttack() && entity.getAttackState().isEmpty()) {
            entity.setSprinting(false);
            this.resetAttackCooldown();

            if (entity.isAlliedTo(pEnemy)) {
                return;
            }

            if (entity.getRandom().nextFloat() < 0.6f) entity.startAttack(AttackType.REGIUS_ATTACK_TURN);
            else if (entity.getRandom().nextFloat() > 0.7f) entity.startAttack(AttackType.REGIUS_JUMP_ATTACK);
            else entity.startAttack(AttackType.REGIUS_ATTACK);

            TickHelper.tickLater(this.mob.level(), 14, () -> {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
                this.entity.setBloody(true);
            });
        }

        if (pDistToEnemySqr > d0 - 1) {
            entity.setSprinting(true);
        }
    }
}
