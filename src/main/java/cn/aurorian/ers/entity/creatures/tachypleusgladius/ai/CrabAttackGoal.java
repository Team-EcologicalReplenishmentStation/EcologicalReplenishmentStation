package cn.aurorian.ers.entity.creatures.tachypleusgladius.ai;

import cn.aurorian.ers.entity.creatures.tachypleusgladius.TachypleusGladiusEntity;
import net.minecraft.world.entity.ai.goal.Goal;

public class CrabAttackGoal extends Goal {
    private final TachypleusGladiusEntity mob;
    public CrabAttackGoal(TachypleusGladiusEntity pMob) {
        this.mob = pMob;
    }

    @Override
    public boolean canUse() {
        return this.mob.isWaiting() && this.mob.getTarget() != null && this.mob.getCooldown() <= 0 && this.mob.getTarget().isInWater();
    }

    @Override
    public void start() {
        this.mob.crabAttack();
        this.mob.setWaiting(false);
    }
}
