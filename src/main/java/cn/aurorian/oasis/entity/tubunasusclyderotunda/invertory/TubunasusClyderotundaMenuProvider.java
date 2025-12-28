package cn.aurorian.oasis.entity.tubunasusclyderotunda.invertory;

import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class TubunasusClyderotundaMenuProvider implements MenuProvider {
    private final TubunasusClyderotundaEntity entity;

    public TubunasusClyderotundaMenuProvider(TubunasusClyderotundaEntity entity) {
        this.entity = entity;
    }
    @Override
    public @NotNull Component getDisplayName() {
        return entity.getDisplayName();
    }
    @Override
    public AbstractContainerMenu createMenu(int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new TubunasusClyderotundaContainerMenu(pContainerId,pPlayerInventory, entity);
    }
}
