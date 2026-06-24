package cn.aurorian.oasis.entity.tubunasusclyderotunda.inventory;

import cn.aurorian.ers.entity.inventory.AbstractMountContainerMenu;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import cn.aurorian.oasis.init.OasisContainers;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class TubunasusClyderotundaContainerMenu extends AbstractMountContainerMenu<TubunasusClyderotundaEntity> {

    public TubunasusClyderotundaContainerMenu(
            int pContainerId, Inventory pPlayerInventory, TubunasusClyderotundaEntity entity) {
        super(OasisContainers.CLYDEROTUNDA_CONTAINER.get(), pContainerId, pPlayerInventory, entity);
    }

    @Override
    protected void addEntitySlots() {
        Container container = entity.getInventory();

        // 槽1、2、3：装备
        this.addSlot(new Slot(container, 1, 82, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        this.addSlot(new Slot(container, 2, 82 + 18, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        this.addSlot(new Slot(container, 3, 82 + 36, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        // 槽4-13：库存（2行×5列）
        for (int i = 4; i <= 13; i++) {
            this.addSlot(new Slot(container, i, 82 + (i - 4) % 5 * 18, 36 + (i - 4) / 5 * 18));
        }
    }

    @Override
    protected int getEntitySlotCount() {
        return 13;
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
