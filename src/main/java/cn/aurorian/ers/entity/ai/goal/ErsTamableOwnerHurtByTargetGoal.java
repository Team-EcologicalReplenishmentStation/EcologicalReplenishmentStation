package cn.aurorian.ers.entity.ai.goal;

import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;

public class ErsTamableOwnerHurtByTargetGoal extends OwnerHurtByTargetGoal {
    ErsTamable<?> tamable;
    public ErsTamableOwnerHurtByTargetGoal(TamableAnimal pTameAnimal) {
        super(pTameAnimal);
        tamable = (ErsTamable<?>) pTameAnimal;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && tamable.getCommand() != 1;
    }
}
