package cn.aurorian.ers.entity.creatures.terridensaurussaevus;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.GeneralVehicleBodyControl;

public class TerridensaurusSaevusBodyControl extends GeneralVehicleBodyControl {
    private final ErsTamableVehicle<?> entity;
    public TerridensaurusSaevusBodyControl(ErsTamableVehicle pMob) {
        super(pMob);
        this.entity = pMob;
    }

    @Override
    public void clientTick() {
        boolean isVehicle = this.vehicleTick(4.2f,entity.isInWater() ? 1.75f : 3.5f,19,11f,9,25, 75);

        if(isVehicle)
            return;

        aiTick(35f);
    }
}
