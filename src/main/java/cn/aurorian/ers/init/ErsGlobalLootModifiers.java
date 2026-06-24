package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.loot.ArchaeologyLootModifier;
import com.mojang.serialization.Codec;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsGlobalLootModifiers {

    private static final DeferredRegister<Codec<? extends IGlobalLootModifier>> SERIALIZERS = DeferredRegister.create(
            ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<Codec<ArchaeologyLootModifier>> ARCHAEOLOGY_LOOT_MODIFIER =
            SERIALIZERS.register("archaeology_loot_modifier", () -> ArchaeologyLootModifier.CODEC);

    public static void register(IEventBus modEventBus) {
        SERIALIZERS.register(modEventBus);
    }
}
