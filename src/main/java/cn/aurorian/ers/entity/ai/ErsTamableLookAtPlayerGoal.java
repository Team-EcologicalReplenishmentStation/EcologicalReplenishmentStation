package cn.aurorian.ers.entity.ai;

import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class ErsTamableLookAtPlayerGoal extends LookAtPlayerGoal {
    ErsTamable<?> mob;

    public ErsTamableLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> pLookAtType, float pLookDistance) {
        super(mob, pLookAtType, pLookDistance);
        this.mob = (ErsTamable<?>) mob;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        }
        return super.canUse() && !mob.mightBeSleeping();
    }

    @Override
    public boolean canContinueToUse() {
        if (this.mob.isVehicle()) {
            return false;
        }
        return super.canContinueToUse() && !mob.mightBeSleeping();
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.getLookControl().setLookAt(this.mob);
    }
}
