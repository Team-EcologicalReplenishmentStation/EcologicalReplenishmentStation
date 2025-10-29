package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class ScratchingBoard extends Item implements MountEquipment {
    public ScratchingBoard(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {}
}
