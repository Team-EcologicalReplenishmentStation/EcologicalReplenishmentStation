package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.model.entity.AntiquusModel;
import cn.aurorian.ers.client.render.entity.layer.AntiquusHiddenLayer;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class EosuchosaurusAntiquusRender extends ErsRenderer<EosuchosaurusAntiquusEntity> {
    public EosuchosaurusAntiquusRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new AntiquusModel());
        addRenderLayer(new AntiquusHiddenLayer(this));
    }
}
