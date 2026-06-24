package cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.ai;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.AcanthodesChlamydoselachoidesEntity;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.navigation.AcanthodesNavigation;
import cn.aurorian.ers.util.TickHelper;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;

public class AcanthodesMeleeAttackGoal extends MeleeAttackGoal {
    AcanthodesChlamydoselachoidesEntity mob;

    public AcanthodesMeleeAttackGoal(
            AcanthodesChlamydoselachoidesEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        this.mob = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && mob.getTarget().isInWater();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && mob.getTarget().isInWater();
    }

    @Override
    public void start() {
        super.start();
        this.mob.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(3);
    }

    @Override
    public void stop() {
        LivingEntity livingentity = this.mob.getTarget();
        if (!EntitySelector.NO_CREATIVE_OR_SPECTATOR.test(livingentity)) {
            this.mob.setTarget(null);
        }

        this.mob.setAggressive(false);
        this.mob.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.7);
    }

    @Override
    protected void resetAttackCooldown() {
        this.ticksUntilNextAttack = 60;
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && this.getTicksUntilNextAttack() <= 0) {
            this.resetAttackCooldown();
            this.mob.triggerAnim("extra", "attack");
            TickHelper.tickLater(this.mob.level(), 15, () -> {
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
                ErsBleedingEffect.giveBleedingEffect(pEnemy, 2, 5);
                this.ticksUntilNextPathRecalculation = 60 + this.mob.getRandom().nextInt(10);
                if (!this.mob.isPassenger())
                    ((AcanthodesNavigation) this.mob.getNavigation()).alterCreatePath(pEnemy, 0, -1);
            });
        }
    }
}
