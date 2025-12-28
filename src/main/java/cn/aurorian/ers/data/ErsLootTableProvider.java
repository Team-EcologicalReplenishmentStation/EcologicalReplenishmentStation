package cn.aurorian.ers.data;

import cn.aurorian.ers.data.loot.ErsBlockLoot;
import cn.aurorian.ers.data.loot.ErsEntityLoot;
import cn.aurorian.oasis.data.loot.OasisBlockLoot;
import cn.aurorian.oasis.data.loot.OasisEntityLoot;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;

import java.util.List;
import java.util.Set;

public class ErsLootTableProvider extends LootTableProvider {
    public ErsLootTableProvider(PackOutput pOutput) {
        super(pOutput, Set.of(), List.of(
                new SubProviderEntry(ErsBlockLoot::new, LootContextParamSets.BLOCK),
                new SubProviderEntry(ErsEntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(OasisEntityLoot::new, LootContextParamSets.ENTITY),
                new SubProviderEntry(OasisBlockLoot::new, LootContextParamSets.BLOCK)));
    }

}
