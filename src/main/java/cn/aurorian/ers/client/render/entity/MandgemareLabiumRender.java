package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.model.entity.MandgemareLabiumModel;
import cn.aurorian.ers.client.render.entity.layer.MandgemareLabiumMarkLayer;
import cn.aurorian.ers.entity.creatures.mandgemarelabium.MandgemareLabiumEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class MandgemareLabiumRender extends ErsRenderer<MandgemareLabiumEntity> {
    public MandgemareLabiumRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new MandgemareLabiumModel());
        addRenderLayer(new MandgemareLabiumMarkLayer(this));
    }
}
