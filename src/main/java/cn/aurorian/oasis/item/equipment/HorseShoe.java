package cn.aurorian.oasis.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

public class HorseShoe extends Item implements MountEquipment {
    public HorseShoe(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {
    }

    @Override
    public boolean canEquip(ErsTamableVehicle<?> entity) {
        return entity instanceof TubunasusDurovelaEntity;
    }
}
