package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.model.entity.SaevusModel;
import cn.aurorian.ers.client.render.entity.layer.SaevusHiddenLayer;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class TerridensaurusSaevusRender extends ErsRenderer<TerridensaurusSaevusEntity> {
    public TerridensaurusSaevusRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new SaevusModel());
        addRenderLayer(new SaevusHiddenLayer(this));
    }
}
