package cn.aurorian.oasis.entity.tubunasusdurovela.invertory;


import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import cn.aurorian.oasis.init.OasisContainers;
import cn.aurorian.oasis.init.OasisItems;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class TubunasusDurovelaContainerMenu extends AbstractContainerMenu {
    public TubunasusDurovelaEntity entity;
    int x_offset = 8; 
    // 图片偏移
    int y_offset = 84; 
    public TubunasusDurovelaContainerMenu(int pContainerId, Inventory pPlayerInventory, TubunasusDurovelaEntity entity) {
        super(OasisContainers.TUBUNASUS_CONTAINER.get(), pContainerId);
        this.entity = entity;
        addPlayerSlots(pPlayerInventory);
        addSlots(entity);
    }
    private void addSlots(TubunasusDurovelaEntity entity)
    {
        Container container = entity.getInventory();
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
        this.addSlot(new Slot(container, 1, 98, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.PISCIVORES_FEED.get());
            }
        });
        
        this.addSlot(new Slot(container, 2, 98+18, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.PISCIVORES_FEED.get());
            }
        });
        this.addSlot(new Slot(container, 3, 98 + 36, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(ErsItems.PISCIVORES_FEED.get());
                }
        });
        this.addSlot(new Slot(container, 4, 98, 39-18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        this.addSlot(new Slot(container, 5, 98+18, 39-18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        this.addSlot(new Slot(container, 6, 98 + 36, 39 - 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                    return isEquipment(stack);
                }
        });
    }

    private boolean isEquipment(ItemStack stack) {
        return (stack.getItem() instanceof MountEquipment equipment && equipment.canEquip(entity)) || stack.is(Items.TOTEM_OF_UNDYING) || stack.is(Items.ENCHANTED_GOLDEN_APPLE) || stack.is(Items.SHIELD);
    }
        
    
    private void addPlayerSlots(Inventory playerInventory)
    {
        // 添加玩家背包槽位
        for (int row = 0; row < 3; ++row) {
            // 添加玩家背包槽位
            for (int col = 0; col < 9; ++col) {
                // 每个物品槽宽度为18
                int x = x_offset + col * 18;  
                // 每个物品槽高度为18
                int y = y_offset + row * 18;  
                // 添加玩家背包槽位
                this.addSlot(new Slot(playerInventory, col + row * 9 + 9, x, y));
            }
        }
        // 快捷栏位于玩家物品栏的下方
        for (int col = 0; col < 9; ++col) {
            // 快捷栏的 X 坐标
            int x = x_offset + col * 18;  
            // 快捷栏距离物品栏的 Y 偏移通常是58
            int y = y_offset + 58;  
            // 添加快捷栏槽位
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

            int sotekSlotCount = 5;
            int playerInvEnd = sotekSlotCount + 27;
            int playerHotbarEnd = playerInvEnd + 9;

            if (pIndex < sotekSlotCount) {
                if (!this.moveItemStackTo(slotStack, sotekSlotCount, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                ItemStack currentItem = slotStack.copy();
                
                // 如果是鞍具，尝试放入鞍具槽
                if (currentItem.is(OasisItems.TUBUNASUS_SADDLE.get())) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 如果是鱼类，尝试放入鱼类槽
                else if (currentItem.is(net.minecraft.tags.ItemTags.FISHES)) {
                    if (!this.moveItemStackTo(slotStack, 1, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (pIndex < playerInvEnd) {
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
