package cn.aurorian.ers.item;

import cn.aurorian.ers.entity.ErsSaddleable;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class SwampDragonSaddle extends Item {
    public SwampDragonSaddle(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack pStack, @NotNull Player pPlayer, @NotNull LivingEntity pTarget, @NotNull InteractionHand pHand) {
        if(pTarget instanceof DentisaurusLongirostrisEntity){
            if (pTarget instanceof ErsSaddleable saddleable && pTarget.isAlive()) {
                if (!saddleable.isSaddled() && saddleable.isSaddleable()) {
                    if (!pPlayer.level().isClientSide) {
                        saddleable.equipSaddle(SoundSource.NEUTRAL);
                        pTarget.level().gameEvent(pTarget, GameEvent.EQUIP, pTarget.position());
                        pStack.shrink(1);
                    }

                    return InteractionResult.sidedSuccess(pPlayer.level().isClientSide);
                }
            }
        }
        return InteractionResult.PASS;
    }
}
