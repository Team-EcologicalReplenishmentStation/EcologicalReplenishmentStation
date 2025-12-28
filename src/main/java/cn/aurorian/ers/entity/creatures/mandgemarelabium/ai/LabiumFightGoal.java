package cn.aurorian.ers.entity.creatures.mandgemarelabium.ai;

import cn.aurorian.ers.entity.creatures.mandgemarelabium.MandgemareLabiumEntity;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.EnumSet;
import java.util.List;

public class LabiumFightGoal extends Goal {
    private static final TargetingConditions PARTNER_TARGETING = TargetingConditions.forNonCombat().range(8.0D).ignoreLineOfSight();
    protected final MandgemareLabiumEntity animal;
    private final Class<? extends MandgemareLabiumEntity> partnerClass;
    protected final Level level;
    @Nullable
    protected MandgemareLabiumEntity partner;
    private int fightTime;
    private final double speedModifier;

    public LabiumFightGoal(MandgemareLabiumEntity pMandgemareLabiumEntity, double pSpeedModifier) {
        this(pMandgemareLabiumEntity, pSpeedModifier, pMandgemareLabiumEntity.getClass());
    }

    public LabiumFightGoal(MandgemareLabiumEntity pMandgemareLabiumEntity, double pSpeedModifier, Class<? extends MandgemareLabiumEntity> pPartnerClass) {
        this.animal = pMandgemareLabiumEntity;
        this.level = pMandgemareLabiumEntity.level();
        this.partnerClass = pPartnerClass;
        this.speedModifier = pSpeedModifier;
        this.setFlags(EnumSet.of(Goal.Flag.MOVE, Goal.Flag.LOOK));
    }

    public boolean canUse() {
        if (this.animal.getComplete() != 0) {
            return false;
        } else {
            this.partner = this.getFreeTarget();
            return this.partner != null;
        }
    }


    public boolean canContinueToUse() {
        return this.partner.isAlive() && this.partner.getComplete() == 0 && this.fightTime < 60;
    }


    public void stop() {
        this.partner = null;
        this.fightTime = 0;
    }


    public void tick() {
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float)this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.speedModifier);
        ++this.fightTime;
        if (this.fightTime >= this.adjustedTickDelay(60) && this.animal.distanceToSqr(this.partner) < 9.0D) {
            if(this.animal.getForce() >= this.partner.getForce()){
                this.partner.setComplete(12000);
            }else {
                this.animal.setComplete(12000);
            }
        }

    }


    @Nullable
    private MandgemareLabiumEntity getFreeTarget() {
        List<? extends MandgemareLabiumEntity> list = this.level.getNearbyEntities(this.partnerClass, PARTNER_TARGETING, this.animal, this.animal.getBoundingBox().inflate(8.0D));
        double d0 = Double.MAX_VALUE;
        MandgemareLabiumEntity animal = null;

        for(MandgemareLabiumEntity animal1 : list) {
            if (this.animal.getGender() && this.animal.getComplete() == 0 && this.animal.distanceToSqr(animal1) < d0) {
                animal = animal1;
                d0 = this.animal.distanceToSqr(animal1);
            }
        }

        return animal;
    }
}
