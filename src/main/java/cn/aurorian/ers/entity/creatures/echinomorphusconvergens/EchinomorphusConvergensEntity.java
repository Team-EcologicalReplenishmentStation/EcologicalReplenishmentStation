package cn.aurorian.ers.entity.creatures.echinomorphusconvergens;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.SpawnVariant;
import cn.aurorian.ers.entity.ai.movecontrol.DemersalMoveControl;
import cn.aurorian.ers.entity.creatures.ErsWaterAnimal;
import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.BenthosuchusPlanidensPlanidensEntity;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
import com.mojang.serialization.Codec;
import java.util.function.IntFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class EchinomorphusConvergensEntity extends ErsWaterAnimal
        implements GeoEntity,
                ErsEntity<EchinomorphusConvergensEntity>,
                VariantHolder<EchinomorphusConvergensEntity.Variant> {

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(EchinomorphusConvergensEntity.class, EntityDataSerializers.INT);

    public EchinomorphusConvergensEntity(EntityType<? extends ErsWaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new DemersalMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GeneralAnimator<EchinomorphusConvergensEntity> animator = new GeneralAnimator<>(this);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(DATA_VARIANT, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putInt("Variant", this.getVariant().getId());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setVariant(Variant.byId(compoundTag.getInt("Variant")));
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) animator.tick();
    }

    @Override
    public void aiStep() {
        if (!this.isInWater()) {
            this.setOnGround(false);
            this.hasImpulse = false;
        }
        super.aiStep();
    }

    @Override
    protected void registerGoals() {
        this.randomSwimmingGoal = new RandomSwimmingGoal(this, 1, 400) {
            @Nullable
            @Override
            protected Vec3 getPosition() {
                return BehaviorUtils.getRandomSwimmablePos(this.mob, 5, 5);
            }
        };

        this.goalSelector.addGoal(2, this.randomSwimmingGoal);
        this.goalSelector.addGoal(3, new TryFindWaterGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<EchinomorphusConvergensEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            if (isInWater()) {
                if (ErsUtils.isMoving(this)) {
                    builder.thenLoop("animation.idle2");
                } else {
                    builder.thenLoop("animation.idle");
                }
            } else {
                builder.thenLoop("animation.flop");
            }
            return state.setAndContinue(builder);
        });
        controllerRegistrar.add(main);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public GeneralAnimator<EchinomorphusConvergensEntity> getAnimator() {
        return animator;
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 4.0)
                .add(Attributes.MOVEMENT_SPEED, 0.08)
                .add(Attributes.ARMOR, 8.0);
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public void playerTouch(@NotNull Player player) {
        player.hurt(level().damageSources().thorns(this), 1.0F);
        super.playerTouch(player);
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.MOB_ATTACK) || pSource.is(DamageTypes.PLAYER_ATTACK)) {
            if (pSource.getEntity() instanceof LivingEntity living
                    && !(pSource.getEntity() instanceof BenthosuchusPlanidensPlanidensEntity)) {
                living.hurt(level().damageSources().thorns(this), 1.0F);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor pLevel,
            @NotNull DifficultyInstance pDifficulty,
            @NotNull MobSpawnType pReason,
            @Nullable SpawnGroupData pSpawnData,
            @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            return pSpawnData;
        } else {
            RandomSource random = pLevel.getRandom();
            this.setVariant(SpawnVariant.getCommonSpawnVariant(Variant.values(), random));
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        if (getLastHurtByMob() instanceof BenthosuchusPlanidensPlanidensEntity) {
            ItemStack stack = new ItemStack(ErsItems.ECHINOMORPHUS_CONVERGENS_SHELL.get());
            this.spawnAtLocation(stack);
        }
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
    }

    public @NotNull Variant getVariant() {
        return Variant.byId(getVariantId());
    }

    public void setVariant(Variant pVariant) {
        setVariantId(pVariant.getId());
    }

    private int getVariantId() {
        return this.entityData.get(DATA_VARIANT);
    }

    private void setVariantId(int pId) {
        this.entityData.set(DATA_VARIANT, pId);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.ECHINOMORPHUS_CONVERGENS_BUCKET.get());
    }

    public enum Variant implements StringRepresentable, SpawnVariant {
        PINK(0, "pink", true),
        YELLOW(1, "yellow", true);

        private static final IntFunction<Variant> BY_ID =
                ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        private final int id;
        private final String name;
        private final boolean common;

        Variant(int pId, String pName, boolean pCommon) {
            this.id = pId;
            this.name = pName;
            this.common = pCommon;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public boolean isCommon() {
            return this.common;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }

        public static Variant byId(int pId) {
            return BY_ID.apply(pId);
        }
    }
}
