package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsMobEffects;
import cn.aurorian.ers.init.ErsTagKeys;
import cn.aurorian.oasis.init.OasisItems;
import java.util.function.Consumer;
import net.minecraft.advancements.critereon.EffectsChangedTrigger;
import net.minecraft.advancements.critereon.MobEffectsPredicate;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.crafting.ConditionalRecipe;
import net.minecraftforge.common.crafting.conditions.ModLoadedCondition;
import org.jetbrains.annotations.NotNull;

public class ErsRecipeProvider extends RecipeProvider {

    public ErsRecipeProvider(PackOutput output) {
        super(output);
    }

    @Override
    protected void buildRecipes(@NotNull Consumer<FinishedRecipe> pWriter) {
        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ErsItems.SWAMP_DRAGON_SADDLE.get())
                .requires(Items.SADDLE)
                .requires(Items.FISHING_ROD)
                .unlockedBy("in_inventory", has(Items.SADDLE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ErsItems.SAEVUS_SADDLE.get())
                .requires(Items.SADDLE)
                .requires(Items.IRON_SWORD)
                .unlockedBy("in_inventory", has(Items.SADDLE))
                .save(pWriter);
        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ErsItems.DINOSAURIFORMIS_SADDLE.get())
                .pattern("   ")
                .pattern(" AC")
                .pattern(" B ")
                .define('A', Items.SADDLE)
                .define('B', Items.LEATHER)
                .define('C', Items.POTION)
                .unlockedBy("in_inventory", has(Items.SADDLE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ErsItems.ANTIQUUS_SADDLE.get())
                .requires(Items.SADDLE)
                .requires(ErsItems.TOURNIQUET.get())
                .unlockedBy("in_inventory", has(Items.SADDLE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.TOOLS, ErsItems.CRISTATODROMEUS_BRACHYPTERUS_SADDLE.get())
                .requires(Items.SADDLE)
                .requires(Items.CARROT)
                .unlockedBy("in_inventory", has(Items.SADDLE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.GREEN_DYE)
                .requires(ErsItems.CLOVER.get())
                .unlockedBy("has_clover", has(ErsItems.CLOVER.get()))
                .save(pWriter, "ers:green_dye_from_clover");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.DRIED_FISH.get())
                .requires(ErsItems.COOKED_FISH_FILLET.get(), 9)
                .unlockedBy("has_cooked_fish_fillet", has(ErsItems.COOKED_FISH_FILLET.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.TOOLS, ErsItems.LARGE_BUCKET.get())
                .pattern("I I")
                .pattern("I I")
                .pattern(" B ")
                .define('I', Items.IRON_BLOCK)
                .define('B', Items.BUCKET)
                .unlockedBy("has_bucket", has(Items.BUCKET))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.SCRATCHING_BOARD.get())
                .pattern("IBI")
                .pattern("IBI")
                .pattern("IBI")
                .define('I', Items.BAMBOO)
                .define('B', Items.LEAD)
                .unlockedBy("has_bamboo", has(Items.BAMBOO))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.BULLY_STICK.get())
                .pattern("IBI")
                .pattern("CCC")
                .pattern("D D")
                .define('I', Items.LEATHER)
                .define('B', Items.RABBIT_HIDE)
                .define('C', Items.BONE_BLOCK)
                .define('D', Items.LEAD)
                .unlockedBy("has_bone_block", has(Items.BONE_BLOCK))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.BAIT_BOX.get())
                .pattern("III")
                .pattern("IBI")
                .pattern("III")
                .define('I', Items.FERMENTED_SPIDER_EYE)
                .define('B', Items.CHEST)
                .unlockedBy("has_chest", has(Items.CHEST))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.RIDING_GUIDE.get())
                .pattern("ACA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.EXPERIENCE_BOTTLE)
                .define('B', Items.WRITABLE_BOOK)
                .define('C', Items.SADDLE)
                .unlockedBy("has_saddle", has(Items.SADDLE))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 3)
                .requires(Items.SALMON)
                .unlockedBy("has_salmon", has(Items.SALMON))
                .save(pWriter, "ers:fish_fillet_from_salmon");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, Items.BONE_MEAL, 8)
                .requires(ErsItems.DRAGON_BONE.get())
                .unlockedBy("has_dragon_bone", has(ErsItems.DRAGON_BONE.get()))
                .save(pWriter, "ers:bone_meal_from_dragon_bone");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 2)
                .requires(Items.COD)
                .unlockedBy("has_cod", has(Items.COD))
                .save(pWriter, "ers:fish_fillet_from_cod");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 5)
                .requires(ErsItems.PERCH.get())
                .unlockedBy("has_perch", has(ErsItems.PERCH.get()))
                .save(pWriter, "ers:fish_fillet_from_perch");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 10)
                .requires(ErsItems.CHLAMYDOSELACHOIDES.get())
                .unlockedBy("has_acanthodii", has(ErsItems.CHLAMYDOSELACHOIDES.get()))
                .save(pWriter, "ers:fish_fillet_from_acanthodii");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 10)
                .requires(ErsItems.SUCHOMIMUS.get())
                .unlockedBy("has_suchomimus", has(ErsItems.SUCHOMIMUS.get()))
                .save(pWriter, "ers:fish_fillet_from_suchomimus");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 3)
                .requires(ErsItems.SARCOPETERUS.get())
                .unlockedBy("has_sarcopterus", has(ErsItems.SARCOPETERUS.get()))
                .save(pWriter, "ers:fish_fillet_from_sarcopterus");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 3)
                .requires(ErsItems.ACICULABULAR.get())
                .unlockedBy("has_aciculabular", has(ErsItems.ACICULABULAR.get()))
                .save(pWriter, "ers:fish_fillet_from_aciculabular");
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 3)
                .requires(ErsItems.LABIUM.get())
                .unlockedBy("has_labium", has(ErsItems.LABIUM.get()))
                .save(pWriter, "ers:fish_fillet_from_labium");

        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FEED.get())
                .requires(ErsItems.FISH_FILLET.get(), 9)
                .unlockedBy("ers:has_fish_fillet", has(ErsItems.FISH_FILLET.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ErsItems.MEAT_FEED.get())
                .pattern("ABA")
                .pattern("BBB")
                .pattern("ABA")
                .define('A', Items.BONE)
                .define('B', ErsTagKeys.MEAT)
                .unlockedBy("ers:has_meat", has(ErsTagKeys.MEAT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ErsItems.FRUIT_FEED.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.SWEET_BERRIES)
                .define('B', Items.GLOW_BERRIES)
                .define('C', Items.MELON_SLICE)
                .unlockedBy("ers:has_glow_berries", has(Items.GLOW_BERRIES))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ErsItems.WORM_FEED.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.SPIDER_EYE)
                .define('B', Items.HANGING_ROOTS)
                .unlockedBy("ers:has_spider_eye", has(Items.SPIDER_EYE))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ErsItems.HAY_FEED.get())
                .pattern("AA")
                .pattern("AA")
                .define('A', Items.HAY_BLOCK)
                .unlockedBy("ers:has_hay_block", has(Items.HAY_BLOCK))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.FOOD, ErsItems.VEGETABLE_FEED.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.CARROT)
                .define('B', Items.BEETROOT)
                .define('C', Items.PUMPKIN)
                .unlockedBy("ers:has_pumpkin", has(Items.PUMPKIN))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ErsItems.SOUL_CUBE.get())
                .pattern("ABA")
                .pattern("CDC")
                .pattern("ACA")
                .define('A', Items.NETHERITE_INGOT)
                .define('B', Items.TOTEM_OF_UNDYING)
                .define('C', Items.GOLD_INGOT)
                .define('D', Items.SOUL_SAND)
                .unlockedBy("has_totem", has(Items.TOTEM_OF_UNDYING))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.CHLAMYDOSELACHOIDES_TOOTH_SWORD.get())
                .pattern("AA ")
                .pattern("A  ")
                .pattern("C  ")
                .define('A', ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get())
                .define('C', Items.STICK)
                .unlockedBy("has_acanthodii_tooth", has(ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.DRAGON_CLAW_HARPOON.get())
                .pattern(" AA")
                .pattern("CBA")
                .pattern("DC ")
                .define('A', ErsItems.SWAMP_DRAGON_CLAW.get())
                .define('B', Items.TRIDENT)
                .define('C', Items.COPPER_INGOT)
                .define('D', Items.GOLD_INGOT)
                .unlockedBy("has_swamp_dragon_claw", has(ErsItems.SWAMP_DRAGON_CLAW.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.TOURNIQUET.get())
                .pattern(" I ")
                .pattern(" B ")
                .pattern(" I ")
                .define('I', Items.PAPER)
                .define('B', Items.WHEAT)
                .unlockedBy(
                        "bleeding",
                        EffectsChangedTrigger.TriggerInstance.hasEffects(
                                MobEffectsPredicate.effects().and(ErsMobEffects.BLEEDING.get())))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ErsItems.ARTIFICIAL_NEST.get())
                .pattern("ABA")
                .pattern("BCB")
                .pattern("ABA")
                .define('A', Items.WHEAT)
                .define('B', Items.STICK)
                .define('C', Items.HAY_BLOCK)
                .unlockedBy("has_hay_block", has(Items.HAY_BLOCK))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.GILDED_HORN.get())
                .pattern("AAA")
                .pattern("ABA")
                .pattern("AAA")
                .define('A', Items.GOLD_INGOT)
                .define('B', Items.GOAT_HORN)
                .unlockedBy("has_horn", has(Items.GOAT_HORN))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.DRAGON_BONE_FLUTE.get())
                .pattern(" BA")
                .pattern("BA ")
                .pattern("A  ")
                .define('A', ErsItems.DRAGON_BONE.get())
                .define('B', Items.FLINT)
                .unlockedBy("has_dragon_bone", has(ErsItems.DRAGON_BONE.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, ErsItems.SOUL_FLUTE.get())
                .pattern("EBC")
                .pattern("BAB")
                .pattern("CBD")
                .define('A', ErsItems.DRAGON_BONE_FLUTE.get())
                .define('B', Items.END_CRYSTAL)
                .define('C', Items.ECHO_SHARD)
                .define('D', Items.AMETHYST_SHARD)
                .define('E', Items.TOTEM_OF_UNDYING)
                .unlockedBy("has_flute", has(ErsItems.DRAGON_BONE_FLUTE.get()))
                .save(pWriter);

        buildFoodProcessRecipes(
                pWriter, ErsItems.SWAMP_DRAGON_MEAT.get(), ErsItems.COOKED_SWAMP_DRAGON_MEAT.get(), 0.35f);
        buildFoodProcessRecipes(
                pWriter, ErsItems.HORSESHOE_CRAB_MEAT.get(), ErsItems.COOKED_HORSESHOE_CRAB_MEAT.get(), 0.35f);
        buildFoodProcessRecipes(
                pWriter, ErsItems.CHLAMYDOSELACHOIDES.get(), ErsItems.COOKED_CHLAMYDOSELACHOIDES.get(), 0.35f);
        buildFoodProcessRecipes(pWriter, ErsItems.SUCHOMIMUS.get(), ErsItems.COOKED_SUCHOMIMUS.get(), 0.35f);
        buildFoodProcessRecipes(pWriter, ErsItems.FISH_FILLET.get(), ErsItems.COOKED_FISH_FILLET.get(), 0.1f);
        buildFoodProcessRecipes(pWriter, ErsItems.PERCH.get(), ErsItems.COOKED_PERCH.get(), 0.1f);
        buildFoodProcessRecipes(pWriter, ErsItems.SARCOPETERUS.get(), ErsItems.COOKED_SARCOPETERUS.get(), 0.1f);
        buildFoodProcessRecipes(pWriter, ErsItems.SAEVUS_MEAT.get(), ErsItems.COOKED_SAEVUS_MEAT.get(), 0.35f);
        buildFoodProcessRecipes(pWriter, ErsItems.ACICULABULAR.get(), ErsItems.COOKED_ACICULABULAR.get(), 0.35f);
        buildFoodProcessRecipes(pWriter, ErsItems.LABIUM.get(), ErsItems.COOKED_LABIUM.get(), 0.2f);
        buildFoodProcessRecipes(
                pWriter, ErsItems.BENTHOSUCHUS_PLANIDENS.get(), ErsItems.COOKED_BENTHOSUCHUS_PLANIDENS.get(), 0.2f);
        buildFoodProcessRecipes(
                pWriter, ErsItems.PLESIOCHELYS_LONGICOLLIS.get(), ErsItems.COOKED_PLESIOCHELYS_LONGICOLLIS.get(), 0.2f);
        buildFoodProcessRecipes(
                pWriter, ErsItems.DINOSAURIFORMIS_MEAT.get(), ErsItems.COOKED_DINOSAURIFORMIS_MEAT.get(), 0.35f);
        buildFoodProcessRecipes(pWriter, ErsItems.ANTIQUUS_MEAT.get(), ErsItems.COOKED_ANTIQUE_MEAT.get(), 0.35f);

        // Compat
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), 1)
                .requires(ItemTags.FISHES)
                .unlockedBy("has_fishable_fish", has(ItemTags.FISHES))
                .save(pWriter, EcologicalReplenishmentStation.prefix("fish_fillet_from_tag"));

        // Oasis
        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, OasisItems.HORSESHOE.get())
                .pattern(" I ")
                .pattern("I I")
                .pattern("I I")
                .define('I', Items.IRON_INGOT)
                .unlockedBy("has_iron_ingot", has(Items.IRON_INGOT))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.COMBAT, OasisItems.TUBUNASUS_SADDLE.get())
                .pattern("A  ")
                .pattern("BBB")
                .pattern("CDB")
                .define('A', Items.STICK)
                .define('B', OasisItems.LEATHER.get())
                .define('C', Items.LEAD)
                .define('D', Items.IRON_INGOT)
                .unlockedBy("has_leather", has(OasisItems.LEATHER.get()))
                .save(pWriter);

        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, OasisItems.EMPTY_MILK_BOTTLE.get())
                .pattern("LLL")
                .pattern("G G")
                .pattern("GGG")
                .define('L', OasisItems.LEATHER.get())
                .define('G', Items.GLASS)
                .unlockedBy("has_oasis_leather", has(OasisItems.LEATHER.get()))
                .save(pWriter);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.DECORATIONS, OasisItems.DUROVELA_SPECIMEN.get(), 1)
                .requires(OasisItems.BONE.get(), 9)
                .unlockedBy("has_oasis_bone", has(OasisItems.BONE.get()))
                .save(pWriter);

        buildFoodProcessRecipes(pWriter, OasisItems.KIDNEY.get(), OasisItems.COOKED_KIDNEY.get(), 0.6f);
        buildFoodProcessRecipes(pWriter, OasisItems.LUNG.get(), OasisItems.COOKED_LUNG.get(), 0.6f);
        buildFoodProcessRecipes(pWriter, OasisItems.ANNULATUM.get(), OasisItems.COOKED_ANNULATUM.get(), 0.2f);
    }

    public ConditionalRecipe.Builder buildCompatRecipe(ShapelessRecipeBuilder recipe, String path, String modId) {
        return ConditionalRecipe.builder()
                .addCondition(new ModLoadedCondition(modId))
                .addRecipe(consumer -> recipe.save(consumer, EcologicalReplenishmentStation.prefix(path)));
    }

    public void buildFishFilletRecipe(
            @NotNull Consumer<FinishedRecipe> pWriter, ResourceLocation resourceLocation, int count) {
        buildCompatRecipe(
                        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD, ErsItems.FISH_FILLET.get(), count)
                                .requires(BuiltInRegistries.ITEM.get(resourceLocation))
                                .unlockedBy(
                                        getHasName(BuiltInRegistries.ITEM.get(resourceLocation)),
                                        has(BuiltInRegistries.ITEM.get(resourceLocation))),
                        "fish_fillet_from_" + resourceLocation.getPath(),
                        resourceLocation.getNamespace())
                .build(
                        pWriter,
                        EcologicalReplenishmentStation.prefix("fish_fillet_from_" + resourceLocation.getPath()));
    }

    private static void buildFoodProcessRecipes(
            Consumer<FinishedRecipe> recipeOutput, Item input, Item output, float xp) {
        SimpleCookingRecipeBuilder.smelting(Ingredient.of(input), RecipeCategory.FOOD, output, xp, 200)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, EcologicalReplenishmentStation.prefix(getItemName(output) + "_smelting"));
        SimpleCookingRecipeBuilder.smoking(Ingredient.of(input), RecipeCategory.FOOD, output, xp, 100)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, EcologicalReplenishmentStation.prefix(getItemName(output) + "_smoking"));
        SimpleCookingRecipeBuilder.campfireCooking(Ingredient.of(input), RecipeCategory.FOOD, output, xp, 600)
                .unlockedBy(getHasName(input), has(input))
                .save(recipeOutput, EcologicalReplenishmentStation.prefix(getItemName(output) + "_campfire_cooking"));
    }
}
