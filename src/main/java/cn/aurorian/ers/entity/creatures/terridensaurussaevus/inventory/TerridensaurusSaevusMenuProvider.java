package cn.aurorian.ers.entity.creatures.terridensaurussaevus.inventory;

import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class TerridensaurusSaevusMenuProvider implements MenuProvider {
    private final TerridensaurusSaevusEntity entity;

    public TerridensaurusSaevusMenuProvider(TerridensaurusSaevusEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return entity.getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(
            int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new TerridensaurusSaevusContainerMenu(pContainerId, pPlayerInventory, entity);
    }
}
