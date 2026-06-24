package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.core.registries.Registries;
import net.minecraft.tags.ItemTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;

public class ErsTagKeys {
    // To override recipe for ItemTags.FISHES
    public static final TagKey<Item> KNOWN_FISH = createItemTag("known_fish");
    public static final TagKey<Item> MEAT = createItemTag("meat");
    // Entity Type Tag
    public static final TagKey<EntityType<?>> PISCIVOROUS = createEntityTypeTag("piscivorous");
    public static final TagKey<EntityType<?>> ARMORED = createEntityTypeTag("armored");
    public static final TagKey<EntityType<?>> SCAVENGER = createEntityTypeTag("scavenger");
    public static final TagKey<EntityType<?>> PREDATOR = createEntityTypeTag("predator");

    private static TagKey<Item> createItemTag(String name) {
        return ItemTags.create(EcologicalReplenishmentStation.prefix(name));
    }

    private static TagKey<EntityType<?>> createEntityTypeTag(String name) {
        return TagKey.create(Registries.ENTITY_TYPE, EcologicalReplenishmentStation.prefix(name));
    }
}
