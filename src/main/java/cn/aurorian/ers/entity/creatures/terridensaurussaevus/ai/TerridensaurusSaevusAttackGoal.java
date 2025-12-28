package cn.aurorian.ers.entity.creatures.terridensaurussaevus.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.util.TickHelper;
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
        return super.canUse() && !entity.isVehicle() && entity.getCommand() != 1;
    }

    @Override
    public void stop() {
        super.stop();
        entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE,false));
        entity.setSprinting(false);
//        entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18f);
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);


        if (pDistToEnemySqr <= d0 && isTimeToAttack() && entity.getAttackState().isEmpty()) {
            entity.setSprinting(false);
//            entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18f);
            this.resetAttackCooldown();

            if(pEnemy == entity.getOwner() || (pEnemy instanceof TamableAnimal tamableAnimal && entity.isTame() && tamableAnimal.getOwner() == entity.getOwner())) {
                return;
            }

            float random = entity.getRandom().nextFloat();

            if(random < 0.9 || !entity.isMature())
                entity.startAttack(AttackType.SAEVUS_ATTACK);
            else if(entity.onGround() && random < 0.95)
                entity.startAttack(AttackType.SAEVUS_ATTACK_TURN);
            else
                entity.startAttack(AttackType.SAEVUS_ROAR);

            TickHelper.tickLater(this.mob.level(),30, () ->{
                this.mob.swing(InteractionHand.MAIN_HAND);
                this.mob.doHurtTarget(pEnemy);
            });
        }

        if(pDistToEnemySqr > d0){
            entity.setSprinting(true);
//            entity.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.396f);
        }
    }
}
