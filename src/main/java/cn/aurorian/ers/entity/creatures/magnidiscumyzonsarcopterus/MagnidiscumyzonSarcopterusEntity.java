package cn.aurorian.ers.entity.creatures.magnidiscumyzonsarcopterus;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.MagnidiscumyzonSarcopterusAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.movecontrol.DemersalMoveControl;
import cn.aurorian.ers.entity.creatures.magnidiscumyzonsarcopterus.ai.SarcopterusEatGoal;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MagnidiscumyzonSarcopterusEntity extends AbstractFish
        implements GeoEntity, ErsEntity<MagnidiscumyzonSarcopterusEntity> {
    public MagnidiscumyzonSarcopterusEntity(EntityType<? extends AbstractFish> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        animator = new MagnidiscumyzonSarcopterusAnimator(this);
        this.moveControl = new DemersalMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GeneralAnimator<MagnidiscumyzonSarcopterusEntity> animator;
    private static final EntityDataAccessor<Boolean> EATING =
            SynchedEntityData.defineId(MagnidiscumyzonSarcopterusEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isEating() {
        return this.entityData.get(EATING);
    }

    public void setEating(boolean eating) {
        this.entityData.set(EATING, eating);
    }

    @Override
    protected @NotNull SoundEvent getFlopSound() {
        return SoundEvents.SALMON_FLOP;
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.SARCOPTERUS_BUCKET.get());
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pBlock) {}

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
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<MagnidiscumyzonSarcopterusEntity> main =
                new AnimationController<>(this, "main", 10, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    if (isEating()) {
                        if (state.isMoving()) {
                            builder.thenLoop("animation.eat");
                        } else {
                            builder.thenLoop("animation.eat_idle");
                        }
                    } else {
                        if (state.isMoving()) {
                            builder.thenLoop("animation.swim");
                        } else {
                            builder.thenLoop("animation.idle");
                        }
                    }
                    return state.setAndContinue(builder);
                });

        controllerRegistrar.add(main);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(EATING, false);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(5, new SarcopterusEatGoal(this));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public GeneralAnimator<MagnidiscumyzonSarcopterusEntity> getAnimator() {
        return animator;
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes().add(Attributes.MAX_HEALTH, 6.0);
    }
}
