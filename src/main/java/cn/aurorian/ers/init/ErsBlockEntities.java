package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.block.be.SwampDragonArtificialNestBlockEntity;
import cn.aurorian.ers.block.be.SwampDragonFecesBlockEntity;
import cn.aurorian.ers.block.be.SwampDragonNestBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, EcologicalReplenishmentStation.MODID);
    public static final RegistryObject<BlockEntityType<SwampDragonFecesBlockEntity>> SWAMP_DRAGON_BONE_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_bone_feces_block_entity",
    () -> BlockEntityType.Builder.of(SwampDragonFecesBlockEntity::new, ErsBlocks.SWAMP_DRAGON_BONE_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<SwampDragonFecesBlockEntity>> SWAMP_DRAGON_SMALL_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_small_feces_block_entity",
            () -> BlockEntityType.Builder.of(SwampDragonFecesBlockEntity::new, ErsBlocks.SWAMP_DRAGON_SMALL_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<SwampDragonFecesBlockEntity>> SWAMP_DRAGON_LARGE_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_large_feces_block_entity",
            () -> BlockEntityType.Builder.of(SwampDragonFecesBlockEntity::new, ErsBlocks.SWAMP_DRAGON_LARGE_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<SwampDragonFecesBlockEntity>> SWAMP_DRAGON_GLASSES_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_glasses_feces_block_entity",
            () -> BlockEntityType.Builder.of(SwampDragonFecesBlockEntity::new, ErsBlocks.SWAMP_DRAGON_GLASSES_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<SwampDragonFecesBlockEntity>> SWAMP_DRAGON_TEL_FECES_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_tel_feces_block_entity",
            () -> BlockEntityType.Builder.of(SwampDragonFecesBlockEntity::new, ErsBlocks.SWAMP_DRAGON_TEL_FECES.get()).build(null));
    public static final RegistryObject<BlockEntityType<SwampDragonNestBlockEntity>> SWAMP_DRAGON_NEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_nest",
            () -> BlockEntityType.Builder.of(SwampDragonNestBlockEntity::new, ErsBlocks.SWAMP_DRAGON_NEST.get()).build(null));
    public static final RegistryObject<BlockEntityType<SwampDragonArtificialNestBlockEntity>> SWAMP_DRAGON_ARTIFICIAL_NEST_BLOCK_ENTITY = BLOCK_ENTITIES.register("swamp_dragon_artificial_nest",
            () -> BlockEntityType.Builder.of(SwampDragonArtificialNestBlockEntity::new, ErsBlocks.SWAMP_DRAGON_ARTIFICIAL_NEST.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
