package cn.aurorian.ers.entity.ai.goal;

import java.util.List;
import javax.annotation.Nullable;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.animal.Animal;

public class MobFollowParentGoal extends Goal {
    private final Animal animal;

    @Nullable
    private Animal parent;

    private final double speedModifier;
    private int timeToRecalcPath;

    public MobFollowParentGoal(Animal pAnimal, double pSpeedModifier) {
        this.animal = pAnimal;
        this.speedModifier = pSpeedModifier;
    }

    public boolean canUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else {
            List<? extends Animal> $$0 = this.animal
                    .level()
                    .getEntitiesOfClass(
                            this.animal.getClass(), this.animal.getBoundingBox().inflate(8.0, 4.0, 8.0));
            Animal $$1 = null;
            double $$2 = Double.MAX_VALUE;

            for (Animal $$3 : $$0) {
                if ($$3.getAge() >= 0) {
                    double $$4 = this.animal.distanceToSqr($$3);
                    if (!($$4 > $$2)) {
                        $$2 = $$4;
                        $$1 = $$3;
                    }
                }
            }

            if ($$1 == null) {
                return false;
            } else if ($$2 < 16.0) {
                return false;
            } else {
                this.parent = $$1;
                return true;
            }
        }
    }

    public boolean canContinueToUse() {
        if (this.animal.getAge() >= 0) {
            return false;
        } else if (!this.parent.isAlive()) {
            return false;
        } else {
            double $$0 = this.animal.distanceToSqr(this.parent);
            return !($$0 < 16.0) && !($$0 > 256.0);
        }
    }

    public void start() {
        this.timeToRecalcPath = 0;
    }

    public void stop() {
        this.parent = null;
        this.animal.setSprinting(false);
    }

    public void tick() {
        if (--this.timeToRecalcPath <= 0) {
            this.timeToRecalcPath = this.adjustedTickDelay(10);
            if (this.parent.isSprinting()) {
                this.animal.setSprinting(true);
            } else {
                this.animal.setSprinting(false);
            }
            this.animal
                    .getNavigation()
                    .moveTo(this.parent, animal.isSprinting() ? this.speedModifier * 2 : this.speedModifier);
        }
    }
}
