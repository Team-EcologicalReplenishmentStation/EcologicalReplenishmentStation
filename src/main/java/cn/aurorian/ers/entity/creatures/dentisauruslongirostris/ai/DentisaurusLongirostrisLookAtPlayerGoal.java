package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.ai;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;

public class DentisaurusLongirostrisLookAtPlayerGoal extends LookAtPlayerGoal {
    DentisaurusLongirostrisEntity mob;
    public DentisaurusLongirostrisLookAtPlayerGoal(Mob mob, Class<? extends LivingEntity> pLookAtType, float pLookDistance) {
        super(mob, pLookAtType, pLookDistance);
        this.mob = (DentisaurusLongirostrisEntity) mob;
    }

    @Override
    public boolean canUse() {
        return super.canUse() && (mob.updateSkyBrightness() > 4 || mob.getCommand() != 0) && !mob.isVehicle() && !mob.isMoving() && mob.getAttackState().getType().canMove();
    }

    @Override
    public boolean canContinueToUse() {
        return super.canContinueToUse() && (mob.updateSkyBrightness() > 4 || mob.getCommand() != 0) && !mob.isVehicle() && !mob.isMoving();
    }

    @Override
    public void stop() {
        super.stop();
        this.mob.getLookControl().setLookAt(this.mob);
    }
}
