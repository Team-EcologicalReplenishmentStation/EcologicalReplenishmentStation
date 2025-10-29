package cn.aurorian.oasis.entity.pygopodusannulatum;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.goal.MobAvodingEntityGoal;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.oasis.client.animator.PygopodusAnnulatumAnimator;
import cn.aurorian.oasis.entity.pygopodusannulatum.ai.AnnulatumEatGoal;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
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

public class PygopodusAnnulatumEntity extends Animal implements GeoEntity, ErsEntity<PygopodusAnnulatumEntity> {
    public PygopodusAnnulatumEntity(EntityType<? extends Animal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
//        this.moveControl = new LimitedMoveControl(this);
        this.animator = new PygopodusAnnulatumAnimator(this);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GeneralAnimator<PygopodusAnnulatumEntity> animator;
    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME = SynchedEntityData.defineId(PygopodusAnnulatumEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(PygopodusAnnulatumEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(PygopodusAnnulatumEntity.class, EntityDataSerializers.BOOLEAN);

    public float getHunger() {
        return this.entityData.get(HUNGER);
    }

    public void setHunger(float hunger) {
        entityData.set(HUNGER, java.lang.Math.max(java.lang.Math.min(hunger, 100),0));
    }

    public void feed(int foodAmount) {
        setHunger(getHunger() + foodAmount);
    }

    @Override
    public GeneralAnimator<PygopodusAnnulatumEntity> getAnimator() {
        return animator;
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 30.0)
                .add(Attributes.ATTACK_DAMAGE, 2)
                .add(ForgeMod.SWIM_SPEED.get(),3)
                .add(Attributes.MOVEMENT_SPEED, 0.2);
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new MMGroundPathNavigation(this, pLevel);
    }

    @Nullable
    @Override
    public AgeableMob getBreedOffspring(@NotNull ServerLevel serverLevel, @NotNull AgeableMob ageableMob) {
        return null;
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(HUNGER,100f);
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(100,300));
        entityData.define(EATING,false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putFloat("Hunger", this.getHunger());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setHunger(compound.getFloat("Hunger"));
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this,5);
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide){
            animator.tick();
            if (!isBaby() && this.tickCount > this.entityData.get(NEXT_CHANGE_TIME)) {
                this.entityData.set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200,400));
                int newState = RandomSource.create().nextInt(3);
                if(newState != 0 && !ErsUtils.isMoving(this) && !isInWater()){
                    if(newState == 1) {
                        triggerAnim("extra", "idle2");
                    }else {
                        triggerAnim("extra", "idle3");
                    }
                }
            }
        }else {
            float hunger = this.entityData.get(HUNGER);
            if(tickCount % 200 == 0){
                setHunger(hunger - 0.1f);
            }
        }


        if(ErsUtils.isMoving(this)){
            updateMount();
        }
    }

    public void updateMount() {
        this.getEntityData().set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(100,300));
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle2");
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle3");
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MobAvodingEntityGoal<>(this, Player.class, 8f, 1d,1.8d));
        this.goalSelector.addGoal(1, new PanicGoal(this,2){
            @Override
            public void start() {
                super.start();
                this.mob.setSprinting(true);
            }

            @Override
            public void stop() {
                super.stop();
                this.mob.setSprinting(false);
            }
        });
        this.goalSelector.addGoal(2, new RandomStrollGoal(this,1,40));
        this.goalSelector.addGoal(3, new AnnulatumEatGoal(this));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<PygopodusAnnulatumEntity> main = new AnimationController<>(this, "main", 10 , state -> {
            RawAnimation builder = RawAnimation.begin();
            if(isInWater() && !onGround()){
                if(isSprinting() || ErsUtils.isMoving(this)){
                    builder.thenLoop("animation.swim");
                }
               else {
                    builder.thenLoop("animation.swim_idle");
                }
            }else {
                if (isSprinting()) {
                    builder.thenLoop("animation.run");
                } else if (state.isMoving() || ErsUtils.isMoving(this)) {
                    if(getEntityData().get(EATING)){
                        builder.thenLoop("animation.walk2");
                    }else
                        builder.thenLoop("animation.walk");
                } else {
                    builder.thenLoop("animation.idle");
                }
            }
            return state.setAndContinue(builder);
        });

        AnimationController<PygopodusAnnulatumEntity> extra = new AnimationController<>(this, "extra",2, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle2"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle3"));

        controllerRegistrar.add(main,extra);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }
}
