package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.data.BiomeSpawnCostModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsBiomeModifierSerializers {
    private static final DeferredRegister<Codec<? extends BiomeModifier>> SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, EcologicalReplenishmentStation.MODID);

    private static boolean isInitialised = false;

    public static final RegistryObject<Codec<BiomeSpawnCostModifier>> ADD_MOB_SPAWN_COST =
            SERIALIZERS.register("add_mob_spawn_cost", BiomeSpawnCostModifier::makeCodec);

    /**
     * Registers the {@link DeferredRegister} instance with the mod event bus.
     *
     * <p>This should be called during mod construction.
     *
     * @param modEventBus The mod event bus
     */
    public static void register(final IEventBus modEventBus) {
        if (isInitialised) {
            throw new IllegalStateException("Already initialised");
        }

        SERIALIZERS.register(modEventBus);

        isInitialised = true;
    }
}
