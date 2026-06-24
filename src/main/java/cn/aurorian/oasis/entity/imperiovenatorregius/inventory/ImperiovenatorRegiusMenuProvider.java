package cn.aurorian.oasis.entity.imperiovenatorregius.inventory;

import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class ImperiovenatorRegiusMenuProvider implements MenuProvider {
    private final ImperiovenatorRegiusEntity entity;

    public ImperiovenatorRegiusMenuProvider(ImperiovenatorRegiusEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return entity.getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(
            int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new ImperiovenatorRegiusContainerMenu(pContainerId, pPlayerInventory, entity);
    }
}
