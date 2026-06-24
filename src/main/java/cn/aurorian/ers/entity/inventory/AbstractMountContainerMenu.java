package cn.aurorian.ers.entity.inventory;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

/**
 * 所有坐骑 ContainerMenu 的抽象基类。
 *
 * <p>子类只需实现：
 *
 * <ul>
 *   <li>{@link #addEntitySlots()} — 添加实体专属槽位
 *   <li>{@link #getEntitySlotCount()} — 返回实体槽总数（用于 quickMoveStack 边界计算）
 *   <li>{@link #moveToEntitySlots(ItemStack, ItemStack)} — 处理 Shift+Click 时将物品移入实体槽的逻辑
 * </ul>
 */
public abstract class AbstractMountContainerMenu<T extends ErsTamableVehicle<?>> extends AbstractContainerMenu {

    public final T entity;
    protected static final int PLAYER_INV_X = 8;
    protected static final int PLAYER_INV_Y = 84;

    protected AbstractMountContainerMenu(MenuType<?> menuType, int containerId, Inventory playerInventory, T entity) {
        super(menuType, containerId);
        this.entity = entity;
        addPlayerSlots(playerInventory);
        addEntitySlots();
    }

    // -------------------------------------------------------
    // 子类必须实现的部分
    // -------------------------------------------------------

    /** 添加实体专属槽位（鞍具、装备、食物等）。在构造时自动调用。 */
    protected abstract void addEntitySlots();

    /** 实体专属槽位总数，用于 quickMoveStack 边界计算。 */
    protected abstract int getEntitySlotCount();

    /**
     * Shift+Click 时，将来自玩家背包的物品移入实体槽的逻辑。 返回 false 表示无法移入任何槽，quickMoveStack 将返回 EMPTY。
     *
     * @param stack 当前点击的物品副本（用于判断类型，不要直接修改）
     * @param slotStack 实际槽内的 ItemStack（传给 moveItemStackTo）
     * @return 是否成功移入至少一个
     */
    protected abstract boolean moveToEntitySlots(ItemStack stack, ItemStack slotStack);

    // -------------------------------------------------------
    // 通用方法
    // -------------------------------------------------------

    /** 添加玩家背包和快捷栏槽位。 */
    private void addPlayerSlots(Inventory playerInventory) {
        // 背包（3×9）
        for (int row = 0; row < 3; ++row) {
            for (int col = 0; col < 9; ++col) {
                this.addSlot(
                        new Slot(playerInventory, col + row * 9 + 9, PLAYER_INV_X + col * 18, PLAYER_INV_Y + row * 18));
            }
        }
        // 快捷栏
        for (int col = 0; col < 9; ++col) {
            this.addSlot(new Slot(playerInventory, col, PLAYER_INV_X + col * 18, PLAYER_INV_Y + 58));
        }
    }

    @Override
    public @NotNull ItemStack quickMoveStack(@NotNull Player pPlayer, int pIndex) {
        ItemStack itemstack = ItemStack.EMPTY;
        Slot slot = this.slots.get(pIndex);

        if (slot.hasItem()) {
            ItemStack slotStack = slot.getItem();
            itemstack = slotStack.copy();

            int entitySlotCount = getEntitySlotCount();
            int playerInvEnd = entitySlotCount + 27;
            int playerHotbarEnd = playerInvEnd + 9;

            if (pIndex < entitySlotCount) {
                // 实体槽 → 玩家背包/快捷栏
                if (!this.moveItemStackTo(slotStack, entitySlotCount, playerHotbarEnd, true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                // 玩家背包/快捷栏 → 实体槽 / 背包内互移
                ItemStack currentItem = slotStack.copy();
                if (!moveToEntitySlots(currentItem, slotStack)) {
                    if (pIndex < playerInvEnd) {
                        if (!this.moveItemStackTo(slotStack, playerInvEnd, playerHotbarEnd, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else if (pIndex < playerHotbarEnd) {
                        if (!this.moveItemStackTo(slotStack, entitySlotCount, playerInvEnd, false)) {
                            return ItemStack.EMPTY;
                        }
                    } else {
                        return ItemStack.EMPTY;
                    }
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
