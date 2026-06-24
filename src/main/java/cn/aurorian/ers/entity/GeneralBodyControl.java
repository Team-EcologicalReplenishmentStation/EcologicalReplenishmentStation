package cn.aurorian.ers.entity;

import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.control.BodyRotationControl;

public class GeneralBodyControl extends BodyRotationControl {
    Mob mob;
    int maxBodyRotStep;

    public GeneralBodyControl(Mob pMob) {
        this(pMob, 5);
    }

    public GeneralBodyControl(Mob pMob, int maxRotationStep) {
        super(pMob);
        this.mob = pMob;
        this.maxBodyRotStep = maxRotationStep;
    }

    @Override
    public void clientTick() {
        this.mob.yBodyRot = Mth.approachDegrees(this.mob.yBodyRot, this.mob.getYRot(), maxBodyRotStep);
        if (ErsUtils.isMoving(mob)) {
            this.mob.yHeadRot =
                    Mth.rotateIfNecessary(this.mob.yHeadRot, this.mob.yBodyRot, (float) this.mob.getMaxHeadYRot());
        }
    }
}
