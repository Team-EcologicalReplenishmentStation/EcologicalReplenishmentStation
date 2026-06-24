package cn.aurorian.ers.entity;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.phys.Vec3;

/**
 * 生物攻击状态管理器。
 *
 * <p>该类用于管理可驯服生物的攻击状态，包括：
 *
 * <ul>
 *   <li>攻击类型 - 当前执行的攻击类型
 *   <li>动画刻数 - 攻击动画剩余的播放时间
 *   <li>判定目标 - 攻击判定的目标实体
 *   <li>触发状态 - 攻击是否已被触发
 *   <li>转向攻击方向 - 转向攻击的移动方向
 * </ul>
 *
 * <p>该类将攻击逻辑与实体分离，使攻击行为更加模块化。 每种攻击类型（{@link AttackType}）都有自己的动画时长、移动限制和执行逻辑。
 *
 * @author mlus
 * @version 1.2.0-alpha
 * @see AttackType
 * @see ErsTamable
 */
public class MobAttack {

    /** 当前攻击类型 */
    public ErsAttackType type;

    /** 动画剩余刻数 */
    public int animatorTick;

    /** 关联的可驯服实体 */
    ErsTamable<?> entity;

    /** 攻击判定目标 */
    public LivingEntity judgementTarget;

    /** 转向攻击的移动方向 */
    public Vec3 turningAttackDirection = Vec3.ZERO;

    /** 攻击是否已被触发 */
    public boolean triggered = false;

    /** 触发时间点 */
    public int triggerTime = 0;

    /** 是否为同步实例 */
    public boolean isSyncInstance = false;

    /**
     * 构造攻击状态实例。
     *
     * @param type 攻击类型
     * @param entity 关联的实体
     */
    public MobAttack(ErsAttackType type, ErsTamable<?> entity) {
        this.type = type;
        this.animatorTick = type.getAnimationLength();
        this.entity = entity;
    }

    public void tick() {
        if (isSyncInstance) {
            return;
        }

        if (entity == null) return;

        if (!this.isEmpty()) {
            if (type.getStaminaCost() != 0 && entity instanceof ErsTamableVehicle<?> vehicle) {
                vehicle.staminaCount = 0;
            }

            if (type != AttackType.EMPTY) {
                type.execute(this, entity);
            }

            if (!type.canMove()) {
                entity.getNavigation().stop();
            }

            if (animatorTick > 0) {
                animatorTick--;
            }
            if (animatorTick == 0) {
                this.entity.setAttackState(new MobAttack(AttackType.EMPTY, this.entity));
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

    public ErsAttackType getType() {
        return type;
    }

    public ErsTamable<?> getEntity() {
        return this.entity;
    }

    public boolean isEmpty() {
        return type == AttackType.EMPTY;
    }
}
