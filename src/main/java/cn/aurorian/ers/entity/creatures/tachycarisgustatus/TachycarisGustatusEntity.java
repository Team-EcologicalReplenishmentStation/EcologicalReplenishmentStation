package cn.aurorian.ers.entity.creatures.tachycarisgustatus;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.GustatusAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
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
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class TachycarisGustatusEntity extends WaterAnimal
        implements GeoEntity, Bucketable, ErsEntity<TachycarisGustatusEntity> {
    public TachycarisGustatusEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        animator = new GustatusAnimator(this);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private final GeneralAnimator<TachycarisGustatusEntity> animator;

    private static final EntityDataAccessor<Integer> KNOCKDOWN =
            SynchedEntityData.defineId(TachycarisGustatusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(TachycarisGustatusEntity.class, EntityDataSerializers.BOOLEAN);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(KNOCKDOWN, 40);
        entityData.define(FROM_BUCKET, false);
    }

    public int getKnockdown() {
        return this.entityData.get(KNOCKDOWN);
    }

    public void setKnockdown(int knockdown) {
        this.entityData.set(KNOCKDOWN, knockdown);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<TachycarisGustatusEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            if (ErsUtils.isMoving(this)) {
                builder.thenLoop("animation.walk");
            } else {
                builder.thenLoop("animation.idle");
            }
            return state.setAndContinue(builder);
        });

        AnimationController<TachycarisGustatusEntity> extra = new AnimationController<>(
                        this, "extra", 4, state -> PlayState.STOP)
                .triggerableAnim("knockdown", RawAnimation.begin().thenPlay("animation.knockdown"));

        controllerRegistrar.add(main, extra);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.38)
                .add(Attributes.ARMOR, 8);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this) {
            @Override
            public boolean canUse() {
                return super.canUse() && getKnockdown() == 0;
            }
        });
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.0) {
            @Override
            public boolean canUse() {
                return super.canUse() && getKnockdown() == 0;
            }
        });
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this, 1.0, 40) {
            @Override
            public boolean canUse() {
                return super.canUse() && getKnockdown() == 0;
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        animator.tick();

        if (getKnockdown() > 0) {
            setKnockdown(getKnockdown() - 1);

            if (getKnockdown() == 0) {
                getAttribute(Attributes.ARMOR).setBaseValue(8);
            }
        }
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this, 20);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public GeneralAnimator<TachycarisGustatusEntity> getAnimator() {
        return animator;
    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    protected @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        if (!pPlayer.getMainHandItem().is(Items.WATER_BUCKET)) {
            triggerAnim("extra", "knockdown");
            setKnockdown(40);
            getAttribute(Attributes.ARMOR).setBaseValue(4);
            navigation.stop();
            return InteractionResult.SUCCESS;
        }

        return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }

    public void saveToBucketTag(@NotNull ItemStack pStack) {
        Bucketable.saveDefaultDataToBucketTag(this, pStack);
    }

    public void loadFromBucketTag(@NotNull CompoundTag pTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, pTag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.TACHYCARIS_GUSTATUS_BUCKET.get());
    }

    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    protected void handleAirSupply(int pAirSupply) {}
}
