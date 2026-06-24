package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.inventory;

import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.jetbrains.annotations.NotNull;

public class AquicornisDinosauriformisMenuProvider implements MenuProvider {
    private final AquicornisDinosauriformisEntity entity;

    public AquicornisDinosauriformisMenuProvider(AquicornisDinosauriformisEntity entity) {
        this.entity = entity;
    }

    @Override
    public @NotNull Component getDisplayName() {
        return entity.getDisplayName();
    }

    @Override
    public AbstractContainerMenu createMenu(
            int pContainerId, @NotNull Inventory pPlayerInventory, @NotNull Player pPlayer) {
        return new AquicornisDinosauriformisContainerMenu(pContainerId, pPlayerInventory, entity);
    }
}
