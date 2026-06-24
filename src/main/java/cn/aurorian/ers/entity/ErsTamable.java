package cn.aurorian.ers.entity;

import cn.aurorian.ers.config.ErsServerConfig;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsSerializers;
import cn.aurorian.ers.item.FilledGildedHorn;
import cn.aurorian.ers.util.ErsUtils;
import java.util.UUID;
import javax.annotation.Nonnull;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

/**
 * 可驯服生物的基础抽象类。
 *
 * <p>继承自Minecraft的{@link TamableAnimal}并实现GeckoLib的{@link GeoEntity}接口， 提供了以下功能：
 *
 * <ul>
 *   <li>驯服系统 - 玩家可以驯服并成为生物的主人
 *   <li>年龄系统 - 生物会随时间成长
 *   <li>饥饿度系统 - 生物需要定期喂食
 *   <li>重生系统 - 驯服的生物可以在指定位置重生
 *   <li>灵魂模式 - 特殊的灵魂形态生物
 *   <li>命令系统 - 玩家可以给生物下达命令
 *   <li>攻击状态 - 管理生物的攻击行为
 * </ul>
 *
 * <p>所有模组中的可骑乘和可驯服生物都应该继承此类。
 *
 * @param <T> 具体的生物子类类型
 * @author mlus
 * @version 1.2.0-alpha
 * @see TamableAnimal
 * @see GeoEntity
 * @see ErsEntity
 */
public abstract class ErsTamable<T extends ErsTamable<?>> extends TamableAnimal implements GeoEntity, ErsEntity<T> {
    protected ErsTamable(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    private static final EntityDataAccessor<Boolean> ENABLE =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<String> DIMENSION =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.STRING);

    private static final EntityDataAccessor<Integer> X =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> Y =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> Z =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> AGE_TICKS =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> HUNGER =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Integer> COMMAND =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<MobAttack> ATTACK_STATE =
            SynchedEntityData.defineId(ErsTamable.class, ErsSerializers.MOB_ATTACK_SERIALIZER.get());

    private static final EntityDataAccessor<Vector3f> FOOD_POSITION =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.VECTOR3);

    private static final EntityDataAccessor<Boolean> SOUL =
            SynchedEntityData.defineId(ErsTamable.class, EntityDataSerializers.BOOLEAN);

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected boolean doHunger = false;

    protected boolean doAgeTick = false;

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean canSprint() {
        return true;
    }

    public boolean isRespawnEnable() {
        return this.entityData.get(ENABLE);
    }

    public void setEnable(boolean enable) {
        this.entityData.set(ENABLE, enable);
    }

    public BlockPos getRespawnPos() {
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

    public float getHunger() {
        return this.entityData.get(HUNGER);
    }

    public void setHunger(float hunger) {
        entityData.set(HUNGER, java.lang.Math.max(java.lang.Math.min(hunger, 100), 0));
    }

    public void setAttackState(MobAttack mobAttack) {
        this.entityData.set(ATTACK_STATE, mobAttack);
    }

    public MobAttack getAttackState() {
        return this.entityData.get(ATTACK_STATE);
    }

    public void setFoodPosition(Vector3f foodPosition) {
        entityData.set(FOOD_POSITION, foodPosition);
    }

    public Vector3f getFoodPosition() {
        return entityData.get(FOOD_POSITION);
    }

    public int getCommand() {
        return this.entityData.get(COMMAND);
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, command);
    }

    public boolean isSoul() {
        return this.entityData.get(SOUL);
    }

    public void setSoul(boolean soul) {
        this.entityData.set(SOUL, soul);
    }

    public void feed(int foodAmount) {
        setHunger(getHunger() + foodAmount);
    }

    public boolean doHunger() {
        return this.doHunger;
    }

    public boolean doAgeTick() {
        return this.doAgeTick;
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
        this.entityData.define(HUNGER, 100f);
        this.entityData.define(COMMAND, 0);
        this.entityData.define(ATTACK_STATE, new MobAttack(AttackType.EMPTY, this));
        this.entityData.define(FOOD_POSITION, new Vector3f(0, 0, 0));
        this.entityData.define(SOUL, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        if (isTame()) {
            compound.putString("StringUUID", this.getStringUUID());
        }
        compound.putBoolean("Enable", this.isRespawnEnable());

        if (isRespawnEnable()) {
            compound.putString("RespawnDimension", this.getDimension());
            BlockPos pos = this.getRespawnPos();
            compound.putInt("RespawnX", pos.getX());
            compound.putInt("RespawnY", pos.getY());
            compound.putInt("RespawnZ", pos.getZ());
        }

        if (this.doHunger) {
            compound.putFloat("Hunger", this.getHunger());
        }
        if (this.doAgeTick) {
            compound.putInt("AgeTicks", this.getAgeInTicks());
        }

        compound.putBoolean("Soul", isSoul());

        if (hasCustomName()) compound.putString("CustomName", Component.Serializer.toJson(this.getCustomName()));
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        boolean enable = compoundTag.getBoolean("Enable");
        this.setEnable(enable);

        if (enable) {
            this.setDimension(compoundTag.getString("RespawnDimension"));
            int x = compoundTag.getInt("RespawnX");
            int y = compoundTag.getInt("RespawnY");
            int z = compoundTag.getInt("RespawnZ");
            this.setRespawnPos(new BlockPos(x, y, z));
        }

        if (this.doHunger) {
            this.setHunger(compoundTag.getFloat("Hunger"));
        }

        if (this.doAgeTick) {
            this.setAgeInTicks(compoundTag.getInt("AgeTicks"));
        }

        if (compoundTag.contains("StringUUID")) {
            setUUID(UUID.fromString(compoundTag.getString("StringUUID")));
        }

        setSoul(compoundTag.getBoolean("Soul"));

        if (compoundTag.contains("CustomName"))
            this.setCustomName(Component.Serializer.fromJson(compoundTag.getString("CustomName")));
    }

    @Override
    protected void dropFromLootTable(@NotNull DamageSource pDamageSource, boolean pAttackedRecently) {
        if (isSoul()) return;
        super.dropFromLootTable(pDamageSource, pAttackedRecently);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor pLevel,
            @NotNull DifficultyInstance pDifficulty,
            @NotNull MobSpawnType pReason,
            @Nullable SpawnGroupData pSpawnData,
            @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            if (pDataTag != null && pDataTag.contains("StringUUID")) {
                setUUID(UUID.fromString(pDataTag.getString("StringUUID")));
            }
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return isSoul();
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            this.getAttackState().tick();
            if (tickCount % 20 == 0 && !isSoul()) {
                float hunger = getHunger();
                if (doAgeTick && hunger > 0) {
                    this.setAgeInTicks(this.getAgeInTicks() + 20 * ErsServerConfig.MATURE_RATE.get());
                }
            }
        }
    }

    public void startAttack(ErsAttackType type) {
        if (this.getAttackState().getType() == AttackType.KNOCK_DOWN_LEFT
                || this.getAttackState().getType() == AttackType.KNOCK_DOWN_RIGHT) {
            return;
        }

        this.setAttackState(new MobAttack(type, this));
        if (type.isForcePlay()) {
            this.stopTriggeredAnimation("attack", type.getAnimName());
        }
        triggerAnim("attack", type.getAnimName());
    }

    @Override
    public ErsTamable<?> getBreedOffspring(@Nonnull ServerLevel level, @Nonnull AgeableMob otherParent) {
        return null;
    }

    @Override
    public boolean isAlliedTo(@NotNull Entity pEntity) {
        return super.isAlliedTo(pEntity)
                || (pEntity instanceof TamableAnimal tamableAnimal
                        && getOwner() != null
                        && tamableAnimal.getOwner() == getOwner());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if (player.getMainHandItem().getItem() == ErsItems.GILDED_HORN.get()
                && this.isTame()
                && this.isOwnedBy(player)
                && !canBreatheUnderwater()) {
            return FilledGildedHorn.hornPickup(player, pHand, this).orElse(InteractionResult.PASS);
        }
        return super.mobInteract(player, pHand);
    }

    @Override
    public boolean canMate(@NotNull Animal pOtherAnimal) {
        if (pOtherAnimal == this) {
            return false;
        } else if (pOtherAnimal.getClass() != this.getClass()) {
            return false;
        } else {
            if (!(this instanceof HasGender)) return true;

            return this.isInLove()
                    && pOtherAnimal.isInLove()
                    && ((HasGender) this).getGender() != ((HasGender) pOtherAnimal).getGender();
        }
    }

    public void updateAgeFromServer() {}

    public boolean isMoving() {
        return ErsUtils.isMoving(this);
    }

    public int updateSkyBrightness() {
        return ErsUtils.updateSkyBrightness(level());
    }

    public boolean mightBeSleeping() {
        return false;
    }
}
