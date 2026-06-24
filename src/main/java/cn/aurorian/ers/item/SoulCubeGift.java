package cn.aurorian.ers.item;

import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class SoulCubeGift extends Item {
    public SoulCubeGift(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
            @NotNull ItemStack itemStack,
            @NotNull Player player,
            @NotNull LivingEntity target,
            @NotNull InteractionHand pUsedHand) {
        if (target instanceof ErsTamable<?> pet) {
            if (!pet.isTame()) return InteractionResult.PASS;
            if (!pet.isOwnedBy(player)) return InteractionResult.PASS;

            pet.setEnable(true);

            CompoundTag tag = itemStack.getOrCreateTag();
            pet.setDimension(target.level().dimension().toString());
            pet.setRespawnPos(new BlockPos(tag.getInt("X"), tag.getInt("Y"), tag.getInt("Z")));
            itemStack.shrink(1);
            player.level()
                    .playSound(
                            player,
                            target.getOnPos(),
                            SoundEvents.RESPAWN_ANCHOR_SET_SPAWN,
                            SoundSource.NEUTRAL,
                            1.0F,
                            1.0F);
        }

        return InteractionResult.CONSUME;
    }
}
