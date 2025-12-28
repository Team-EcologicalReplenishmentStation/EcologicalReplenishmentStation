package cn.aurorian.oasis.init;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.block.be.ClyderotundaSpecimenBlockEntity;
import cn.aurorian.oasis.block.be.DurovelaSpecimenBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisBlockEntities {
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, Oasis.MODID);
    public static final RegistryObject<BlockEntityType<DurovelaSpecimenBlockEntity>> DUROVELA_SPECIMEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("durovela_specimen",
            () -> BlockEntityType.Builder.of(DurovelaSpecimenBlockEntity::new, OasisBlocks.DUROVELA_SPECIMEN.get()).build(null));
    public static final RegistryObject<BlockEntityType<ClyderotundaSpecimenBlockEntity>> CLYDEROTUNDA_SPECIMEN_BLOCK_ENTITY = BLOCK_ENTITIES.register("clyderotunda_specimen",
            () -> BlockEntityType.Builder.of(ClyderotundaSpecimenBlockEntity::new, OasisBlocks.CLYDEROTUNDA_SPECIMEN.get()).build(null));
    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
