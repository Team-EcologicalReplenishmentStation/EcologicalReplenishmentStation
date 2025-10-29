package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsTagKeys;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class ErsItemTagProvider extends ItemTagsProvider{

    public ErsItemTagProvider(PackOutput p_275343_, CompletableFuture<HolderLookup.Provider> p_275729_, CompletableFuture<TagLookup<Block>> p_275322_, @Nullable ExistingFileHelper existingFileHelper) {
        super(p_275343_, p_275729_, p_275322_, EcologicalReplenishmentStation.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.@NotNull Provider provider) {
        this.tag(ItemTags.FISHES)
                .add(ErsItems.PERCH.get())
                .add(ErsItems.CHLAMYDOSELACHOIDES.get())
                .add(ErsItems.SUCHOMIMUS.get());

        this.tag(ErsTagKeys.KNOWN_FISH)
                .add(ErsItems.PERCH.get())
                .add(ErsItems.CHLAMYDOSELACHOIDES.get())
                .add(ErsItems.SUCHOMIMUS.get());
    }
}
