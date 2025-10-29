package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.ai;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;

public class DentisaurusLongirostrisOwnerHurtByTargetGoal extends OwnerHurtByTargetGoal {
    DentisaurusLongirostrisEntity sotek;
    public DentisaurusLongirostrisOwnerHurtByTargetGoal(TamableAnimal pTameAnimal) {
        super(pTameAnimal);
        sotek = (DentisaurusLongirostrisEntity) pTameAnimal;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && sotek.getCommand() != 1;
    }
}
