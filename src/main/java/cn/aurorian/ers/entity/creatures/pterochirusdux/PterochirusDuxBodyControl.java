package cn.aurorian.ers.entity.creatures.pterochirusdux;

import cn.aurorian.ers.entity.ErsFlyableVehicle;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.GeneralVehicleBodyControl;
import cn.aurorian.ers.entity.MobRotDirection;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;

public class PterochirusDuxBodyControl extends GeneralVehicleBodyControl {
    private final ErsTamableVehicle<?> entity;

    public PterochirusDuxBodyControl(ErsTamableVehicle pMob) {
        super(pMob);
        this.entity = pMob;
    }

    @Override
    public void clientTick() {
        // 飞行时：限制转向速度，平滑跟随玩家朝向
        if (entity instanceof ErsFlyableVehicle<?> flyable && flyable.isFlying() && !entity.onGround()) {
            if (entity.getControllingPassenger() instanceof Player driver) {
                float targetYaw = driver.getYHeadRot();
                float turnSpeed = 2.5f;
                float smoothedYaw = Mth.rotateIfNecessary(targetYaw, entity.getYRot(), turnSpeed);
                entity.setYRot(smoothedYaw);
                entity.yBodyRot = smoothedYaw;
                entity.yHeadRot = Mth.approachDegrees(entity.yHeadRot, driver.getYHeadRot(), 6f);

                // 转向方向标记
                float diff = Mth.degreesDifference(smoothedYaw, targetYaw);
                if (Math.abs(diff) > 2f) {
                    if (diff > 0) {
                        entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.LEFT, false), true);
                    } else {
                        entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.RIGHT, false), true);
                    }
                } else {
                    entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false), true);
                }
            }
            return;
        }

        // 地面：正常身体旋转控制
        boolean isVehicle = this.vehicleTick(4.8f, 4f, 6, 11f, 9, 25, 75, 80);

        if (isVehicle) return;

        if (entity.getCommand() == 1) {
            if (!this.entity.getRotDirection().isNone()) {
                this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
            }
            if (!this.entity.getLookControl().isLookingAtTarget()) {
                this.entity.yBodyRot = Mth.approachDegrees(this.entity.yBodyRot, this.entity.getYRot(), 1);
            }
            this.entity.yHeadRot = Mth.rotateIfNecessary(this.entity.yHeadRot, this.entity.yBodyRot, 55);
        } else if (entity.getAttackState().getType().canMove()) {
            aiTick(35f, 4.3f, 10f);
        }
    }
}
