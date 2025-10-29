package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.oasis.init.OasisEntities;
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

import java.util.List;

public class ErsBiomeModifier
{
    public static void register(BootstapContext<net.minecraftforge.common.world.BiomeModifier> context) {
        var biomes = context.lookup(Registries.BIOME);
        context.register(key("swamp_dragon_biome_modifier"), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BEACH),biomes.getOrThrow(Biomes.SWAMP),biomes.getOrThrow(Biomes.MANGROVE_SWAMP))
                , List.of(
                        new MobSpawnSettings.SpawnerData(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), 5, 1, 1)
        )));
        context.register(key("swamp_biome_modifier"), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.SWAMP),biomes.getOrThrow(Biomes.MANGROVE_SWAMP))
                , List.of(
                new MobSpawnSettings.SpawnerData(ErsEntities.TACHYPLEUS_GLADIUS.get(), 5, 2, 4),
                new MobSpawnSettings.SpawnerData(ErsEntities.LATIMERIA_PERCOIDES.get(), 15, 1, 3),
                new MobSpawnSettings.SpawnerData(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), 8, 1, 2)
        )));

        context.register(key("river_biome_modifier"), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.RIVER))
                , List.of(
                new MobSpawnSettings.SpawnerData(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), 10, 1, 2)
        )));

        context.register(key("ocean_biome_modifier"), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                biomes.getOrThrow(BiomeTags.IS_OCEAN)
                , List.of(
                new MobSpawnSettings.SpawnerData(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), 1, 1, 1)
        )));

        context.register(key("tubunasus_biome_modifier"), new ForgeBiomeModifiers.AddSpawnsBiomeModifier(
                HolderSet.direct(biomes.getOrThrow(Biomes.BEACH))
                , List.of(
                new MobSpawnSettings.SpawnerData(OasisEntities.TUBUNASUS_DUROVELA.get(), 5, 2, 4)
        )));
    }


    private static ResourceKey<net.minecraftforge.common.world.BiomeModifier> key(String path)
    {
        return ResourceKey.create(ForgeRegistries.Keys.BIOME_MODIFIERS, ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID,path));
    }
}
