package cn.aurorian.oasis.entity.tubunasusclyderotunda.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class TubunasusClyderotundaMeleeAttackGoal extends MeleeAttackGoal {
    TubunasusClyderotundaEntity durovelaTubunasusEntity;
    public TubunasusClyderotundaMeleeAttackGoal(TubunasusClyderotundaEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        durovelaTubunasusEntity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !durovelaTubunasusEntity.isVehicle();
    }

    @Override
    public void tick() {
        super.tick();
        if(ErsUtils.isMoving(durovelaTubunasusEntity))
            durovelaTubunasusEntity.setSprinting(true);
    }

    @Override
    public void stop() {
        super.stop();
        durovelaTubunasusEntity.setSprinting(false);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && isTimeToAttack()) {
            this.resetAttackCooldown();
            durovelaTubunasusEntity.startAttack(AttackType.TUBUNASUS_ATTACK);
            if(pEnemy == durovelaTubunasusEntity.getOwner() || (pEnemy instanceof TamableAnimal tamableAnimal && tamableAnimal.getOwner() == durovelaTubunasusEntity.getOwner())) {
                return;
            }
            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
        }
    }
}
