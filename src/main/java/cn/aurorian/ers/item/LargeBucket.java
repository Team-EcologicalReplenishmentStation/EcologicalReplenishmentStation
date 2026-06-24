package cn.aurorian.ers.item;

import cn.aurorian.ers.init.ErsItems;
import java.util.function.Supplier;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BucketPickup;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.HitResult;
import org.jetbrains.annotations.NotNull;

public class LargeBucket extends BucketItem {

    public LargeBucket(Supplier<? extends Fluid> supplier, Properties builder) {
        super(supplier, builder);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(
            @NotNull Level pLevel, Player pPlayer, @NotNull InteractionHand pHand) {
        ItemStack itemstack = pPlayer.getItemInHand(pHand);
        BlockHitResult blockhitresult = getPlayerPOVHitResult(pLevel, pPlayer, ClipContext.Fluid.SOURCE_ONLY);
        InteractionResultHolder<ItemStack> ret =
                net.minecraftforge.event.ForgeEventFactory.onBucketUse(pPlayer, pLevel, itemstack, blockhitresult);
        if (ret != null) return ret;

        if (blockhitresult.getType() == HitResult.Type.MISS) {
            return InteractionResultHolder.pass(itemstack);
        } else if (blockhitresult.getType() != HitResult.Type.BLOCK) {
            return InteractionResultHolder.pass(itemstack);
        } else {
            BlockPos blockpos = blockhitresult.getBlockPos();
            Direction direction = blockhitresult.getDirection();
            BlockPos blockpos1 = blockpos.relative(direction);

            if (pLevel.mayInteract(pPlayer, blockpos) && pPlayer.mayUseItemAt(blockpos1, direction, itemstack)) {
                BlockState blockstate = pLevel.getBlockState(blockpos);

                // 检查方块是否为水并且可以被捡起
                if (blockstate.getBlock() instanceof BucketPickup bucketpickup
                        && blockstate.getFluidState().is(Fluids.WATER)) {
                    // 尝试捡起方块
                    ItemStack pickedUpFluid = bucketpickup.pickupBlock(pLevel, blockpos, blockstate);

                    if (!pickedUpFluid.isEmpty()) {
                        // 播放音效和增加统计数据
                        pPlayer.awardStat(Stats.ITEM_USED.get(this));
                        bucketpickup
                                .getPickupSound(blockstate)
                                .ifPresent((sound) -> pPlayer.playSound(sound, 1.0F, 1.0F));

                        // 触发游戏事件
                        pLevel.gameEvent(pPlayer, GameEvent.FLUID_PICKUP, blockpos);

                        // 创建装满水的大桶
                        ItemStack waterBucket = new ItemStack(ErsItems.LARGE_WATER_BUCKET.get());

                        // 根据玩家是否为创造模式决定如何处理物品
                        ItemStack resultStack;
                        if (pPlayer.getAbilities().instabuild) {
                            // 创造模式下不减少物品数量
                            resultStack = itemstack;
                            // 如果玩家还没有这个物品，添加到背包
                            if (!pPlayer.getInventory().contains(waterBucket)) {
                                pPlayer.getInventory().add(waterBucket);
                            }
                        } else {
                            // 生存模式下使用后数量-1
                            if (itemstack.getCount() > 1) {
                                // 如果有多个空桶，减少一个，并给玩家添加一个水桶
                                ItemStack itemstack1 = itemstack.copy();
                                itemstack1.shrink(1);
                                if (!pPlayer.getInventory().add(waterBucket)) {
                                    pPlayer.drop(waterBucket, false);
                                }
                                resultStack = itemstack1;
                            } else {
                                // 如果只有一个空桶，直接替换为水桶
                                resultStack = waterBucket;
                            }
                        }
                        // 触发成就
                        if (!pLevel.isClientSide && pPlayer instanceof ServerPlayer) {
                            CriteriaTriggers.FILLED_BUCKET.trigger((ServerPlayer) pPlayer, waterBucket);
                        }
                        return InteractionResultHolder.sidedSuccess(resultStack, pLevel.isClientSide());
                    }
                }
                // 如果不是水或者不能捡起，返回失败
            }
            return InteractionResultHolder.fail(itemstack);
        }
    }
}
