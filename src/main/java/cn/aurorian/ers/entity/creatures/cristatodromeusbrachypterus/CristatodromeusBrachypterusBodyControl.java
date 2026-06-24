package cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus;

import cn.aurorian.ers.entity.GeneralVehicleBodyControl;
import cn.aurorian.ers.entity.MobRotDirection;
import net.minecraft.util.Mth;

public class CristatodromeusBrachypterusBodyControl extends GeneralVehicleBodyControl {
    private final CristatodromeusBrachypterusEntity entity;

    public CristatodromeusBrachypterusBodyControl(CristatodromeusBrachypterusEntity entity) {
        super(entity);
        this.entity = entity;
    }

    @Override
    public void clientTick() {
        boolean isVehicle = this.vehicleTick(6.0f, 5.0f, 8, 14f, 10f, 25f, 80f, 70f);

        if (isVehicle) {
            return;
        }

        if (entity.getCommand() == 1) {
            if (!this.entity.getRotDirection().isNone()) {
                this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
            }
            if (!this.entity.getLookControl().isLookingAtTarget()) {
                this.entity.yBodyRot = Mth.approachDegrees(this.entity.yBodyRot, this.entity.getYRot(), 2.0f);
            }
            this.entity.yHeadRot = Mth.rotateIfNecessary(this.entity.yHeadRot, this.entity.yBodyRot, 60f);
        } else if (entity.getAttackState().getType().canMove()) {
            aiTick(60f, 7.0f, 12.0f);
        }
    }
}
