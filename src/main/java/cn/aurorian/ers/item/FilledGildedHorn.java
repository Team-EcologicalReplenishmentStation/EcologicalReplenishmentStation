package cn.aurorian.ers.item;

import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.init.ErsItems;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;

public class FilledGildedHorn extends Item {
    public FilledGildedHorn(Properties pProperties) {
        super(pProperties);
    }

    public void checkExtraContent(@Nullable Player pPlayer, Level pLevel, ItemStack pContainerStack, BlockPos pPos) {
        if (pLevel instanceof ServerLevel) {
            this.spawn((ServerLevel) pLevel, pContainerStack, pPos);
            pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, pPos);
        }
    }

    private void spawn(ServerLevel pServerLevel, ItemStack gildedStack, BlockPos pPos) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(
                ResourceLocation.parse(gildedStack.getTag().getString("EntityType")));
        Entity entity = type.spawn(pServerLevel, gildedStack, null, pPos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof ErsTamable<?> tamable) {
            tamable.readAdditionalSaveData(gildedStack.getOrCreateTag());
        }
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);

        if (!itemstack.hasTag() || !itemstack.getOrCreateTag().contains("EntityType")) {
            return InteractionResultHolder.pass(itemstack);
        }

        BlockHitResult blockhitresult =
                getPlayerPOVHitResult(pLevel, pPlayer, net.minecraft.world.level.ClipContext.Fluid.NONE);
        InteractionResultHolder<ItemStack> ret =
                ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, blockhitresult);
        if (ret != null) {
            return ret;
        } else if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);
            if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                this.checkExtraContent(pPlayer, pLevel, itemstack, blockpos);
                pPlayer.awardStat(Stats.ITEM_USED.get(this));
                return InteractionResultHolder.sidedSuccess(
                        getEmptySuccessItem(itemstack, pPlayer), pLevel.isClientSide());
            } else {
                return InteractionResultHolder.fail(itemstack);
            }
        }
    }

    public static ItemStack getEmptySuccessItem(ItemStack pBucketStack, Player pPlayer) {
        return !pPlayer.getAbilities().instabuild ? new ItemStack(ErsItems.GILDED_HORN.get()) : pBucketStack;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.TOOT_HORN;
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack pStack,
            @Nullable Level pLevel,
            @NotNull List<Component> pTooltipComponents,
            @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        if (!pStack.hasTag() || !pStack.getOrCreateTag().contains("EntityType")) {
            return;
        }
        MutableComponent entityType = Component.translatable(BuiltInRegistries.ENTITY_TYPE
                .get(ResourceLocation.parse(pStack.getOrCreateTag().getString("EntityType")))
                .getDescriptionId());
        pTooltipComponents.add(entityType.withStyle(ChatFormatting.GRAY));

        int ageInDays = pStack.getOrCreateTag().getInt("AgeTicks") / 24000;
        pTooltipComponents.add(Component.translatable("tooltip.ers.age", ageInDays));
    }

    public static <T extends ErsTamable<?>> Optional<InteractionResult> hornPickup(
            Player player, InteractionHand interactionHand, T entity) {
        ItemStack itemstack = player.getItemInHand(interactionHand);
        if (itemstack.getItem() == ErsItems.GILDED_HORN.get() && entity.isAlive() && !entity.isSoul()) {
            ItemStack itemstack1 = new ItemStack(ErsItems.FILLED_GILDED_HORN.get());
            entity.addAdditionalSaveData(itemstack1.getOrCreateTag());
            itemstack1
                    .getOrCreateTag()
                    .putString(
                            "EntityType",
                            BuiltInRegistries.ENTITY_TYPE
                                    .getKey(entity.getType())
                                    .toString());
            ItemStack itemstack2 = ItemUtils.createFilledResult(itemstack, player, itemstack1, false);
            player.setItemInHand(interactionHand, itemstack2);
            Level level = entity.level();
            entity.discard();
            return Optional.of(InteractionResult.sidedSuccess(level.isClientSide));
        } else {
            return Optional.empty();
        }
    }
}
