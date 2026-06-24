package cn.aurorian.ers.item;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.init.ErsMobEffects;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemUtils;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

public class Tourniquet extends Item {
    public Tourniquet(Properties pProperties) {
        super(pProperties.stacksTo(16));
    }

    @Override
    public @NotNull ItemStack finishUsingItem(
            @NotNull ItemStack item, @NotNull Level level, @NotNull LivingEntity entity) {
        Player player = entity instanceof Player ? (Player) entity : null;

        if (player instanceof ServerPlayer) {
            CriteriaTriggers.CONSUME_ITEM.trigger((ServerPlayer) player, item);
        }

        if (player != null) {
            player.awardStat(Stats.ITEM_USED.get(this));
            if (!player.getAbilities().instabuild) {
                item.shrink(1);
            }
            player.removeEffect(ErsMobEffects.BLEEDING.get());
        }

        level.gameEvent(entity, GameEvent.ITEM_INTERACT_FINISH, entity.getEyePosition());
        return item;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(
            @NotNull ItemStack itemStack,
            @NotNull Player player,
            @NotNull LivingEntity target,
            @NotNull InteractionHand pUsedHand) {
        if (target.hasEffect(ErsMobEffects.BLEEDING.get())) {
            ErsBleedingEffect.removeBleedingEffect(target);
            itemStack.shrink(1);
            return InteractionResult.CONSUME;
        }

        return InteractionResult.PASS;
    }

    @Override
    public int getUseDuration(@NotNull ItemStack pStack) {
        return 32;
    }

    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack p_42997_) {
        return UseAnim.BRUSH;
    }

    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level p_42993_, @NotNull Player p_42994_, @NotNull InteractionHand p_42995_) {
        return ItemUtils.startUsingInstantly(p_42993_, p_42994_, p_42995_);
    }
}
