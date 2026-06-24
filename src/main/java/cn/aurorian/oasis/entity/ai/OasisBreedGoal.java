package cn.aurorian.oasis.entity.ai;

import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.world.entity.ai.goal.BreedGoal;

public class OasisBreedGoal extends BreedGoal {
    private int breedDelay;
    private final double moveSpeed;

    public OasisBreedGoal(ErsTamable tamable, double speed) {
        super(tamable, speed);
        this.moveSpeed = speed;
    }

    public void stop() {
        this.partner = null;
        this.breedDelay = 0;
    }

    public void tick() {
        super.tick();
        this.animal.getLookControl().setLookAt(this.partner, 10.0F, (float) this.animal.getMaxHeadXRot());
        this.animal.getNavigation().moveTo(this.partner, this.moveSpeed);
        ++this.breedDelay;
        if (this.breedDelay >= 60 && this.animal.distanceToSqr(this.partner) < 20.0) {

            this.breed();
        }
    }
}
