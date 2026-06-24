package cn.aurorian.oasis.entity.imperiovenatorregius;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.GeneralVehicleBodyControl;
import cn.aurorian.ers.entity.MobRotDirection;
import net.minecraft.util.Mth;

public class ImperiovenatorRegiusBodyControl extends GeneralVehicleBodyControl {
    private final ErsTamableVehicle<?> entity;

    public ImperiovenatorRegiusBodyControl(ErsTamableVehicle pMob) {
        super(pMob);
        this.entity = pMob;
    }

    @Override
    public void clientTick() {
        boolean isVehicle = this.vehicleTick(
                entity.isMoving() ? 3.6f : 4.2f, entity.isInWater() ? 2.2f : 4f, 10, 12f, 9, 25, 75, 60);

        if (isVehicle) return;

        if (entity.getCommand() == 1 && !entity.isInWater()) {
            if (!this.entity.getRotDirection().isNone()) {
                this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
            }
            if (!this.entity.getLookControl().isLookingAtTarget()) {
                this.entity.yBodyRot = Mth.approachDegrees(this.entity.yBodyRot, this.entity.getYRot(), 1);
            }
            this.entity.yHeadRot = Mth.rotateIfNecessary(this.entity.yHeadRot, this.entity.yBodyRot, 55);
        } else if (entity.getAttackState().getType().canMove()) aiTick(35f, 4.3f, entity.isInWater() ? 2.3f : 10f);
    }
}
