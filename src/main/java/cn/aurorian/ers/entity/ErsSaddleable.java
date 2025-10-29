package cn.aurorian.ers.entity;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;

import javax.annotation.Nullable;

public interface ErsSaddleable {
    boolean isSaddleable();

    void equipSaddle(@Nullable SoundSource var1);

    default SoundEvent getSaddleSoundEvent() {
        return SoundEvents.HORSE_SADDLE;
    }

    boolean isSaddled();
}
