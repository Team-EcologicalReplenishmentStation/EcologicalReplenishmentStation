package cn.aurorian.ers.effect;

import cn.aurorian.ers.init.ErsDamageSource;
import cn.aurorian.ers.init.ErsMobEffects;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

public class ErsBleedingEffect extends MobEffect {
    private static final Map<LivingEntity, Vec3> previousPositions = new HashMap<>();
    private static final Map<LivingEntity,Boolean> still = new HashMap<>();
    public ErsBleedingEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(@NotNull LivingEntity pLivingEntity, int pAmplifier) {
        if(pLivingEntity.getMobType().equals(MobType.UNDEAD)){
            pLivingEntity.removeEffect(this);
            return;
        }

        Vec3 currentPosition = new Vec3(pLivingEntity.position().x(), pLivingEntity.position().y(), pLivingEntity.position().z());
        Vec3 previousPosition = previousPositions.get(pLivingEntity);

        if(still.get(pLivingEntity) != null && still.get(pLivingEntity)){
            still.put(pLivingEntity, false);
            MobEffectInstance instance = pLivingEntity.getEffect(this);
            int amp = instance.getAmplifier();
            pLivingEntity.removeEffect(this);
            if(amp != 0){
                pLivingEntity.addEffect(new MobEffectInstance(this, instance.getDuration(), amp - 1, false, false, true));
            }
        }

        float modifier = 1f;
        if (!previousPositions.isEmpty() && previousPosition != null && previousPosition.equals(currentPosition)) {
            modifier = 0.5f;
            still.put(pLivingEntity, true);
        }else{
            if(pLivingEntity.isSprinting())
                modifier = 2f;
            still.put(pLivingEntity, false);
        }

        pLivingEntity.hurt(ErsDamageSource.getDamageSource(pLivingEntity.level(),ErsDamageSource.DIE_OF_BLEED), pLivingEntity.getMaxHealth() * 0.01f * (pAmplifier + 1) * modifier);

        previousPositions.clear();
        previousPositions.put(pLivingEntity, currentPosition);
    }

    @Override
    public void removeAttributeModifiers(@NotNull LivingEntity pLivingEntity, @NotNull AttributeMap pAttributeMap, int pAmplifier) {
        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);
        previousPositions.clear();
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return pDuration > 0 && pDuration % 120 == 0;
    }

    public static void giveBleedingEffect(LivingEntity target, int maxAmp) {
        giveBleedingEffect(target, 1, maxAmp);
    }

    public static void giveBleedingEffect(LivingEntity target, int step , int maxAmp){
        if(target.level().isClientSide)
            return;

        if(target.isDeadOrDying() || target.getMobType().equals(MobType.UNDEAD)) {
            return;
        }

        MobEffectInstance instance = target.getEffect(ErsMobEffects.BLEEDING.get());
        if(instance == null){
            target.addEffect(new MobEffectInstance(ErsMobEffects.BLEEDING.get(), 1200, 0, false, false, true));
        }else {
            int currentAmp = instance.getAmplifier();
            if (currentAmp + step < maxAmp) {
                instance.update(new MobEffectInstance(ErsMobEffects.BLEEDING.get(), 1200, currentAmp + step, false, false, true));
                previousPositions.clear();
            }else {
                instance.update(new MobEffectInstance(ErsMobEffects.BLEEDING.get(), 1200, maxAmp, false, false, true));
                previousPositions.clear();
            }
        }
    }

    public static void removeBleedingEffect(LivingEntity target) {
        if(target == null || target.isDeadOrDying()) {
            return;
        }

        MobEffectInstance instance = target.getEffect(ErsMobEffects.BLEEDING.get());

        if(instance == null)
            return;

        int currentAmp = instance.getAmplifier();
        target.removeEffect(ErsMobEffects.BLEEDING.get());

        int duration = instance.getDuration();
        if(currentAmp > 0) {
            target.addEffect(new MobEffectInstance(ErsMobEffects.BLEEDING.get(), duration, currentAmp - 1, false, false, true));
        }

    }
}
