package cn.aurorian.ers.entity;

import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;

/**
 * Generic replacement for per-entity GroupData inner classes. Stores an array of variant candidates
 * and picks one randomly on spawn.
 *
 * @param <V> the Variant enum type
 */
public class ErsGroupData<V> extends AgeableMob.AgeableMobGroupData {
    private final V[] variants;

    @SafeVarargs
    public ErsGroupData(boolean shouldSpawnBaby, V... variants) {
        super(shouldSpawnBaby);
        this.variants = variants;
    }

    @SafeVarargs
    public ErsGroupData(float babySpawnChance, V... variants) {
        super(babySpawnChance);
        this.variants = variants;
    }

    public V getVariant(RandomSource random) {
        return this.variants[random.nextInt(this.variants.length)];
    }
}
