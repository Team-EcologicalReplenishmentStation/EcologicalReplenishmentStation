package cn.aurorian.ers.entity.creatures.terridensaurussaevus.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class TerridensaurusSaevusAttackGoal extends MeleeAttackGoal {
    TerridensaurusSaevusEntity entity;
    public TerridensaurusSaevusAttackGoal(TerridensaurusSaevusEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        entity = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !entity.isVehicle();
    }

    @Override
    public void stop() {
        super.stop();
        entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE,false));
        entity.setSprinting(false);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && isTimeToAttack() && entity.getAttackState().isEmpty()) {
            this.resetAttackCooldown();

            if(pEnemy == entity.getOwner() || (pEnemy instanceof TamableAnimal tamableAnimal && entity.isTame() && tamableAnimal.getOwner() == entity.getOwner())) {
                return;
            }

            if(entity.getRandom().nextFloat() < 0.9)
                entity.startAttack(AttackType.SAEVUS_ATTACK);
            else
                entity.startAttack(AttackType.SAEVUS_ATTACK_TURN);

            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
        }

        if(pDistToEnemySqr > d0){
            entity.setSprinting(true);
        }
    }
}
