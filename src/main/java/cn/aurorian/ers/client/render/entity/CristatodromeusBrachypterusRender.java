package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.model.entity.CristatodromeusBrachypterusModel;
import cn.aurorian.ers.client.render.entity.layer.CristatodromeusBrachypterusHiddenLayer;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class CristatodromeusBrachypterusRender extends ErsRenderer<CristatodromeusBrachypterusEntity> {
    public CristatodromeusBrachypterusRender(EntityRendererProvider.Context context) {
        super(context, new CristatodromeusBrachypterusModel(), 10);
        addRenderLayer(new CristatodromeusBrachypterusHiddenLayer(this));
    }
}
