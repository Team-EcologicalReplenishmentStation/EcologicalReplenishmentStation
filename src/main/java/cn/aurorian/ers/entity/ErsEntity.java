package cn.aurorian.ers.entity;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

public interface ErsEntity<T extends Mob & GeoAnimatable> {
    GeneralAnimator<T> getAnimator();

    default float getVolume(){
        return 1f;
    }

    default float getSoundRange(){
        return 25f;
    }
}
