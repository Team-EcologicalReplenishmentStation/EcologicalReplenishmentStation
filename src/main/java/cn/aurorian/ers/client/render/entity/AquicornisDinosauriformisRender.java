package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.model.entity.DinosauriformisModel;
import cn.aurorian.ers.client.render.entity.layer.AquicornisDinosauriformisHiddenLayer;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;

public class AquicornisDinosauriformisRender extends ErsRenderer<AquicornisDinosauriformisEntity> {

    public AquicornisDinosauriformisRender(EntityRendererProvider.Context ctx) {
        super(ctx, new DinosauriformisModel(), 12);
        addRenderLayer(new AquicornisDinosauriformisHiddenLayer(this));
    }
}
