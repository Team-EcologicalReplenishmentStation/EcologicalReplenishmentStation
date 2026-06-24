package cn.aurorian.ers.entity.creatures.pterochirusdux;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.PterochirusDuxAnimator;
import cn.aurorian.ers.entity.ErsFlyableVehicle;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.ai.ErsTamableLookAtPlayerGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtTargetGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowOwnerGoal;
import cn.aurorian.ers.entity.ai.goal.MobWanderGoal;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class PterochirusDuxEntity extends ErsFlyableVehicle<PterochirusDuxEntity> implements PlayerRideableJumping {
    private static final float BASE_MOVE_SPEED = 0.22f;
    private static final float BASE_HEALTH = 65.0f;
    private static final float BASE_ATTACK_DAMAGE = 8.0f;

    private final GeneralAnimator<PterochirusDuxEntity> animator;
    private float baseHealth = BASE_HEALTH;

    // 蓄力起飞相关（PlayerRideableJumping）
    // 由 LocalPlayer.rideTick() 驱动，蓄满后松开空格才起飞
    private float playerJumpPendingScale = 0.0f;
    // 用于 tickRidden 中检测空格松开
    private boolean ers$wasJumping = false;
    // 起飞延迟计时
    private int takeoffDelayTicks = 0;
    private float pendingTakeoffScale = 0.0f;

    public PterochirusDuxEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.animator = new PterochirusDuxAnimator(this);
        this.createInventory();
        this.setMaxUpStep(1.2f);
        this.SPRINT_COST = 0.12f;
        this.RECOVER = 0.2f;
        this.RECOVER_WHEN_WALK = false;
        this.doAgeTick = true;
        this.doHunger = true;
        this.healInterval = 240;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new MeleeAttackGoal(this, 1.25D, false));
        goalSelector.addGoal(2, new MobFollowOwnerGoal(this, 1.6D, 8.0F, 4.0F, 64.0F, false));
        goalSelector.addGoal(3, new MobWanderGoal(this, 1.0));
        goalSelector.addGoal(4, new ErsTamableLookAtPlayerGoal(this, Player.class, 8.0F));

        targetSelector.addGoal(1, new ErsTamableHurtByTargetGoal(this));
        targetSelector.addGoal(2, new ErsTamableOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new ErsTamableOwnerHurtTargetGoal(this));
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide) {
            tickCommonServer();
        }
    }

    @Override
    protected void tickAutoFeed() {
        if (this.getInventory() == null) {
            return;
        }
        if (!this.getInventory().getItem(2).isEmpty()) {
            this.getInventory().getItem(2).shrink(1);
            this.feed(10);
            this.heal(10);
        }
    }

    //    @Override
    //    public void executeDefaultAttackType() {
    //        startAttack(AttackType.DINOSAURIFORMIS_ATTACK);
    //    }
    //
    //    @Override
    //    public void executeTurnAttackType() {
    //        if (onGround()) {
    //            startAttack(AttackType.DINOSAURIFORMIS_ATTACK);
    //        }
    //    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new PterochirusDuxBodyControl(this);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack handItem = player.getItemInHand(hand);

        if (isMature()
                && isTame()
                && this.isOwnedBy(player)
                && !isVehicle()
                && !player.isCrouching()
                && !isFood(handItem)
                && hand == InteractionHand.MAIN_HAND) {
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        if (hand == InteractionHand.MAIN_HAND
                && player.isCrouching()
                && this.isTame()
                && this.isOwnedBy(player)
                && !isFood(handItem)) {
            navigation.stop();
            if (!level().isClientSide) {
                this.setCommand(this.getCommand() + 1);
                if (this.getCommand() > 2) {
                    this.setCommand(0);
                }
            }
            String commandText = "stand";
            if (this.getCommand() == 1) {
                commandText = "sit";
                this.setTarget(null);
                setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
            } else if (this.getCommand() == 2) {
                commandText = "follow";
                this.setTarget(null);
            }
            player.displayClientMessage(Component.translatable("ers.command." + commandText), true);
            return InteractionResult.sidedSuccess(level().isClientSide);
        }

        if (!level().isClientSide && hand == InteractionHand.MAIN_HAND && handItem.is(ErsItems.MEAT_FEED.get())) {
            if (!this.isTame()) {
                handItem.shrink(1);
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            } else {
                this.feed(10);
                this.heal(10);
                handItem.shrink(1);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ErsItems.MEAT_FEED.get());
    }

    @Override
    public void updateAgeFromServer() {
        if (getAgeInDays() >= 20 && !this.isMature()) {
            this.setMature(true);
        }

        float scale = calculateScale();
        if (isMature()) {
            this.setRenderSize(scale);
        } else {
            this.setRenderSize(ErsUtils.calculateBabyRenderSize(getAgeInDays()));
        }

        this.refreshDimensions();

        if (!this.isMature()) {
            scale *= 0.45f;
        }
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH * scale);
        setBaseHealth(BASE_HEALTH * scale);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BASE_ATTACK_DAMAGE * scale);
        if (!this.isMature()) {
            scale *= 1.35f;
        }
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_MOVE_SPEED * scale);
        containerChanged(this.getInventory());
    }

    @Override
    protected float calculateScale() {
        int age = getAgeInDays();
        if (this.isMature()) {
            age -= 20;
        }
        return ErsUtils.calculateRenderSize(age);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pose) {
        int age = getAgeInDays();
        if (this.isMature()) {
            age -= 20;
        }
        float scale = ErsUtils.calculateRenderSize(age);
        if (!this.isMature()) {
            scale *= 0.45f;
        }
        return this.getType().getDimensions().scale(scale);
    }

    @Override
    public void updateMount() {
        if (this.getAttackState().isEmpty()) {
            stopTriggeredAnimation("attack", "knockdown_left");
            stopTriggeredAnimation("attack", "knockdown_right");
        }
    }

    @Override
    public void equipSaddle() {
        this.inventory.setItem(0, new ItemStack(ErsItems.PTEROCHIRUS_DUX_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    @Override
    public int getInventorySize() {
        return 3;
    }

    @Override
    public boolean isSaddleable() {
        return super.isSaddleable() && isMature();
    }

    @Override
    protected float getBaseHealthValue() {
        return baseHealth;
    }

    protected void setBaseHealth(float value) {
        this.baseHealth = value;
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        updateAgeFromServer();
    }

    @Override
    protected float getBaseArmorValue() {
        return 5;
    }

    @Override
    protected float getBoostedArmorValue() {
        return 8;
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        AnimationController<PterochirusDuxEntity> main = new AnimationController<>(this, "main", 5, state -> {
                    RawAnimation builder = RawAnimation.begin();

                    if (!onGround() && !this.isFlying()) {
                        builder.thenLoop("animation.swoop");
                    } else if (this.isFlying() && !this.onGround()) {
                        boolean isBacking = false;
                        boolean isForwarding = false;
                        boolean isDivingPitch = false;
                        if (this.getControllingPassenger() instanceof Player rider) {
                            isBacking = rider.zza < 0;
                            isForwarding = rider.zza > 0;
                            isDivingPitch = rider.getXRot() > 45f;
                        }
                        if (this.isDiving()) {
                            builder.thenLoop("animation.slow_down");
                        } else if (isDivingPitch && isForwarding) {
                            builder.thenLoop("animation.swoop");
                        } else if (isMoving()) {
                            if (!isBacking) {
                                if (isForwarding) {
                                    builder.thenLoop("animation.fly");
                                } else {
                                    builder.thenLoop("animation.idle_fly");
                                }
                            } else {
                                builder.thenLoop("animation.slow_down");
                            }
                        }
                    } else if (isMoving()) {
                        builder.thenLoop("animation.walk");
                    } else {
                        builder.thenLoop("animation.idle");
                    }

                    return state.setAndContinue(builder);
                })
                .setSoundKeyframeHandler(event -> {});

        AnimationController<PterochirusDuxEntity> attack = new AnimationController<>(
                        this, "attack", 5, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("jump", RawAnimation.begin().thenPlay("animation.jump"));

        controllers.add(main, attack);
    }

    @Override
    public boolean causeFallDamage(float distance, float multiplier, @NotNull DamageSource source) {
        return false;
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor level,
            @NotNull net.minecraft.world.DifficultyInstance difficulty,
            @NotNull net.minecraft.world.entity.MobSpawnType reason,
            @Nullable SpawnGroupData spawnData,
            @Nullable CompoundTag dataTag) {
        this.setAgeInDays(Mth.randomBetweenInclusive(RandomSource.create(), 16, 45));
        updateAgeFromServer();
        this.setHealth(this.getMaxHealth());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public GeneralAnimator<PterochirusDuxEntity> getAnimator() {
        return animator;
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVE_SPEED)
                .add(Attributes.MAX_HEALTH, BASE_HEALTH)
                .add(Attributes.FOLLOW_RANGE, 40)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.6)
                .add(Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE)
                .add(Attributes.ARMOR, 5)
                .add(ForgeMod.SWIM_SPEED.get(), 1);
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player player, @NotNull Vec3 pTravelVector) {
        if (this.rideSpeed != 0 || player.jumping) {
            updateMount();
            if (getCommand() == 1) this.setCommand(0);
        }

        if (this.isInFluidType()) {
            float y = 0;
            if (player.jumping && (getSwimState() != 1 || wasEyeInWater)) {
                y = 0.45f;
                if (!wasEyeInWater && getSwimState() != 3) setSwimState(1);
            }

            if (rideSpeed > 0.1 && getSwimState() == 1) {
                if (this.horizontalCollision && !onGround()) {
                    y = 0.8f;
                }
            }

            if (!getAttackState().getType().canMove()) y = 0;
            return new Vec3(0, y, this.rideSpeed);
        } else {
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return this.rideSpeed + 0.1f;
    }

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 move) {
        // 起飞延迟倒计时
        if (this.takeoffDelayTicks > 0) {
            this.takeoffDelayTicks--;
            if (this.takeoffDelayTicks == 0) {
                this.executeTakeoff(this.pendingTakeoffScale);
                this.pendingTakeoffScale = 0.0f;
            }
            return;
        }

        if (this.onGround()
                && !this.isFlying()
                && this.isControlledByLocalInstance()
                && this.playerJumpPendingScale >= 1.0f
                && !driver.jumping
                && this.ers$wasJumping) {
            this.pendingTakeoffScale = this.playerJumpPendingScale;
            this.playerJumpPendingScale = 0.0f;
            this.ers$wasJumping = false;
            triggerAnim("attack", "jump");
            this.takeoffDelayTicks = 4;
            return;
        }
        this.ers$wasJumping = driver.jumping;

        if (this.isFlying() && !this.onGround()) {
            return;
        }

        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        //        tickStableHead();

        if (this.getAttackState().getType().canMove() && this.isSaddled()) {
            if ((!this.getRotDirection().isLargeTurn() || this.isSprinting())
                    && (org.joml.Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0)) {
                float baseSpeed =
                        (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.rideSpeed = Mth.approach(
                        this.rideSpeed,
                        isSprinting() ? baseSpeed * 2f : isInWater() ? 1.25f * baseSpeed : baseSpeed,
                        0.08f);
            } else {
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.08f * 2);
            }
        } else {
            this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.08f * 3);
        }
    }

    private void executeTakeoff(float scale) {
        double jumpPower = 0.8 + 0.4 * scale;
        super.executeTakeoff(jumpPower);
    }

    @Override
    public void onPlayerJump(int jumpPower) {
        // jumpPower < 0 是原版松开空格时的回调，忽略它
        // 否则蓄力条每 tick 增长，jumpPower = jumpRidingScale * 100
        if (jumpPower < 0) return;
        if (this.isSaddled() && this.onGround() && !this.isFlying()) {
            this.playerJumpPendingScale = Math.min((float) jumpPower / 100.0f, 1.0f);
        }
    }

    @Override
    public float getJumpPower() {
        return 0.5f;
    }

    @Override
    public boolean canJump() {
        return this.isSaddled() && this.onGround() && !this.isFlying();
    }

    @Override
    public void handleStartJump(int jumpPower) {}

    @Override
    public void handleStopJump() {}

    @Override
    protected float getSprintSpeedMultiplier() {
        return 6.5f;
    }

    @Override
    protected float getCruiseSpeedMultiplier() {
        return 3.9f;
    }

    @Override
    protected float getFlightStaminaCost() {
        return 0.08f;
    }

    // 禁用 FLIGHT_VERTICAL_INPUT 的自动起飞机制
    // 避免 ClientForgeListener.onFlightControl 发送的 VehicleFlightControlPacket 导致一按空格就起飞
    @Override
    public int getFlightVerticalInput() {
        return 0;
    }

    @Override
    public void onFlightKeyUpdate(int verticalInput) {
        // PterochirusDuxEntity 使用 PlayerRideableJumping 蓄力空格机制起飞
        // 禁用按键直接起飞
    }

    public boolean isSaddled() {
        return true;
    }
}
