package cn.aurorian.ers.item.weapon;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.item.Tier;
import org.jetbrains.annotations.NotNull;

public class AcanthodiiToothKnife extends SwordItem {
    public AcanthodiiToothKnife(
            Tier pTier, int pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public boolean hurtEnemy(ItemStack pStack, @NotNull LivingEntity target, @NotNull LivingEntity pAttacker) {
        pStack.hurtAndBreak(1, pAttacker, (p_43296_) -> p_43296_.broadcastBreakEvent(EquipmentSlot.MAINHAND));
        ErsBleedingEffect.giveBleedingEffect(target, 2);
        return true;
    }
}
