package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.oasis.init.OasisEntities;
import java.util.List;
import net.minecraft.core.HolderSet;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.worldgen.BootstapContext;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BiomeTags;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraftforge.common.world.ForgeBiomeModifiers;
import net.minecraftforge.registries.ForgeRegistries;

public class ErsBiomeModifier {
    public static void register(BootstapContext<net.minecraftforge.common.world.BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);
        context.register(
                key("percoides_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(
                                biomes.getOrThrow(Biomes.BEACH),
                                biomes.getOrThrow(Biomes.SWAMP),
                                biomes.getOrThrow(Biomes.MANGROVE_SWAMP)),
                        List.of(new MobSpawnSettings.SpawnerData(ErsEntities.LATIMERIA_PERCOIDES.get(), 20, 2, 4))));
        context.register(
                key("swamp_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.SWAMP), biomes.getOrThrow(Biomes.MANGROVE_SWAMP)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), 6, 1, 2),
                                new MobSpawnSettings.SpawnerData(ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(), 6, 1, 2),
                                new MobSpawnSettings.SpawnerData(ErsEntities.TACHYPLEUS_GLADIUS.get(), 5, 2, 4),
                                new MobSpawnSettings.SpawnerData(ErsEntities.LATIMERIA_PERCOIDES.get(), 15, 1, 3),
                                new MobSpawnSettings.SpawnerData(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), 6, 1, 2),
                                new MobSpawnSettings.SpawnerData(
                                        ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(), 8, 2, 3),
                                new MobSpawnSettings.SpawnerData(ErsEntities.BENTHOSUCHUS_PLANIDENS.get(), 4, 1, 2),
                                new MobSpawnSettings.SpawnerData(
                                        ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(), 2, 1, 2))));

        context.register(
                key("mangrove_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.MANGROVE_SWAMP)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(), 8, 2, 4),
                                new MobSpawnSettings.SpawnerData(ErsEntities.TACHYCARIS_GUSTATUS.get(), 8, 1, 3),
                                new MobSpawnSettings.SpawnerData(
                                        ErsEntities.ECHINOMORPHUS_CONVERGENS.get(), 10, 2, 4))));

        context.register(
                key("beach_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.BEACH)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(), 8, 2, 4),
                                new MobSpawnSettings.SpawnerData(ErsEntities.TACHYCARIS_GUSTATUS.get(), 8, 1, 3))));

        context.register(
                key("river_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.RIVER)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), 10, 1, 2),
                                new MobSpawnSettings.SpawnerData(
                                        ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(), 10, 2, 3),
                                new MobSpawnSettings.SpawnerData(ErsEntities.BENTHOSUCHUS_PLANIDENS.get(), 8, 1, 2),
                                new MobSpawnSettings.SpawnerData(
                                        ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(), 2, 1, 2))));

        context.register(
                key("ocean_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        biomes.getOrThrow(BiomeTags.IS_OCEAN),
                        List.of(
                                new MobSpawnSettings.SpawnerData(
                                        ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), 1, 1, 1),
                                new MobSpawnSettings.SpawnerData(ErsEntities.ARGENTUMNISCUS_ACICULAULAR.get(), 4, 2, 4),
                                new MobSpawnSettings.SpawnerData(ErsEntities.ECHINOMORPHUS_CONVERGENS.get(), 8, 2, 4),
                                new MobSpawnSettings.SpawnerData(ErsEntities.REMIPES_SICARIUS.get(), 1, 1, 1))));

        context.register(
                key("warm_ocean_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(List.of(
                                biomes.getOrThrow(Biomes.WARM_OCEAN),
                                biomes.getOrThrow(Biomes.LUKEWARM_OCEAN),
                                biomes.getOrThrow(Biomes.DEEP_LUKEWARM_OCEAN))),
                        List.of(new MobSpawnSettings.SpawnerData(ErsEntities.TACHYCARIS_GUSTATUS.get(), 8, 1, 3))));

        context.register(
                key("savana_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.SAVANNA)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.TERRIDENSAURUS_SAEVUS.get(), 3, 1, 1),
                                new MobSpawnSettings.SpawnerData(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 5, 1, 1),
                                new MobSpawnSettings.SpawnerData(
                                        ErsEntities.CRISTATODROMEUS_BRACHYPTERUS.get(), 2, 3, 5),
                                new MobSpawnSettings.SpawnerData(
                                        OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 5, 2, 4))));

        context.register(
                key("plain_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.PLAINS)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 5, 1, 1),
                                new MobSpawnSettings.SpawnerData(OasisEntities.PYGOPODUS_ANNULATUM.get(), 5, 3, 5),
                                new MobSpawnSettings.SpawnerData(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 5, 2, 4),
                                new MobSpawnSettings.SpawnerData(OasisEntities.IMPERIOVENATOR_REGIUS.get(), 5, 1, 1))));

        context.register(
                key("snowy_plain_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.SNOWY_PLAINS)),
                        List.of(new MobSpawnSettings.SpawnerData(
                                OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 5, 2, 4))));

        context.register(
                key("desert_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.DESERT)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 5, 1, 1),
                                new MobSpawnSettings.SpawnerData(
                                        OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 5, 2, 4))));

        context.register(
                key("badlands_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.BADLANDS)),
                        List.of(
                                new MobSpawnSettings.SpawnerData(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), 5, 1, 1),
                                new MobSpawnSettings.SpawnerData(
                                        OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), 5, 2, 4))));

        context.register(
                key("tubunasus_biome_modifier"),
                new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                        HolderSet.direct(biomes.getOrThrow(Biomes.BEACH)),
                        List.of(new MobSpawnSettings.SpawnerData(OasisEntities.TUBUNASUS_DUROVELA.get(), 5, 2, 4))));
    }

    private static ResourceKey<net.minecraftforge.common.world.BiomeModifier> key(String path) {
        return ResourceKey.create(
                ForgeRegistries.Keys.BIOME_MODIFIERS,
                ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, path));
    }
}
