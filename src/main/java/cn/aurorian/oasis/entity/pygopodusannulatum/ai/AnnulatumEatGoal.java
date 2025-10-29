package cn.aurorian.oasis.entity.pygopodusannulatum.ai;

import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.level.block.Blocks;

public class AnnulatumEatGoal extends Goal {
    protected final PygopodusAnnulatumEntity mob;

    public AnnulatumEatGoal(PygopodusAnnulatumEntity pMob) {
        this.mob = pMob;
    }

    private int tickCount = 0;

    @Override
    public boolean canUse() {
        if (this.mob.isVehicle()) {
            return false;
        } else {
            if (this.mob.getNoActionTime() >= 100) {
                return false;
            }

            if (this.mob.getRandom().nextInt(reducedTickDelay(60)) != 0) {
                return false;
            }


            if(this.mob.level().getBlockState(this.mob.getOnPos().above()).is(Blocks.GRASS))
                return true;

        }
        return false;
    }

    @Override
    public void tick() {
        super.tick();
        tickCount++;
        if(this.mob.level().getBlockState(this.mob.getOnPos().above()).is(Blocks.GRASS)){
            this.mob.level().destroyBlock(this.mob.getOnPos().above(), false);
            tickCount = 0;
        }
    }

    @Override
    public void start() {
        this.mob.getEntityData().set(PygopodusAnnulatumEntity.EATING,true);
        if(this.mob.level().getBlockState(this.mob.getOnPos().above()).is(Blocks.GRASS)){
            this.mob.level().destroyBlock(this.mob.getOnPos().above(), false);
        }
    }

    @Override
    public void stop() {
        this.mob.getEntityData().set(PygopodusAnnulatumEntity.EATING,false);
        this.mob.feed(2);
        tickCount = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return (this.mob.level().getBlockState(this.mob.getOnPos().above()).is(Blocks.GRASS) || tickCount > 100) && ErsUtils.isMoving(this.mob);
    }
}
