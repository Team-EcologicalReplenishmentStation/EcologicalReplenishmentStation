package cn.aurorian.ers.client.render.block;

import cn.aurorian.ers.block.be.FecesBlockEntity;
import cn.aurorian.ers.client.model.block.FecesBlockModel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class FecesRender extends GeoBlockRenderer<FecesBlockEntity>{

    public FecesRender(@NotNull BlockEntityType<FecesBlockEntity> blockEntityType) {
        super(new FecesBlockModel(blockEntityType));
    }
}