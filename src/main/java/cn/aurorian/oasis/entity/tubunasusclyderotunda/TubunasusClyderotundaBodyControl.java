package cn.aurorian.oasis.entity.tubunasusclyderotunda;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.player.Player;

public class TubunasusClyderotundaBodyControl extends BodyRotationControl {
    public TubunasusClyderotundaBodyControl(TubunasusClyderotundaEntity entity) {
        super(entity);
        this.entity = entity;
    }

    private final TubunasusClyderotundaEntity entity;
    private float initialTurnAngle = 0;
    private boolean isTurning = false;
    private float targetYRot = 0;
    private int tickCount = 0;
    private boolean isTurningAttack = false;
    private boolean wasLargeTurn = false;
    private boolean rideBefore;
    @Override
    public void clientTick() {
        if(entity.getControllingPassenger() instanceof Player driver && entity.isTame() && entity.getOwner() == driver && entity.level().isClientSide) {
            if(this.entity.getAttackState().getType() == AttackType.TUBUNASUS_ATTACK_TURN)
                tickCount++;

            rideBefore = true;
            if (Math.abs(driver.xxa) > 0 || Math.abs(driver.zza) > 0) {
                float targetYaw = driver.yHeadRot - ErsUtils.getDirectionAngle(Math.signum(driver.xxa), Math.signum(driver.zza));
                if (!isTurning) {
                    initialTurnAngle = this.entity.getYRot();
                    isTurning = true;
                    float initialDifference = Math.abs(Mth.degreesDifference(targetYaw, initialTurnAngle));
                    wasLargeTurn = initialDifference > 80;
                }
                else if (isTurning && Math.abs(driver.zza) > 0 && driver.xxa == 0) {
                    initialTurnAngle = this.entity.getYRot();
                }

                float totalAngleDifference = Math.abs(Mth.degreesDifference(targetYaw, initialTurnAngle));
                boolean isLargeTurn = wasLargeTurn || totalAngleDifference > 80;

                if(this.entity.getAttackState().getType().canMove()){
                    if(Mth.degreesDifference(targetYaw, this.entity.getYRot()) > 0 && entity.isControlledByLocalInstance()) {
                        this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.LEFT, isLargeTurn),true);
                        entity.updateMount();
                    } else if(entity.isControlledByLocalInstance()){
                        this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.RIGHT, isLargeTurn),true);
                        entity.updateMount();
                    }else {
                        this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
                    }
                }

                float rotSpeed = 3;
                if(entity.getRushTimer() > 100)
                    rotSpeed = 2;
                if(this.entity.getAttackState().getType().canMove())
                    this.entity.setYRot(Mth.rotateIfNecessary(targetYaw, this.entity.getYRot(), rotSpeed));

                if(Mth.degreesDifference(targetYaw, this.entity.getYRot()) == 0) {
                    if(entity.isControlledByLocalInstance())
                        entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false),true);
                    isTurning = false;
                    wasLargeTurn = false;
                }
            }
            else if(this.entity.getAttackState().getType() == AttackType.TUBUNASUS_ATTACK_TURN){
                if(!isTurningAttack){
                    targetYRot = driver.yHeadRot;
                    isTurningAttack = true;
                }
                if(tickCount < 9) return;

                this.entity.setYRot(Mth.rotateIfNecessary(targetYRot, this.entity.getYRot(), 15));
                this.entity.yHeadRot = Mth.approachDegrees(this.entity.yHeadRot, driver.yHeadRot, 20);
            }
            else {
                if(entity.isControlledByLocalInstance())
                    entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false),true);
                isTurning = false;
                wasLargeTurn = false;
                isTurningAttack = false;
                tickCount = 0;
            }

            this.entity.yBodyRot = this.entity.getYRot();
            if(this.entity.getRushTimer() < 200){
                this.entity.yHeadRot = Mth.approachDegrees(this.entity.yHeadRot, driver.yHeadRot, 6);
            }else {
                this.entity.yHeadRot = Mth.approachDegrees(this.entity.yHeadRot,this.entity.yBodyRot,3);
            }

            float targetRot = Mth.clamp(driver.getXRot(), -20, 20);
            if(entity.getRushTimer() < 100)
                this.entity.setXRot(Mth.approachDegrees(this.entity.getXRot(), targetRot, 10));

            this.entity.yHeadRot = Mth.rotateIfNecessary(this.entity.yHeadRot, this.entity.yBodyRot,
                    25f);
        }
        else if(!this.entity.isVehicle()){
            if(rideBefore){
                this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
                rideBefore = false;
            }

            this.entity.yHeadRot = this.entity.getYRot();
            float degreeDifference = Mth.degreesDifference(this.entity.getYRot(),this.entity.yBodyRot);
            if(degreeDifference > 0) {
                entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.LEFT, true));
            } else if(degreeDifference < 0) {
                entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.RIGHT, true));
            }else {
                entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
            }

            this.entity.yBodyRot = Mth.approachDegrees(this.entity.yBodyRot,this.entity.getYRot(), 3);

            this.entity.yBodyRot = Mth.rotateIfNecessary(this.entity.yBodyRot, this.entity.yHeadRot,
                    25f);
        }
    }
}
