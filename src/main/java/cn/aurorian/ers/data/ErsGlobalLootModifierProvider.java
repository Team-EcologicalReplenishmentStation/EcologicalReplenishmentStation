package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.loot.ArchaeologyLootModifier;
import java.util.List;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.data.GlobalLootModifierProvider;
import net.minecraftforge.common.loot.LootTableIdCondition;

public class ErsGlobalLootModifierProvider extends GlobalLootModifierProvider {

    public ErsGlobalLootModifierProvider(PackOutput output) {
        super(output, EcologicalReplenishmentStation.MODID);
    }

    @Override
    protected void start() {
        List<ItemStack> eggAdditions = List.of(
                new ItemStack(ErsItems.SWAMP_DRAGON_EGG.get()),
                new ItemStack(ErsItems.SAEVUS_EGG.get()),
                new ItemStack(ErsItems.DINOSAURIFORMIS_EGG.get()),
                new ItemStack(ErsItems.ANTIQUUS_EGG.get()));

        List<ItemStack> oceanEggAdditions = List.of(
                new ItemStack(ErsItems.SWAMP_DRAGON_EGG.get()), new ItemStack(ErsItems.DINOSAURIFORMIS_EGG.get()));

        add(
                "archaeology_desert_pyramid",
                new ArchaeologyLootModifier(
                        new LootItemCondition[] {
                            LootTableIdCondition.builder(
                                            ResourceLocation.withDefaultNamespace("archaeology/desert_pyramid"))
                                    .build()
                        },
                        eggAdditions,
                        2.0f / 98.0f));

        add(
                "archaeology_desert_well",
                new ArchaeologyLootModifier(
                        new LootItemCondition[] {
                            LootTableIdCondition.builder(
                                            ResourceLocation.withDefaultNamespace("archaeology/desert_well"))
                                    .build()
                        },
                        eggAdditions,
                        2.0f / 87.0f));

        add(
                "archaeology_ocean_ruin_warm",
                new ArchaeologyLootModifier(
                        new LootItemCondition[] {
                            LootTableIdCondition.builder(
                                            ResourceLocation.withDefaultNamespace("archaeology/ocean_ruin_warm"))
                                    .build()
                        },
                        oceanEggAdditions,
                        2.0f / 92.0f));
    }
}
