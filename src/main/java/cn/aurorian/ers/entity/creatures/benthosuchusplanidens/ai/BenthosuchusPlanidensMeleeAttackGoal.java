package cn.aurorian.ers.entity.creatures.benthosuchusplanidens.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.BenthosuchusPlanidensPlanidensEntity;
import cn.aurorian.ers.util.TickHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

public class BenthosuchusPlanidensMeleeAttackGoal extends MeleeAttackGoal {
    private final BenthosuchusPlanidensPlanidensEntity mob;

    public BenthosuchusPlanidensMeleeAttackGoal(
            BenthosuchusPlanidensPlanidensEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.mob = pMob;
    }

    @Override
    public void start() {
        super.start();
        this.mob.setSprinting(true);
        this.mob.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(7);
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.setSprinting(false);
        this.mob.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(5);
    }

    @Override
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 60;
    }

    @Override
    public boolean canUse() {
        return super.canUse()
                && this.mob.getAttackState().getType() != AttackType.PLANIDENS_HOLD
                && !this.mob.isVehicle();
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 - 9 && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            if (this.mob.getAttackState().isEmpty()) {
                this.mob.triggerAnim("attack", "attack");
                if (this.mob.level().random.nextFloat() < 0.5F) {
                    this.mob.triggerAnim("attack", "pickup");
                    TickHelper.tickLater(this.mob.level(), 20, () -> {
                        this.mob.swing(InteractionHand.MAIN_HAND);
                        this.mob.doHurtTarget(pEnemy);
                        this.ticksUntilNextPathRecalculation =
                                60 + this.mob.getRandom().nextInt(10);

                        if (pEnemy.getDimensions(Pose.STANDING).height
                                        * pEnemy.getDimensions(Pose.STANDING).width
                                        * pEnemy.getDimensions(Pose.STANDING).width
                                <= 0.649F) {
                            if (this.mob.getAttackState().isEmpty()) {
                                this.mob.startAttack(AttackType.PLANIDENS_HOLD);
                            }
                        }
                    });
                }
            }
        }
    }
}
