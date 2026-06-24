package cn.aurorian.ers.block;

import cn.aurorian.ers.block.be.NestBlockEntity;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class NestBlock extends BaseEntityBlock {
    private final Supplier<BlockEntityType<NestBlockEntity>> blockEntityType;

    public NestBlock(Properties pProperties, Supplier<BlockEntityType<NestBlockEntity>> blockEntityType) {
        super(pProperties);
        this.blockEntityType = blockEntityType;
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return new NestBlockEntity(blockPos, blockState);
    }

    public BlockEntityType<NestBlockEntity> getBlockEntityType() {
        return blockEntityType.get();
    }

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }
}
