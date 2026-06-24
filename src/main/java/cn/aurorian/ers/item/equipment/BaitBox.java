package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.item.ItemStack;

public class BaitBox extends MountEquipmentItem {
    public BaitBox(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {}

    @Override
    public boolean canEquip(ErsTamableVehicle<?> entity) {
        return entity instanceof DentisaurusLongirostrisEntity || entity instanceof AquicornisDinosauriformisEntity;
    }
}
