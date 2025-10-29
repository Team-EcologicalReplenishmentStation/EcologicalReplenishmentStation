package cn.aurorian.ers.block.plant;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.IPlantable;
import net.minecraftforge.common.PlantType;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("deprecation")
public class EquisetumBlock extends Block implements IPlantable {
    public static final IntegerProperty AGE = BlockStateProperties.AGE_15;
    public static final IntegerProperty FLOWER = IntegerProperty.create("flower", 0, 3);
    public EquisetumBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(this.stateDefinition.any().setValue(AGE, 0).setValue(FLOWER, 0));
    }

    @Override
    public void tick(BlockState pState, @NotNull ServerLevel pLevel, @NotNull BlockPos pPos, @NotNull RandomSource pRandom) {
        if (!pState.canSurvive(pLevel, pPos)) {
            pLevel.destroyBlock(pPos, true);
        }
    }

    @Override
    public @NotNull VoxelShape getShape(@NotNull BlockState pState, @NotNull BlockGetter pLevel, @NotNull BlockPos pPos, @NotNull CollisionContext pContext) {
        return  Block.box(2.0, 0.0, 2.0, 14.0, 16.0, 14.0);
    }

    @Override
    public @NotNull BlockState updateShape(BlockState pState, @NotNull Direction pFacing, @NotNull BlockState pFacingState, @NotNull LevelAccessor pLevel, @NotNull BlockPos pCurrentPos, @NotNull BlockPos pFacingPos) {
        if (!pState.canSurvive(pLevel, pCurrentPos)) {
            pLevel.scheduleTick(pCurrentPos, this, 1);
        }

        return super.updateShape(pState, pFacing, pFacingState, pLevel, pCurrentPos, pFacingPos);
    }

    @Override
    public boolean canSurvive(@NotNull BlockState pState, LevelReader pLevel, BlockPos pPos) {
        BlockState soil = pLevel.getBlockState(pPos.below());
        if (soil.canSustainPlant(pLevel, pPos.below(), Direction.UP, this)) {
            return true;
        } else {
            BlockState blockstate = pLevel.getBlockState(pPos.below());
            if (blockstate.is(this)) {
                return true;
            } else {
                if (blockstate.is(BlockTags.DIRT) || blockstate.is(BlockTags.SAND)) {
                    BlockPos blockpos = pPos.below();

                    for (Direction direction : Direction.Plane.HORIZONTAL) {
                        BlockState blockstate1 = pLevel.getBlockState(blockpos.relative(direction));
                        FluidState fluidstate = pLevel.getFluidState(blockpos.relative(direction));
                        if (pState.canBeHydrated(pLevel, pPos, fluidstate, blockpos.relative(direction)) || blockstate1.is(Blocks.FROSTED_ICE)) {
                            return true;
                        }
                    }
                }

                return false;
            }
        }
    }

    private boolean hasNearbyWaterSource(LevelReader level, BlockPos pos) {
        BlockPos soilPos = pos.below();

        for (int x = -2; x <= 2; x++) {
            for (int z = -2; z <= 2; z++) {
                for (int y = -1; y <= 1; y++) {
                    BlockPos checkPos = soilPos.offset(x, y, z);
                    FluidState fluidState = level.getFluidState(checkPos);

                    if (fluidState.is(FluidTags.WATER) ||
                            level.getBlockState(checkPos).is(Blocks.FROSTED_ICE)) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    @Override
    public void randomTick(@NotNull BlockState pState, ServerLevel pLevel, BlockPos pPos, @NotNull RandomSource pRandom) {
        if (pLevel.isEmptyBlock(pPos.above()) && pState.getValue(FLOWER) <= 1) {
            int i;
            for(i = 1; pLevel.getBlockState(pPos.below(i)).is(this); ++i) {}

            if (i < 8) {
                int j = pState.getValue(AGE);
                if (ForgeHooks.onCropsGrowPre(pLevel, pPos, pState, true)) {
                    if (j == 15) {
                        BlockState growing = this.defaultBlockState();
                        if(pRandom.nextFloat() < 0.5F)
                            growing = this.defaultBlockState().setValue(FLOWER, 1);
                        if(i > 5 && pRandom.nextFloat() < (0.33F * (i - 5))) {
                            if(pRandom.nextFloat() < 0.5F) {
                                growing = this.defaultBlockState().setValue(FLOWER, 2);
                            } else {
                                growing = this.defaultBlockState().setValue(FLOWER, 3);
                            }
                        }
                        pLevel.setBlockAndUpdate(pPos.above(), growing);
                        ForgeHooks.onCropsGrowPost(pLevel, pPos.above(), growing);
                        pLevel.setBlock(pPos, pState.setValue(AGE, 0), 4);
                    } else {
                        pLevel.setBlock(pPos, pState.setValue(AGE, j + 1), 4);
                    }
                }
            }
        }

    }

    @Override
    public BlockState getPlant(BlockGetter blockGetter, BlockPos blockPos) {
        return this.defaultBlockState();
    }

    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> pBuilder) {
        pBuilder.add(AGE).add(FLOWER);
    }

    public PlantType getPlantType(BlockGetter world, BlockPos pos) {
        return PlantType.BEACH;
    }

}
