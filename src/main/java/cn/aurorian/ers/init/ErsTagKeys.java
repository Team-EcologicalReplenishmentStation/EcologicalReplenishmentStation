package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;

public class ErsTagKeys{
    //To override recipe for ItemTags.FISHES
    public static final TagKey<Item> KNOWN_FISH = create("known_fish");

    private static TagKey<Item> create(String name) {
        return ItemTags.create(EcologicalReplenishmentStation.prefix(name));
    }
}
