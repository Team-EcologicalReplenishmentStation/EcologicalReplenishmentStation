package cn.aurorian.ers.item;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class ErsSaddleItem extends Item {
    public ErsSaddleItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
            @NotNull ItemStack pStack,
            @NotNull Player pPlayer,
            @NotNull LivingEntity pTarget,
            @NotNull InteractionHand pHand) {
        if (pTarget instanceof ErsTamableVehicle<?> entity && canEquip(entity)) {
            if (entity.isAlive()) {
                if (!entity.isSaddled() && entity.isSaddleable()) {
                    if (!pPlayer.level().isClientSide) {
                        entity.equipSaddle();
                        pTarget.level().gameEvent(pTarget, GameEvent.EQUIP, pTarget.position());
                        pStack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(pPlayer.level().isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }

    protected boolean canEquip(ErsTamableVehicle<?> entity) {
        return false;
    }
}
