package cn.aurorian.ers.entity.creatures.dentisauruslongirostris.inventory;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class DentisaurusLongirostrisMenuProvider implements MenuProvider {
    private final DentisaurusLongirostrisEntity entity;

    public DentisaurusLongirostrisMenuProvider(DentisaurusLongirostrisEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return entity.getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(
            int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new DentisaurusLongirostrisContainerMenu(pContainerId, pPlayerInventory, entity);
    }
}
