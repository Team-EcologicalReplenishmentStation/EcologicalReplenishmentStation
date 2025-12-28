package cn.aurorian.oasis.data.loot;

import cn.aurorian.oasis.init.OasisBlocks;
import net.minecraft.data.loot.packs.VanillaBlockLoot;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class OasisBlockLoot extends VanillaBlockLoot {
    @Override
    protected void generate() {
        this.add(OasisBlocks.DUROVELA_SPECIMEN.get(), block -> this.createSingleItemTable(OasisBlocks.DUROVELA_SPECIMEN.get()));
        this.add(OasisBlocks.CLYDEROTUNDA_SPECIMEN.get(), block -> this.createSingleItemTable(OasisBlocks.CLYDEROTUNDA_SPECIMEN.get()));
    }

    @Override
    protected @NotNull Iterable<Block> getKnownBlocks() {
        return OasisBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
