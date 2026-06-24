package cn.aurorian.ers.entity.creatures.latimeriasuchomimus.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import cn.aurorian.ers.util.TickHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class SuchomimusMeleeAttackGoal extends MeleeAttackGoal {
    LatimeriaSuchomimusEntity mob;

    public SuchomimusMeleeAttackGoal(
            LatimeriaSuchomimusEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
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
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 60;
    }

    @Override
    public boolean canUse() {
        return super.canUse()
                && this.mob.getAttackState().getType() != AttackType.SUCHOMIMUS_HOLD
                && !this.mob.isVehicle();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 - 9 && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            if (this.mob.getAttackState().isEmpty()) {
                if (this.mob.isInWater()) {
                    this.mob.triggerAnim("attack", "attack");
                } else {
                    this.mob.triggerAnim("attack", "attack_land");
                }
            }

            TickHelper.tickLater(this.mob.level(), 15, () -> {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
                this.ticksUntilNextPathRecalculation = 60 + this.mob.getRandom().nextInt(10);

                if (pEnemy.getDimensions(Pose.STANDING).height
                                * pEnemy.getDimensions(Pose.STANDING).width
                                * pEnemy.getDimensions(Pose.STANDING).width
                        <= 0.649F) {
                    if (this.mob.getAttackState().isEmpty()
                            && this.mob.level().random.nextFloat() < 0.5F) {
                        this.mob.startAttack(AttackType.SUCHOMIMUS_HOLD);
                    }
                }
            });
        }
    }
}
