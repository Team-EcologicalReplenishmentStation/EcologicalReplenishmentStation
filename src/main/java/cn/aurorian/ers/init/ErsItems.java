package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.item.*;
import cn.aurorian.ers.item.equipment.*;
import cn.aurorian.ers.item.weapon.DragonClawHarpoon;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.ComposterBlock;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsItems {
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS,
            EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<Item> PERCH_BUCKET = ITEMS.register("perch_bucket",
            () -> new MobBucketItem(
                    ErsEntities.LATIMERIA_PERCOIDES,
                () -> Fluids.WATER,
                () -> SoundEvents.BUCKET_EMPTY_AXOLOTL,
                new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<Item> TACHYPLEUS_GLADIUS_BUCKET = ITEMS.register("horseshoe_crab_bucket",
            () -> new MobBucketItem(
                    ErsEntities.TACHYPLEUS_GLADIUS,
                () -> Fluids.WATER,
                () -> SoundEvents.BUCKET_EMPTY_AXOLOTL,
                new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<Item> SWAMP_DRAGON_LARGE_BUCKET = ITEMS.register("swamp_dragon_large_bucket",
            () -> new ErsMobLargeBucket(
                    ErsEntities.DENTISAURUS_LONGIROSTRIS,  // 实体类型供应器
                () -> Fluids.WATER,              // 流体供应器
                () -> SoundEvents.BUCKET_EMPTY_AXOLOTL,  // 声音供应器
                new Item.Properties().stacksTo(1)
            ));

    public static final RegistryObject<Item> CHLAMYDOSELACHOIDES_LARGE_BUCKET = ITEMS.register("chlamydoselachoides_large_bucket",
            () -> new ErsMobLargeBucket(
                    ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES,
                () -> Fluids.WATER,
                () -> SoundEvents.BUCKET_EMPTY_AXOLOTL,
                new Item.Properties().stacksTo(1)
            ));
    public static final RegistryObject<Item> SUCHOMIMUS_LARGE_BUCKET = ITEMS.register("suchomimus_large_bucket",
            () -> new ErsMobLargeBucket(
                    ErsEntities.LATIMERIA_SUCHOMIMUS,
                () -> Fluids.WATER,
                () -> SoundEvents.BUCKET_EMPTY_AXOLOTL,
                new Item.Properties().stacksTo(1)
            ));
    
    public static final RegistryObject<Item> LARGE_BUCKET = ITEMS.register("large_bucket", 
            () -> new LargeBucket(() -> Fluids.EMPTY, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> LARGE_WATER_BUCKET = ITEMS.register("large_water_bucket",
            () -> new WaterLargeBucket(() -> Fluids.WATER, new Item.Properties().stacksTo(1).craftRemainder(ErsItems.LARGE_BUCKET.get())));

    public static final RegistryObject<Item> SWAMP_DRAGON_SADDLE = ITEMS.register("swamp_dragon_saddle",
            () -> new SwampDragonSaddle(new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_DRAGON_FECES = ITEMS.register("swamp_dragon_feces",
            () -> new SwampDragonFecesItem(new Item.Properties()));
    //装备
    public static final RegistryObject<Item> CLOVER = ITEMS.register("clover",
            () -> new Clover(new Item.Properties()));
    public static final RegistryObject<Item> BAIT_BOX = ITEMS.register("bait_box",
            () -> new BaitBox(new Item.Properties()));
    public static final RegistryObject<Item> BULLY_STICK = ITEMS.register("bully_stick",
            () -> new BullyStick(new Item.Properties()));
    public static final RegistryObject<Item> SCRATCHING_BOARD = ITEMS.register("scratching_board",
            () -> new ScratchingBoard(new Item.Properties()));
    public static final RegistryObject<Item> DRIED_FISH = ITEMS.register("dried_fish",
            () -> new DriedFish(new Item.Properties()));
    public static final RegistryObject<Item> RIDING_GUIDE = ITEMS.register("riding_guide",
            () -> new RidingGuide(new Item.Properties()));

    // 刷怪蛋
    public static final RegistryObject<Item> DENTISAURUS_LONGIROSTRIS_SPAWN_EGG = ITEMS.register("longirostris_spawn_egg",
    () -> new ForgeSpawnEggItem(
            ErsEntities.DENTISAURUS_LONGIROSTRIS,
            0xFFFFFF,
        0x00000,
        new Item.Properties()
    ));
    public static final RegistryObject<Item> LATIMERIA_PERCOIDES_SPAWN_EGG = ITEMS.register("percoides_spawn_egg",
    () -> new ForgeSpawnEggItem(
            ErsEntities.LATIMERIA_PERCOIDES,
            0xFFFFFF,
        0x000000,
        new Item.Properties()
    ));
    public static final RegistryObject<Item> TACHYPLEUS_GLADIUS_SPAWN_EGG = ITEMS.register("gladius_spawn_egg",
            () -> new ForgeSpawnEggItem(
                    ErsEntities.TACHYPLEUS_GLADIUS,
                    0xFFFFFF,
                    0x000000,
                    new Item.Properties()
            ));
    public static final RegistryObject<Item> CHLAMYDOSELACHOIDES_SPAWN_EGG = ITEMS.register("chlamydoselachoides_spawn_egg",
            () -> new ForgeSpawnEggItem(
                    ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES,
                    0xFFFFFF,
                    0x000000,
                    new Item.Properties()
            ));
    public static final RegistryObject<Item> LATIMERIA_SUCHOMIMUS_SPAWN_EGG = ITEMS.register("suchomimus_spawn_egg",
            () -> new ForgeSpawnEggItem(
                    ErsEntities.LATIMERIA_SUCHOMIMUS,
                    0xFFFFFF,
                    0x000000,
                    new Item.Properties()
            ));
    //Feces
    public static final RegistryObject<Item> SWAMP_DRAGON_BONE_FECES = ITEMS.register("swamp_dragon_bone_feces",
            () -> new SwampDragonFecesBlockItem(ErsBlocks.SWAMP_DRAGON_BONE_FECES.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_DRAGON_SMALL_FECES = ITEMS.register("swamp_dragon_small_feces",
            () -> new SwampDragonFecesBlockItem(ErsBlocks.SWAMP_DRAGON_SMALL_FECES.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_DRAGON_LARGE_FECES = ITEMS.register("swamp_dragon_large_feces",
            () -> new SwampDragonFecesBlockItem(ErsBlocks.SWAMP_DRAGON_LARGE_FECES.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_DRAGON_GLASSES_FECES = ITEMS.register("swamp_dragon_glasses_feces",
            () -> new SwampDragonFecesBlockItem(ErsBlocks.SWAMP_DRAGON_GLASSES_FECES.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_DRAGON_TEL_FECES = ITEMS.register("swamp_dragon_tel_feces",
            () -> new SwampDragonFecesBlockItem(ErsBlocks.SWAMP_DRAGON_TEL_FECES.get(), new Item.Properties()));

    public static final RegistryObject<Item> SOUL_CUBE = ITEMS.register("soul_cube",
            () -> new BlockItem(ErsBlocks.SOUL_CUBE.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_DRAGON_NEST = ITEMS.register("swamp_dragon_nest",
            () -> new BlockItem(ErsBlocks.SWAMP_DRAGON_NEST.get(), new Item.Properties()));
    public static final RegistryObject<Item> SWAMP_DRAGON_ARTIFICIAL_NEST = ITEMS.register("swamp_dragon_artificial_nest",
            () -> new BlockItem(ErsBlocks.SWAMP_DRAGON_ARTIFICIAL_NEST.get(), new Item.Properties()));
    public static final RegistryObject<Item> EQUISETUM = ITEMS.register("equisetum",
            () -> new BlockItem(ErsBlocks.EQUISETUM.get(),new Item.Properties()));

    public static final RegistryObject<Item> SWAMP_DRAGON_EGG = ITEMS.register("swamp_dragon_egg",
            () -> new Item(new Item.Properties().rarity(Rarity.RARE)));
    public static final RegistryObject<Item> SAEVUS_EGG = ITEMS.register("saevus_egg",
            () -> new Item(new Item.Properties().rarity(Rarity.EPIC)));
    public static final RegistryObject<Item> SOUL_CUBE_GIFT = ITEMS.register("soul_cube_gift",
            () -> new Item(new Item.Properties().stacksTo(1).fireResistant()));
    public static final RegistryObject<Item> FISH_FILLET = ITEMS.register("fish_fillet",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.15f).build())));
    public static final RegistryObject<Item> COOKED_FISH_FILLET = ITEMS.register("cooked_fish_fillet",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build())));
    public static final RegistryObject<Item> PERCH = ITEMS.register("perch",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(2).saturationMod(0.3f).build())));
    public static final RegistryObject<Item> SUCHOMIMUS = ITEMS.register("suchomimus",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final RegistryObject<Item> COOKED_SUCHOMIMUS = ITEMS.register("cooked_suchomimus",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1.0f).build())));
    public static final RegistryObject<Item> COOKED_PERCH = ITEMS.register("cooked_perch",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(5).saturationMod(0.6f).build())));
    public static final RegistryObject<Item> PISCIVORES_FEED = ITEMS.register("piscivores_feed",
            () -> new Item(new Item.Properties().stacksTo(16)));
    public static final RegistryObject<Item> SWAMP_DRAGON_CLAW = ITEMS.register("swamp_dragon_claw",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FLUORESCENCE_LENS = ITEMS.register("fluorescence_lens",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(1).saturationMod(0.1f)
                    .effect(()-> new MobEffectInstance(MobEffects.NIGHT_VISION,-1),1).build())));
    public static final RegistryObject<Item> SWAMP_DRAGON_MEAT = ITEMS.register("swamp_dragon_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final RegistryObject<Item> COOKED_SWAMP_DRAGON_MEAT = ITEMS.register("cooked_swamp_dragon_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1.0f).build())));
    public static final RegistryObject<Item> HORSESHOE_CRAB_MEAT = ITEMS.register("horseshoe_crab_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(4).saturationMod(0.5f).build())));
    public static final RegistryObject<Item> COOKED_HORSESHOE_CRAB_MEAT = ITEMS.register("cooked_horseshoe_crab_meat",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(8).saturationMod(0.8f).build())));
    public static final RegistryObject<Item> HORSESHOE_CRAB_STINGER = ITEMS.register("horseshoe_crab_stinger",
            () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> CHLAMYDOSELACHOIDES = ITEMS.register("chlamydoselachoides",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(6).saturationMod(0.8f).build())));
    public static final RegistryObject<Item> COOKED_CHLAMYDOSELACHOIDES = ITEMS.register("cooked_chlamydoselachoides",
            () -> new Item(new Item.Properties().food(new FoodProperties.Builder().nutrition(10).saturationMod(1.0f).build())));
    public static final RegistryObject<Item> CHLAMYDOSELACHOIDES_TOOTH = ITEMS.register("chlamydoselachoides_tooth",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> CHLAMYDOSELACHOIDES_TOOTH_SWORD = ITEMS.register("chlamydoselachoides_tooth_sword",
            () -> new AcanthodiiToothKnife(
                    Tiers.IRON,
                    3,
                    -2.6F,
                    new Item.Properties().durability(250)
            ));
    public static final RegistryObject<Item> DRAGON_CLAW_HARPOON = ITEMS.register("dragon_claw_harpoon",
            () -> new DragonClawHarpoon(new Item.Properties().durability(250)));
    public static final RegistryObject<Item> DRAGON_CLAW_HARPOON_INVENTORY = ITEMS.register("dragon_claw_harpoon_inventory",
            () -> new Item(new Item.Properties().stacksTo(1)));
    public static final RegistryObject<Item> TOURNIQUET = ITEMS.register("tourniquet",
            () -> new Tourniquet(new Item.Properties()));

    public static void registerComposterBlock(){
        ComposterBlock.COMPOSTABLES.put(ErsItems.SWAMP_DRAGON_BONE_FECES.get(), 0.95F);
        ComposterBlock.COMPOSTABLES.put(ErsItems.SWAMP_DRAGON_SMALL_FECES.get(), 0.95F);
        ComposterBlock.COMPOSTABLES.put(ErsItems.SWAMP_DRAGON_LARGE_FECES.get(), 0.95F);
        ComposterBlock.COMPOSTABLES.put(ErsItems.SWAMP_DRAGON_GLASSES_FECES.get(), 0.95F);
        ComposterBlock.COMPOSTABLES.put(ErsItems.SWAMP_DRAGON_TEL_FECES.get(), 0.95F);
        ComposterBlock.COMPOSTABLES.put(ErsItems.SWAMP_DRAGON_FECES.get(), 0.95F);
    }
            
    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
