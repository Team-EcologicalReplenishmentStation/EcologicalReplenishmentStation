package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.ai;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class DentisaurusLongirostrisHurtByTargetGoal extends HurtByTargetGoal {

    DentisaurusLongirostrisEntity mob;
    boolean restore;

    public DentisaurusLongirostrisHurtByTargetGoal(PathfinderMob pMob, Class<?>... pToIgnoreDamage) {
        super(pMob, pToIgnoreDamage);
        this.mob = (DentisaurusLongirostrisEntity) pMob;
    }

    @Override
    public boolean canUse() {
        if(this.mob.getLastHurtByMob() instanceof TamableAnimal tamableAnimal){
            if(this.mob.isTame() && tamableAnimal.getOwner() == this.mob.getOwner())
                return false;
        }
        return super.canUse();
    }

    @Override
    public void start() {
        super.start();
        if (mob.getCommand() == 1) {
            mob.setCommand(0);
            restore = true;
        }
    }

    @Override
    public void stop() {
        super.stop();
        if (mob.getCommand() == 0 && restore) {
            mob.setCommand(1);
            restore = false;
        }
    }
}
