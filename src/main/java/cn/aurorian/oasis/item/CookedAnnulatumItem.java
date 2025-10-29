package cn.aurorian.oasis.item;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class CookedAnnulatumItem extends Item {
    public CookedAnnulatumItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        if (!pLevel.isClientSide) {
            clearHarmfulEffects(pLivingEntity);
        }
        return pLivingEntity.eat(pLevel, pStack);
    }

    public void clearHarmfulEffects(LivingEntity living) {
        Collection<MobEffectInstance> effects = living.getActiveEffects();

        List<MobEffect> toRemove = new ArrayList<>();
        for (MobEffectInstance effect : effects) {
            if (effect.getEffect().getCategory() == MobEffectCategory.HARMFUL) {
                toRemove.add(effect.getEffect());
            }
        }
        for (MobEffect effect : toRemove) {
            living.removeEffect(effect);
        }
    }
}
