package cn.aurorian.ers.client.render.item;

import cn.aurorian.ers.client.model.item.SwampDragonFecesBlockItemModel;
import cn.aurorian.ers.item.SwampDragonFecesBlockItem;
import net.minecraft.world.level.block.Block;
import software.bernie.geckolib.renderer.GeoItemRenderer;

public class SwampDragonFecesBlockItemRender extends GeoItemRenderer<SwampDragonFecesBlockItem>{

    public SwampDragonFecesBlockItemRender(Block block) {
        super(new SwampDragonFecesBlockItemModel(block));
    }
}