package cn.aurorian.ers.entity.creatures.latimeriasuchomimus;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobAttack;

public class SuchomimusAttackExecutor {
    private static final SuchomimusAttackExecutor INSTANCE = new SuchomimusAttackExecutor();
    public static SuchomimusAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeSuchomimusHold(MobAttack attack, LatimeriaSuchomimusEntity entity) {
        if(attack.animatorTick == AttackType.SUCHOMIMUS_HOLD.getAnimationLength()){
            attack.judgementTarget = entity.getLastHurtMob();
            entity.setTarget(null);
            entity.setAggressive(false);
            entity.getNavigation().stop();
            entity.tryFindWaterGoal.start();
        }

//        if(!entity.level().getFluidState(new BlockPos((int) entity.getMoveControl().getWantedX(), (int) entity.getMoveControl().getWantedY(), (int) entity.getMoveControl().getWantedZ())).is(FluidTags.WATER)){
//            entity.tryFindWaterGoal.start();
//        }
        entity.getNavigation().moveTo(entity.getMoveControl().getWantedX(), entity.getMoveControl().getWantedY(), entity.getMoveControl().getWantedZ(), 1.0D);


        if(entity.getHealth() <= 20 && entity.getLastHurtMob() == attack.judgementTarget){
            attack.judgementTarget = null;
        }

        if(attack.judgementTarget != null){
            if(attack.judgementTarget.getVehicle() != entity)
                attack.judgementTarget.startRiding(entity,true);
            attack.judgementTarget.setDeltaMovement(0,0,0);
            attack.judgementTarget.hurt(attack.judgementTarget.damageSources().mobAttack(entity),1f);
            attack.judgementTarget.setYRot(90);
            attack.judgementTarget.setXRot(90);
            if(attack.judgementTarget.isDeadOrDying()){
                attack.animatorTick = 0;
                entity.stopTriggeredAnimation("attack","hold");
            }
        }else {
            attack.animatorTick = 0;
            entity.stopTriggeredAnimation("attack","hold");
        }

        if(attack.animatorTick == 1){
            attack.judgementTarget.stopRiding();
            entity.stopTriggeredAnimation("attack","hold");
        }
    }
}
