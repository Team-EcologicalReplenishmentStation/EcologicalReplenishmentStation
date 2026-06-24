package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.util.TickHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class DentisaurusLongirostrisMeleeAttackGoal extends MeleeAttackGoal {
    DentisaurusLongirostrisEntity entity;

    public DentisaurusLongirostrisMeleeAttackGoal(
            DentisaurusLongirostrisEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
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
        this.entity.setSprinting(true);
        this.entity
                .getAnimatableInstanceCache()
                .getManagerForId(this.entity.getId())
                .stopTriggeredAnimation("catch_fish-small");
        this.entity
                .getAnimatableInstanceCache()
                .getManagerForId(this.entity.getId())
                .stopTriggeredAnimation("catch_fish-middle");
        if (this.entity.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL
                || this.entity.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE) {
            this.entity.startAttack(AttackType.EMPTY);
        }
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

            boolean enemyIsRiding = pEnemy.getVehicle() == entity;

            if (!entity.isMature()) {
                entity.startAttack(AttackType.SWAMP_DRAGON_ATTACK);
            } else if (!enemyIsRiding && entity.getRandom().nextFloat() < 0.8F) {
                entity.startAttack(AttackType.SWAMP_DRAGON_ATTACK);
            } else {
                entity.getNavigation().stop();
                if (enemyIsRiding || entity.getRandom().nextBoolean() && !entity.isInWater()) {
                    entity.startAttack(AttackType.SWAMP_DRAGON_JUMP);
                    entity.setDeltaMovement(entity.getDeltaMovement().add(0, 0.4, 0));
                    TickHelper.tickLater(
                            entity.level(), 40, () -> entity.startAttack(AttackType.SWAMP_DRAGON_JUMP_ATTACK));
                } else if (entity.isInWater() && !entity.onGround()) {
                    entity.startAttack(AttackType.SWAMP_DRAGON_SWIM_SPECIAL_ATTACK);
                } else {
                    entity.startAttack(AttackType.SWAMP_DRAGON_SPECIAL_ATTACK);
                }
            }

            this.entity.swing(InteractionHand.MAIN_HAND);
            this.entity.doHurtTarget(pEnemy);

            this.entity.setBloody(true);
        }

        if (pDistToEnemySqr > d0 - 2) {
            entity.setSprinting(true);
        }
    }
}
