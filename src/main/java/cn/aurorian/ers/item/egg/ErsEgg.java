package cn.aurorian.ers.item.egg;

import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;

public abstract class ErsEgg extends Item {
    public ErsEgg(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    public abstract int getHatchTime();

    public abstract ErsTamable<?> born(Level level);
}
