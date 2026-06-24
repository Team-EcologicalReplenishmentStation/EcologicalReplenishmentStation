package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.item.ItemStack;

public class BullyStick extends MountEquipmentItem {

    public BullyStick(Properties properties) {
        super(properties.stacksTo(1));
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {}
}
