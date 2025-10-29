package cn.aurorian.ers.entity;

import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class GeneralBodyControl extends BodyRotationControl {
    Mob mob;
    int maxBodyRot;
    public GeneralBodyControl(Mob pMob) {
        this(pMob, 5);
    }

    public GeneralBodyControl(Mob pMob, int maxRotationStep) {
        super(pMob);
        this.mob = pMob;
        this.maxBodyRot = maxRotationStep;
    }

    @Override
    public void clientTick() {
        if (ErsUtils.isMoving(mob)) {
            if(Math.abs(Mth.degreesDifference(this.mob.yBodyRot,this.mob.getYRot())) < maxBodyRot)
                this.mob.yBodyRot = this.mob.getYRot();
            this.mob.yBodyRot = Mth.approachDegrees(this.mob.yBodyRot,this.mob.getYRot(),maxBodyRot);
            this.mob.yHeadRot = Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float)this.mob.getMaxHeadYRot());
        } else {
           super.clientTick();
        }
    }
}
