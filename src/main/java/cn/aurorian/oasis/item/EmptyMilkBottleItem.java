package cn.aurorian.oasis.item;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.HasGender;
import cn.aurorian.oasis.init.OasisItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import org.jetbrains.annotations.NotNull;

public class EmptyMilkBottleItem extends Item {
    public EmptyMilkBottleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
            @NotNull ItemStack itemStack,
            @NotNull Player player,
            @NotNull LivingEntity target,
            @NotNull InteractionHand usedHand) {
        if (!canMilk(target)) {
            return InteractionResult.PASS;
        }

        if (!target.level().isClientSide) {
            ItemStack milk = new ItemStack(OasisItems.CUDMILK.get());
            ItemStack heldItem = player.getItemInHand(usedHand);

            if (heldItem.getCount() > 1) {
                ItemStack result = ItemUtils.createFilledResult(heldItem, player, milk, false);
                player.setItemInHand(usedHand, result);
            } else {
                player.setItemInHand(usedHand, ItemStack.EMPTY);

                if (!player.getInventory().add(milk)) {
                    player.drop(milk, false);
                }
            }

            target.playSound(SoundEvents.BOTTLE_FILL, 1.0F, 1.0F);
        }

        return InteractionResult.sidedSuccess(target.level().isClientSide);
    }

    private static boolean canMilk(LivingEntity target) {
        boolean isSupported = target instanceof ErsTamableVehicle<?>
                && target instanceof HasGender hasGender
                && !hasGender.getGender();

        if (!isSupported) return false;
        Animal animal = (Animal) target;
        return animal.isInLove();
    }
}
