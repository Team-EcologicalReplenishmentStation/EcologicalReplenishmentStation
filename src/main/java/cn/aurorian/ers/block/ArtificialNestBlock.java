package cn.aurorian.ers.block;

import cn.aurorian.ers.block.be.ArtificialNestBlockEntity;
import cn.aurorian.ers.init.ErsBlockEntities;
import cn.aurorian.ers.item.egg.ErsEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class ArtificialNestBlock extends BaseEntityBlock {
    public ArtificialNestBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new ArtificialNestBlockEntity(blockPos, blockState);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(
            @NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(
                pBlockEntityType, ErsBlockEntities.ARTIFICIAL_NEST_BLOCK_ENTITY.get(), ArtificialNestBlockEntity::tick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onRemove(
            BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof ArtificialNestBlockEntity entity) {
                if (!entity.getEgg().isEmpty())
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), entity.getEgg()));
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand pHand,
            @NotNull BlockHitResult pHit) {
        ItemStack stack = player.getItemInHand(pHand);
        if (stack.getItem() instanceof ErsEgg) {
            if (level.isClientSide) {
                return InteractionResult.SUCCESS;
            } else {
                BlockEntity be = level.getBlockEntity(pos);
                if (be instanceof ArtificialNestBlockEntity nest) {
                    if (nest.getEgg().isEmpty()) {
                        level.playSound(
                                player,
                                pos,
                                Blocks.TURTLE_EGG
                                        .getSoundType(Blocks.TURTLE_EGG.defaultBlockState())
                                        .getPlaceSound(),
                                player.getSoundSource(),
                                0.7F,
                                0.9F + level.random.nextFloat() * 0.2F);
                        nest.setEgg(stack.copy());
                        if (!player.getAbilities().instabuild) stack.shrink(1);
                        return InteractionResult.SUCCESS;
                    }
                }
            }
        }
        return InteractionResult.PASS;
    }
}
