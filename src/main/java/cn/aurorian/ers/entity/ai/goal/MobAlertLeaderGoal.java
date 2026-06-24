package cn.aurorian.ers.entity.ai.goal;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;

public class MobAlertLeaderGoal extends HurtByTargetGoal {
    public ErsTamableVehicle<?> mob;

    public MobAlertLeaderGoal(PathfinderMob pMob, Class<?>... pToIgnoreDamage) {
        super(pMob, pToIgnoreDamage);
        this.mob = (ErsTamableVehicle<?>) pMob;
    }

    @Override
    public boolean canUse() {
        if (this.mob.getLastHurtByMob() instanceof TamableAnimal tamableAnimal) {
            if (tamableAnimal.getOwner() == this.mob.getOwner() && tamableAnimal.getOwner() != null) return false;
        }
        return super.canUse();
    }

    //    @Override
    //    protected void alertOthers() {
    //        if(!shouldAlertMob(this.mob.getLastHurtByMob()))
    //            return;
    //
    //        this.mob.updateLeader();
    //
    //        ErsTamable<?> leader = this.mob.leader;
    //        if(leader != null){
    //            leader.updateLeader();
    //            alertOther(leader, this.mob.getLastHurtByMob());
    //        }
    //
    //    }
    //
    //    private boolean shouldAlertMob(LivingEntity otherMob) {
    //        if (this.mob == otherMob) {
    //            return false;
    //        }
    //
    //        if (otherMob instanceof Mob mob1 && mob1.getTarget() != null) {
    //            return false;
    //        }
    //
    //        if (this.mob != null && otherMob instanceof TamableAnimal) {
    //            LivingEntity thisOwner = this.mob.getOwner();
    //            LivingEntity otherOwner = ((TamableAnimal) otherMob).getOwner();
    //            if (thisOwner != otherOwner) {
    //                return false;
    //            }
    //        }
    //
    //        if(otherMob.getType() == this.mob.getType()) {
    //            return false;
    //        }
    //
    //        if (otherMob.isAlliedTo(this.mob.getLastHurtByMob())) {
    //            return false;
    //        }
    //
    //        return true;
    //    }
}
