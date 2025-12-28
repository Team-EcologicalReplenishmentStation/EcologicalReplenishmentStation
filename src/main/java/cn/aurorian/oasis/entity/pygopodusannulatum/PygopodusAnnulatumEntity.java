package cn.aurorian.oasis.entity.pygopodusannulatum;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.goal.MobAvodingEntityGoal;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.ers.util.SimpleAutoPlayingSoundKeyFrameHandler;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.client.animator.PygopodusAnnulatumAnimator;
import cn.aurorian.oasis.entity.pygopodusannulatum.ai.AnnulatumEatGoal;
import cn.aurorian.oasis.init.OasisEntities;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.RandomSource;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.BreedGoal;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class PygopodusAnnulatumEntity extends ErsTamable<PygopodusAnnulatumEntity>{
    public PygopodusAnnulatumEntity(EntityType<? extends ErsTamable> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new LimitedMoveControl(this);
        this.animator = new PygopodusAnnulatumAnimator(this);
        this.doHunger = true;
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final GeneralAnimator<PygopodusAnnulatumEntity> animator;
    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME = SynchedEntityData.defineId(PygopodusAnnulatumEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> EATING = SynchedEntityData.defineId(PygopodusAnnulatumEntity.class, EntityDataSerializers.BOOLEAN);
    public void setEating(boolean eating){
        this.entityData.set(EATING,eating);
    }
    public boolean isEating(){
        return this.entityData.get(EATING);
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

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(100,300));
        entityData.define(EATING,false);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this,15);
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
            float hunger = getHunger();
            if(tickCount % 200 == 0){
                setHunger(hunger - 0.1f);
            }

            if(hunger > 90f && getAge() == 0 && canFallInLove() && !isBaby()){
                setInLove(null);
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
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if(isMoving() && !isBaby() && level().random.nextFloat() < 0.25f)
        {
            if(pSource.getEntity() instanceof LivingEntity livingEntity){
                if(ErsUtils.calculateFallDirection(livingEntity,this)){
                    triggerAnim("extra","dodge_right");
                }else{
                    triggerAnim("extra","dodge_left");
                }
            }
            return false;
        }
        setHunger(Math.min(89.9f,getHunger()));
        return super.hurt(pSource, pAmount);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new MobAvodingEntityGoal<>(this, Player.class, 8f, 1d,1.8d));
        this.goalSelector.addGoal(1, new PanicGoal(this,2.4f){
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
        this.goalSelector.addGoal(2, new BreedGoal(this,1));
        this.goalSelector.addGoal(3, new RandomStrollGoal(this,1,40));
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
                    if(isEating()){
                        builder.thenLoop("animation.walk2");
                    }else{
                        if(isBaby()){
                            builder.thenLoop("animation.run");
                        }else
                            builder.thenLoop("animation.walk");
                    }
                } else {
                    builder.thenLoop("animation.idle");
                }
            }
            return state.setAndContinue(builder);
        }).setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>(Oasis.MODID));

        AnimationController<PygopodusAnnulatumEntity> extra = new AnimationController<>(this, "extra",2, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle2"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle3"))
                .triggerableAnim("dodge_left", RawAnimation.begin().thenPlay("animation.dodge_left"))
                .triggerableAnim("dodge_right", RawAnimation.begin().thenPlay("animation.dodge_right"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>(Oasis.MODID));

        controllerRegistrar.add(main,extra);
    }

    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel pLevel, @NotNull Animal pMate) {
        super.spawnChildFromBreeding(pLevel, pMate);
        this.setHunger(getHunger() - 10);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    @Override
    public ErsTamable<?> getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        return OasisEntities.PYGOPODUS_ANNULATUM.get().create(level);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pSpawnData == null) {
            pSpawnData = new AgeableMob.AgeableMobGroupData(0.2F);
        }
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public float getVolume() {
        return 0.5f;
    }

    @Override
    public float getSoundRange() {
        return 16f;
    }
}
