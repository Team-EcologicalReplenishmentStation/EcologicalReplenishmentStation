package cn.aurorian.ers.item;

import cn.aurorian.ers.init.ErsItems;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.GrassBlock;
import net.minecraft.world.level.block.LevelEvent;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;

public class SwampDragonFecesItem extends Item {
    public SwampDragonFecesItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResult useOn(UseOnContext context) {
        if (context.getLevel().isClientSide) {
            // 客户端直接返回SUCCESS以触发动画
            return InteractionResult.SUCCESS;
        }

        ItemStack stack = context.getItemInHand();
        if (tryBonemealEffect(
                context.getLevel(),
                context.getClickedPos(),
                context.getLevel().getBlockState(context.getClickedPos()))) {
            if (context.getPlayer() != null && !context.getPlayer().getAbilities().instabuild) {
                stack.shrink(1);
            }
            // 播放粒子效果
            context.getLevel().levelEvent(LevelEvent.PARTICLES_PLANT_GROWTH, context.getClickedPos(), 0);
            return InteractionResult.SUCCESS;
        }

        List<BlockItem> fecesBlocks = List.of(
                (BlockItem) ErsItems.BONE_FECES.get(), (BlockItem) ErsItems.LARGE_FECES.get(),
                (BlockItem) ErsItems.SMALL_FECES.get(), (BlockItem) ErsItems.GLASSES_FECES.get());
        BlockItem selected = fecesBlocks.get(RandomSource.create().nextInt(fecesBlocks.size()));
        InteractionResult result = selected.place(new BlockPlaceContext(context));
        // 播放音效
        context.getLevel()
                .playSound(
                        null,
                        context.getClickedPos().getX() + 0.5,
                        context.getClickedPos().getY() + 0.5,
                        context.getClickedPos().getZ() + 0.5,
                        SoundEvents.SLIME_BLOCK_PLACE,
                        SoundSource.BLOCKS,
                        0.5f,
                        1.0f);
        if (result == InteractionResult.SUCCESS) {
            Player player = context.getPlayer();
            if (player != null && !player.getAbilities().instabuild) {
                stack.shrink(1);
            }
        }
        return result;
    }

    private boolean tryBonemealEffect(Level level, BlockPos pos, BlockState state) {
        Block block = state.getBlock();

        if (block instanceof BonemealableBlock growable && !(block instanceof GrassBlock)) {
            if (growable.isValidBonemealTarget(level, pos, state, level.isClientSide)) {
                if (RandomSource.create().nextFloat() < 0.75) {
                    growable.performBonemeal((ServerLevel) level, level.random, pos, state);
                }
                return true;
            }
        }
        return false;
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BLOCK;
    }
}
