package cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.GeneralVehicleBodyControl;
import cn.aurorian.ers.entity.MobRotDirection;
import net.minecraft.util.Mth;

public class EosuchosaurusAntiquusBodyControl extends GeneralVehicleBodyControl {
    private final ErsTamableVehicle<?> entity;

    public EosuchosaurusAntiquusBodyControl(ErsTamableVehicle pMob) {
        super(pMob);
        this.entity = pMob;
    }

    @Override
    public void clientTick() {
        boolean isVehicle = this.vehicleTick(4.8f, entity.isInWater() ? 2.6f : 4f, 6, 11f, 9, 25, 75, 80);

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
