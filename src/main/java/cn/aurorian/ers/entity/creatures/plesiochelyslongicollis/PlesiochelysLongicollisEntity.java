package cn.aurorian.ers.entity.creatures.plesiochelyslongicollis;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.PlesiochelysLongicollisAnimator;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsMobEffects;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.FollowOwnerGoal;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.SitWhenOrderedToGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.level.pathfinder.PathComputationType;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class PlesiochelysLongicollisEntity extends ErsTamable<PlesiochelysLongicollisEntity> implements Bucketable {
    private static final EntityDataAccessor<Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(PlesiochelysLongicollisEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> BASKING =
            SynchedEntityData.defineId(PlesiochelysLongicollisEntity.class, EntityDataSerializers.BOOLEAN);

    private static final int COMFORT_DURATION = 160;
    private static final int COMFORT_AURA_INTERVAL = 40;
    private static final int COMFORT_RADIUS = 16;
    private static final int MAX_COMFORT_AMPLIFIER = 2;
    private static final String COMFORT_SOURCE_TAG = "ers:plesiochelys_comfort_sources";
    private static final int BASKING_MIN_DURATION = 1200;
    private static final int BASKING_MAX_DURATION = 14400;
    private static final int LAND_SEEKING_CHANCE = 2400;
    private static final int BASKING_START_CHANCE = 2;
    private static final int BASKING_CHECK_INTERVAL = 600;
    private static final int BASKING_MIN_LIGHT_LEVEL = 9;

    private final GeneralAnimator<PlesiochelysLongicollisEntity> animator = new PlesiochelysLongicollisAnimator(this);
    private int landSeekingTicks;
    private int lastComfortAuraTick = -COMFORT_DURATION;
    private int baskingTicksRemaining;
    private int baskingCheckCooldown = BASKING_CHECK_INTERVAL;
    private boolean hasPreviousYaw;
    private float previousYaw;
    private int turnAnimationTicks;
    private int turnDirection;

    public PlesiochelysLongicollisEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.doAgeTick = false;
        this.doHunger = false;
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setMaxUpStep(1.0F);
        switchNavigator(false);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
        this.entityData.define(BASKING, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putBoolean("FromBucket", fromBucket());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        setFromBucket(compound.getBoolean("FromBucket"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SitWhenOrderedToGoal(this));
        this.goalSelector.addGoal(2, new FindWaterAwayFromPlayerGoal(this, 10.0F, 1D));
        this.goalSelector.addGoal(3, new FishTemptGoal(this, 0.8D));
        this.goalSelector.addGoal(4, new FindLandGoal(this, 0.9D));
        this.goalSelector.addGoal(5, new FollowOwnerGoal(this, 1.0D, 8.0F, 3.0F, true));
        this.goalSelector.addGoal(6, new RandomSwimmingGoal(this, 0.8D, 80));
        this.goalSelector.addGoal(7, new RandomStrollGoal(this, 0.8D, 80));
        this.goalSelector.addGoal(8, new LookAtPlayerGoal(this, Player.class, 8.0F));
        this.targetSelector.addGoal(1, new OwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new OwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        updateTurnAnimationState();
        if (level().isClientSide()) {
            animator.tick();
            return;
        }

        updateNavigator();
        if (isInWater() && --landSeekingTicks <= 0 && random.nextInt(LAND_SEEKING_CHANCE) == 0) {
            landSeekingTicks = 600;
        }
        updateBasking();
    }

    private void updateTurnAnimationState() {
        float currentYaw = getYRot();
        if (!hasPreviousYaw) {
            hasPreviousYaw = true;
            previousYaw = currentYaw;
            return;
        }

        float yawDelta = Mth.degreesDifference(previousYaw, currentYaw);
        previousYaw = currentYaw;
        if (!isInWater() && !isBasking() && Math.abs(yawDelta) > 1.0F) {
            turnDirection = yawDelta > 0.0F ? 1 : -1;
            turnAnimationTicks = 10;
        } else if (turnAnimationTicks > 0) {
            turnAnimationTicks--;
        } else {
            turnDirection = 0;
        }
    }

    private boolean isTurningRight() {
        return turnAnimationTicks > 0 && turnDirection > 0;
    }

    private boolean isTurningLeft() {
        return turnAnimationTicks > 0 && turnDirection < 0;
    }

    private boolean isMovingForAnimation() {
        return getDeltaMovement().horizontalDistanceSqr() > 1.0E-5D
                || !getNavigation().isDone();
    }

    private void updateNavigator() {
        if (isInWater() && !(this.moveControl instanceof AquaticMoveControl)) {
            switchNavigator(false);
        } else if (!isInWater() && this.moveControl instanceof AquaticMoveControl) {
            switchNavigator(true);
        }
    }

    private void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new LimitedMoveControl(this);
            this.navigation = new MMGroundPathNavigation(this, level());
        } else {
            this.moveControl = new AquaticMoveControl(this, 1.0F).applyGravity(false);
            this.navigation = new WaterBoundPathNavigation(this, level());
        }
    }

    private void updateBasking() {
        boolean canBask =
                !isInWater() && onGround() && getTarget() == null && hasBaskingLight() && !hasUntamedNearbyPlayer();
        if (isBasking()) {
            if (!canBask || --baskingTicksRemaining <= 0) {
                stopBasking();
            } else if (tickCount - lastComfortAuraTick >= COMFORT_AURA_INTERVAL) {
                applyComfortAura();
            }
            return;
        }

        boolean canCheckBasking =
                canBask && getNavigation().isDone() && getDeltaMovement().horizontalDistanceSqr() < 0.0025D;
        if (!canCheckBasking) {
            baskingCheckCooldown = BASKING_CHECK_INTERVAL;
            return;
        }

        if (--baskingCheckCooldown > 0) {
            return;
        }
        baskingCheckCooldown = BASKING_CHECK_INTERVAL;
        if (random.nextInt(BASKING_START_CHANCE) == 0) {
            startBasking();
        }
    }

    private boolean hasUntamedNearbyPlayer() {
        return !isTame() && level().getNearestPlayer(this, 8.0D) != null;
    }

    private boolean hasBaskingLight() {
        BlockPos pos = blockPosition();
        return level().isDay()
                && level().canSeeSky(pos)
                && !level().isRainingAt(pos)
                && level().getRawBrightness(pos, 0) >= BASKING_MIN_LIGHT_LEVEL;
    }

    private void applyComfortAura() {
        lastComfortAuraTick = tickCount;
        long gameTime = level().getGameTime();
        for (ErsTamable<?> tamable :
                level().getEntitiesOfClass(ErsTamable.class, getBoundingBox().inflate(COMFORT_RADIUS))) {
            applyComfortFromThisTurtle(tamable, gameTime);
        }
    }

    private void applyComfortFromThisTurtle(ErsTamable<?> tamable, long gameTime) {
        CompoundTag persistentData = tamable.getPersistentData();
        CompoundTag sources = persistentData.getCompound(COMFORT_SOURCE_TAG);
        for (String source : new ArrayList<>(sources.getAllKeys())) {
            if (sources.getLong(source) <= gameTime) {
                sources.remove(source);
            }
        }

        sources.putLong(getStringUUID(), gameTime + COMFORT_DURATION);
        persistentData.put(COMFORT_SOURCE_TAG, sources);
        int amplifier = Math.min(sources.getAllKeys().size(), MAX_COMFORT_AMPLIFIER + 1) - 1;
        tamable.addEffect(
                new MobEffectInstance(ErsMobEffects.COMFORT.get(), COMFORT_DURATION, amplifier, false, false, true));
    }

    private void startBasking() {
        baskingTicksRemaining = BASKING_MIN_DURATION + random.nextInt(BASKING_MAX_DURATION - BASKING_MIN_DURATION + 1);
        setBasking(true);
        applyComfortAura();
    }

    private void stopBasking() {
        setBasking(false);
        baskingTicksRemaining = 0;
        baskingCheckCooldown = BASKING_CHECK_INTERVAL;
    }

    public boolean isBasking() {
        return entityData.get(BASKING);
    }

    public void setBasking(boolean basking) {
        entityData.set(BASKING, basking);
    }

    @Override
    public boolean hurt(@NotNull DamageSource source, float amount) {
        boolean wasBasking = isBasking();
        boolean hurt = super.hurt(source, amount);
        if (hurt && wasBasking && !level().isClientSide) {
            stopBasking();
        }
        return hurt;
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        if (stack.getItem() == ErsItems.LARGE_WATER_BUCKET.get()) {
            return ErsMobLargeBucket.bucketMobPickup(player, hand, this).orElse(InteractionResult.PASS);
        }

        if (isFood(stack)) {
            if (!level().isClientSide) {
                if (!player.getAbilities().instabuild) {
                    stack.shrink(1);
                }
                if (isTame()) {
                    heal(4.0F);
                    feed(3);
                } else if (random.nextInt(3) == 0) {
                    tame(player);
                    setOrderedToSit(true);
                    navigation.stop();
                    level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    level().broadcastEntityEvent(this, (byte) 6);
                }
            }
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        return super.mobInteract(player, hand);
    }

    public boolean isFood(ItemStack stack) {
        return stack.is(ItemTags.FISHES) || stack.is(ErsItems.FISH_FEED.get());
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.WATER;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(LevelReader level) {
        return level.isUnobstructed(this);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new MMGroundPathNavigation(this, level);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this, 7);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pos, @NotNull BlockState state) {}

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.TURTLE_SWIM;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Nullable
    @Override
    public PlesiochelysLongicollisEntity getBreedOffspring(
            @NotNull net.minecraft.server.level.ServerLevel level, @NotNull AgeableMob otherParent) {
        return null;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<PlesiochelysLongicollisEntity> main = new AnimationController<>(this, "main", 5, state -> {
            RawAnimation animation = RawAnimation.begin();
            boolean moving = state.isMoving() || isMovingForAnimation();
            if (isBasking()) {
                animation.thenLoop("animation.idle_sit");
            } else if (isInWater()) {
                if (moving) {
                    animation.thenLoop(isSprinting() ? "animation.quickly_swim" : "animation.swim");
                } else {
                    animation.thenLoop("animation.idle");
                }
            } else if (isTurningRight()) {
                animation.thenLoop("animation.turn_right");
            } else if (isTurningLeft()) {
                animation.thenLoop("animation.turn_left");
            } else if (moving) {
                animation.thenLoop("animation.walk");
            } else {
                animation.thenLoop("animation.idle");
            }
            return state.setAndContinue(animation);
        });
        controllerRegistrar.add(main);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 28.0D)
                .add(Attributes.ARMOR, 6.0D)
                .add(Attributes.MOVEMENT_SPEED, 0.12D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(ForgeMod.SWIM_SPEED.get(), 3D);
    }

    @Override
    public GeneralAnimator<PlesiochelysLongicollisEntity> getAnimator() {
        return animator;
    }

    @Override
    public boolean fromBucket() {
        return entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean fromBucket) {
        entityData.set(FROM_BUCKET, fromBucket);
    }

    @Override
    public void saveToBucketTag(@NotNull ItemStack stack) {
        addAdditionalSaveData(stack.getOrCreateTag());
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag tag) {
        readAdditionalSaveData(tag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.PLESIOCHELYS_LONGICOLLIS_LARGE_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    private static boolean isStillTemptingPlayer(Player player) {
        return isHoldingFish(player) && player.getDeltaMovement().horizontalDistanceSqr() < 0.0025D;
    }

    private static boolean isHoldingFish(Player player) {
        return player.getMainHandItem().is(ItemTags.FISHES)
                || player.getMainHandItem().is(ErsItems.FISH_FEED.get())
                || player.getOffhandItem().is(ItemTags.FISHES)
                || player.getOffhandItem().is(ErsItems.FISH_FEED.get());
    }

    private static boolean isLand(Level level, BlockPos pos) {
        return level.getBlockState(pos.below()).isSolidRender(level, pos.below())
                && level.getFluidState(pos).isEmpty()
                && level.getBlockState(pos).getCollisionShape(level, pos).isEmpty()
                && level.getBlockState(pos.above())
                        .getCollisionShape(level, pos.above())
                        .isEmpty();
    }

    private static boolean touchesWater(Level level, BlockPos pos) {
        for (BlockPos check : BlockPos.betweenClosed(pos.offset(-1, -1, -1), pos.offset(1, 1, 1))) {
            if (level.getFluidState(check).is(FluidTags.WATER)) {
                return true;
            }
        }
        return false;
    }

    private static @Nullable BlockPos findClosestLandNearWater(PathfinderMob mob, int horizontal, int vertical) {
        BlockPos origin = mob.blockPosition();
        return BlockPos.betweenClosedStream(
                        origin.offset(-horizontal, -vertical, -horizontal),
                        origin.offset(horizontal, vertical, horizontal))
                .filter(pos -> isLand(mob.level(), pos) && touchesWater(mob.level(), pos))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(origin)))
                .map(BlockPos::immutable)
                .orElse(null);
    }

    private static @Nullable BlockPos findClosestWater(PathfinderMob mob, int horizontal, int vertical) {
        BlockPos origin = mob.blockPosition();
        return BlockPos.betweenClosedStream(
                        origin.offset(-horizontal, -vertical, -horizontal),
                        origin.offset(horizontal, vertical, horizontal))
                .filter(pos -> mob.level().getFluidState(pos).is(FluidTags.WATER)
                        && mob.level()
                                .getBlockState(pos.above())
                                .isPathfindable(mob.level(), pos.above(), PathComputationType.WATER))
                .min(Comparator.comparingDouble(pos -> pos.distSqr(origin)))
                .map(BlockPos::immutable)
                .orElse(null);
    }

    private static @Nullable BlockPos findWaterAwayFromPlayer(
            PathfinderMob mob, Player player, int horizontal, int vertical) {
        BlockPos origin = mob.blockPosition();
        double currentDistance = player.distanceToSqr(mob);
        return BlockPos.betweenClosedStream(
                        origin.offset(-horizontal, -vertical, -horizontal),
                        origin.offset(horizontal, vertical, horizontal))
                .filter(pos -> mob.level().getFluidState(pos).is(FluidTags.WATER)
                        && mob.level()
                                .getBlockState(pos.above())
                                .isPathfindable(mob.level(), pos.above(), PathComputationType.WATER)
                        && player.distanceToSqr(Vec3.atCenterOf(pos)) > currentDistance)
                .max(Comparator.comparingDouble(pos -> player.distanceToSqr(Vec3.atCenterOf(pos))))
                .map(BlockPos::immutable)
                .orElse(null);
    }

    private static class FindLandGoal extends Goal {
        private final PlesiochelysLongicollisEntity mob;
        private final double speed;
        private BlockPos targetPos;

        private FindLandGoal(PlesiochelysLongicollisEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            setFlags(EnumSet.of(Flag.MOVE));
        }

        @Override
        public boolean canUse() {
            if (!mob.isInWater() || mob.landSeekingTicks <= 0 || mob.getTarget() != null) {
                return false;
            }
            targetPos = findClosestLandNearWater(mob, 14, 5);
            return targetPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return targetPos != null && mob.isInWater() && mob.landSeekingTicks > 0;
        }

        @Override
        public void stop() {
            targetPos = null;
        }

        @Override
        public void tick() {
            mob.getMoveControl()
                    .setWantedPosition(targetPos.getX() + 0.5D, targetPos.getY(), targetPos.getZ() + 0.5D, speed);
            if (mob.distanceToSqr(Vec3.atCenterOf(targetPos)) < 4.0D) {
                mob.landSeekingTicks = 0;
            }
        }
    }

    private static class FindWaterAwayFromPlayerGoal extends Goal {
        private final PlesiochelysLongicollisEntity mob;
        private final float radius;
        private final double speed;
        private Player player;
        private BlockPos waterPos;
        private int repathTicks;

        private FindWaterAwayFromPlayerGoal(PlesiochelysLongicollisEntity mob, float radius, double speed) {
            this.mob = mob;
            this.radius = radius;
            this.speed = speed;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (mob.isTame()) {
                return false;
            }
            player = mob.level().getNearestPlayer(mob, radius);
            if (player == null || isStillTemptingPlayer(player)) {
                return false;
            }
            waterPos = mob.isInWater() ? findWaterAwayFromPlayer(mob, player, 16, 5) : findClosestWater(mob, 16, 5);
            return waterPos != null;
        }

        @Override
        public boolean canContinueToUse() {
            return player != null
                    && player.isAlive()
                    && !mob.isTame()
                    && !isStillTemptingPlayer(player)
                    && mob.distanceToSqr(player) < radius * radius
                    && waterPos != null;
        }

        @Override
        public void start() {
            repathTicks = 0;
            mob.stopBasking();
            mob.setSprinting(true);
        }

        @Override
        public void stop() {
            player = null;
            waterPos = null;
            mob.setSprinting(false);
        }

        @Override
        public void tick() {
            mob.stopBasking();
            mob.getLookControl().setLookAt(player, 30.0F, 30.0F);
            if (mob.isInWater()) {
                if (--repathTicks <= 0 || mob.distanceToSqr(Vec3.atCenterOf(waterPos)) < 4.0D) {
                    BlockPos nextWaterPos = findWaterAwayFromPlayer(mob, player, 16, 5);
                    if (nextWaterPos != null) {
                        waterPos = nextWaterPos;
                    }
                    repathTicks = 20;
                }
                mob.getNavigation().moveTo(waterPos.getX() + 0.5D, waterPos.getY(), waterPos.getZ() + 0.5D, speed);
            } else {
                mob.getNavigation().moveTo(waterPos.getX() + 0.5D, waterPos.getY(), waterPos.getZ() + 0.5D, speed);
            }
        }
    }

    private static class FishTemptGoal extends Goal {
        private final PlesiochelysLongicollisEntity mob;
        private final double speed;
        private Player player;

        private FishTemptGoal(PlesiochelysLongicollisEntity mob, double speed) {
            this.mob = mob;
            this.speed = speed;
            setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
        }

        @Override
        public boolean canUse() {
            if (mob.isTame()) {
                return false;
            }
            player = mob.level().getNearestPlayer(mob, 10.0D);
            return player != null && isStillTemptingPlayer(player);
        }

        @Override
        public boolean canContinueToUse() {
            return player != null
                    && player.isAlive()
                    && !mob.isTame()
                    && mob.distanceToSqr(player) < 100.0D
                    && isStillTemptingPlayer(player);
        }

        @Override
        public void stop() {
            player = null;
            mob.getNavigation().stop();
        }

        @Override
        public void tick() {
            mob.stopBasking();
            mob.getLookControl().setLookAt(player, 30.0F, 30.0F);
            if (mob.distanceToSqr(player) > 6.25D) {
                mob.getNavigation().moveTo(player, speed);
            } else {
                mob.getNavigation().stop();
            }
        }
    }
}
