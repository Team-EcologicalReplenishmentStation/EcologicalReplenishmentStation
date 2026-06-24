package cn.aurorian.ers.data;

import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.ers.init.ErsTagKeys;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.EntityTypeTagsProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ErsEntityTypeTagsProvider extends EntityTypeTagsProvider {
    public ErsEntityTypeTagsProvider(
            PackOutput pOutput,
            CompletableFuture<HolderLookup.Provider> pProvider,
            String modId,
            @Nullable ExistingFileHelper existingFileHelper) {
        super(pOutput, pProvider, modId, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider pProvider) {
        this.tag(ErsTagKeys.PISCIVOROUS).add(ErsEntities.DENTISAURUS_LONGIROSTRIS.get());
        this.tag(ErsTagKeys.ARMORED)
                .add(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get());
        this.tag(ErsTagKeys.SCAVENGER)
                .add(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get());
        this.tag(ErsTagKeys.PREDATOR)
                .add(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), ErsEntities.TERRIDENSAURUS_SAEVUS.get());
    }
}
