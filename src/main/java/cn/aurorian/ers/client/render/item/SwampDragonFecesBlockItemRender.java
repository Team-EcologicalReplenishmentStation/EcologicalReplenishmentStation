package cn.aurorian.ers.client.render.item;

import cn.aurorian.ers.client.model.item.FecesBlockItemModel;
import cn.aurorian.ers.item.FecesBlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SwampDragonFecesBlockItemRender extends GeoItemRenderer<FecesBlockItem> {

    public SwampDragonFecesBlockItemRender(Block block) {
        super(new FecesBlockItemModel(block));
    }
}
