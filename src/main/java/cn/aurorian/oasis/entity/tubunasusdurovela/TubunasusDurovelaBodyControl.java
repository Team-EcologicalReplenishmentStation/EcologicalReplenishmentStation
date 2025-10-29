package cn.aurorian.oasis.entity.tubunasusdurovela;

import cn.aurorian.ers.entity.GeneralVehicleBodyControl;

public class TubunasusDurovelaBodyControl extends GeneralVehicleBodyControl {
    public TubunasusDurovelaBodyControl(TubunasusDurovelaEntity entity) {
        super(entity);
        this.entity = entity;
    }

    private final TubunasusDurovelaEntity entity;
    @Override
    public void clientTick() {
        if(entity.isTame() && entity.getOwner() == entity.getControllingPassenger()) {
            vehicleTick(entity.getRushTimer() > 100 ? 2 : 3, entity.getRushTimer() > 100 ? 2 : 3,9,
                    this.entity.getRushTimer() < 200 ? 5 : 2, entity.getRushTimer() < 100 ? 9 : 0, 20,25);

        } else
            aiTick(25f);
    }
}
