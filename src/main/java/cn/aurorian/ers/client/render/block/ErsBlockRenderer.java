package cn.aurorian.ers.client.render.block;

import cn.aurorian.ers.client.model.block.ErsBlockModel;
import net.minecraft.world.level.block.entity.BlockEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class ErsBlockRenderer<T extends BlockEntity & GeoAnimatable> extends GeoBlockRenderer<T> {
    public ErsBlockRenderer() {
        super(new ErsBlockModel<>());
    }
}
