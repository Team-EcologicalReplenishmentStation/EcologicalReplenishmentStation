package cn.aurorian.ers.entity.creatures.dentisauruslongirostris;

import cn.aurorian.ers.entity.GeneralVehicleBodyControl;
import cn.aurorian.ers.entity.MobRotDirection;
import net.minecraft.util.Mth;

public class DentisaurusLongirostrisBodyControl extends GeneralVehicleBodyControl {
    private final DentisaurusLongirostrisEntity entity;

    public DentisaurusLongirostrisBodyControl(DentisaurusLongirostrisEntity entity) {
        super(entity);
        this.entity = entity;
    }
    @Override
    public void clientTick() {
        boolean isVehicle = this.vehicleTick(3.5f,4f,13,11f,9,25,75);

        if(isVehicle)
            return;

        if(entity.updateSkyBrightness() < 4 && !entity.isInWater() && entity.getControllingPassenger() == null && entity.getCommand() == 0 && !entity.isMoving() && entity.getCommand() == 0){
            this.entity.yHeadRot = this.entity.yBodyRot = this.entity.getYRot();
        }else if(entity.getCommand() == 1 && !entity.isInWater()) {
            if(!this.entity.getRotDirection().isNone()){
                this.entity.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
            }
            if(!this.entity.getLookControl().isLookingAtTarget()){
                this.entity.yBodyRot = Mth.approachDegrees(this.entity.yBodyRot,this.entity.getYRot(),1);
            }
            this.entity.yHeadRot = Mth.rotateIfNecessary(this.entity.yHeadRot, this.entity.yBodyRot,
                    55);
        }else if(entity.getAttackState().getType().canMove())
            aiTick((float)this.entity.getMaxHeadYRot(),2.5f, entity.isInWater() ? 2.3f : 10f);
    }
}
