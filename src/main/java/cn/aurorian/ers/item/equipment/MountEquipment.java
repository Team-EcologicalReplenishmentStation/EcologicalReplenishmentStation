package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.item.ItemStack;

public interface MountEquipment {
    void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity);

    default boolean canEquip(ErsTamableVehicle<?> entity) {
        return true;
    }
}
