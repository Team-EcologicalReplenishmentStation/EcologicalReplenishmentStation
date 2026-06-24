package cn.aurorian.oasis.entity.tubunasusdurovela.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class TubunasusMeleeAttackGoal extends MeleeAttackGoal {
    ErsTamableVehicle<?> entity;

    public TubunasusMeleeAttackGoal(
            ErsTamableVehicle<?> pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && entity.getControllingPassenger() == null;
    }

    @Override
    public void stop() {
        super.stop();
        entity.setSprinting(false);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && isTimeToAttack() && entity.getAttackState().isEmpty()) {
            this.resetAttackCooldown();
            entity.setSprinting(false);
            if (entity.isAlliedTo(pEnemy)) return;
            entity.startAttack(AttackType.TUBUNASUS_ATTACK);
            this.mob.doHurtTarget(pEnemy);
        }

        if (pDistToEnemySqr > d0) {
            entity.setSprinting(true);
        }
    }
}
