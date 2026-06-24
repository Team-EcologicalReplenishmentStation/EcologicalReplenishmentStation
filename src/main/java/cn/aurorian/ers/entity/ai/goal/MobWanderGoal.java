package cn.aurorian.ers.entity.ai.goal;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.ai.util.LandRandomPos;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

/** Random stroll goal for non-flying/swimming mobs */
public class MobWanderGoal extends RandomStrollGoal {
    int waterVerticalRange = 8;

    public MobWanderGoal(TamableAnimal dinosaur, double speed) {
        this(dinosaur, speed, 120);
    }

    public MobWanderGoal(TamableAnimal dinosaur, double speed, int interval) {
        super(dinosaur, speed, interval);
    }

    public MobWanderGoal setWaterVerticalRange(int waterVerticalRange) {
        this.waterVerticalRange = waterVerticalRange;
        return this;
    }

    @Override
    public boolean canUse() {
        if (this.mob.isInWater()
                || !((ErsTamableVehicle<?>) this.mob).getAttackState().getType().canMove()) {
            return false;
        }
        return super.canUse() && ((ErsTamableVehicle<?>) this.mob).getCommand() == 0;
    }

    @Override
    public boolean canContinueToUse() {
        return !this.mob.getNavigation().isDone()
                && (!this.mob.isVehicle() || this.mob.getControllingPassenger() == null);
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        Vec3 randomPos;
        int verticalDistance = 7;

        if (mob.isInWater()) {
            randomPos = LandRandomPos.getPos(mob, 30, waterVerticalRange);
            return randomPos == null ? LandRandomPos.getPos(mob, 10, verticalDistance) : randomPos;
        }
        randomPos = mob.getRandom().nextFloat() > 0.001
                ? LandRandomPos.getPos(mob, 10, verticalDistance)
                : DefaultRandomPos.getPos(mob, 10, verticalDistance);
        return randomPos;
    }
}
