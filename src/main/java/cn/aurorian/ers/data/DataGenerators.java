package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.data.language.ErsChineseLanguangeProvider;
import cn.aurorian.ers.data.language.ErsEnglishLanguageProvider;
import cn.aurorian.oasis.data.OasisItemModelProvider;
import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.data.event.GatherDataEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class DataGenerators {
    @SubscribeEvent
    public static void gatherData(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput output = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper helper = event.getExistingFileHelper();

        generator.addProvider(event.includeClient(), new ErsItemModelProvider(output, helper));

        generator.addProvider(event.includeServer(), new ErsRecipeProvider(output));
        generator.addProvider(event.includeClient(), new ErsEnglishLanguageProvider(output));
        generator.addProvider(event.includeClient(), new ErsChineseLanguangeProvider(output));
        generator.addProvider(event.includeClient(), new ErsBlockStateProvider(output, helper));
        generator.addProvider(event.includeServer(), new ModDatapackEntries(output, lookupProvider));
        generator.addProvider(event.includeClient(), new ErsSoundDefinitionProvider(output, helper));
        generator.addProvider(event.includeServer(), new ErsLootTableProvider(output));
        generator.addProvider(event.includeServer(), new ErsGlobalLootModifierProvider(output));
        generator.addProvider(event.includeServer(), new ErsAdvancementProvider(output, lookupProvider, helper));

        generator.addProvider(event.includeClient(), new OasisItemModelProvider(output, helper));

        ErsBlockTagProvider blockTagsProvider = new ErsBlockTagProvider(output, lookupProvider, helper);
        generator.addProvider(event.includeServer(), blockTagsProvider);
        generator.addProvider(
                event.includeServer(),
                new ErsItemTagProvider(output, lookupProvider, blockTagsProvider.contentsGetter(), helper));
        ErsEntityTypeTagsProvider entityTypeTagsProvider =
                new ErsEntityTypeTagsProvider(output, lookupProvider, EcologicalReplenishmentStation.MODID, helper);
        generator.addProvider(event.includeServer(), entityTypeTagsProvider);
    }
}
