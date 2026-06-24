package cn.aurorian.ers.entity;

import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

/**
 * 可飞行坐骑基类。
 *
 * <p>飞行方向由玩家视角决定，速度基于实体属性。
 * 支持俯冲、上升、滑翔、悬停（Dive Key）等飞行机制。
 */
public abstract class ErsFlyableVehicle<T extends ErsFlyableVehicle<?>> extends ErsTamableVehicle<T> {
    private static final EntityDataAccessor<Integer> FLIGHT_VERTICAL_INPUT =
            SynchedEntityData.defineId(ErsFlyableVehicle.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> IS_FLYING =
            SynchedEntityData.defineId(ErsFlyableVehicle.class, EntityDataSerializers.BOOLEAN);

    // 用于跟踪转向状态
    protected float currentRoll = 0;
    protected float targetRoll = 0;

    protected float glideForwardSpeed;

    // 防止刚起飞时被 travel() 的落地检测逻辑重置
    protected int takeoffGraceTicks = 0;

    protected ErsFlyableVehicle(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FLIGHT_VERTICAL_INPUT, 0);
        this.entityData.define(IS_FLYING, false);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.takeoffGraceTicks > 0) {
            this.takeoffGraceTicks--;
        }
    }

    public void onFlightKeyUpdate(int verticalInput) {
        if (!level().isClientSide) {
            setFlightVerticalInput(verticalInput);
            if (verticalInput > 0) {
                setFlying(true);
            }
        }
    }

    public int getFlightVerticalInput() {
        return entityData.get(FLIGHT_VERTICAL_INPUT);
    }

    public void setFlightVerticalInput(int verticalInput) {
        entityData.set(FLIGHT_VERTICAL_INPUT, Mth.clamp(verticalInput, -1, 1));
    }

    public boolean isFlying() {
        return entityData.get(IS_FLYING);
    }

    public void setFlying(boolean flying) {
        entityData.set(IS_FLYING, flying);
    }

    @Override
    protected void removePassenger(@NotNull Entity passenger) {
        super.removePassenger(passenger);
        this.setFlightVerticalInput(0);
        this.setFlying(false);
        this.glideForwardSpeed = 0;
    }

    @Override
    public void travel(@NotNull Vec3 travelVector) {
        // 在水中时禁用飞行，走正常物理
        if (this.isInWaterOrBubble() || this.isInLava()) {
            if (this.isFlying()) {
                this.setFlying(false);
                this.setNoGravity(false);
                this.glideForwardSpeed = 0;
            }
            super.travel(travelVector);
            return;
        }

        // 服务端同步：客户端起飞后实体位置同步过来，服务端自动进入飞行模式
        if (!level().isClientSide && !this.onGround() && !this.isFlying()) {
            this.setFlying(true);
            this.setNoGravity(true);
        }

        if (this.getControllingPassenger() instanceof Player rider) {
            boolean isMovingForward = rider.zza > 0;

            // 不在地面 + 前进 → 保持飞行模式
            if (isMovingForward && !this.onGround() && !this.isFlying()) {
                this.setFlying(true);
            }

            // 飞行模式
            if (this.isFlying() && !this.onGround()) {
                tickFlightSpeed(rider);
                applyFlightPhysics(rider);
                return;
            }
        }

        // 落地时重置飞行状态（刚起飞的几 tick 内不重置）
        if (this.isFlying() && this.onGround() && this.takeoffGraceTicks <= 0) {
            this.setFlying(false);
            this.setNoGravity(false);
            this.setDeltaMovement(Vec3.ZERO);
        }

        // 非飞行状态：恢复正常物理
        if (this.isNoGravity() && !isInWaterOrBubble() && !isInLava()) {
            this.setNoGravity(false);
            this.setFlying(false);
            this.glideForwardSpeed = 0;
        }

        super.travel(travelVector);
    }

    /**
     * 应用飞行物理计算：垂直/水平速度、惯性、旋转。
     */
    protected void applyFlightPhysics(Player rider) {
        this.setNoGravity(true);
        this.fallDistance = 0;

        if (this.isDiving()) {
            // 悬停：平滑衰减到停止
            Vec3 current = this.getDeltaMovement();
            Vec3 stopped = current.multiply(0.95, 0, 0.95);
            this.setDeltaMovement(stopped);
            this.move(MoverType.SELF, stopped);
            applyFlightRotation(rider);
            return;
        }

        float speed = this.rideSpeed;
        boolean isMovingBackward = rider.zza < 0;
        float pitch = rider.getXRot();

        // 计算垂直速度
        double yDir = calculateVerticalSpeed(rider, speed, pitch, isMovingBackward);

        // 水平方向：使用实体自身朝向（而非玩家朝向）和 rideSpeed
        float yawRad = this.getYRot() * ((float) Math.PI / 180F);
        double xDir = -Mth.sin(yawRad) * speed;
        double zDir = Mth.cos(yawRad) * speed;

        Vec3 targetVelocity = new Vec3(xDir, yDir, zDir);

        // 应用惯性和阻力平滑过渡
        Vec3 currentVelocity = this.getDeltaMovement();
        float inertiaFactor = getInertiaFactor();
        Vec3 newVelocity = currentVelocity
                .multiply(inertiaFactor, 1.0, inertiaFactor)
                .add(targetVelocity.multiply(1.0 - inertiaFactor, 1.0 - inertiaFactor, 1.0 - inertiaFactor));

        double verticalDrag = this.isDiving() ? 0.5D : getVerticalDrag();
        newVelocity = new Vec3(newVelocity.x, newVelocity.y * verticalDrag, newVelocity.z);

        this.setDeltaMovement(newVelocity);
        this.move(MoverType.SELF, newVelocity);

        // 旋转控制
        applyFlightRotation(rider);
    }

    /**
     * 根据玩家输入和视角计算垂直速度。
     */
    protected double calculateVerticalSpeed(Player rider, float speed, float pitch, boolean isMovingBackward) {
        if (this.isDiving()) {
            // 按下 Dive Key：悬停，保持当前高度
            return getHoverVerticalSpeed(speed);
        } else if (isMovingBackward) {
            // 后退：只减速，不下降
            return getBackwardVerticalSpeed(speed);
        } else if (rider.zza > 0) {
            // 有前进输入时：看向哪里飞向哪里
            // 抬头（负pitch）上升，低头（正pitch）下降，平视保持水平
            float pitchRad = pitch * ((float) Math.PI / 180F);
            return -Mth.sin(pitchRad) * speed * 0.0875f;
        } else {
            // 无输入：滑翔，固定缓慢下降，不随视角变化
            return getGlideVerticalSpeed(speed);
        }
    }

    /**
     * 应用飞行旋转控制：Yaw、Pitch、Roll。
     */
    protected void applyFlightRotation(Player rider) {
        float targetYaw = rider.getYRot();
        float turnSpeed = getFlightTurnSpeed();
        float oldYaw = this.getYRot();
        float smoothedYaw = Mth.rotateIfNecessary(targetYaw, oldYaw, turnSpeed);
        this.setYRot(smoothedYaw);
        this.yBodyRot = smoothedYaw;
        this.yHeadRot = smoothedYaw;
        this.setXRot(Mth.clamp(rider.getXRot() * 0.3f, -45f, 45f));

        // Roll
        float yawDiff = Mth.degreesDifference(smoothedYaw, oldYaw);
        this.targetRoll = -yawDiff * getFlightRollFactor();
        this.currentRoll = Mth.lerp(0.1f, this.currentRoll, this.targetRoll);
        this.yHeadRot = Mth.rotateIfNecessary(smoothedYaw + this.currentRoll, smoothedYaw, 30f);
    }

    /**
     * 通用起飞方法。子类调用并传入跳跃力度。
     */
    protected void executeTakeoff(double jumpPower) {
        this.setFlying(true);
        this.setNoGravity(true);
        this.fallDistance = 0;
        Vec3 current = this.getDeltaMovement();
        this.setDeltaMovement(current.x, jumpPower, current.z);
        this.hasImpulse = true;
        this.takeoffGraceTicks = 5;
    }

    /**
     * 飞行状态下的速度控制。由子类 tickRidden() 在飞行时调用。
     */
    protected void tickFlightSpeed(Player driver) {
        float baseSpeed = (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
        boolean isMovingForward = driver.zza > 0;
        boolean isMovingBackward = driver.zza < 0;
        float pitch = driver.getXRot();
        float approachRate = 0.08f;

        // 悬停（Dive Key）：屏蔽所有俯冲逻辑，rideSpeed 平滑归零
        if (this.isDiving()) {
            this.rideSpeed = Mth.approach(this.rideSpeed, 0, approachRate * 2);
            return;
        }

        boolean isDivingPitch = pitch > getDiveAngleThreshold();

        // 飞行体力：有向前输入时消耗，滑翔（无输入）时不消耗
        if (isMovingForward && !level().isClientSide && getFlightStaminaCost() > 0) {
            this.setStamina(this.getStamina() - getFlightStaminaCost());
        }

        if (isMovingForward) {
            if (isDivingPitch) {
                // 俯冲：大幅加速，水平速度增加
                float pitchRad = pitch * ((float) Math.PI / 180F);
                float diveBonus = Mth.sin(pitchRad) * baseSpeed * getCruiseSpeedMultiplier() * getDiveForwardBonus();
                float targetSpeed = baseSpeed * getCruiseSpeedMultiplier() + diveBonus;
                this.rideSpeed =
                        Mth.approach(this.rideSpeed, targetSpeed, approachRate * getDiveApproachRateMultiplier());
            } else {
                float targetSpeed = driver.isSprinting()
                        ? baseSpeed * getSprintSpeedMultiplier()
                        : baseSpeed * getCruiseSpeedMultiplier();
                this.rideSpeed = Mth.approach(this.rideSpeed, targetSpeed, approachRate);
            }
        } else if (isMovingBackward) {
            float targetSpeed = baseSpeed * getCruiseSpeedMultiplier() * getBackwardSpeedMultiplier();
            this.rideSpeed = Mth.approach(this.rideSpeed, targetSpeed, approachRate);
        } else {
            // 无输入：滑翔，缓慢减速
            float glideSpeed = baseSpeed * getCruiseSpeedMultiplier() * getGlideSpeedMultiplier();
            this.rideSpeed = Mth.approach(this.rideSpeed, glideSpeed, approachRate * getGlideApproachRateMultiplier());
        }
    }

    // ========== 可调参数（子类覆写）==========

    /** 俯冲角度阈值（度），朝下超过此值才算俯冲。 */
    protected float getDiveAngleThreshold() {
        return 45f;
    }

    /** 爬升角度阈值（度），抬头超过此值（更负）才算爬升。 */
    protected float getClimbAngleThreshold() {
        return -45f;
    }

    /** 悬停时的垂直速度（按下 Dive Key）。 */
    protected double getHoverVerticalSpeed(float speed) {
        return 0;
    }

    /** 俯冲时的垂直速度。 */
    protected double getDiveVerticalSpeed(float speed, float pitch) {
        float diveFactor = (pitch - getDiveAngleThreshold()) / (90f - getDiveAngleThreshold());
        return -speed * (0.04f + 0.12f * diveFactor);
    }

    /** 爬升时的垂直速度。 */
    protected double getClimbVerticalSpeed(float speed, float pitch) {
        float climbFactor = (-pitch - Math.abs(getClimbAngleThreshold())) / (90f - Math.abs(getClimbAngleThreshold()));
        return speed * (0.06f + 0.1f * climbFactor);
    }

    /** 后退时的垂直速度。 */
    protected double getBackwardVerticalSpeed(float speed) {
        return 0;
    }

    /** 滑翔时的垂直速度。 */
    protected double getGlideVerticalSpeed(float speed) {
        return -speed * 0.01f;
    }

    /** 俯冲时额外向前速度倍率。 */
    protected float getDiveForwardBonus() {
        return 1.5f;
    }

    /** 俯冲时速度逼近倍率。 */
    protected float getDiveApproachRateMultiplier() {
        return 0.8f;
    }

    /** 后退时速度倍率（相对于巡航速度）。 */
    protected float getBackwardSpeedMultiplier() {
        return 0.3f;
    }

    /** 滑翔时速度倍率（相对于巡航速度）。 */
    protected float getGlideSpeedMultiplier() {
        return 0.78f;
    }

    /** 滑翔时速度逼近倍率。 */
    protected float getGlideApproachRateMultiplier() {
        return 0.3f;
    }

    /** 飞行体力消耗（每 tick，有向前输入时）。0 表示不消耗。 */
    protected float getFlightStaminaCost() {
        return 0.05F;
    }

    /** 飞行转向速度（度/tick），越小转向越平滑。 */
    protected float getFlightTurnSpeed() {
        return 1.5f;
    }

    /** 飞行转向时的身体倾斜系数。 */
    protected float getFlightRollFactor() {
        return 1.5f;
    }

    /** 垂直方向空气阻力（0-1，越小阻力越大）。 */
    protected double getVerticalDrag() {
        return 0.98D;
    }

    /** 疾跑时的速度倍率。 */
    protected float getSprintSpeedMultiplier() {
        return 5.0f;
    }

    /** 正常飞行时的速度倍率。 */
    protected float getCruiseSpeedMultiplier() {
        return 3.0f;
    }

    /** 惯性因子（0-1），越小加速越慢但停止也越慢。 */
    protected float getInertiaFactor() {
        return 0.2f;
    }
}
