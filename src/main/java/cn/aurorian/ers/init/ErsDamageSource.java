package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.level.Level;

import java.util.Locale;

public class ErsDamageSource {
    public static final ResourceKey<DamageType> DIE_OF_BLEED = ResourceKey.create(Registries.DAMAGE_TYPE, EcologicalReplenishmentStation.prefix("bleeding".toLowerCase(Locale.ROOT)));

    public static DamageSource getDamageSource(Level level, ResourceKey<DamageType> type) {
        return new DamageSource(level.registryAccess().registryOrThrow(Registries.DAMAGE_TYPE).getHolderOrThrow(type), null, null);
    }
}
