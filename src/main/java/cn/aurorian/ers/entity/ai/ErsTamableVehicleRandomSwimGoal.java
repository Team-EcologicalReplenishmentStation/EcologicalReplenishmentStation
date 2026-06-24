package cn.aurorian.ers.entity.ai;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Nullable;

public class ErsTamableVehicleRandomSwimGoal extends RandomSwimmingGoal {
    private final ErsTamableVehicle<?> dino;

    public ErsTamableVehicleRandomSwimGoal(ErsTamableVehicle<?> dino, double speedModifier) {
        super(dino, speedModifier, 120);
        this.dino = dino;
    }

    @Override
    public boolean canUse() {
        if (!dino.isInWater()
                || dino.getTarget() != null
                || dino.getCommand() != 0
                || !dino.getAttackState().getType().canMove()) {
            return false;
        }
        return super.canUse();
    }

    @Nullable
    @Override
    protected Vec3 getPosition() {
        return BehaviorUtils.getRandomSwimmablePos(this.mob, 25, 7);
    }
}
