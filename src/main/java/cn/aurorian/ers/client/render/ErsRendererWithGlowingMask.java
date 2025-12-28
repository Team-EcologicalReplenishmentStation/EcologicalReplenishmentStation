package cn.aurorian.ers.client.render;

import cn.aurorian.ers.client.render.entity.ErsRenderer;
import cn.aurorian.ers.entity.ErsEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

public class ErsRendererWithGlowingMask<A extends Mob & ErsEntity<A> & GeoAnimatable> extends ErsRenderer<A> {
    public ErsRendererWithGlowingMask(EntityRendererProvider.Context renderManager) {
        super(renderManager);
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    public ErsRendererWithGlowingMask(EntityRendererProvider.Context renderManager, float XRotDegree) {
        super(renderManager, XRotDegree);
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    public ErsRendererWithGlowingMask(EntityRendererProvider.Context renderManager, GeoModel model) {
        super(renderManager, model);
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }

    public ErsRendererWithGlowingMask(EntityRendererProvider.Context renderManager, GeoModel model, float XRotDegree) {
        super(renderManager, model, XRotDegree);
        addRenderLayer(new AutoGlowingGeoLayer<>(this));
    }
}
