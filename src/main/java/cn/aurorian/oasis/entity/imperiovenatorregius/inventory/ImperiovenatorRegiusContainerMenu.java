package cn.aurorian.oasis.entity.imperiovenatorregius.inventory;

import cn.aurorian.ers.entity.inventory.AbstractMountContainerMenu;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import cn.aurorian.oasis.init.OasisContainers;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class ImperiovenatorRegiusContainerMenu extends AbstractMountContainerMenu<ImperiovenatorRegiusEntity> {

    public ImperiovenatorRegiusContainerMenu(
            int pContainerId, Inventory pPlayerInventory, ImperiovenatorRegiusEntity entity) {
        super(OasisContainers.REGIUS_CONTAINER.get(), pContainerId, pPlayerInventory, entity);
    }

    @Override
    protected void addEntitySlots() {
        Container container = entity.getInventory();

        // 槽1、2、3：装备
        this.addSlot(new Slot(container, 1, 98, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        this.addSlot(new Slot(container, 2, 98 + 18, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        this.addSlot(new Slot(container, 3, 98 + 36, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
    }

    @Override
    protected int getEntitySlotCount() {
        return 3;
    }

    @Override
    protected boolean moveToEntitySlots(ItemStack currentItem, ItemStack slotStack) {
        if (isEquipment(currentItem)) {
            return this.moveItemStackTo(slotStack, 0, 3, false);
        }
        return false;
    }

    private boolean isEquipment(ItemStack stack) {
        return (stack.getItem() instanceof MountEquipment equipment && equipment.canEquip(entity))
                || stack.is(Items.TOTEM_OF_UNDYING)
                || stack.is(Items.ENCHANTED_GOLDEN_APPLE)
                || stack.is(Items.SHIELD);
    }
}
