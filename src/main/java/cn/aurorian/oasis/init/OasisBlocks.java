package cn.aurorian.oasis.init;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.block.SpecimenBlock;
import cn.aurorian.oasis.block.be.ClyderotundaSpecimenBlockEntity;
import cn.aurorian.oasis.block.be.DurovelaSpecimenBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.NotNull;

public class OasisBlocks {
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, Oasis.MODID);
    public static final RegistryObject<Block> DUROVELA_SPECIMEN = BLOCKS.register("durovela_specimen",
            () -> new SpecimenBlock(Block.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .sound(SoundType.STONE)
                    .strength(1F)
                    .noOcclusion()
            ) {
                @Override
                public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
                    return new DurovelaSpecimenBlockEntity(pos, state);
                }
            });

    public static final RegistryObject<Block> CLYDEROTUNDA_SPECIMEN = BLOCKS.register("clyderotunda_specimen",
            () -> new SpecimenBlock(Block.Properties.of()
                    .mapColor(MapColor.TERRACOTTA_WHITE)
                    .sound(SoundType.STONE)
                    .strength(1F)
                    .noOcclusion()
            ) {
                @Override
                public @NotNull BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
                    return new ClyderotundaSpecimenBlockEntity(pos, state);
                }
            });
    public static void register(IEventBus eventBus) {
        BLOCKS.register(eventBus);
    }
}
