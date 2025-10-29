package cn.aurorian.ers.client.render.block;

import cn.aurorian.ers.block.be.SwampDragonArtificialNestBlockEntity;
import cn.aurorian.ers.client.render.block.layer.SwampDragonEggLayer;

public class SwampDragonNestRender extends ErsBlockRenderer<SwampDragonArtificialNestBlockEntity> {
    public SwampDragonNestRender() {
        super();
        this.addRenderLayer(new SwampDragonEggLayer(this));
    }
}
