package cn.aurorian.oasis.client.render;

import cn.aurorian.ers.client.render.entity.ErsRenderer;
import cn.aurorian.oasis.client.model.OasisModel;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PygopodusAnnulatumRender extends ErsRenderer<PygopodusAnnulatumEntity> {
    public PygopodusAnnulatumRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new OasisModel<>());
    }
}
