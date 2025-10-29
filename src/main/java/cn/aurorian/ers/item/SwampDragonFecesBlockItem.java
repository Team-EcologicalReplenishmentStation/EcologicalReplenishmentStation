package cn.aurorian.ers.item;

import cn.aurorian.ers.client.render.item.SwampDragonFecesBlockItemRender;
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
import software.bernie.geckolib.util.RenderUtils;

import java.util.function.Consumer;

public class SwampDragonFecesBlockItem extends BlockItem implements GeoItem {

    private final AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    public SwampDragonFecesBlockItem(Block pBlock, Properties pProperties) {
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
        AnimationController<SwampDragonFecesBlockItem> main =  new AnimationController<>(this, "main",10, state -> {
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
    @Override
    public double getTick(Object itemStack) {
        return RenderUtils.getCurrentTick();
    }

}
