package cn.aurorian.oasis.init;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class OasisPotionRecipe {
    public static void registerRecipes() {
        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.AWKWARD)),
                Ingredient.of(OasisItems.EMBRYO.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), OasisPotion.EMBRYO_HEALING.get()));
    }
}
