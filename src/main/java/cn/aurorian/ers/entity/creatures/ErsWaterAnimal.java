package cn.aurorian.ers.entity.creatures;

import cn.aurorian.ers.entity.GeneralBodyControl;
import java.util.List;
import java.util.stream.Stream;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.util.GeckoLibUtil;

public abstract class ErsWaterAnimal extends WaterAnimal implements GeoEntity, Bucketable {
    @Nullable
    private ErsWaterAnimal leader;

    private int schoolSize = 1;

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    protected ErsWaterAnimal(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    protected @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    private static final EntityDataAccessor<Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(ErsWaterAnimal.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
    }

    @Nullable
    public RandomSwimmingGoal randomSwimmingGoal;

    @Override
    protected void registerGoals() {
        this.randomSwimmingGoal = new RandomSwimmingGoal(this, 1, 40) {
            @Nullable
            @Override
            protected Vec3 getPosition() {
                return BehaviorUtils.getRandomSwimmablePos(this.mob, 35, 7);
            }
        };

        this.goalSelector.addGoal(2, this.randomSwimmingGoal);
        this.goalSelector.addGoal(3, new TryFindWaterGoal(this));
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this, 6);
    }

    public void saveToBucketTag(@NotNull ItemStack pStack) {
        Bucketable.saveDefaultDataToBucketTag(this, pStack);
    }

    public void loadFromBucketTag(@NotNull CompoundTag pTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, pTag);
    }

    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    public int getMaxSpawnClusterSize() {
        return this.getMaxSchoolSize();
    }

    public int getMaxSchoolSize() {
        return super.getMaxSpawnClusterSize();
    }

    protected boolean canRandomSwim() {
        return !this.isFollower();
    }

    public boolean isFollower() {
        return this.leader != null && this.leader.isAlive();
    }

    public ErsWaterAnimal startFollowing(ErsWaterAnimal pLeader) {
        this.leader = pLeader;
        pLeader.addFollower();
        return pLeader;
    }

    public void stopFollowing() {
        this.leader.removeFollower();
        this.leader = null;
    }

    private void addFollower() {
        ++this.schoolSize;
    }

    private void removeFollower() {
        --this.schoolSize;
    }

    public boolean canBeFollowed() {
        return this.hasFollowers() && this.schoolSize < this.getMaxSchoolSize();
    }

    private int stuckTicks = 0;
    private Vec3 lastStuckPos = Vec3.ZERO;

    public void tick() {
        super.tick();
        if (this.hasFollowers() && this.level().random.nextInt(200) == 1) {
            List<? extends ErsWaterAnimal> $$0 = this.level()
                    .getEntitiesOfClass(this.getClass(), this.getBoundingBox().inflate(14.0, 14.0, 14.0));
            if ($$0.size() <= 1) {
                this.schoolSize = 1;
            }
        }

        if (!this.level().isClientSide && this.isInWater() && this.tickCount % 20 == 0) {
            if (this.distanceToSqr(this.lastStuckPos) < 0.04D) {
                this.stuckTicks++;
                if (this.stuckTicks >= 3) {
                    this.getNavigation().stop();
                    if (this.randomSwimmingGoal != null) {
                        this.randomSwimmingGoal.trigger();
                    }
                    this.stuckTicks = 0;
                }
            } else {
                this.stuckTicks = 0;
            }
            this.lastStuckPos = this.position();
        }
    }

    public boolean hasFollowers() {
        return this.schoolSize > 1;
    }

    public boolean inRangeOfLeader() {
        return this.distanceToSqr(this.leader) <= 225.0;
    }

    public void pathToLeader() {
        if (this.isFollower()) {
            this.getNavigation().moveTo(this.leader, level().random.nextIntBetweenInclusive(8, 12) * 0.1f);
        }
    }

    public void addFollowers(Stream<? extends ErsWaterAnimal> pFollowers) {
        pFollowers
                .limit(this.getMaxSchoolSize() - this.schoolSize)
                .filter((p_27538_) -> p_27538_ != this)
                .forEach((p_27536_) -> p_27536_.startFollowing(this));
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName();
    }

    @Nullable
    public SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor pLevel,
            @NotNull DifficultyInstance pDifficulty,
            @NotNull MobSpawnType pReason,
            @Nullable SpawnGroupData pSpawnData,
            @Nullable CompoundTag pDataTag) {
        super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if (pSpawnData == null) {
            pSpawnData = new ErsWaterAnimal.SchoolSpawnGroupData(this);
        } else {
            this.startFollowing(((ErsWaterAnimal.SchoolSpawnGroupData) pSpawnData).leader);
        }

        return pSpawnData;
    }

    public static class SchoolSpawnGroupData implements SpawnGroupData {
        public final ErsWaterAnimal leader;

        public SchoolSpawnGroupData(ErsWaterAnimal pLeader) {
            this.leader = pLeader;
        }
    }
}
