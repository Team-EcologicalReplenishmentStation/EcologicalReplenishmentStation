package cn.aurorian.oasis.effect;

import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

public class AphrodisiacEffect extends MobEffect {
    public AphrodisiacEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity instanceof Player player && player.isSleeping()){
            player.stopSleeping();
        }

        if(ErsUtils.updateSkyBrightness(pLivingEntity.level()) < 4){
            this.removeAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
        }else {
            this.addAttributeModifiers(pLivingEntity, pLivingEntity.getAttributes(), pAmplifier);
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 40 == 0;
    }
}
