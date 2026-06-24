package cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.inventory;

import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
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

public class EosuchosaurusAntiquusContainerMenu extends AbstractMountContainerMenu<EosuchosaurusAntiquusEntity> {

    public EosuchosaurusAntiquusContainerMenu(
            int pContainerId, Inventory pPlayerInventory, EosuchosaurusAntiquusEntity entity) {
        super(ErsContainers.EOSUCHOSAURUS_ANTIQUUS_CONTAINER.get(), pContainerId, pPlayerInventory, entity);
    }

    @Override
    protected void addEntitySlots() {
        Container container = entity.getInventory();

        // 槽0：鞍具
        this.addSlot(new Slot(container, 0, 8, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.ANTIQUUS_SADDLE.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        // 槽1、2：装备
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

        this.addSlot(new Slot(container, 5, 98, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.MEAT_FEED.get());
            }
        });
        this.addSlot(new Slot(container, 6, 98 + 18, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.MEAT_FEED.get());
            }
        });
    }

    @Override
    protected int getEntitySlotCount() {
        return 5;
    }

    @Override
    protected boolean moveToEntitySlots(ItemStack currentItem, ItemStack slotStack) {
        if (currentItem.is(ErsItems.ANTIQUUS_SADDLE.get())) {
            return this.moveItemStackTo(slotStack, 0, 1, false);
        } else if (isEquipment(currentItem)) {
            return this.moveItemStackTo(slotStack, 1, 3, false);
        } else if (currentItem.is(ErsItems.MEAT_FEED.get())) {
            return this.moveItemStackTo(slotStack, 5, 6, false);
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
