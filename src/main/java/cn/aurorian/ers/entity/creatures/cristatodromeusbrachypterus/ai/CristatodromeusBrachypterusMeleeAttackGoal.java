package cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class CristatodromeusBrachypterusMeleeAttackGoal extends MeleeAttackGoal {
    private final CristatodromeusBrachypterusEntity entity;

    public CristatodromeusBrachypterusMeleeAttackGoal(
            CristatodromeusBrachypterusEntity entity, double speedModifier, boolean followingTargetEvenIfNotSeen) {
        super(entity, speedModifier, followingTargetEvenIfNotSeen);
        this.entity = entity;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && entity.getControllingPassenger() == null && entity.getCommand() != 1;
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && entity.getControllingPassenger() == null && entity.getCommand() != 1;
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
    protected void checkAndPerformAttack(@NotNull LivingEntity enemy, double distanceToEnemySqr) {
        double reach = this.getAttackReachSqr(enemy);
        if (distanceToEnemySqr <= reach
                && isTimeToAttack()
                && entity.getAttackState().isEmpty()) {
            this.resetAttackCooldown();
            if (!entity.isAlliedTo(enemy)) {
                entity.startAttack(AttackType.CRISTATODROMEUS_KICK);
            }
        }

        if (distanceToEnemySqr > reach - 2) {
            entity.setSprinting(true);
        }
    }
}
