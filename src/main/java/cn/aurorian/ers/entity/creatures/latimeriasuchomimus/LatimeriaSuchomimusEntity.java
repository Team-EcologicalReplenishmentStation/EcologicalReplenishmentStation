package cn.aurorian.ers.entity.creatures.latimeriasuchomimus;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.SarcopterySuchomimusAnimator;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.movecontrol.DemersalMoveControl;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.ai.SuchomimusMeleeAttackGoal;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobType;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class LatimeriaSuchomimusEntity extends ErsTamable<LatimeriaSuchomimusEntity> implements Bucketable {
    public LatimeriaSuchomimusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.moveControl = new DemersalMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        animator = new SarcopterySuchomimusAnimator(this);
    }

    private final GeneralAnimator<LatimeriaSuchomimusEntity> animator;
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(LatimeriaSuchomimusEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
    }

    public boolean canBreatheUnderwater() {
        return true;
    }

    public @NotNull MobType getMobType() {
        return MobType.WATER;
    }
    public boolean checkSpawnObstruction(LevelReader pLevel) {
        return pLevel.isUnobstructed(this);
    }

    public int getAmbientSoundInterval() {
        return 120;
    }

    public int getExperienceReward() {
        return 1 + this.level().random.nextInt(3);
    }
    public boolean isPushedByFluid() {
        return false;
    }

    public boolean canBeLeashed(@NotNull Player pPlayer) {
        return false;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new AmphibiousPathNavigation(this,pLevel);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {}

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<LatimeriaSuchomimusEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            if(isInWater()){
                if(state.isMoving()){
                    if (isAggressive()) {
                        builder.thenLoop("animation.swim");
                    } else {
                        builder.thenLoop("animation.slow_swim");
                    }
                }else {
                    builder.thenLoop("animation.idle");
                }
            }else {
                if(ErsUtils.isMoving(this)){
                    builder.thenLoop("animation.walk");
                }else {
                    builder.thenLoop("animation.idle");
                }
            }

            return state.setAndContinue(builder);
        });

        AnimationController<LatimeriaSuchomimusEntity> extra = new AnimationController<>(this, "extra", 4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"));

        controllerRegistrar.add(main,extra);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MOVEMENT_SPEED,1)
                .add(Attributes.FOLLOW_RANGE,24)
                .add(ForgeMod.SWIM_SPEED.get(),1);
    }

    public GeneralAnimator<LatimeriaSuchomimusEntity> getAnimator() {
        return animator;
    }

    @Override
    public void tick() {
        super.tick();
        animator.tick();
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new SuchomimusMeleeAttackGoal(this,1,false));
        this.goalSelector.addGoal(2, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(3, new RandomSwimmingGoal(this,1,140){
            @Nullable
            @Override
            protected Vec3 getPosition() {
                return BehaviorUtils.getRandomSwimmablePos(this.mob, 35, 5);
            }
        });
        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true){
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame();
            }
        });
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Monster.class, true){
            @Override
            public boolean canUse() {
                return super.canUse() && isTame();
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Animal.class,true,
                entity -> entity.getType() != this.getType()){
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame();
            }
        });
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this,6);
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
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

    @NotNull
    public InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if(player.getMainHandItem().getItem() == ErsItems.LARGE_WATER_BUCKET.get() && this.isTame() && this.isOwnedBy(player))
        {
            return ErsMobLargeBucket.bucketMobPickup(player, pHand, this).orElse(InteractionResult.PASS);
        }
        if (!level().isClientSide() && (player.getMainHandItem().is(Items.MUTTON) || player.getMainHandItem().is(Items.BEEF))) {
            if(!this.isTame()){
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)){
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte)7);
                }else{
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }
        }
        return super.mobInteract(player, pHand);
    }

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
        return new ItemStack(ErsItems.SUCHOMIMUS_LARGE_BUCKET.get());
    }

    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }
}
