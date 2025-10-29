package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class RidingGuide extends Item implements MountEquipment {
    public RidingGuide(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {}
}
