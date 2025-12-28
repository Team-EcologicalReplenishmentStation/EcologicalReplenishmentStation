package cn.aurorian.ers.item;

import cn.aurorian.ers.init.ErsItems;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class DragonMeat extends Item {
    public DragonMeat(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull ItemStack finishUsingItem(@NotNull ItemStack pStack, @NotNull Level pLevel, @NotNull LivingEntity pLivingEntity) {
        if(pLivingEntity instanceof Player player){
            player.addItem(new ItemStack(ErsItems.DRAGON_BONE.get()));
        }
        return super.finishUsingItem(pStack, pLevel, pLivingEntity);
    }
}
