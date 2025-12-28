package cn.aurorian.oasis.entity.tubunasusdurovela.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class TubunasusMeleeAttackGoal extends MeleeAttackGoal {
    ErsTamableVehicle<?> tubunasusEntity;
    public TubunasusMeleeAttackGoal(ErsTamableVehicle<?> pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        tubunasusEntity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !tubunasusEntity.isVehicle();
    }

    @Override
    public void stop() {
        super.stop();
        tubunasusEntity.setSprinting(false);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && isTimeToAttack() && tubunasusEntity.getAttackState().isEmpty()) {
            this.resetAttackCooldown();
            tubunasusEntity.setSprinting(false);
            if(tubunasusEntity.isAlliedTo(pEnemy))
                return;
            tubunasusEntity.startAttack(AttackType.TUBUNASUS_ATTACK);
            this.mob.doHurtTarget(pEnemy);
        }

        if(pDistToEnemySqr > d0){
            tubunasusEntity.setSprinting(true);
        }
    }
}
