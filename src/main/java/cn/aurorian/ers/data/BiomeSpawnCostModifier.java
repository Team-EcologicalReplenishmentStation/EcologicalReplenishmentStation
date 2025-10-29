package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.oasis.init.OasisEntities;
import com.mojang.serialization.Codec;
import net.minecraft.core.Holder;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraftforge.common.world.BiomeModifier;
import net.minecraftforge.common.world.ModifiableBiomeInfo;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class BiomeSpawnCostModifier implements BiomeModifier {

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(EcologicalReplenishmentStation.prefix("add_spawn_cost"), ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS, EcologicalReplenishmentStation.MODID);
    @Override
    public void modify(final Holder<Biome> biome, final Phase phase, final ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            if(biome.is(BiomeTags.IS_OCEAN)){
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), 0.7, 3);
            }
            if(biome.is(BiomeTags.IS_BEACH) || biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP)){
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), 0.6,4);
                if(biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP)){
                    builder.getMobSpawnSettings().addMobCharge(ErsEntities.LATIMERIA_SUCHOMIMUS.get(),0.7,2.8);
                }
            }
            if(biome.is(BiomeTags.IS_BEACH)){
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.TUBUNASUS_DUROVELA.get(),0.7,4);
            }
            if(biome.is(Biomes.RIVER)){
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.LATIMERIA_SUCHOMIMUS.get(),0.7,3);
            }
        }
    }

    @Override
    public Codec<? extends BiomeModifier> codec() {
        return SERIALIZER.get();
    }

    public static Codec<BiomeSpawnCostModifier> makeCodec() {
        return Codec.unit(BiomeSpawnCostModifier::new);
    }
}
