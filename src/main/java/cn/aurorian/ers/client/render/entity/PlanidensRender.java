package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.BenthosuchusPlanidensPlanidensEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class PlanidensRender extends ErsRenderer<BenthosuchusPlanidensPlanidensEntity> {
    public PlanidensRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, 40);
    }
}
