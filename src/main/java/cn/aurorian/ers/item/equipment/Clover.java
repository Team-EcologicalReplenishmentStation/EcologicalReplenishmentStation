package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

public class Clover extends MountEquipmentItem {
    public Clover(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {
        if (entity.getControllingPassenger() instanceof Player player) {
            player.addEffect(new MobEffectInstance(MobEffects.LUCK, 11));
        }
    }
}
