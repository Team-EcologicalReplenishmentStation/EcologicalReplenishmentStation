package cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides;

import cn.aurorian.ers.client.animator.ChlamydoselachusAcanthodiiAnimator;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.movecontrol.AcanthodesMoveControl;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.ai.AcanthodesMeleeAttackGoal;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.navigation.AcanthodesNavigation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsMobEffects;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
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
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class AcanthodesChlamydoselachoidesEntity extends WaterAnimal implements GeoEntity, ErsEntity<AcanthodesChlamydoselachoidesEntity>, Bucketable {
    public AcanthodesChlamydoselachoidesEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new AcanthodesMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        animator = new ChlamydoselachusAcanthodiiAnimator(this);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GeneralAnimator<AcanthodesChlamydoselachoidesEntity> animator;
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(AcanthodesChlamydoselachoidesEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new AcanthodesNavigation(this,pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<AcanthodesChlamydoselachoidesEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            if(isInWater()){
                if (isAggressive()) {
                    builder.thenLoop("animation.swim");
                } else {
                    builder.thenLoop("animation.slow_swim");
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

        AnimationController<AcanthodesChlamydoselachoidesEntity> extra = new AnimationController<>(this, "extra", 4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"));

        controllerRegistrar.add(main,extra);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MOVEMENT_SPEED,1)
                .add(Attributes.FOLLOW_RANGE,64)
                .add(ForgeMod.SWIM_SPEED.get(),1);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public GeneralAnimator<AcanthodesChlamydoselachoidesEntity> getAnimator() {
        return animator;
    }

    @Override
    public void tick() {
        super.tick();
        animator.tick();
    }

    @Nullable
    public RandomSwimmingGoal randomSwimmingGoal;

    @Override
    protected void registerGoals() {
        this.randomSwimmingGoal = new RandomSwimmingGoal(this,1,40){
            @Override
            public boolean canUse() {
                return super.canUse() && !this.mob.isAggressive();
            }

            @Nullable
            @Override
            protected Vec3 getPosition() {
                return BehaviorUtils.getRandomSwimmablePos(this.mob, 35, 7);
            }
        };

        this.goalSelector.addGoal(1, new AcanthodesMeleeAttackGoal(this,2,false));
        this.goalSelector.addGoal(2, this.randomSwimmingGoal);
        this.goalSelector.addGoal(3, new TryFindWaterGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Squid.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class,true,
                entity -> entity.hasEffect(ErsMobEffects.BLEEDING.get()) && entity.getType() != this.getType()));
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

    protected @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return ErsMobLargeBucket.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }

    public void saveToBucketTag(@NotNull ItemStack pStack) {
        Bucketable.saveDefaultDataToBucketTag(this, pStack);
    }

    public void loadFromBucketTag(@NotNull CompoundTag pTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, pTag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.CHLAMYDOSELACHOIDES_LARGE_BUCKET.get());
    }

    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }
}
