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

    private static final RegistryObject<Codec<? extends BiomeModifier>> SERIALIZER = RegistryObject.create(
            EcologicalReplenishmentStation.prefix("add_spawn_cost"),
            ForgeRegistries.Keys.BIOME_MODIFIER_SERIALIZERS,
            EcologicalReplenishmentStation.MODID);

    @Override
    public void modify(
            final Holder<Biome> biome, final Phase phase, final ModifiableBiomeInfo.BiomeInfo.Builder builder) {
        if (phase == Phase.ADD) {
            if (biome.is(BiomeTags.IS_OCEAN)) {
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), 0.8, 3.2);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.ARGENTUMNISCUS_ACICULAULAR.get(), 0.7, 2.1);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.TACHYCARIS_GUSTATUS.get(), 0.4, 1.2);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.REMIPES_SICARIUS.get(), 0.3, 0.8);
            }
            if (biome.is(Biomes.SWAMP) || biome.is(Biomes.MANGROVE_SWAMP)) {
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), 1, 5);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(), 1, 5);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.LATIMERIA_PERCOIDES.get(), 0.7, 2.1);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), 0.8, 1.4);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(), 0.6, 1.8);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.TACHYPLEUS_GLADIUS.get(), 0.5, 1.5);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.BENTHOSUCHUS_PLANIDENS.get(), 0.4, 0.6);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(), 0.7, 2.0);

                if (biome.is(Biomes.MANGROVE_SWAMP)) {
                    builder.getMobSpawnSettings().addMobCharge(ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(), 0.5, 2);
                    builder.getMobSpawnSettings().addMobCharge(ErsEntities.TACHYCARIS_GUSTATUS.get(), 0.4, 1.2);
                }
            }
            if (biome.is(BiomeTags.IS_BEACH)) {
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.TUBUNASUS_DUROVELA.get(), 0.8, 3.2);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(), 0.5, 2);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.TACHYCARIS_GUSTATUS.get(), 0.4, 1.2);
            }
            if (biome.is(Biomes.RIVER)) {
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), 0.7, 1.8);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(), 0.6, 1.8);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.BENTHOSUCHUS_PLANIDENS.get(), 0.5, 0.8);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(), 0.7, 2.0);
            }
            if (biome.is(Biomes.SAVANNA)) {
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 1.5, 2.6);
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.TERRIDENSAURUS_SAEVUS.get(), 1, 4);
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 0.8, 4);
            }
            if (biome.is(Biomes.SNOWY_PLAINS)) {
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 0.8, 3.2);
            }
            if (biome.is(Biomes.BADLANDS)) {
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 1.5, 2.6);
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 0.8, 3.2);
            }
            if (biome.is(Biomes.DESERT)) {
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 1.5, 2.6);
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 0.8, 3.2);
            }
            if (biome.is(Biomes.PLAINS)) {
                builder.getMobSpawnSettings().addMobCharge(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 1.5, 2.6);
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.PYGOPODUS_ANNULATUM.get(), 0.6, 2.4);
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.TUBUNASUS_DUROVELA.get(), 1, 4);
                builder.getMobSpawnSettings().addMobCharge(OasisEntities.IMPERIOVENATOR_REGIUS.get(), 1.5, 2.6);
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
