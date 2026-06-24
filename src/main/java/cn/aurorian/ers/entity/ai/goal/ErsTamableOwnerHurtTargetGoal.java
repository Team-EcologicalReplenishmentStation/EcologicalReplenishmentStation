package cn.aurorian.ers.entity.ai.goal;

import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;

public class ErsTamableOwnerHurtTargetGoal extends OwnerHurtTargetGoal {
    ErsTamable<?> tamable;

    public ErsTamableOwnerHurtTargetGoal(TamableAnimal pTameAnimal) {
        super(pTameAnimal);
        tamable = (ErsTamable<?>) pTameAnimal;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && tamable.getCommand() != 1;
    }
}
