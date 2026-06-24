package cn.aurorian.ers.entity.creatures.benthosuchusplanidens;

import cn.aurorian.ers.client.animator.BenthosuchusPlanidensPlanidensAnimator;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.movecontrol.DemersalMoveControl;
import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.ai.BenthosuchusPlanidensMeleeAttackGoal;
import cn.aurorian.ers.entity.creatures.echinomorphusconvergens.EchinomorphusConvergensEntity;
import cn.aurorian.ers.entity.creatures.tachycarisgustatus.TachycarisGustatusEntity;
import cn.aurorian.ers.entity.creatures.tachypleusgladius.TachypleusGladiusEntity;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.ai.navigation.WaterBoundPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class BenthosuchusPlanidensPlanidensEntity extends ErsTamable<BenthosuchusPlanidensPlanidensEntity>
        implements Bucketable {

    private final GeneralAnimator<BenthosuchusPlanidensPlanidensEntity> animator;

    private static final EntityDataAccessor<Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(BenthosuchusPlanidensPlanidensEntity.class, EntityDataSerializers.BOOLEAN);

    public BenthosuchusPlanidensPlanidensEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new DemersalMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setMaxUpStep(1.0F);
        animator = new BenthosuchusPlanidensPlanidensAnimator(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
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
    public boolean checkSpawnObstruction(LevelReader pLevel) {
        return pLevel.isUnobstructed(this);
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public boolean canBeLeashed(@NotNull Player pPlayer) {
        return false;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new WaterBoundPathNavigation(this, pLevel);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {}

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return (float) this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<BenthosuchusPlanidensPlanidensEntity> main =
                new AnimationController<>(this, "main", 4, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    if (isInWater()) {
                        if (state.isMoving() || yOld != getY()) {
                            if (isAggressive()) {
                                builder.thenLoop("animation.quickly_swim");
                            } else {
                                builder.thenLoop("animation.swim");
                            }
                        } else {
                            builder.thenLoop("animation.idle");
                        }
                    } else {
                        builder.thenLoop("animation.flop");
                    }
                    return state.setAndContinue(builder);
                });

        AnimationController<BenthosuchusPlanidensPlanidensEntity> extra = new AnimationController<>(
                        this, "attack", 4, state -> PlayState.STOP)
                .triggerableAnim("pickup", RawAnimation.begin().thenPlay("animation.pickup"))
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("hold", RawAnimation.begin().thenPlay("animation.hold"));

        controllerRegistrar.add(main, extra);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 60.0)
                .add(Attributes.ATTACK_DAMAGE, 25)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.11)
                .add(Attributes.FOLLOW_RANGE, 20)
                .add(ForgeMod.SWIM_SPEED.get(), 5);
    }

    @Override
    public GeneralAnimator<BenthosuchusPlanidensPlanidensEntity> getAnimator() {
        return animator;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            animator.tick();
        } else {
            if (tickCount % 400 == 0) {
                this.heal(1);
            }
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        return null;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new BenthosuchusPlanidensMeleeAttackGoal(this, 1.2f, false));
        this.goalSelector.addGoal(2, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this, 1, 100) {
            @Nullable
            @Override
            protected Vec3 getPosition() {
                return BehaviorUtils.getRandomSwimmablePos(this.mob, 35, 5);
            }
        });

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, TachypleusGladiusEntity.class, true));
        this.targetSelector.addGoal(1, new NearestAttackableTargetGoal<>(this, TachycarisGustatusEntity.class, true));
        this.targetSelector.addGoal(
                1, new NearestAttackableTargetGoal<>(this, EchinomorphusConvergensEntity.class, true));
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this, 6);
    }

    @Override
    public boolean canRiderInteract() {
        return true;
    }

    @Override
    public boolean shouldRiderSit() {
        return false;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 2;
    }

    @Override
    protected @NotNull SoundEvent getSwimSound() {
        return SoundEvents.FISH_SWIM;
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if (player.getMainHandItem().getItem() == ErsItems.LARGE_WATER_BUCKET.get()) {
            return ErsMobLargeBucket.bucketMobPickup(player, pHand, this).orElse(InteractionResult.PASS);
        }
        return super.mobInteract(player, pHand);
    }

    @Override
    protected void positionRider(@NotNull Entity pPassenger, @NotNull MoveFunction pCallback) {
        Vector3f vector3f = this.getFoodPosition();
        if (vector3f != null) {
            pCallback.accept(pPassenger, this.getX() + vector3f.x, this.getY() + vector3f.y, this.getZ() + vector3f.z);
        }
    }

    //    @Override
    //    protected void customServerAiStep() {
    //        super.customServerAiStep();
    //        if (isInWater() && !(this.moveControl instanceof DemersalMoveControl)) {
    //            switchNavigator(false);
    //        } else if (!isInWater() && this.moveControl instanceof DemersalMoveControl) {
    //            switchNavigator(true);
    //        }
    //    }

    //    protected void switchNavigator(boolean onLand) {
    //        if (onLand) {
    //            moveControl = new LimitedMoveControl(this);
    //            navigation = new MMGroundPathNavigation(this, level());
    //        } else {
    //            moveControl = new DemersalMoveControl(this);
    //            navigation = createNavigation(level());
    //        }
    //    }

    @Override
    public void saveToBucketTag(ItemStack pStack) {
        this.addAdditionalSaveData(pStack.getOrCreateTag());
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag pTag) {
        this.readAdditionalSaveData(pTag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.BENTHOSUCHUS_PLANIDENS_LARGE_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }
}
