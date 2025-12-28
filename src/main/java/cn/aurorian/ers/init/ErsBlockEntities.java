package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.block.be.ArtificialNestBlockEntity;
import cn.aurorian.ers.block.be.FecesBlockEntity;
import cn.aurorian.ers.block.be.SaevusNestBlockEntity;
import cn.aurorian.ers.block.be.SwampDragonNestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EcologicalReplenishmentStation.MODID);
    public static final RegistryObject<BlockEntityType<FecesBlockEntity>> BONE_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("bone_feces_block_entity",
    () -> BlockEntityType.Builder.of(FecesBlockEntity::new, ErsBlocks.BONE_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<FecesBlockEntity>> SMALL_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("small_feces_block_entity",
            () -> BlockEntityType.Builder.of(FecesBlockEntity::new, ErsBlocks.SMALL_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<FecesBlockEntity>> LARGE_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("large_feces_block_entity",
            () -> BlockEntityType.Builder.of(FecesBlockEntity::new, ErsBlocks.LARGE_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<FecesBlockEntity>> GLASSES_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("glasses_feces_block_entity",
            () -> BlockEntityType.Builder.of(FecesBlockEntity::new, ErsBlocks.GLASSES_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<FecesBlockEntity>> TEL_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("tel_feces_block_entity",
            () -> BlockEntityType.Builder.of(FecesBlockEntity::new, ErsBlocks.TEL_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<SwampDragonNestBlockEntity>> SWAMP_DRAGON_NEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_nest",
            () -> BlockEntityType.Builder.of(SwampDragonNestBlockEntity::new, ErsBlocks.SWAMP_DRAGON_NEST.get()).build(null));
    public static final RegistryObject<BlockEntityType<SaevusNestBlockEntity>> SAEVUS_NEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("saevus_nest",
            () -> BlockEntityType.Builder.of(SaevusNestBlockEntity::new, ErsBlocks.SAEVUS_NEST.get()).build(null));
    public static final RegistryObject<BlockEntityType<ArtificialNestBlockEntity>> ARTIFICIAL_NEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("artificial_nest",
            () -> BlockEntityType.Builder.of(ArtificialNestBlockEntity::new, ErsBlocks.ARTIFICIAL_NEST.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
