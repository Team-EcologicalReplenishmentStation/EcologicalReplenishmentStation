package cn.aurorian.ers.client.render.block;

import cn.aurorian.ers.block.be.SwampDragonFecesBlockEntity;
import cn.aurorian.ers.client.model.block.SwampDragonFecesBlockModel;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoBlockRenderer;

public class SwampDragonFecesRender extends GeoBlockRenderer<SwampDragonFecesBlockEntity>{

    public SwampDragonFecesRender(@NotNull BlockEntityType<SwampDragonFecesBlockEntity> blockEntityType) {
        super(new SwampDragonFecesBlockModel(blockEntityType));
    }
}