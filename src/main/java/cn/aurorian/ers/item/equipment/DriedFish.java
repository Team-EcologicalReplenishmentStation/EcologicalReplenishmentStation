package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.item.ItemStack;

public class DriedFish extends MountEquipmentItem {
    public DriedFish(Properties pProperties) {
        super(pProperties.stacksTo(16));
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {
        if ((entity.getHunger() < 50 || entity.getHealth() < entity.getMaxHealth()) && entity.tickCount % 200 == 0) {
            stack.shrink(1);
            entity.feed(2);
            entity.heal(5);
        }
    }

    @Override
    public boolean canEquip(ErsTamableVehicle<?> entity) {
        return entity instanceof DentisaurusLongirostrisEntity || entity instanceof AquicornisDinosauriformisEntity;
    }
}
