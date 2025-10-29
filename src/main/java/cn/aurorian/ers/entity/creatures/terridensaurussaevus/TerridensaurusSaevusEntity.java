package cn.aurorian.ers.entity.creatures.terridensaurussaevus;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.TerridensaurusSaevusAnimator;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.ai.TerridensaurusSaevusAttackGoal;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.OwnerHurtTargetGoal;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class TerridensaurusSaevusEntity extends ErsTamableVehicle<TerridensaurusSaevusEntity> implements ContainerListener, HasCustomInventoryScreen {
    public TerridensaurusSaevusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        moveControl = new LimitedMoveControl(this);
        animator = new TerridensaurusSaevusAnimator(this);
        this.setMaxUpStep(2f);
        this.SWIM_COST = 0.15f;
        this.SPRINT_COST = 0.1f;
        this.RECOVER = 0.35f;
        this.RECOVER_WHEN_WALK = false;
    }

    private final GeneralAnimator<TerridensaurusSaevusEntity> animator;

    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.16)
                .add(Attributes.MAX_HEALTH, 200)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.ATTACK_DAMAGE, 35)
                .add(Attributes.ARMOR,6)
                .add(ForgeMod.SWIM_SPEED.get(),4);
    }

    @Override
    public GeneralAnimator<TerridensaurusSaevusEntity> getAnimator() {
        return animator;
    }

    @Override
    public void updateMount() {
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public void executeDefaultAttackType() {
        startAttack(AttackType.SAEVUS_ATTACK);
    }

    @Override
    public void executeSpecialAttackType() {
        super.executeSpecialAttackType();
    }

    @Override
    public void executeJudgementAttackType() {
        super.executeJudgementAttackType();
    }

    @Override
    public void executeTurnAttackType() {
        startAttack(AttackType.SAEVUS_ATTACK_TURN);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if (isTame() && !isVehicle() && this.isOwnedBy(player) && !player.isCrouching() && !player.getMainHandItem().is(ItemTags.FISHES)) {
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
        }

        if (!level().isClientSide && (player.getMainHandItem().is(ErsItems.SWAMP_DRAGON_MEAT.get()))) {
            // 驯服
            if(!this.isTame()){
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)){
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte)7);
                }else{
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }else if(this.isTame()) {
                this.heal(5);
                player.getMainHandItem().shrink(1);
            }

        }
        return super.mobInteract(player, pHand);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new TerridensaurusSaevusAttackGoal(this,2,false));
//        goalSelector.addGoal(2, new MobFollowOwnerGoal(this,2.2D,7.0F,4.0F,64F,false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.2){
            @Override
            public boolean canUse() {
                if (!((TerridensaurusSaevusEntity)this.mob).getAttackState().getType().canMove()) {
                    return false;
                }
                return super.canUse();
            }
        });
        goalSelector.addGoal(6, new LookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(1, new HurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,Player.class, true){
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame();
            }
        });
        targetSelector.addGoal(2, new OwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new OwnerHurtTargetGoal(this));
//        targetSelector.addGoal(4, new AttackFishGoal(this, AbstractFish.class, Boolean.TRUE));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<TerridensaurusSaevusEntity> main = new AnimationController<>(this, "main", 10 , state -> {
            RawAnimation builder = RawAnimation.begin();
            if (isInWater() && !onGround()) {
                if (isMoving() && isSprinting()) {
                    builder.thenLoop("animation.quickly_swimming");
                } else if (isMoving() && !isSprinting()) {
                    builder.thenLoop("animation.swim");
                } else {
                    builder.thenLoop("animation.swim_idle");
                }
            } else {
                if (isMoving() && isSprinting()) {
                    builder.thenLoop("animation.run");
                } else if (isMoving()) {
                    builder.thenLoop("animation.walk");
                } else if (!this.getRotDirection().isNone()) {
                    if (this.getRotDirection().isLeft()) {
                        if(isVehicle())
                            builder.thenLoop("animation.-left");
                        else
                            builder.thenLoop("animation.-left_ai");
                    } else {
                        if(isVehicle())
                            builder.thenLoop("animation.+right");
                        else
                            builder.thenLoop("animation.+right_ai");
                    }
                } else {
                        builder.thenLoop("animation.idle");
                }
            }

            return state.setAndContinue(builder);
        });

        AnimationController<TerridensaurusSaevusEntity> attack = new AnimationController<>(this, "attack",10, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("left_attack_turn", RawAnimation.begin().thenPlay("animation.left_attack"))
                .triggerableAnim("right_attack_turn", RawAnimation.begin().thenPlay("animation.right_attack"));

        controllerRegistrar.add(main,attack);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new TerridensaurusSaevusBodyControl(this);
    }

    @Override
    protected void positionRider(@NotNull Entity pPassenger, @NotNull MoveFunction pCallback) {
        if(this.level().isClientSide)
        {
            float forward = 2.30f;
            float yRot = Math.toRadians(this.getYRot());
            float offsetX = Math.sin(-yRot) * forward;
            float offsetZ = Math.cos(yRot) * forward;

            pCallback.accept(pPassenger,
                        this.getX() + offsetX,
                        this.getY() + 3.9f,
                        this.getZ() + offsetZ);
        }
        else
        {
            super.positionRider(pPassenger, pCallback);
        }
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        if(this.isInFluidType() && !this.onGround()){
            float y = 0;

            if(pPlayer.jumping && getSwimState() == 2){
                y = 0.4f;
                if(!wasEyeInWater)
                    setSwimState(1);
            }

            if (rideSpeed != 0 && getSwimState() == 1) {
                if (this.horizontalCollision) {
                    y = 0.6f;
                }
            }

            return new Vec3(0, y, this.rideSpeed);
        }
        else
        {
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 move)
    {
        tickStableHead();
        if(this.getAttackState().getType().canMove() && this.isSaddled()){
            if(((!this.getRotDirection().isLargeTurn() || isInWater()) && (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0)) || this.isSprinting())
            {
                float baseSpeed = (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.rideSpeed = Mth.approach(this.rideSpeed, isSprinting() ? baseSpeed * 3f : baseSpeed, 0.08f);
            }else{
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.16f);
            }
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return this.rideSpeed + 0.2f;
    }

    public void tickStableHead(){
        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        if(this.isInWater())
        {
            setStableHead(pitch < 20);
        }
        else if(this.onGround())
        {
            setStableHead(true);
        }
    }

    @Override
    public boolean isSaddleable() {
        return isAlive() && isTame();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource var1) {
//        this.inventory.setItem(0, new ItemStack(ErsItems.SWAMP_DRAGON_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    @Override
    public boolean isSaddled() {
        return true;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    @Override
    public void containerChanged(@NotNull Container container) {

    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {

    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("AgeTicks", this.getAgeInTicks());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
    }

    @Override
    public void tick() {
        super.tick();
        if(tickCount % 200 == 0){
            heal(1);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.type().msgId().equals("sweetBerryBush") || source.getEntity() instanceof Pufferfish || source.type().msgId().equals("cactus")) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {}

    @Override
    public void aiStep() {
        super.aiStep();
        waterAiStep(3.4f);
        if(isInWater()){
            if(onGround()){
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(8f);
            }else
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4f);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        this.setAgeInDays(this.random.nextIntBetweenInclusive(15,50));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }
}
