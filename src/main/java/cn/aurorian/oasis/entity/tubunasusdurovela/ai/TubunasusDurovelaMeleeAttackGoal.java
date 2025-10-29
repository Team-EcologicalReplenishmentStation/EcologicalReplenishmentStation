package cn.aurorian.oasis.entity.tubunasusdurovela.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class TubunasusDurovelaMeleeAttackGoal extends MeleeAttackGoal {
    TubunasusDurovelaEntity tubunasusDurovelaEntity;
    public TubunasusDurovelaMeleeAttackGoal(TubunasusDurovelaEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        tubunasusDurovelaEntity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !tubunasusDurovelaEntity.isVehicle();
    }

    @Override
    public void stop() {
        super.stop();
        tubunasusDurovelaEntity.setSprinting(false);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && isTimeToAttack()) {
            this.resetAttackCooldown();
            tubunasusDurovelaEntity.startAttack(AttackType.TUBUNASUS_ATTACK);
            if(pEnemy == tubunasusDurovelaEntity.getOwner() || (pEnemy instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwner() == tubunasusDurovelaEntity.getOwner())) {
                return;
            }
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
        }

        if(pDistToEnemySqr > d0){
            tubunasusDurovelaEntity.setSprinting(true);
        }
    }
}
