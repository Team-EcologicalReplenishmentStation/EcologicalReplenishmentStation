package cn.aurorian.ers.entity;

import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

import javax.annotation.Nonnull;

public abstract class ErsTamable <T extends ErsTamable<?>> extends TamableAnimal implements GeoEntity, ErsEntity<T>{

    protected ErsTamable(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> ENABLE = SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<String> DIMENSION = SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.STRING);
    private static final EntityDataAccessor<Integer> X = SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> Y = SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> Z = SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Integer> AGE_TICKS = SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean canSprint(){
        return true;
    }

    public boolean isEnable() {
        return this.entityData.get(ENABLE);
    }

    public void setEnable(boolean enable) {
        this.entityData.set(ENABLE, enable);
    }

    public BlockPos getRespawnPos(){
        return new BlockPos(this.entityData.get(X), this.entityData.get(Y), this.entityData.get(Z));
    }

    public void setRespawnPos(BlockPos pos) {
        this.entityData.set(X, pos.getX());
        this.entityData.set(Y, pos.getY());
        this.entityData.set(Z, pos.getZ());
    }

    public void setDimension(String dimension) {
        this.entityData.set(DIMENSION, dimension);
    }

    public String getDimension() {
        return this.entityData.get(DIMENSION);
    }
    public int getAgeInDays() {
        return this.entityData.get(AGE_TICKS) / 24000;
    }
    public void setAgeInDays(int age) {
        this.entityData.set(AGE_TICKS, age * 24000);
    }
    public int getAgeInTicks() {
        return this.entityData.get(AGE_TICKS);
    }
    public void setAgeInTicks(int age) {
        this.entityData.set(AGE_TICKS, age);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ENABLE, false);
        this.entityData.define(DIMENSION, "");
        this.entityData.define(X, 0);
        this.entityData.define(Y, 0);
        this.entityData.define(Z, 0);
        this.entityData.define(AGE_TICKS, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putBoolean("Enable", this.isEnable());

        if(isEnable()){
            pCompound.putString("RespawnDimension", this.getDimension());
            BlockPos pos = this.getRespawnPos();
            pCompound.putInt("RespawnX", pos.getX());
            pCompound.putInt("RespawnY", pos.getY());
            pCompound.putInt("RespawnZ", pos.getZ());
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        boolean enable = pCompound.getBoolean("Enable");
        this.setEnable(enable);

        if(enable){
            this.setDimension(pCompound.getString("RespawnDimension"));
            int x = pCompound.getInt("RespawnX");
            int y = pCompound.getInt("RespawnY");
            int z = pCompound.getInt("RespawnZ");
            this.setRespawnPos(new BlockPos(x, y, z));
        }
    }

    @Override
    public ErsTamable<?> getBreedOffspring(@Nonnull ServerLevel level, @Nonnull AgeableMob otherParent) {
        return null;
    }

    public boolean isMoving() {
        return ErsUtils.isMoving(this);
    }
}
