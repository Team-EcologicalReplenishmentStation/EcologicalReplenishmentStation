package cn.aurorian.ers.client.render.block;

import cn.aurorian.ers.block.be.ArtificialNestBlockEntity;
import cn.aurorian.ers.client.render.block.layer.ArtifacialNestEggLayer;

public class ArtifacialNestRender extends ErsBlockRenderer<ArtificialNestBlockEntity> {
    public ArtifacialNestRender() {
        super();
        this.addRenderLayer(new ArtifacialNestEggLayer(this));
    }
}
