package cn.aurorian.oasis.entity.imperiovenatorregius;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.*;
import cn.aurorian.ers.entity.ai.ErsTamableLookAtPlayerGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtTargetGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowOwnerGoal;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.SimpleAutoPlayingSoundKeyFrameHandler;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.client.animator.ImperiovenatorRegiusAnimator;
import cn.aurorian.oasis.entity.ai.OasisBreedGoal;
import cn.aurorian.oasis.entity.imperiovenatorregius.ai.ImperiovenatorRegiusAttackGoal;
import cn.aurorian.oasis.entity.imperiovenatorregius.inventory.ImperiovenatorRegiusMenuProvider;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import cn.aurorian.oasis.init.OasisEntities;
import cn.aurorian.oasis.init.OasisItems;
import cn.aurorian.oasis.init.OasisSounds;
import com.mojang.serialization.Codec;
import java.util.UUID;
import java.util.function.IntFunction;
import net.minecraft.Util;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class ImperiovenatorRegiusEntity extends ErsTamableVehicle<ImperiovenatorRegiusEntity>
        implements ContainerListener,
                HasCustomInventoryScreen,
                VariantHolder<ImperiovenatorRegiusEntity.Variant>,
                HasGender {
    public ImperiovenatorRegiusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        switchNavigator(true);
        animator = new ImperiovenatorRegiusAnimator(this);
        this.setMaxUpStep(2f);
        this.createInventory();
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.SWIM_COST = 0.12f;
        this.SPRINT_COST = 0.06f;
        this.RECOVER = 0.25f;
        this.RECOVER_WHEN_WALK = true;

        this.doAgeTick = false;
        this.doHunger = false;
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            moveControl = new LimitedMoveControl(this);
            navigation = new MMGroundPathNavigation(this, level());
        } else {
            moveControl = new AquaticMoveControl(this, 1).applyGravity(false);
            navigation = new AmphibiousPathNavigation(this, level());
        }
    }

    private final GeneralAnimator<ImperiovenatorRegiusEntity> animator;

    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> SAIL =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> SCALE =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> GENDER =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> ROAR_TIME =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> HANGING =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> SATISFIED_TIME =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> TAMING_PROGRESS =
            SynchedEntityData.defineId(ImperiovenatorRegiusEntity.class, EntityDataSerializers.INT);

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(200, 400));
        entityData.define(SAIL, true);
        entityData.define(SCALE, 1.0f);
        entityData.define(GENDER, true);
        entityData.define(ROAR_TIME, 0);
        entityData.define(HANGING, false);
        entityData.define(SATISFIED_TIME, 0);
        entityData.define(TAMING_PROGRESS, 0);
    }

    public boolean isSailUp() {
        return entityData.get(SAIL);
    }

    public void setSailUp(boolean sail) {
        entityData.set(SAIL, sail);
    }

    public float getScale() {
        return entityData.get(SCALE);
    }

    public void setScale(float scale) {
        entityData.set(SCALE, Math.clamp(scale, 0.8f, 1.1f));
    }

    @Override
    public void setGender(boolean gender) {
        this.entityData.set(GENDER, gender);
    }

    @Override
    public boolean getGender() {
        return this.entityData.get(GENDER);
    }

    public int getRoarTime() {
        return entityData.get(ROAR_TIME);
    }

    public void setRoarTime(int time) {
        entityData.set(ROAR_TIME, time);
    }

    public boolean isHanging() {
        return entityData.get(HANGING);
    }

    public void setHanging(boolean hanging) {
        entityData.set(HANGING, hanging);
    }

    public int getSatisfiedTime() {
        return entityData.get(SATISFIED_TIME);
    }

    public void setSastisfiedTime(int satisfied_time) {
        entityData.set(SATISFIED_TIME, satisfied_time);
    }

    public int getTamingProgress() {
        return entityData.get(TAMING_PROGRESS);
    }

    public void setTamingProgress(int progress) {
        entityData.set(TAMING_PROGRESS, progress);
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.17)
                .add(Attributes.MAX_HEALTH, 175)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.8)
                .add(Attributes.ATTACK_DAMAGE, 15)
                .add(Attributes.ARMOR, 2)
                .add(ForgeMod.SWIM_SPEED.get(), 3);
    }

    @Override
    public GeneralAnimator<ImperiovenatorRegiusEntity> getAnimator() {
        return animator;
    }

    @Override
    public void updateMount() {
        this.getEntityData()
                .set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200, 400));
        getAnimatableInstanceCache()
                .getManagerForId(getId())
                .getAnimationControllers()
                .get("extra")
                .stop();
        if (this.getAttackState().isEmpty()) {
            stopTriggeredAnimation("attack", "knockdown_left");
            stopTriggeredAnimation("attack", "knockdown_right");
        }
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        return false;
    }

    @Override
    protected void positionRider(@NotNull Entity pPassenger, @NotNull MoveFunction pCallback) {
        if (pPassenger == getOwner()) super.positionRider(pPassenger, pCallback);
        else
            pCallback.accept(
                    pPassenger, position().x + getFoodPosition().x, position().y, position().z + getFoodPosition().z);
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public void executeDefaultAttackType() {
        if (getAttackState().isEmpty()) startAttack(AttackType.REGIUS_ATTACK);
    }

    @Override
    public void executeSpecialAttackType() {
        if (getAttackState().isEmpty()) {
            if (getRoarTime() == 0) startAttack(AttackType.REGIUS_ROAR);
        }
    }

    @Override
    public void executeJudgementAttackType() {
        setSailUp(!isSailUp());
    }

    @Override
    public void executeTurnAttackType() {
        if (getAttackState().isEmpty()) {
            if (onGround()) startAttack(AttackType.REGIUS_ATTACK_TURN);
        }
    }

    @Override
    public void executeJumpAttackType() {
        if (getAttackState().getType() == AttackType.REGIUS_JUMP_ATTACK) {
            getAttackState().judgementTarget.stopRiding();
            this.startAttack(AttackType.EMPTY);
            this.stopRiding();
        } else if (getAttackState().getType() == AttackType.REGIUS_JUMP && !onGround() && !isInWater()) {
            this.startAttack(AttackType.REGIUS_JUMP_ATTACK);
        }
    }

    @Override
    public boolean isInvulnerableTo(@NotNull DamageSource pSource) {
        return super.isInvulnerableTo(pSource)
                || ((pSource.getEntity() == getVehicle() || getPassengers().contains(pSource.getEntity()))
                        && getAttackState().getType() == AttackType.REGIUS_JUMP_ATTACK);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return pStack.is(OasisItems.ANNULATUM.get()) || pStack.is(OasisItems.COOKED_ANNULATUM.get());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if (isTame()
                && this.isOwnedBy(player)
                && !isBaby()
                && !player.isCrouching()
                && !isFood(player.getMainHandItem())
                && !player.getMainHandItem().is(ErsItems.GILDED_HORN.get())) {
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
        }

        if (pHand == InteractionHand.MAIN_HAND
                && player.isCrouching()
                && this.isTame()
                && this.isOwnedBy(player)
                && !isFood(player.getMainHandItem())) {
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
            if (isOrderedToSit()) setTarget(null);
        }

        if (!level().isClientSide) {
            if (!this.isTame()) {
                if (!isBaby()) {
                    if (player.getMainHandItem().is(OasisItems.TUBUNASUS_MEAT.get())) {
                        setTarget(null);
                        setSastisfiedTime(60);
                        player.getMainHandItem().shrink(1);
                    } else if (isFood(player.getMainHandItem()) && getSatisfiedTime() > 0) {
                        setInLove(player);
                        player.getMainHandItem().shrink(1);
                    }
                } else {
                    if (player.getMainHandItem().is(OasisItems.CUDMILK.get()) && getSatisfiedTime() <= 0) {
                        setSastisfiedTime(60);
                        setTamingProgress(getTamingProgress() + 1);
                        player.getMainHandItem().shrink(1);
                        player.addItem(new ItemStack(OasisItems.EMPTY_MILK_BOTTLE.get()));
                    }

                    if (getTamingProgress() >= 5) {
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte) 7);
                    }
                }
            } else if (this.isTame() && isFood(player.getMainHandItem())) {
                this.feed(5);
                this.heal(5);
                player.getMainHandItem().shrink(1);
            }
        }

        return super.mobInteract(player, pHand);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new ImperiovenatorRegiusAttackGoal(this, 2, false));
        goalSelector.addGoal(2, new MobFollowOwnerGoal(this, 2.2D, 7.0F, 4.0F, 64F, false));
        goalSelector.addGoal(3, new OasisBreedGoal(this, 1.0D));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.2) {
            @Override
            public boolean canUse() {
                if (!((ImperiovenatorRegiusEntity) this.mob)
                        .getAttackState()
                        .getType()
                        .canMove()) {
                    return false;
                }
                return super.canUse() && ((ImperiovenatorRegiusEntity) this.mob).getCommand() != 1;
            }
        });
        goalSelector.addGoal(6, new ErsTamableLookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(1, new ErsTamableHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame();
            }
        });
        targetSelector.addGoal(2, new ErsTamableOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new ErsTamableOwnerHurtTargetGoal(this));
        targetSelector.addGoal(
                4, new NearestAttackableTargetGoal<>(this, TubunasusClyderotundaEntity.class, 100, true, false, null) {
                    @Override
                    public boolean canUse() {
                        return super.canUse() && !isTame();
                    }
                });
        targetSelector.addGoal(
                4, new NearestAttackableTargetGoal<>(this, PygopodusAnnulatumEntity.class, 100, true, false, null));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<ImperiovenatorRegiusEntity> main = new AnimationController<>(this, "main", 2, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    if (getAttackState().getType() == AttackType.REGIUS_JUMP_ATTACK) {
                        if (isHanging()) {
                            builder.thenLoop("animation.hold_attack_air");
                        } else {
                            builder.thenLoop("animation.hold_attack_ground");
                        }
                    } else if (isFalling() && !onGround()) {
                        builder.thenLoop("animation.fall");
                    } else if (isInWater() && !onGround() && getFluidTypeHeight(ForgeMod.WATER_TYPE.get()) > 2f) {
                        if (isMoving() && isSprinting()) {
                            builder.thenLoop("animation.quickly_swim");
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
                                builder.thenLoop("animation.-left");
                            } else {
                                builder.thenLoop("animation.+right");
                            }
                        } else {
                            if (getCommand() == 1) {
                                builder.thenLoop("animation.idle_sit");
                            } else {
                                builder.thenLoop("animation.idle");
                            }
                        }
                    }

                    return state.setAndContinue(builder);
                })
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>(Oasis.MODID));

        AnimationController<ImperiovenatorRegiusEntity> extra = new AnimationController<>(
                        this, "extra", 10, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle2"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle3"))
                .triggerableAnim("idle_sit2", RawAnimation.begin().thenPlay("animation.idle_sit2"))
                .triggerableAnim("idle_sit3", RawAnimation.begin().thenPlay("animation.idle_sit3"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>(Oasis.MODID));

        AnimationController<ImperiovenatorRegiusEntity> attack = new AnimationController<>(
                        this, "attack", 10, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("hold", RawAnimation.begin().thenPlay("animation.hold"))
                .triggerableAnim("roar", RawAnimation.begin().thenPlay("animation.roar"))
                .triggerableAnim("strike", RawAnimation.begin().thenPlay("animation.strike"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"))
                .triggerableAnim("attack_turn", RawAnimation.begin().thenPlay("animation.attack_turn"))
                .triggerableAnim("jump", RawAnimation.begin().thenPlay("animation.jump"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>(Oasis.MODID));

        AnimationController<ImperiovenatorRegiusEntity> control =
                new AnimationController<>(this, "control", 0, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    if (isBaby()) return PlayState.STOP;

                    if (isSailUp()) {
                        builder.thenPlayAndHold("animation.open");
                    } else {
                        builder.thenPlayAndHold("animation.close");
                    }
                    return state.setAndContinue(builder);
                });

        controllerRegistrar.add(main, attack, extra, control);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new ImperiovenatorRegiusBodyControl(this);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity passenger = getFirstPassenger();
        if (passenger == null) return null;
        if (passenger instanceof LivingEntity livingEntity) {
            return this.isOwnedBy(livingEntity) ? livingEntity : null;
        }
        return null;
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return OasisSounds.REGIUS_HURT.get();
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player player, @NotNull Vec3 pTravelVector) {
        if (this.rideSpeed != 0 || player.jumping) {
            updateMount();
            if (getCommand() == 1) this.setCommand(0);
        }
        if (this.isInFluidType() && !this.onGround()) {
            float y = 0;

            if (player.jumping && getSwimState() == 2) {
                y = 0.4f;
                if (!wasEyeInWater) setSwimState(1);
            }

            if (rideSpeed != 0 && getSwimState() == 1) {
                if (this.horizontalCollision) {
                    y = 0.6f;
                }
            }

            return new Vec3(0, y, this.rideSpeed);
        } else {
            if (onGround() && player.jumping && getAttackState().isEmpty()) {
                startAttack(AttackType.REGIUS_JUMP);
                this.hasImpulse = true;
                this.setDeltaMovement(this.getDeltaMovement().scale(2.2).add(0, 1.2, 0));
            }
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 move) {
        tickStableHead();
        if (this.getAttackState().getType().canMove()) {
            if (((!this.getRotDirection().isLargeTurn() || isInWater())
                            && (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0))
                    || this.isSprinting()) {
                float baseSpeed =
                        (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.rideSpeed = Mth.approach(this.rideSpeed, isSprinting() ? baseSpeed * 3f : baseSpeed, 0.08f);
            } else {
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.16f);
            }
        } else {
            this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.48f);
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return this.rideSpeed + 0.05f;
    }

    public void tickStableHead() {
        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        if (this.isInWater()) {
            setStableHead(pitch < 20);
        } else if (this.onGround()) {
            setStableHead(true);
        }
    }

    @Override
    public boolean isPushable() {
        return (isMoving() || isSprinting()) && isVehicle();
    }

    @Override
    public void containerChanged(@NotNull Container container) {
        boolean healthBoost = false;
        boolean armorBoost = false;
        for (int i = 1; i <= 3; i++) {
            ItemStack itemStack = this.inventory.getItem(i);
            if (!itemStack.isEmpty()) {
                if (itemStack.is(Items.ENCHANTED_GOLDEN_APPLE)) {
                    healthBoost = true;
                }
                if (itemStack.is(Items.SHIELD)) {
                    armorBoost = true;
                }
            }
        }

        if (healthBoost) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(175 + 20);
        }

        if (armorBoost) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(10);
        } else {
            this.getAttribute(Attributes.ARMOR).setBaseValue(6);
        }
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!level().isClientSide() && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new ImperiovenatorRegiusMenuProvider(this),
                    buf -> buf.writeInt(this.getId()));
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Command", this.getCommand());
        compound.putBoolean("Gender", getGender());
        compound.putFloat("Scale", getScale());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setCommand(compoundTag.getInt("Command"));
        this.setGender(compoundTag.getBoolean("Gender"));
        this.setScale(compoundTag.getFloat("Scale"));
    }

    public int getInventorySize() {
        return 4;
    }

    @Override
    public void equipSaddle() {}

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            this.RECOVER = isSailUp() ? 0.3f : 0.25f;

            if (tickCount % 20 == 0) {
                if (tickCount % 200 == 0) {
                    this.heal(1);
                }

                if (getRoarTime() > 0) {
                    setRoarTime(getRoarTime() - 1);
                    if (getRoarTime() > 120) {
                        this.setStamina(100f);
                    }
                }

                if (getSatisfiedTime() > 0) {
                    setSastisfiedTime(getSatisfiedTime() - 1);
                }
            }

            if (this.isSprinting() && !isBaby()) {
                stompEffect(1f, 2f, checkEquipment(ErsItems.SCRATCHING_BOARD.get()) ? 2f : 1f);
            }
        } else {
            boolean flag2 = onGround() && !isMoving() && this.getAttackState().getType() == AttackType.EMPTY;

            //                if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME)) {
            //                    this.entityData.set(NEXT_CHANGE_TIME, this.tickCount +
            // RandomSource.create().nextIntBetweenInclusive(200,400));
            //                    int newState = RandomSource.create().nextInt(4);
            //                    if(newState != 0 && flag2) {
            //                        if(getCommand() == 1){
            //                            if(newState == 1) {
            //                                triggerAnim("extra", "idle_sit2");
            //                            }else if(newState == 2) {
            //                                triggerAnim("extra", "idle_sit3");
            //                            }else {
            //                                triggerAnim("extra", "idle_sit3");
            //                            }
            //                        }else{
            //                            if(newState == 1) {
            //                                triggerAnim("extra", "idle2");
            //                            }else {
            //                                triggerAnim("extra", "idle3");
            //                            }
            //                        }
            //                    }
            //                }
            if (this.isMoving()) {
                updateMount();
            }
        }
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (isInWater() && !(this.moveControl instanceof AquaticMoveControl)) {
            switchNavigator(false);
        } else if (!isInWater() && this.moveControl instanceof AquaticMoveControl) {
            switchNavigator(true);
        }
        breakBlock();
    }

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {}

    @Override
    public void aiStep() {
        super.aiStep();

        waterAiStep(2.2f);

        if (isInWater()) {
            if (onGround()) {
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4f);
            } else this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(3f);
        }
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor pLevel,
            @NotNull DifficultyInstance pDifficulty,
            @NotNull MobSpawnType pReason,
            @Nullable SpawnGroupData pSpawnData,
            @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            if (pDataTag != null && pDataTag.contains("StringUUID")) {
                setUUID(UUID.fromString(pDataTag.getString("StringUUID")));
            }
            return pSpawnData;
        } else {
            RandomSource $$6 = pLevel.getRandom();
            if (pSpawnData == null) {
                pSpawnData = new ErsGroupData<>(
                        1f,
                        Util.getRandom(Variant.values(), $$6),
                        Util.getRandom(Variant.values(), $$6),
                        Util.getRandom(Variant.values(), $$6),
                        Util.getRandom(Variant.values(), $$6));
            }

            this.setVariant(((ErsGroupData<Variant>) pSpawnData).getVariant($$6));

            if (this.random.nextFloat() <= 0.5f) this.setGender(false);

            if (getGender()) this.setScale(Mth.randomBetween(this.random, 1f, 1.1f));
            else this.setScale(Mth.randomBetween(this.random, 0.9f, 1f));

            if (isBaby()) {
                this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1);
                this.setHealth(this.getMaxHealth());
            }

            return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        }
    }

    @Override
    public ErsTamable<?> getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        ImperiovenatorRegiusEntity child =
                OasisEntities.IMPERIOVENATOR_REGIUS.get().create(level);
        return child;
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if (isBaby()) {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(20);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.1);
        } else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(175);
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.17);
            this.setHealth(this.getMaxHealth());
        }
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        float scale = getScale();
        if (isBaby()) scale *= 0.4f;
        return this.getType().getDimensions().scale(scale);
    }

    @Override
    public void spawnChildFromBreeding(@NotNull ServerLevel pLevel, @NotNull Animal pMate) {
        super.spawnChildFromBreeding(pLevel, pMate);
        if (!getGender()) {
            setAge(60000);
        }

        if (!((HasGender) pMate).getGender()) {
            setAge(60000);
        }
    }

    public @NotNull Variant getVariant() {
        return Variant.byId(getVariantId());
    }

    public void setVariant(Variant pVariant) {
        setVariantId(pVariant.getId());
    }

    private static final UUID SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(
            SPEED_MODIFIER_SPRINTING_UUID, "Sprinting speed boost", 0.40D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void setSprinting(boolean pSprinting) {
        if (!isBaby()) {
            super.setSprinting(pSprinting);
            return;
        }

        this.setSharedFlag(3, pSprinting);
        AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeinstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
            attributeinstance.removeModifier(SPEED_MODIFIER_SPRINTING);
        }

        if (pSprinting) {
            attributeinstance.addTransientModifier(SPEED_MODIFIER_SPRINTING);
        }
    }

    public enum Variant implements StringRepresentable {
        ORIGINAL(0, "original"),
        GREY(1, "grey");

        private static final IntFunction<Variant> BY_ID =
                ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        private final int id;
        private final String name;

        Variant(int pId, String pName) {
            this.id = pId;
            this.name = pName;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }

        public static Variant byId(int pId) {
            return BY_ID.apply(pId);
        }
    }
}
