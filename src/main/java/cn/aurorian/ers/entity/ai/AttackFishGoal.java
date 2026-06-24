package cn.aurorian.ers.entity.ai;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;

public class AttackFishGoal extends NearestAttackableTargetGoal {

    private final ErsTamableVehicle<?> mob;

    public AttackFishGoal(Mob p_26060_, Class p_26061_, boolean p_26062_) {
        super(p_26060_, p_26061_, p_26062_);
        mob = (ErsTamableVehicle<?>) p_26060_;
    }

    @Override
    public boolean canUse() {
        return super.canUse()
                && mob.getHunger() < 50
                && (mob.updateSkyBrightness() > 4 || mob.isInWater())
                && mob.getControllingPassenger() == null
                && mob.getCommand() == 0;
    }
}
