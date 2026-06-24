package cn.aurorian.ers.block.be;

import cn.aurorian.ers.block.NestBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class NestBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    public NestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(getBlockEntityType(pBlockState), pPos, pBlockState);
    }

    private static BlockEntityType<NestBlockEntity> getBlockEntityType(BlockState pState) {
        return ((NestBlock) pState.getBlock()).getBlockEntityType();
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
