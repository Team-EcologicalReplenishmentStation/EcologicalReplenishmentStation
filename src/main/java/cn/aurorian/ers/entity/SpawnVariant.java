package cn.aurorian.ers.entity;

import java.util.Arrays;
import net.minecraft.util.RandomSource;

/**
 * Interface for Variant enums that distinguish between common and rare spawn variants. Implement
 * this on inner Variant enums to deduplicate common/rare filtering logic.
 */
public interface SpawnVariant {

    boolean isCommon();

    static <V extends Enum<V> & SpawnVariant> V getCommonSpawnVariant(V[] values, RandomSource random) {
        return getFilteredVariant(values, random, true);
    }

    static <V extends Enum<V> & SpawnVariant> V getRareSpawnVariant(V[] values, RandomSource random) {
        return getFilteredVariant(values, random, false);
    }

    private static <V extends Enum<V> & SpawnVariant> V getFilteredVariant(
            V[] values, RandomSource random, boolean common) {
        var filtered = Arrays.stream(values).filter(v -> v.isCommon() == common).toList();
        return filtered.get(random.nextInt(filtered.size()));
    }
}
