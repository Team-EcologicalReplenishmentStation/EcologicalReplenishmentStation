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
    @Shadow @Final
    NonNullList<Ingredient> ingredients;
    @Shadow @Final
    ItemStack result;

    @Redirect(method = "matches(Lnet/minecraft/world/inventory/CraftingContainer;Lnet/minecraft/world/level/Level;)Z",at = @At(value = "INVOKE", target = "Lnet/minecraft/world/entity/player/StackedContents;accountStack(Lnet/minecraft/world/item/ItemStack;I)V"))
    public void matches(StackedContents instance, ItemStack itemStack, int pAmount) {
        if(result.is(ErsItems.FISH_FILLET.get())){
            if(!itemStack.is(ErsTagKeys.KNOWN_FISH) || ingredients.get(0).getItems()[0].is(ErsTagKeys.KNOWN_FISH))
                instance.accountStack(itemStack, 1);
        }else {
            instance.accountStack(itemStack, 1);
        }
    }
}
