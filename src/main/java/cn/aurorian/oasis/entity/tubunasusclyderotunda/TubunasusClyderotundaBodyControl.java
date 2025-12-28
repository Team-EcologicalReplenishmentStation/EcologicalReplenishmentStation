package cn.aurorian.oasis.entity.tubunasusclyderotunda;

import cn.aurorian.ers.entity.GeneralVehicleBodyControl;

public class TubunasusClyderotundaBodyControl extends GeneralVehicleBodyControl {
    public TubunasusClyderotundaBodyControl(TubunasusClyderotundaEntity entity) {
        super(entity);
        this.entity = entity;
    }

    private final TubunasusClyderotundaEntity entity;
    @Override
    public void clientTick() {
        if(entity.isTame() && entity.getOwner() == entity.getControllingPassenger()) {
            vehicleTick(3.5f, 3.5f,9,
                    5, 9 , 20,25);

        } else
            aiTick(35f,2f, entity.isInWater() ? 2.3f : 10f);
    }
}
