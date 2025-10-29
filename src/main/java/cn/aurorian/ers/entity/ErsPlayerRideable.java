package cn.aurorian.ers.entity;

import net.minecraft.world.entity.PlayerRideable;

public interface ErsPlayerRideable extends PlayerRideable {
    MobRotDirection getRotDirection();
    void setRotDirection(MobRotDirection direction);
    void onDiveKeyUpdate(boolean diving);
    int getSwimState();
    void setSwimState(int state);
}
