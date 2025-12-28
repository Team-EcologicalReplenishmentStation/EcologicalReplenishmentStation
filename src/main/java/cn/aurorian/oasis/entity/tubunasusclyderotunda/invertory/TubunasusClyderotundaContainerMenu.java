package cn.aurorian.oasis.entity.tubunasusclyderotunda.invertory;


import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import cn.aurorian.oasis.init.OasisContainers;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class TubunasusClyderotundaContainerMenu extends AbstractContainerMenu {
    public TubunasusClyderotundaEntity entity;
    int x_offset = 8; 
    // 图片偏移
    int y_offset = 84; 
    public TubunasusClyderotundaContainerMenu(int pContainerId, Inventory pPlayerInventory, TubunasusClyderotundaEntity entity) {
        super(OasisContainers.CLYDEROTUNDA_CONTAINER.get(), pContainerId);
        this.entity = entity;
        addPlayerSlots(pPlayerInventory);
        addSlots(entity);
    }
    private void addSlots(TubunasusClyderotundaEntity entity)
    {
        Container container = entity.getInventory();
        this.addSlot(new Slot(container, 1, 82, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        this.addSlot(new Slot(container, 2,  82+ 18, 18) {
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

        for (int i = 4; i <= 13; i++) {
            this.addSlot(new Slot(container, i, 82 + (i - 4) % 5 * 18, 36 + (i - 4) / 5 * 18));
        }
    }

    private boolean isEquipment(ItemStack stack) {
        return (stack.getItem() instanceof MountEquipment equipment && equipment.canEquip(entity)) || stack.is(Items.TOTEM_OF_UNDYING) || stack.is(Items.ENCHANTED_GOLDEN_APPLE) || stack.is(Items.SHIELD);
    }


    private void addPlayerSlots(Inventory playerInventory)
    {
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                int x = x_offset + col * 18;
                int y = y_offset + row * 18;
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }

        for (int col = 0; col < 9; ++col) {
            int x = x_offset + col * 18;
            int y = y_offset + 58;
            this.addSlot(new Slot(playerInventory, col, x, y));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);
        
        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            int sotekSlotCount = 19;
            int playerInvEnd = sotekSlotCount + 27;
            int playerHotbarEnd = playerInvEnd + 9;

            if (pIndex < sotekSlotCount) {
                if (!this.moveItemStackTo(slotStack, sotekSlotCount, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                if (pIndex < playerInvEnd) {
                    if (!this.moveItemStackTo(slotStack, playerInvEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (pIndex < playerHotbarEnd && !this.moveItemStackTo(slotStack, sotekSlotCount, playerInvEnd, false)) {
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

    @Override
    public boolean stillValid(@NotNull Player pPlayer) {
        return true;
    }
}
