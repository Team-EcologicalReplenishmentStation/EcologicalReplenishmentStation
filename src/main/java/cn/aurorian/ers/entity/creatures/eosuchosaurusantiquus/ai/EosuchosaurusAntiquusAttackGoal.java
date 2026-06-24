package cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import cn.aurorian.ers.util.TickHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class EosuchosaurusAntiquusAttackGoal extends MeleeAttackGoal {
    EosuchosaurusAntiquusEntity entity;

    public EosuchosaurusAntiquusAttackGoal(
            EosuchosaurusAntiquusEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && entity.getControllingPassenger() == null && entity.getCommand() != 1;
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
            entity.setSprinting(false);
            this.resetAttackCooldown();

            if (entity.isAlliedTo(pEnemy)) {
                return;
            }

            entity.startAttack(AttackType.ANTIQUUS_ATTACK);

            TickHelper.tickLater(this.mob.level(), 15, () -> {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
                this.entity.setBloody(true);
            });
        }

        if (pDistToEnemySqr > d0 - 2) {
            entity.setSprinting(true);
        }
    }
}
