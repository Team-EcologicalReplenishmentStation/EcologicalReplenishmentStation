package cn.aurorian.ers.block.be;

import cn.aurorian.ers.init.ErsBlockEntities;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.util.GeckoLibUtil;

public class FecesBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public FecesBlockEntity(BlockPos pPos, BlockState pState) {
        super(getBlockEntityType(pState), pPos, pState);
    }

    private static BlockEntityType<FecesBlockEntity> getBlockEntityType(BlockState pState) {
        BlockEntityType<FecesBlockEntity> type;
        if (pState.is(ErsBlocks.BONE_FECES.get())) {
            type = ErsBlockEntities.BONE_FECES_BLOCK_ENTITY.get();
        } else if (pState.is(ErsBlocks.SMALL_FECES.get())) {
            type = ErsBlockEntities.SMALL_FECES_BLOCK_ENTITY.get();
        } else if (pState.is(ErsBlocks.LARGE_FECES.get())) {
            type = ErsBlockEntities.LARGE_FECES_BLOCK_ENTITY.get();
        } else if (pState.is(ErsBlocks.GLASSES_FECES.get())) {
            type = ErsBlockEntities.GLASSES_FECES_BLOCK_ENTITY.get();
        } else {
            type = ErsBlockEntities.TEL_FECES_BLOCK_ENTITY.get();
        }
        return type;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {}

    public void clientTick(BlockEntity blockEntity, Level level, BlockPos pos) {
        if (blockEntity.getType() == ErsBlockEntities.TEL_FECES_BLOCK_ENTITY.get() && level.getGameTime() % 90 == 0) {
            level.playSound(null, pos, ErsSounds.TEL_RING.get(), SoundSource.BLOCKS, 0.5f, 1.0f);
        }
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
