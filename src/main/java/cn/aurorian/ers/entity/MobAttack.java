package cn.aurorian.ers.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

public class MobAttack {
    public AttackType type;
    public int animatorTick;
    ErsTamable<?> entity;
    public LivingEntity judgementTarget;
    public Vec3 turningAttackDirection = Vec3.ZERO;
    public boolean triggered = false;
    public int triggerTime = 0;
    public boolean isSyncInstance = false;

    public MobAttack(AttackType type, ErsTamable<?> entity) {
        this.type = type;
        this.animatorTick = type.getAnimationLength();
        this.entity = entity;
    }

    public void tick() {
        if (isSyncInstance) {
            return;
        }

        if(entity == null)
            return;

        if(!this.isEmpty()){
            if(type.getStaminaCost() != 0 && entity instanceof ErsTamableVehicle<?> vehicle){
                vehicle.staminaCount = 0;
            }

            if (type != AttackType.EMPTY) {
                type.execute(this, entity);
            }

            if(!type.canMove()){
                entity.getNavigation().stop();
            }

            if (animatorTick > 0) {
                animatorTick--;
            }
            if(animatorTick == 0){
                this.entity.setAttackState(new MobAttack(AttackType.EMPTY,this.entity));
                triggered = false;
            }
        }
    }

    public int getAnimatorTick() {
        return animatorTick;
    }
    public boolean isFinished() {
        return animatorTick <= 0;
    }
    public AttackType getType() {
        return type;
    }
    public ErsTamable<?> getEntity()
    {
        return this.entity;
    }
    public boolean isEmpty() {
        return type == AttackType.EMPTY;
    }
}
