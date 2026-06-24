package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.ers.init.ErsItems;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.FrameType;
import net.minecraft.advancements.critereon.EnterBlockTrigger;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.TameAnimalTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.ForgeAdvancementProvider;
import org.jetbrains.annotations.NotNull;

public class ErsAdvancementProvider extends ForgeAdvancementProvider {

    private static final ResourceLocation BACKGROUND_LOCATION =
            EcologicalReplenishmentStation.prefix("textures/block/soul_cube_side.png");

    public ErsAdvancementProvider(
            PackOutput output,
            CompletableFuture<HolderLookup.Provider> registries,
            ExistingFileHelper existingFileHelper) {
        super(output, registries, existingFileHelper, List.of(new ErsAdvancements()));
    }

    private static class ErsAdvancements implements ForgeAdvancementProvider.AdvancementGenerator {
        @Override
        public void generate(
                HolderLookup.@NotNull Provider provider,
                @NotNull Consumer<Advancement> consumer,
                @NotNull ExistingFileHelper existingFileHelper) {
            Advancement enterWorld = createAdvancement(
                            "enter_world",
                            null,
                            ErsItems.SOUL_CUBE.get(),
                            BACKGROUND_LOCATION,
                            FrameType.TASK,
                            true,
                            true,
                            false)
                    .addCriterion("entered_world", EnterBlockTrigger.TriggerInstance.entersBlock(Blocks.AIR))
                    .save(consumer, EcologicalReplenishmentStation.MODID + ":enter_world");

            Advancement swamp_dragon_egg = createAdvancement(
                            "swamp_dragon_egg",
                            enterWorld,
                            ErsItems.SWAMP_DRAGON_EGG.get(),
                            FrameType.TASK,
                            true,
                            true,
                            true)
                    .addCriterion(
                            "has_swamp_dragon_egg",
                            net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(
                                    ErsItems.SWAMP_DRAGON_EGG.get()))
                    .save(consumer, EcologicalReplenishmentStation.MODID + ":swamp_dragon_egg");
            createAdvancement(
                            "tame_swamp_dragon",
                            swamp_dragon_egg,
                            ErsItems.SWAMP_DRAGON_SADDLE.get(),
                            FrameType.TASK,
                            true,
                            true,
                            true)
                    .addCriterion(
                            "tamed_swamp_dragon",
                            TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity()
                                    .of(ErsEntities.DENTISAURUS_LONGIROSTRIS.get())
                                    .build()))
                    .save(consumer, EcologicalReplenishmentStation.MODID + ":tame_swamp_dragon");

            Advancement saevus_egg = createAdvancement(
                            "saevus_egg", enterWorld, ErsItems.SAEVUS_EGG.get(), FrameType.TASK, true, true, true)
                    .addCriterion(
                            "has_saevus_egg",
                            net.minecraft.advancements.critereon.InventoryChangeTrigger.TriggerInstance.hasItems(
                                    ErsItems.SAEVUS_EGG.get()))
                    .save(consumer, EcologicalReplenishmentStation.MODID + ":saevus_egg");
            createAdvancement("tame_saevus", saevus_egg, ErsItems.SAEVUS_SADDLE.get(), FrameType.TASK, true, true, true)
                    .addCriterion(
                            "tamed_saevus",
                            TameAnimalTrigger.TriggerInstance.tamedAnimal(EntityPredicate.Builder.entity()
                                    .of(ErsEntities.TERRIDENSAURUS_SAEVUS.get())
                                    .build()))
                    .save(consumer, EcologicalReplenishmentStation.MODID + ":tame_saevus");
        }
    }

    private static Advancement.Builder createAdvancement(
            String name,
            Advancement parent,
            ItemLike icon,
            ResourceLocation backGround,
            FrameType frame,
            boolean showToast,
            boolean announceToChat,
            boolean hidden) {
        return Advancement.Builder.advancement()
                .parent(parent)
                .display(
                        icon,
                        Component.translatable(
                                "advancements." + EcologicalReplenishmentStation.MODID + "." + name + ".title"),
                        Component.translatable(
                                "advancements." + EcologicalReplenishmentStation.MODID + "." + name + ".desc"),
                        backGround,
                        frame,
                        showToast,
                        announceToChat,
                        hidden);
    }

    private static Advancement.Builder createAdvancement(
            String name,
            Advancement parent,
            ItemLike icon,
            FrameType frame,
            boolean showToast,
            boolean announceToChat,
            boolean hidden) {
        return createAdvancement(name, parent, icon, null, frame, showToast, announceToChat, hidden);
    }
}
