package cn.aurorian.oasis.init;

import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.item.CookedAnnulatumItem;
import cn.aurorian.oasis.item.TubunasusSaddle;
import cn.aurorian.oasis.item.equipment.HorseShoe;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            Oasis.MODID);
    public static final RegistryObject<Item> TUBUNASUS_DUROVELA_SPAWN_EGG = OasisItems.ITEMS.register("durovela_spawn_egg",
            () -> new ForgeSpawnEggItem(
                    OasisEntities.TUBUNASUS_DUROVELA,
                    0xFFFFFF,
                    0x000000,
                    new Item.Properties()
            ));

    public static final RegistryObject<Item> PYGOPODUS_ANNULATUM_SPAWN_EGG = OasisItems.ITEMS.register("annulatum_spawn_egg",
            () -> new ForgeSpawnEggItem(
                    OasisEntities.PYGOPODUS_ANNULATUM,
                    0xFFFFFF,
                    0x000000,
                    new Item.Properties()
            ));

    public static final RegistryObject<Item> TUBUNASUS_DUROVELA_LARGE_BUCKET = OasisItems.ITEMS.register("durovela_large_bucket",
            () -> new ErsMobLargeBucket(
                    OasisEntities.TUBUNASUS_DUROVELA,
                    () -> Fluids.WATER,
                    () -> SoundEvents.BUCKET_EMPTY_AXOLOTL,
                    new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<Item> TUBUNASUS_SADDLE = OasisItems.ITEMS.register("tubunasus_saddle",
            () -> new TubunasusSaddle(new Item.Properties()));

    public static final RegistryObject<Item> HORSESHOE = OasisItems.ITEMS.register("horseshoe",
            () -> new HorseShoe(new Item.Properties()));
    public static final RegistryObject<Item> BONE = ITEMS.register("bone",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> HEART = ITEMS.register("heart",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> INTESTINES = ITEMS.register("intestines",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LEATHER = ITEMS.register("leather",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LIVER = ITEMS.register("liver",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> LUNG = ITEMS.register("lung",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> EMBRYO = ITEMS.register("embryo",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COOKED_LUNG = ITEMS.register("cooked_lung",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6f)
                    .effect(() -> new MobEffectInstance(OasisMobEffects.BREATH_HOLD.get(),72000),1).build())));
    public static final RegistryObject<Item> KIDNEY = ITEMS.register("kidney",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> COOKED_KIDNEY = ITEMS.register("cooked_kidney",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(1f)
                    .effect(() -> new MobEffectInstance(OasisMobEffects.APHRODISIAC.get(),72000),1).build())));
    public static final RegistryObject<Item> ANNULATUM = ITEMS.register("annulatum",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.5f).build())));
    public static final RegistryObject<Item> COOKED_ANNULATUM = ITEMS.register("cooked_annulatum",
            () -> new CookedAnnulatumItem(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f)
                    .build())));

    public static final RegistryObject<Item> TEASELGOURD = ITEMS.register("teaselgourd",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.4f).build())));


    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
