package cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.inventory;

import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import cn.aurorian.ers.entity.inventory.AbstractMountContainerMenu;
import cn.aurorian.ers.init.ErsContainers;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.item.equipment.MountEquipment;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class CristatodromeusBrachypterusContainerMenu
        extends AbstractMountContainerMenu<CristatodromeusBrachypterusEntity> {
    private static final int ENTITY_SLOT_START = 36;
    private static final int ENTITY_SLOT_END = 41;
    private static final int PLAYER_INV_START = 0;
    private static final int PLAYER_INV_END = 27;
    private static final int PLAYER_HOTBAR_START = 27;
    private static final int PLAYER_HOTBAR_END = 36;

    public CristatodromeusBrachypterusContainerMenu(
            int pContainerId, Inventory pPlayerInventory, CristatodromeusBrachypterusEntity entity) {
        super(ErsContainers.CRISTATODROMEUS_BRACHYPTERUS_CONTAINER.get(), pContainerId, pPlayerInventory, entity);
    }

    @Override
    protected void addEntitySlots() {
        Container container = entity.getInventory();

        this.addSlot(new Slot(container, 0, 8, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.CRISTATODROMEUS_BRACHYPTERUS_SADDLE.get());
            }

            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        this.addSlot(new Slot(container, 1, 98, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        this.addSlot(new Slot(container, 2, 116, 21) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        this.addSlot(new Slot(container, 5, 98, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return entity.isFood(stack);
            }
        });

        this.addSlot(new Slot(container, 6, 116, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return entity.isFood(stack);
            }
        });
    }

    @Override
    protected int getEntitySlotCount() {
        return 5;
    }

    @Override
    protected boolean moveToEntitySlots(ItemStack currentItem, ItemStack slotStack) {
        if (currentItem.is(ErsItems.CRISTATODROMEUS_BRACHYPTERUS_SADDLE.get())) {
            return this.moveItemStackTo(slotStack, ENTITY_SLOT_START, ENTITY_SLOT_START + 1, false);
        } else if (isEquipment(currentItem)) {
            return this.moveItemStackTo(slotStack, ENTITY_SLOT_START + 1, ENTITY_SLOT_START + 3, false);
        } else if (entity.isFood(currentItem)) {
            return this.moveItemStackTo(slotStack, ENTITY_SLOT_START + 3, ENTITY_SLOT_END, false);
        }
        return false;
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            if (pIndex >= ENTITY_SLOT_START && pIndex < ENTITY_SLOT_END) {
                if (!this.moveItemStackTo(slotStack, PLAYER_INV_START, PLAYER_HOTBAR_END, true)) {
                    return ItemStack.EMPTY;
                }
            } else if (!moveToEntitySlots(slotStack.copy(), slotStack)) {
                if (pIndex < PLAYER_INV_END) {
                    if (!this.moveItemStackTo(slotStack, PLAYER_HOTBAR_START, PLAYER_HOTBAR_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (pIndex < PLAYER_HOTBAR_END) {
                    if (!this.moveItemStackTo(slotStack, PLAYER_INV_START, PLAYER_INV_END, false)) {
                        return ItemStack.EMPTY;
                    }
                } else {
                    return ItemStack.EMPTY;
                }
            }

            if (slotStack.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }

            if (slotStack.getCount() == itemstack.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(pPlayer, slotStack);
        }

        return itemstack;
    }

    private boolean isEquipment(ItemStack stack) {
        return (stack.getItem() instanceof MountEquipment equipment && equipment.canEquip(entity))
                || stack.is(Items.TOTEM_OF_UNDYING)
                || stack.is(Items.TURTLE_HELMET)
                || stack.is(Items.ENCHANTED_GOLDEN_APPLE)
                || stack.is(Items.SHIELD);
    }
}
