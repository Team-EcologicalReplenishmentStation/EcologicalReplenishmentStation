package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.inventory;

import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.inventory.AbstractMountContainerMenu;
import cn.aurorian.ers.init.ErsContainers;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.item.equipment.MountEquipment;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class AquicornisDinosauriformisContainerMenu
        extends AbstractMountContainerMenu<AquicornisDinosauriformisEntity> {

    public AquicornisDinosauriformisContainerMenu(
            int pContainerId, Inventory pPlayerInventory, AquicornisDinosauriformisEntity entity) {
        super(ErsContainers.AQUICORNIS_DINOSAURIFORMIS_CONTAINER.get(), pContainerId, pPlayerInventory, entity);
    }

    @Override
    protected void addEntitySlots() {
        Container container = entity.getInventory();

        this.addSlot(new Slot(container, 0, 8, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.DINOSAURIFORMIS_SADDLE.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        this.addSlot(new Slot(container, 1, 115, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        this.addSlot(new Slot(container, 2, 115, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return entity.foodPredicate.test(stack);
            }
        });
    }

    @Override
    protected int getEntitySlotCount() {
        return 3;
    }

    @Override
    protected boolean moveToEntitySlots(ItemStack currentItem, ItemStack slotStack) {
        if (currentItem.is(ErsItems.DINOSAURIFORMIS_SADDLE.get())) {
            return this.moveItemStackTo(slotStack, 0, 1, false);
        } else if (isEquipment(currentItem)) {
            return this.moveItemStackTo(slotStack, 1, 2, false);
        } else if (currentItem.is(ErsItems.FISH_FEED.get())) {
            return this.moveItemStackTo(slotStack, 2, 3, false);
        }
        return false;
    }

    private boolean isEquipment(ItemStack stack) {
        return (stack.getItem() instanceof MountEquipment equipment && equipment.canEquip(entity))
                || stack.is(Items.TOTEM_OF_UNDYING)
                || stack.is(Items.TURTLE_HELMET)
                || stack.is(Items.ENCHANTED_GOLDEN_APPLE)
                || stack.is(Items.SHIELD);
    }
}
