package cn.aurorian.ers.block;

import cn.aurorian.ers.block.be.SwampDragonFecesBlockEntity;
import cn.aurorian.ers.init.ErsBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.BlockPlaceContext;
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
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.border.WorldBorder;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class SwampDragonFecesBlock extends BaseEntityBlock {
    public static final DirectionProperty FACING = BlockStateProperties.FACING;

    public SwampDragonFecesBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.defaultBlockState().setValue(FACING, Direction.NORTH));
    }


    @Override
    public @NotNull VoxelShape getCollisionShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return Shapes.empty();
    }

    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        return new SwampDragonFecesBlockEntity(pPos, pState);
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context) {
        Level level = context.getLevel();
        WorldBorder border = level.getWorldBorder();
        BlockPos clickedPos = context.getClickedPos();
        Direction direction = context.getHorizontalDirection();
        BlockPos relative1 = clickedPos.relative(direction.getClockWise());
        BlockPos relative2 = clickedPos.relative(direction.getCounterClockWise());
        boolean flag1 = level.getBlockState(relative1).canBeReplaced(context) && border.isWithinBounds(relative1);
        boolean flag2 = level.getBlockState(relative2).canBeReplaced(context) && border.isWithinBounds(relative2);
        return flag1 || flag2 ? this.defaultBlockState().setValue(FACING, direction) : null;
    }

    @Override
    public void setPlacedBy(Level level, @NotNull BlockPos pos, @NotNull BlockState state, @Nullable LivingEntity placer, @NotNull ItemStack stack) {
        if (!level.isClientSide) {
            Direction facing = state.getValue(FACING);
            BlockPos presetPos2 = pos.relative(facing.getCounterClockWise());
            if (level.getBlockState(presetPos2).canBeReplaced()) {
                pos = presetPos2;
            }
            level.blockUpdated(pos, Blocks.AIR);
            state.updateNeighbourShapes(level, pos, 3);
        }
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level pLevel, @NotNull BlockState pState, @NotNull BlockEntityType<T> pBlockEntityType) {
        return (level1, blockPos, blockState, t) -> {
            if (t instanceof SwampDragonFecesBlockEntity tickable && t.getType() == ErsBlockEntities.SWAMP_DRAGON_TEL_FECES_BLOCK_ENTITY.get()) {
                tickable.clientTick(tickable, level1, blockPos);
            }
            if (level1.getGameTime() % 20000 == 0){
                level1.removeBlock(blockPos, false);
                if(level1.getBlockState(blockPos.below()) instanceof net.minecraft.world.level.block.BonemealableBlock growable) {
                    if (growable.isValidBonemealTarget(level1, blockPos.below(), level1.getBlockState(blockPos.below()), level1.isClientSide)) {
                        growable.performBonemeal((ServerLevel) level1, level1.random, blockPos.below(), level1.getBlockState(blockPos.below()));
                    }
                }
            }
        };
    }
}
