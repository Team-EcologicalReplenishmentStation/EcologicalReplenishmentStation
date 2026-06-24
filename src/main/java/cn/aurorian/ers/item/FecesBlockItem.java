package cn.aurorian.ers.item;

import cn.aurorian.ers.client.render.item.SwampDragonFecesBlockItemRender;
import java.util.function.Consumer;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.object.PlayState;

public class FecesBlockItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public FecesBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
    }

    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new IClientItemExtensions() {
            private SwampDragonFecesBlockItemRender renderer;

            @Override
            public BlockEntityWithoutLevelRenderer getCustomRenderer() {
                if (renderer == null) {
                    renderer = new SwampDragonFecesBlockItemRender(getBlock());
                }
                return renderer;
            }
        });
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        AnimationController<FecesBlockItem> main = new AnimationController<>(this, "main", 10, state -> {
            if (state.getController().hasAnimationFinished()) {
                return PlayState.STOP;
            }
            return PlayState.CONTINUE;
        });
        controllers.add(main);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }
}
