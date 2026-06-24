package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.block.*;
import cn.aurorian.ers.block.plant.EquisetumBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsBlocks {
    public static final DeferredRegister<Block> BLOCKS =
            DeferredRegister.create(ForgeRegistries.BLOCKS, EcologicalReplenishmentStation.MODID);

    private static BlockBehaviour.Properties fecesProperties() {
        return Block.Properties.of()
                .mapColor(MapColor.COLOR_BLACK)
                .sound(SoundType.SLIME_BLOCK)
                .strength(0.5F)
                .noOcclusion();
    }

    public static final RegistryObject<Block> BONE_FECES =
            BLOCKS.register("bone_feces", () -> new SwampDragonFecesBlock(fecesProperties()));
    public static final RegistryObject<Block> SMALL_FECES =
            BLOCKS.register("small_feces", () -> new SwampDragonFecesBlock(fecesProperties()));
    public static final RegistryObject<Block> LARGE_FECES =
            BLOCKS.register("large_feces", () -> new SwampDragonFecesBlock(fecesProperties()));
    public static final RegistryObject<Block> GLASSES_FECES =
            BLOCKS.register("glasses_feces", () -> new SwampDragonFecesBlock(fecesProperties()));
    public static final RegistryObject<Block> TEL_FECES =
            BLOCKS.register("tel_feces", () -> new SwampDragonFecesBlock(fecesProperties()));
    public static final RegistryObject<Block> SOUL_CUBE = BLOCKS.register(
            "soul_cube",
            () -> new SoulCubeBlock(BlockBehaviour.Properties.of()
                    .requiresCorrectToolForDrops()
                    .mapColor(MapColor.GOLD)
                    .sound(SoundType.STONE)
                    .strength(5F)
                    .explosionResistance(6000)
                    .noOcclusion()));
    public static final RegistryObject<Block> SWAMP_DRAGON_NEST = BLOCKS.register(
            "swamp_dragon_nest",
            () -> new NestBlock(
                    Block.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .sound(SoundType.GRASS)
                            .strength(1F)
                            .noOcclusion(),
                    ErsBlockEntities.SWAMP_DRAGON_NEST_BLOCK_ENTITY));
    public static final RegistryObject<Block> SAEVUS_NEST = BLOCKS.register(
            "saevus_nest",
            () -> new NestBlock(
                    Block.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .sound(SoundType.GRASS)
                            .strength(1F)
                            .noOcclusion(),
                    ErsBlockEntities.SAEVUS_NEST_BLOCK_ENTITY));
    public static final RegistryObject<Block> DINOSAURIFORMIS_NEST = BLOCKS.register(
            "dinosauriformis_nest",
            () -> new NestBlock(
                    Block.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .sound(SoundType.GRASS)
                            .strength(1F)
                            .noOcclusion(),
                    ErsBlockEntities.DINOSAURIFORMIS_NEST_BLOCK_ENTITY));
    public static final RegistryObject<Block> ANTIQUUS_NEST = BLOCKS.register(
            "antiquus_nest",
            () -> new NestBlock(
                    Block.Properties.of()
                            .mapColor(MapColor.COLOR_BROWN)
                            .sound(SoundType.GRASS)
                            .strength(1F)
                            .noOcclusion(),
                    ErsBlockEntities.ANTIQUUS_NEST_BLOCK_ENTITY));
    public static final RegistryObject<Block> ARTIFICIAL_NEST = BLOCKS.register(
            "artificial_nest",
            () -> new ArtificialNestBlock(Block.Properties.of()
                    .mapColor(MapColor.COLOR_BROWN)
                    .sound(SoundType.GRASS)
                    .strength(1F)
                    .noOcclusion()));

    public static final RegistryObject<Block> EQUISETUM = BLOCKS.register(
            "equisetum",
            () -> new EquisetumBlock(BlockBehaviour.Properties.of()
                    .mapColor(MapColor.COLOR_GREEN)
                    .sound(SoundType.GRASS)
                    .strength(0.2F)
                    .noOcclusion()
                    .randomTicks()));

    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
