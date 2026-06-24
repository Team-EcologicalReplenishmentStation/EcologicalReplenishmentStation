package cn.aurorian.ers.entity.creatures.remipessicarius;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.ConstantSwimGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtTargetGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowOwnerGoal;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.entity.creatures.remipessicarius.ai.RemipesSicariusMeleeAttackGoal;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.MoveControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.vehicle.Boat;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class RemipesSicariusEntity extends ErsTamable<RemipesSicariusEntity> implements GeoEntity, Bucketable {
    private final RemipesSicariusAnimator animator;
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(RemipesSicariusEntity.class, EntityDataSerializers.BOOLEAN);

    public RemipesSicariusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        this.setMaxUpStep(1.0F);
        this.animator = new RemipesSicariusAnimator(this);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(FROM_BUCKET, false);
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean isPushedByFluid() {
        return false;
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    @Override
    public boolean canBeLeashed(@NotNull Player pPlayer) {
        return false;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new AmphibiousPathNavigation(this, pLevel);
    }

    @Override
    protected void playStepSound(@NotNull net.minecraft.core.BlockPos pPos, @NotNull BlockState pState) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<RemipesSicariusEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            if (this.isInWater()) {
                if (this.isSprinting()) {
                    builder.thenLoop("animation.quickly_swim");
                } else {
                    builder.thenLoop("animation.swim");
                }
            } else {
                builder.thenLoop("animation.flop");
            }
            return state.setAndContinue(builder);
        });

        AnimationController<RemipesSicariusEntity> attack = new AnimationController<>(
                        this, "attack", 0, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"));

        controllerRegistrar.add(main, attack);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    public GeneralAnimator<RemipesSicariusEntity> getAnimator() {
        return this.animator;
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 12.0)
                .add(Attributes.ATTACK_DAMAGE, 3)
                .add(Attributes.ARMOR, 4)
                .add(Attributes.MOVEMENT_SPEED, 0.156)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0)
                .add(ForgeMod.SWIM_SPEED.get(), 2.6);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new RemipesSicariusMeleeAttackGoal(this, 1.0, false));
        this.goalSelector.addGoal(2, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new ConstantSwimGoal(this, 1.0, 60));
        this.goalSelector.addGoal(4, new MobFollowOwnerGoal(this, 1.2, 10, 2, false));

        this.targetSelector.addGoal(1, new ErsTamableOwnerHurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new ErsTamableOwnerHurtTargetGoal(this));
        this.targetSelector.addGoal(3, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && !RemipesSicariusEntity.this.isTame() && !isRidingLargeDragon(this.target);
            }
        });
        this.targetSelector.addGoal(5, new NearestAttackableTargetGoal<>(this, AbstractFish.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && !RemipesSicariusEntity.this.isTame();
            }
        });
    }

    private static boolean isRidingLargeDragon(@Nullable LivingEntity target) {
        if (target == null) return false;
        Entity vehicle = target.getVehicle();
        return vehicle instanceof ErsTamableVehicle<?>;
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this, 6);
    }

    @Override
    public void tick() {
        super.tick();
        if (this.level().isClientSide()) {
            this.animator.tick();
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (this.isInWater() && !(this.moveControl instanceof SmoothSwimmingMoveControl)) {
            switchNavigator(false);
        } else if (!this.isInWater() && this.moveControl instanceof SmoothSwimmingMoveControl) {
            switchNavigator(true);
        }
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            this.moveControl = new MoveControl(this) {
                @Override
                public void tick() {
                    this.mob.setSpeed(0.0F);
                    this.mob.setXxa(0.0F);
                    this.mob.setYya(0.0F);
                    this.mob.setZza(0.0F);
                }
            };
            this.navigation = new MMGroundPathNavigation(this, this.level());
        } else {
            this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
            this.navigation = this.createNavigation(this.level());
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return itemStack.is(ItemTags.FISHES);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (player.getMainHandItem().is(Items.WATER_BUCKET) && (!this.isTame() || this.isOwnedBy(player))) {
            return Bucketable.bucketMobPickup(player, hand, this).orElse(super.mobInteract(player, hand));
        }

        if (!this.level().isClientSide() && this.isFood(player.getMainHandItem())) {
            if (!this.isTame()) {
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
                player.getMainHandItem().shrink(1);
            } else if (this.isOwnedBy(player)) {
                this.feed(1);
                this.heal(1);
                player.getMainHandItem().shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean doHurtTarget(@NotNull Entity target) {
        if (target instanceof Player player && player.getVehicle() instanceof Boat) {
            player.stopRiding();
        }
        return super.doHurtTarget(target);
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
    public void saveToBucketTag(@NotNull ItemStack pStack) {
        this.addAdditionalSaveData(pStack.getOrCreateTag());
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag pTag) {
        this.readAdditionalSaveData(pTag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.REMIPES_SICARIUS_BUCKET.get());
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return !this.fromBucket() && !this.hasCustomName() && !this.isTame();
    }
}
