package cn.aurorian.oasis.entity.pygopodusannulatum.ai;

import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
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
        if (this.mob.isVehicle() ||this.mob.isBaby()) {
            return false;
        } else {
            if (this.mob.getNoActionTime() >= 100) {
                return false;
            }

            if (this.mob.getRandom().nextInt(reducedTickDelay(60)) != 0) {
                return false;
            }


            return isGrass() && !mob.isSprinting();

        }
    }

    @Override
    public void tick() {
        tickCount++;
        if(isGrass()){
            this.mob.level().destroyBlock(this.mob.getOnPos().above(), false);
            this.mob.feed(2);
            tickCount = 0;
        }
    }

    @Override
    public void start() {
        this.mob.setEating(true);
        this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.05);
        if(isGrass()){
            this.mob.level().destroyBlock(this.mob.getOnPos().above(), false);
            this.mob.feed(2);
        }
    }

    @Override
    public void stop() {
        this.mob.setEating(false);
        this.mob.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2);
        tickCount = 0;
    }

    @Override
    public boolean canContinueToUse() {
        return (isGrass() || tickCount < 100);
    }

    private boolean isGrass(){
        return this.mob.level().getBlockState(this.mob.getOnPos().above()).is(Blocks.GRASS) || this.mob.level().getBlockState(this.mob.getOnPos().above()).is(Blocks.TALL_GRASS);
    }
}
