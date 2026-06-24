package cn.aurorian.ers.entity.creatures.benthosuchusplanidens;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobAttack;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;

public class PlanidensAttackExecutor {
    private static final PlanidensAttackExecutor INSTANCE = new PlanidensAttackExecutor();

    public static PlanidensAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeHold(MobAttack attack, BenthosuchusPlanidensPlanidensEntity entity) {
        if (attack.animatorTick == AttackType.PLANIDENS_HOLD.getAnimationLength()) {
            attack.judgementTarget = entity.getLastHurtMob();
            entity.setTarget(null);
            entity.setAggressive(false);
            entity.getNavigation().stop();
        }

        if (entity.getHealth() <= 30 && entity.getLastHurtMob() == attack.judgementTarget) {
            attack.judgementTarget = null;
        }

        if (attack.judgementTarget != null) {
            if (attack.judgementTarget.getVehicle() != entity) attack.judgementTarget.startRiding(entity, true);
            attack.judgementTarget.setDeltaMovement(0, 0, 0);
            if (attack.animatorTick == 20) {
                entity.triggerAnim("attack", "attack");
                attack.judgementTarget.hurt(
                        attack.judgementTarget.damageSources().mobAttack(entity), 25f);
                reduceEquipmentDurability(attack.judgementTarget);
            }

            attack.judgementTarget.setYRot(90);
            attack.judgementTarget.setXRot(90);
            if (attack.judgementTarget.isDeadOrDying()) {
                attack.animatorTick = 0;
                entity.stopTriggeredAnimation("attack", "hold");
            }
        } else {
            attack.animatorTick = 0;
            entity.stopTriggeredAnimation("attack", "hold");
        }

        if (attack.animatorTick == 1) {
            attack.judgementTarget.stopRiding();
            entity.stopTriggeredAnimation("attack", "hold");
        }
    }

    private void reduceEquipmentDurability(LivingEntity target) {
        for (EquipmentSlot slot : EquipmentSlot.values()) {
            if (slot.getType() != EquipmentSlot.Type.ARMOR) {
                continue;
            }

            target.getItemBySlot(slot).hurtAndBreak(250, target, broken -> broken.broadcastBreakEvent(slot));
        }
    }
}
