package cn.aurorian.ers.mixin;

import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsTagKeys;
import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.StackedContents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.ShapelessRecipe;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = ShapelessRecipe.class)
public abstract class MixinShapelessRecipe {
    @Shadow
    @Final
    NonNullList<Ingredient> ingredients;

    @Shadow
    @Final
    ItemStack result;

    @Redirect(
            method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lnet/minecraft/world/entity/player/StackedContents;accountStack(Lnet/minecraft/world/item/ItemStack;I)V"))
    public void matches(StackedContents instance, ItemStack itemStack, int pAmount) {
        if (result.is(ErsItems.FISH_FILLET.get())) {
            boolean isKnownFish = itemStack.is(ErsTagKeys.KNOWN_FISH);
            boolean recipeAllowsKnownFish = ingredients.stream().anyMatch(MixinShapelessRecipe::isKnownFishOnly);
            if (!isKnownFish || recipeAllowsKnownFish) {
                instance.accountStack(itemStack, 1);
            }
            return;
        }
        instance.accountStack(itemStack, 1);
    }

    private static boolean isKnownFishOnly(Ingredient ingredient) {
        ItemStack[] items = ingredient.getItems();
        if (items.length == 0) {
            return false;
        }
        for (ItemStack item : items) {
            if (!item.is(ErsTagKeys.KNOWN_FISH)) {
                return false;
            }
        }
        return true;
    }
}
