package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class AquicornisDinosauriformisMeleeAttackGoal extends MeleeAttackGoal {
    AquicornisDinosauriformisEntity entity;

    public AquicornisDinosauriformisMeleeAttackGoal(
            AquicornisDinosauriformisEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && entity.getControllingPassenger() == null;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && entity.getControllingPassenger() == null;
    }

    @Override
    public void start() {
        super.start();
        entity.setSprinting(true);
    }

    @Override
    public void stop() {
        super.stop();
        entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
        entity.setSprinting(false);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && isTimeToAttack() && entity.getAttackState().isEmpty()) {
            this.resetAttackCooldown();
            if (entity.isAlliedTo(pEnemy)) {
                return;
            }

            entity.startAttack(AttackType.DINOSAURIFORMIS_ATTACK);

            this.entity.swing(InteractionHand.MAIN_HAND);
            this.entity.doHurtTarget(pEnemy);
        }

        if (pDistToEnemySqr > d0 - 2) {
            entity.setSprinting(true);
        }
    }
}
