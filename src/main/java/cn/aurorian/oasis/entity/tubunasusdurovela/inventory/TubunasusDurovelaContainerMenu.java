package cn.aurorian.oasis.entity.tubunasusdurovela.inventory;

import cn.aurorian.ers.entity.inventory.AbstractMountContainerMenu;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import cn.aurorian.oasis.init.OasisContainers;
import cn.aurorian.oasis.init.OasisItems;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class TubunasusDurovelaContainerMenu extends AbstractMountContainerMenu<TubunasusDurovelaEntity> {

    public TubunasusDurovelaContainerMenu(
            int pContainerId, Inventory pPlayerInventory, TubunasusDurovelaEntity entity) {
        super(OasisContainers.DUROVELA_CONTAINER.get(), pContainerId, pPlayerInventory, entity);
    }

    @Override
    protected void addEntitySlots() {
        Container container = entity.getInventory();

        // 槽0：鞍具
        this.addSlot(new Slot(container, 0, 8, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(OasisItems.TUBUNASUS_SADDLE.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        // 槽1、2、3：装备
        this.addSlot(new Slot(container, 1, 82, 6) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        this.addSlot(new Slot(container, 2, 82 + 18, 6) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        this.addSlot(new Slot(container, 3, 82 + 36, 6) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        // 槽4-18：库存（3行×5列）
        for (int i = 4; i <= 18; i++) {
            this.addSlot(new Slot(container, i, 82 + (i - 4) % 5 * 18, 24 + (i - 4) / 5 * 18));
        }
    }

    @Override
    protected int getEntitySlotCount() {
        return 19;
    }

    @Override
    protected boolean moveToEntitySlots(ItemStack currentItem, ItemStack slotStack) {
        if (currentItem.is(OasisItems.TUBUNASUS_SADDLE.get())) {
            return this.moveItemStackTo(slotStack, 0, 1, false);
        } else if (isEquipment(currentItem)) {
            return this.moveItemStackTo(slotStack, 1, 4, false);
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
