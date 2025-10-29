package cn.aurorian.oasis.entity.tubunasusdurovela.ai;

import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class TubunasusDurovelaSailGoal extends Goal {
    private final TubunasusDurovelaEntity mob;

    public TubunasusDurovelaSailGoal(TubunasusDurovelaEntity mob) {
        this.mob = mob;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle() || this.mob.isBaby()) {
            return false;
        } else {
            if (this.mob.getRandom().nextInt(reducedTickDelay(600)) != 0) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void start() {
        this.mob.setSailUp(!this.mob.isSailUp());
    }

    @Override
    public boolean canContinueToUse() {
        return false;
    }
}
