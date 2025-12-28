package cn.aurorian.ers.entity.creatures.terridensaurussaevus.invertory;

import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
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

public class TerridensaurusSaevusContainerMenu extends AbstractContainerMenu {
    public TerridensaurusSaevusEntity entity;
    int x_offset = 8;
    int y_offset = 84; 
    public TerridensaurusSaevusContainerMenu(int pContainerId, Inventory pPlayerInventory, TerridensaurusSaevusEntity entity) {
        super(ErsContainers.TERRIDENS_SAEVUS_CONTAINER.get(), pContainerId);
        this.entity = entity;
        addPlayerSlots(pPlayerInventory);
        addSlots(entity);
    }
    private void addSlots(TerridensaurusSaevusEntity entity)
    {
        Container container = entity.getInventory();
        this.addSlot(new Slot(container, 0, 8, 18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.SAEVUS_SADDLE.get());
            }
            @Override
            public int getMaxStackSize() {
                return 1;
            }
        });

        this.addSlot(new Slot(container, 1, 98, 39-18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });

        this.addSlot(new Slot(container, 2, 98 + 18, 39-18) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return isEquipment(stack);
            }
        });
        if(entity.isElite()) {
            this.addSlot(new Slot(container, 3, 98 + 36, 39 - 18) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return isEquipment(stack);
                }
            });
            this.addSlot(new Slot(container, 4, 8, 54) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {return stack.is(Items.IRON_HORSE_ARMOR);}
                @Override
                public int getMaxStackSize() {return 1;}
            });
        }
        this.addSlot(new Slot(container, 5, 98, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.CARNIVORE_FEED.get());
            }
        });
        this.addSlot(new Slot(container, 6, 98 + 18, 39) {
            @Override
            public boolean mayPlace(@NotNull ItemStack stack) {
                return stack.is(ErsItems.CARNIVORE_FEED.get());
            }
        });
        if(entity.isElite()) {
            this.addSlot(new Slot(container, 7, 98 + 36, 39) {
                @Override
                public boolean mayPlace(@NotNull ItemStack stack) {
                    return stack.is(ErsItems.CARNIVORE_FEED.get());
                }
            });
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

            int entitySlotCount = this.entity.isElite() ? 8 : 5;
            int playerInvEnd = entitySlotCount + 27;
            int playerHotbarEnd = playerInvEnd + 9;

            if (pIndex < entitySlotCount) {
                if (!this.moveItemStackTo(slotStack, entitySlotCount, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            }
            else {
                ItemStack currentItem = slotStack.copy();

                if (currentItem.is(ErsItems.SAEVUS_SADDLE.get())) {
                    if (!this.moveItemStackTo(slotStack, 0, 1, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (isEquipment(currentItem)) {
                    if (!this.moveItemStackTo(slotStack, 1, this.entity.isElite() ? 3 : 2, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (currentItem.is(Items.IRON_HORSE_ARMOR) && this.entity.isElite()) {
                    if (!this.moveItemStackTo(slotStack, 4, 5, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (currentItem.is(ErsItems.CARNIVORE_FEED.get())) {
                    if (!this.moveItemStackTo(slotStack, this.entity.isElite() ? 5 : 3, this.entity.isElite() ? 7 :4 , false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (pIndex < playerInvEnd) {
                    if (!this.moveItemStackTo(slotStack, playerInvEnd, playerHotbarEnd, false)) {
                        return ItemStack.EMPTY;
                    }
                }
                else if (pIndex < playerHotbarEnd && !this.moveItemStackTo(slotStack, entitySlotCount, playerInvEnd, false)) {
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
