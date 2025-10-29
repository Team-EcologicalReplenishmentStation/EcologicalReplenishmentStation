package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.ai;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import org.jetbrains.annotations.NotNull;

public class DentisaurusLongirostrisMeleeAttackGoal extends MeleeAttackGoal {
    DentisaurusLongirostrisEntity sotek;
    public DentisaurusLongirostrisMeleeAttackGoal(DentisaurusLongirostrisEntity pMob, double pSpeedModifier, boolean pFollowingTargetEvenIfNotSeen) {
        super(pMob, pSpeedModifier, pFollowingTargetEvenIfNotSeen);
        sotek = pMob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && !sotek.isVehicle();
    }

    @Override
    public void start() {
        super.start();
        this.sotek.getAnimatableInstanceCache().getManagerForId(this.sotek.getId()).stopTriggeredAnimation("catch_fish-small");
        this.sotek.getAnimatableInstanceCache().getManagerForId(this.sotek.getId()).stopTriggeredAnimation("catch_fish-middle");
        if(this.sotek.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL || this.sotek.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE) {
            this.sotek.startAttack(AttackType.EMPTY);
        }
    }

    @Override
    public void stop() {
        super.stop();
        sotek.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE,false));
    }

    @Override
    protected void checkAndPerformAttack(@NotNull LivingEntity pEnemy, double pDistToEnemySqr) {
        double d0 = this.getAttackReachSqr(pEnemy);
        if (pDistToEnemySqr <= d0 && isTimeToAttack() && sotek.getAttackState().isEmpty()) {
            this.resetAttackCooldown();
            if(pEnemy == sotek.getOwner() || (pEnemy instanceof TamableAnimal tamableAnimal && sotek.isTame() && tamableAnimal.getOwner() == sotek.getOwner())) {
                return;
            }

            if(sotek.getRandom().nextFloat() < 0.9)
                sotek.startAttack(AttackType.SWAMP_DRAGON_ATTACK);
            else{
                if(sotek.isInWater() && !sotek.onGround()){
                    sotek.startAttack(AttackType.SWAMP_DRAGON_SWIM_SPECIAL_ATTACK);
                }else
                    sotek.startAttack(AttackType.SWAMP_DRAGON_SPECIAL_ATTACK);
            }


            this.mob.swing(InteractionHand.MAIN_HAND);
            this.mob.doHurtTarget(pEnemy);
            this.mob.getEntityData().set(DentisaurusLongirostrisEntity.BLOODY,true);
        }
    }
}
