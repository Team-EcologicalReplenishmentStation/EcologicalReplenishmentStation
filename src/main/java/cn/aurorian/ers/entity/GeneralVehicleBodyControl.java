package cn.aurorian.ers.entity;

import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.player.Player;

public class GeneralVehicleBodyControl extends BodyRotationControl {
    private final ErsTamableVehicle<?> vehicle;
    private float initialTurnAngle = 0; // 记录开始转弯时的角度
    private boolean isTurning = false; // 是否正在转弯
    private boolean wasLargeTurn = false; // 添加这个变量来记录是否是大转弯状态
    private boolean isTurningAttack = false;
    private float targetYRot = 0; // 目标Y旋转角度
    private int tickCount = 0;
    public boolean rideBefore;

    public GeneralVehicleBodyControl(ErsTamableVehicle pMob) {
        super(pMob);
        this.vehicle = pMob;
    }

    protected boolean vehicleTick(
            float walkRotSpeed,
            float sprintRotSpeed,
            int startAttackTurningTick,
            float yHeadRotSpeed,
            float xRotSpeed,
            float xRotLimit,
            float maxHeadYRotDegree,
            float largeTurnThreshold) {
        if (vehicle.getControllingPassenger() instanceof Player driver && vehicle.level().isClientSide) {
            if (vehicle.getAttackState().getType().isTurning()) tickCount++;

            rideBefore = true;
            if (vehicle.getAttackState().getType().canMove()
                    && (Math.abs(driver.xxa) > 0 || Math.abs(driver.zza) > 0)
                    && (vehicle.onGround() || vehicle.isInWater())) {
                float targetYaw =
                        driver.yHeadRot - ErsUtils.getDirectionAngle(Math.signum(driver.xxa), Math.signum(driver.zza));
                // 如果是新的转弯（之前没有在转弯）
                if (!isTurning) {
                    initialTurnAngle = this.vehicle.getYRot();
                    isTurning = true;
                    // 计算初始的大转弯状态
                    float initialDifference = Math.abs(Mth.degreesDifference(targetYaw, initialTurnAngle));
                    wasLargeTurn = initialDifference > largeTurnThreshold;
                }
                // 如果正在转弯且只按W/S
                else if (isTurning && Math.abs(driver.zza) > 0 && driver.xxa == 0) {
                    // 更新初始角度，但保持大转弯状态
                    initialTurnAngle = this.vehicle.getYRot();
                }

                // 计算当前的角度差
                float totalAngleDifference = Math.abs(Mth.degreesDifference(targetYaw, initialTurnAngle));
                // 如果之前是大转弯，就保持大转弯状态，直到完全回正
                boolean isLargeTurn = wasLargeTurn || totalAngleDifference > 80;

                if (Mth.degreesDifference(targetYaw, this.vehicle.getYRot()) > 0
                        && vehicle.isControlledByLocalInstance()) {
                    this.vehicle.setRotDirection(
                            MobRotDirection.of(MobRotDirection.RotDirection.LEFT, isLargeTurn), true);
                } else if (vehicle.isControlledByLocalInstance()) {
                    this.vehicle.setRotDirection(
                            MobRotDirection.of(MobRotDirection.RotDirection.RIGHT, isLargeTurn), true);
                }

                if (this.vehicle.getAttackState().getType().canMove()) {
                    float rotSpeed = vehicle.isSprinting() ? sprintRotSpeed : walkRotSpeed;
                    this.vehicle.setYRot(Mth.rotateIfNecessary(targetYaw, this.vehicle.getYRot(), rotSpeed));
                }

                if (Mth.degreesDifference(targetYaw, this.vehicle.getYRot()) == 0) {
                    // 完全回正时重置所有状态
                    if (vehicle.isControlledByLocalInstance())
                        vehicle.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false), true);
                    isTurning = false;
                    wasLargeTurn = false;
                }
            } else if (this.vehicle.getAttackState().getType().isTurning()) {
                if (!isTurningAttack) {
                    targetYRot = driver.yHeadRot;
                    isTurningAttack = true;
                }

                if (tickCount < startAttackTurningTick) return true;

                this.vehicle.setYRot(Mth.rotateIfNecessary(targetYRot, this.vehicle.getYRot(), 15));
                // 双倍转头速度
                this.vehicle.yHeadRot = Mth.approachDegrees(this.vehicle.yHeadRot, driver.yHeadRot, 20);
            } else {
                if (vehicle.isControlledByLocalInstance())
                    vehicle.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false), true);
                isTurning = false;
                wasLargeTurn = false;
                isTurningAttack = false;
                tickCount = 0;
            }

            // 立即更新身体旋转
            this.vehicle.yBodyRot = this.vehicle.getYRot();
            this.vehicle.yHeadRot = Mth.approachDegrees(this.vehicle.yHeadRot, driver.yHeadRot, yHeadRotSpeed);
            float targetRot = Mth.clamp(driver.getXRot(), -xRotLimit, xRotLimit);
            this.vehicle.setXRot(Mth.approachDegrees(this.vehicle.getXRot(), targetRot, xRotSpeed));

            this.vehicle.yHeadRot =
                    Mth.rotateIfNecessary(this.vehicle.yHeadRot, this.vehicle.yBodyRot, maxHeadYRotDegree);

            return true;
        }
        return false;
    }

    protected void aiTick(float maxHeadYRotDegree, float stillRotSpeed, float movingRotSpeed) {
        if (vehicle.getControllingPassenger() == null) {
            if (rideBefore) {
                this.vehicle.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
                rideBefore = false;
            }

            if (this.vehicle.isMoving()) this.vehicle.yHeadRot = this.vehicle.getYRot();

            float pitchResetSpeed = 2.0f;
            float targetPitch = Mth.approachDegrees(this.vehicle.getXRot(), 0, pitchResetSpeed);
            this.vehicle.setXRot(targetPitch);

            float degreeDifference = Mth.degreesDifference(this.vehicle.getYRot(), this.vehicle.yBodyRot);
            if (degreeDifference > 0) {
                vehicle.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.LEFT, true));
                vehicle.updateMount();
            } else if (degreeDifference < 0) {
                vehicle.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.RIGHT, true));
                vehicle.updateMount();
            } else {
                vehicle.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
            }

            if (this.vehicle.isMoving()) {
                this.vehicle.yBodyRot =
                        Mth.approachDegrees(this.vehicle.yBodyRot, this.vehicle.getYRot(), movingRotSpeed);
            } else {
                this.vehicle.yBodyRot =
                        Mth.approachDegrees(this.vehicle.yBodyRot, this.vehicle.getYRot(), stillRotSpeed);
            }

            this.vehicle.yHeadRot =
                    Mth.rotateIfNecessary(this.vehicle.yHeadRot, this.vehicle.yBodyRot, maxHeadYRotDegree);
        }
    }
}
