package cn.aurorian.ers.effect;

import cn.aurorian.ers.config.ErsServerConfig;
import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import org.jetbrains.annotations.NotNull;

public class ComfortEffect extends MobEffect {
    private static final int MAX_AMPLIFIER = 2;
    private static final int EXTRA_AGE_TICKS_PER_LEVEL = 3;

    public ComfortEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity living, int amplifier) {
        if (!(living instanceof ErsTamable<?> pet)
                || pet.level().isClientSide
                || !pet.doAgeTick()
                || pet.isSoul()
                || pet.getHunger() <= 0) {
            return;
        }

        int comfortLevel = Math.min(amplifier, MAX_AMPLIFIER) + 1;
        if (comfortLevel > 0) {
            pet.setAgeInTicks(
                    pet.getAgeInTicks() + comfortLevel * EXTRA_AGE_TICKS_PER_LEVEL * ErsServerConfig.MATURE_RATE.get());
        }
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration > 0 && pDuration % 20 == 0;
    }
}
