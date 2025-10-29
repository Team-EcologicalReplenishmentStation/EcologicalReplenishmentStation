package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.invertory;


import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.init.ErsContainers;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.item.equipment.MountEquipment;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.NotNull;

public class DentisaurusLongirostrisContainerMenu extends AbstractContainerMenu {
    public DentisaurusLongirostrisEntity sotek;
    int x_offset = 8; 
    // 图片偏移
    int y_offset = 84; 
    public DentisaurusLongirostrisContainerMenu(int pContainerId, Inventory pPlayerInventory, DentisaurusLongirostrisEntity sotek) {
        super(ErsContainers.SWAMP_DRAGON_CONTAINER.get(), pContainerId);
        this.sotek = sotek;
        addPlayerSlots(pPlayerInventory);
        addSlots(sotek);
    }
    private void addSlots(DentisaurusLongirostrisEntity sotek)
    {
        Container container = sotek.getInventory();
            // 第0个槽位只能放置鞍具
        this.addSlot(new Slot(container, 0, 8, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.SWAMP_DRAGON_SADDLE.get());
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        if(sotek.isElite()){
            // 第4个槽位只能放置黄金马凯
            this.addSlot(new Slot(container, 4, 8, 54) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {return stack.is(Items.GOLDEN_HORSE_ARMOR);}
                @Override
                public int getMaxStackSize() {return 1;}
            });
        }

        // 第1、2、3槽位只能放置鱼类物品
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
        if(sotek.isElite()) {
            this.addSlot(new Slot(container, 3, 98 + 36, 39) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(ErsItems.PISCIVORES_FEED.get());
                }
            });
        }
        //第5、6、7槽位
        this.addSlot(new Slot(container, 5, 98, 39-18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        this.addSlot(new Slot(container, 6, 98+18, 39-18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        if(sotek.isElite()) {
            this.addSlot(new Slot(container, 7, 98 + 36, 39 - 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return isEquipment(stack);
                }
            });
        }
    }

    private boolean isEquipment(ItemStack stack) {
        return (stack.getItem() instanceof MountEquipment equipment && equipment.canEquip(sotek)) || stack.is(Items.TOTEM_OF_UNDYING) || stack.is(Items.TURTLE_HELMET) || stack.is(Items.ENCHANTED_GOLDEN_APPLE) || stack.is(Items.SHIELD);
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
            
            // 定义各个槽位范围
            int sotekSlotCount = 5; // Sotek物品栏有5个槽位
            int playerInvEnd = sotekSlotCount + 27; // 玩家物品栏结束位置
            int playerHotbarEnd = playerInvEnd + 9; // 玩家快捷栏结束位置
            
            // 如果点击的是Sotek物品栏的槽位
            if (pIndex < sotekSlotCount) {
                // 尝试放入玩家物品栏或快捷栏
                if (!this.moveItemStackTo(slotStack, sotekSlotCount, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } 
            // 如果点击的是玩家物品栏或快捷栏
            else {
                // 获取当前的物品
                ItemStack currentItem = slotStack.copy();
                
                // 如果是鞍具，尝试放入鞍具槽
                if (currentItem.is(ErsItems.SWAMP_DRAGON_SADDLE.get())) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                //如果是马铠，尝试放入铠甲槽
                else if (currentItem.is(Items.GOLDEN_HORSE_ARMOR)) {
                    if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 如果是鱼类，尝试放入鱼类槽
                else if (currentItem.is(net.minecraft.tags.ItemTags.FISHES)) {
                    if (!this.moveItemStackTo(slotStack, 1, 4, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                // 如果点击的是玩家物品栏，尝试放入快捷栏
                else if (pIndex < playerInvEnd) {
                    if (!this.moveItemStackTo(slotStack, playerInvEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                } 
                // 如果点击的是快捷栏，尝试放入玩家物品栏
                else if (pIndex < playerHotbarEnd && !this.moveItemStackTo(slotStack, sotekSlotCount, playerInvEnd, false)) {
                    return ItemStack.EMPTY;
                }
            }
            
            // 处理完成后更新槽位
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
