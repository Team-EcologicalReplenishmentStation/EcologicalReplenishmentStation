package cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.inventory;

import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class CristatodromeusBrachypterusMenuProvider implements MenuProvider {
    private final CristatodromeusBrachypterusEntity entity;

    public CristatodromeusBrachypterusMenuProvider(CristatodromeusBrachypterusEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return entity.getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(
            int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new CristatodromeusBrachypterusContainerMenu(pContainerId, pPlayerInventory, entity);
    }
}
