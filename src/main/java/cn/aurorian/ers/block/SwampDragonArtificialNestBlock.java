package cn.aurorian.ers.block;

import cn.aurorian.ers.block.be.SwampDragonArtificialNestBlockEntity;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwampDragonArtificialNestBlock extends BaseEntityBlock {
    public SwampDragonArtificialNestBlock(Properties properties) {
        super(properties);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new SwampDragonArtificialNestBlockEntity(blockPos, blockState);
    }

    static final VoxelShape shape = Block.box(0, 0, 0, 31, 15, 31);
    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return shape;
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return createTickerHelper(pBlockEntityType, cn.aurorian.ers.init.ErsBlockEntities.SWAMP_DRAGON_ARTIFICIAL_NEST_BLOCK_ENTITY.get(), SwampDragonArtificialNestBlockEntity::tick);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    public void onRemove(BlockState state, @NotNull Level level, @NotNull BlockPos pos, BlockState newState, boolean isMoving ) {
        if (state.getBlock() != newState.getBlock()) {
            BlockEntity tileEntity = level.getBlockEntity(pos);
            if (tileEntity instanceof SwampDragonArtificialNestBlockEntity entity) {
                if(entity.hasEgg())
                    level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), new ItemStack(ErsItems.SWAMP_DRAGON_EGG.get())));
                level.updateNeighbourForOutputSignal(pos, this);
            }

            super.onRemove(state, level, pos, newState, isMoving);
        }
    }

    @Override
    public @NotNull InteractionResult use(@NotNull BlockState state, @NotNull Level level, @NotNull BlockPos pos, @NotNull Player player, @NotNull InteractionHand pHand, @NotNull BlockHitResult pHit) {
        ItemStack stack = player.getItemInHand(pHand);
        if (stack.is(ErsItems.SWAMP_DRAGON_EGG.get())){
            if (level.isClientSide) {
                double distance = Math.sqrt(player.distanceToSqr(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5));
                if (distance > 4.5) {
                    return InteractionResult.FAIL;
                }
            }
            BlockEntity be = level.getBlockEntity(pos);
            if(be instanceof SwampDragonArtificialNestBlockEntity nest){
                if(!nest.hasEgg()){
                    level.playSound(player,pos, Blocks.TURTLE_EGG.getSoundType(Blocks.TURTLE_EGG.defaultBlockState()).getPlaceSound(), player.getSoundSource(), 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
                    nest.setHasEgg(true);
                    if(!level.isClientSide && !player.getAbilities().instabuild)
                        stack.shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }
        return InteractionResult.PASS;
    }
}
