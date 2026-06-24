package cn.aurorian.oasis.entity.tubunasusdurovela.inventory;

import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class TubunasusDurovelaMenuProvider implements MenuProvider {
    private final TubunasusDurovelaEntity entity;

    public TubunasusDurovelaMenuProvider(TubunasusDurovelaEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return entity.getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(
            int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new TubunasusDurovelaContainerMenu(pContainerId, pPlayerInventory, entity);
    }
}
