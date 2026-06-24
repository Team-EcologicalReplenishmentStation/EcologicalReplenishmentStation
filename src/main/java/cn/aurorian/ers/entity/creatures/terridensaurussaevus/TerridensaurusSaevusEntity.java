package cn.aurorian.ers.entity.creatures.terridensaurussaevus;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.TerridensaurusSaevusAnimator;
import cn.aurorian.ers.entity.*;
import cn.aurorian.ers.entity.ai.ErsTamableLookAtPlayerGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtTargetGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowOwnerGoal;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.ai.TerridensaurusSaevusAttackGoal;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.inventory.TerridensaurusSaevusMenuProvider;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.ers.util.SimpleAutoPlayingSoundKeyFrameHandler;
import cn.aurorian.ers.util.TickHelper;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.UUID;
import java.util.function.IntFunction;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.WaterAvoidingRandomStrollGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class TerridensaurusSaevusEntity extends ErsTamableVehicle<TerridensaurusSaevusEntity>
        implements ContainerListener, HasCustomInventoryScreen, VariantHolder<TerridensaurusSaevusEntity.Variant> {
    public TerridensaurusSaevusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        switchNavigator(true);
        animator = new TerridensaurusSaevusAnimator(this);
        this.setMaxUpStep(2f);
        this.createInventory();
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.SWIM_COST = 0.15f;
        this.SPRINT_COST = 0.1f;
        this.RECOVER = 0.35f;
        this.RECOVER_WHEN_WALK = false;

        this.doAgeTick = true;
        this.doHunger = true;
        this.healInterval = 200;
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

    private final GeneralAnimator<TerridensaurusSaevusEntity> animator;
    private static final EntityDataAccessor<Integer> NEXT_POOP_TIME =
            SynchedEntityData.defineId(TerridensaurusSaevusEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME =
            SynchedEntityData.defineId(TerridensaurusSaevusEntity.class, EntityDataSerializers.INT);

    public int getNextPoopTime() {
        return this.entityData.get(NEXT_POOP_TIME);
    }

    public void setNextPoopTime(int time) {
        this.entityData.set(NEXT_POOP_TIME, time);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(NEXT_POOP_TIME, RandomSource.create().nextInt(24000));
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(200, 400));
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, 0.18)
                .add(Attributes.MAX_HEALTH, 325)
                .add(Attributes.FOLLOW_RANGE, 48)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.ATTACK_DAMAGE, 27)
                .add(Attributes.ARMOR, 6)
                .add(ForgeMod.SWIM_SPEED.get(), 4);
    }

    @Override
    public GeneralAnimator<TerridensaurusSaevusEntity> getAnimator() {
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
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    public void executeDefaultAttackType() {
        startAttack(AttackType.SAEVUS_ATTACK);
    }

    @Override
    public void executeSpecialAttackType() {
        if (onGround()) startAttack(AttackType.SAEVUS_STRIKE);
    }

    @Override
    public void executeJudgementAttackType() {
        startAttack(AttackType.SAEVUS_ROAR);
    }

    @Override
    public void executeTurnAttackType() {
        if (onGround()) startAttack(AttackType.SAEVUS_ATTACK_TURN);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        int age = getAgeInDays();
        float scale;
        if (this.isMature()) {
            age -= 20;
        }
        scale = ErsUtils.calculateRenderSize(age);
        if (!this.isMature()) {
            scale *= 0.4F;
        }

        return this.getType().getDimensions().scale(scale);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return pStack.is(ErsItems.MEAT_FEED.get())
                || (pStack.getFoodProperties(this) != null
                        && pStack.getFoodProperties(this).isMeat());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if (isTame()
                && !isVehicle()
                && this.isOwnedBy(player)
                && isMature()
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
                && !player.getMainHandItem().is(ErsItems.MEAT_FEED.get())) {
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

        if (!level().isClientSide && (player.getMainHandItem().is(ErsItems.MEAT_FEED.get()))) {
            if (!this.isTame() && this.getAgeInDays() < 1) {
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            } else if (this.isTame()) {
                this.feed(10);
                this.heal(10);
                player.getMainHandItem().shrink(1);
            }
        }

        if (!level().isClientSide) {
            FoodProperties foodProperties = player.getMainHandItem().getFoodProperties(this);
            if (foodProperties != null && foodProperties.isMeat()) {
                this.feed(1);
                this.heal(1);
                player.getMainHandItem().shrink(1);
            }
        }
        return super.mobInteract(player, pHand);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new TerridensaurusSaevusAttackGoal(this, 2, false));
        goalSelector.addGoal(2, new MobFollowOwnerGoal(this, 2.2D, 7.0F, 4.0F, 64F, false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.2) {
            @Override
            public boolean canUse() {
                if (!((TerridensaurusSaevusEntity) this.mob)
                        .getAttackState()
                        .getType()
                        .canMove()) {
                    return false;
                }
                if (((ErsTamable<?>) this.mob).updateSkyBrightness() > 8) return false;
                return super.canUse() && ((TerridensaurusSaevusEntity) this.mob).getCommand() != 1;
            }
        });
        goalSelector.addGoal(6, new ErsTamableLookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(1, new ErsTamableHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame() && !mightBeSleeping();
            }
        });
        targetSelector.addGoal(2, new ErsTamableOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new ErsTamableOwnerHurtTargetGoal(this));
        targetSelector.addGoal(4, new NearestAttackableTargetGoal<>(this, Animal.class, Boolean.TRUE) {
            @Override
            public boolean canUse() {
                return super.canUse()
                        && getHunger() < 70
                        && !target.isAlliedTo(mob)
                        && !target.isInWater()
                        && (target instanceof TamableAnimal ta && !ta.isTame() || !(target instanceof TamableAnimal))
                        && !mightBeSleeping()
                        && !(target instanceof TerridensaurusSaevusEntity);
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<TerridensaurusSaevusEntity> main = new AnimationController<>(this, "main", 2, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    if (isInWater()
                            && !onGround()
                            && (!isMature() || getFluidTypeHeight(ForgeMod.WATER_TYPE.get()) > 2f * getRenderSize())) {
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
                                builder.thenLoop("animation.-left");
                            } else {
                                builder.thenLoop("animation.+right");
                            }
                        } else {
                            if (getCommand() == 1) {
                                builder.thenLoop("animation.idle_sit");
                            } else if (mightBeSleeping()) {
                                builder.thenLoop("animation.sleep");
                            } else {
                                builder.thenLoop("animation.idle");
                            }
                        }
                    }

                    return state.setAndContinue(builder);
                })
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<TerridensaurusSaevusEntity> extra = new AnimationController<>(
                        this, "extra", 10, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle2"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle3"))
                .triggerableAnim("idle_sit2", RawAnimation.begin().thenPlay("animation.idle_sit2"))
                .triggerableAnim("idle_sit3", RawAnimation.begin().thenPlay("animation.idle_sit3"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<TerridensaurusSaevusEntity> attack = new AnimationController<>(
                        this, "attack", 10, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("attack2", RawAnimation.begin().thenPlay("animation.attack2"))
                .triggerableAnim("roar", RawAnimation.begin().thenPlay("animation.roar"))
                .triggerableAnim("strike", RawAnimation.begin().thenPlay("animation.strike"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"))
                .triggerableAnim("left_attack_turn", RawAnimation.begin().thenPlay("animation.left_attack"))
                .triggerableAnim("right_attack_turn", RawAnimation.begin().thenPlay("animation.right_attack"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        controllerRegistrar.add(main, attack, extra);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new TerridensaurusSaevusBodyControl(this);
    }

    @Override
    public boolean mightBeSleeping() {
        return updateSkyBrightness() > 8
                && this.getControllingPassenger() == null
                && getCommand() == 0
                && this.getAttackState().getType() == AttackType.EMPTY
                && this.getTarget() == null
                && !this.isInWater()
                && !isBloody();
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        if (this.rideSpeed != 0 || pPlayer.jumping) {
            updateMount();
            if (getCommand() == 1) this.setCommand(0);
        }
        if (this.isInFluidType() && !this.onGround()) {
            float y = 0;

            if (pPlayer.jumping && getSwimState() == 2) {
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
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 move) {
        tickStableHead();
        if (this.getAttackState().getType().canMove() && this.isSaddled()) {
            if (((!this.getRotDirection().isLargeTurn() || isInWater())
                            && (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0))
                    || this.isSprinting()) {
                float baseSpeed =
                        (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.rideSpeed = Mth.approach(this.rideSpeed, isSprinting() ? baseSpeed * 2.2f : baseSpeed, 0.08f);
            } else {
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.16f);
            }
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return this.rideSpeed + 0.1f;
    }

    public void setRiddenSpeed(float speed) {
        this.rideSpeed = speed;
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
    public void equipSaddle() {
        this.inventory.setItem(0, new ItemStack(ErsItems.SAEVUS_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    @Override
    public boolean isPushable() {
        return isSprinting() && isVehicle();
    }

    @Override
    protected float getBaseArmorValue() {
        return 6;
    }

    @Override
    protected float getBoostedArmorValue() {
        return 10;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!level().isClientSide() && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new TerridensaurusSaevusMenuProvider(this),
                    buf -> buf.writeInt(this.getId()));
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Command", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setCommand(compoundTag.getInt("Command"));
        updateAgeFromServer();
    }

    public int getInventorySize() {
        return 8;
    }

    @Override
    public void tick() {
        super.tick();
        if (!level().isClientSide()) {
            tickCommonServer();

            if (tickCount % 20 == 0) {
                int nextPoopTime = getNextPoopTime();
                if (nextPoopTime > 0) {
                    setNextPoopTime(nextPoopTime - 20);
                } else {
                    this.generateFeces();
                    setNextPoopTime(10000 + RandomSource.create().nextInt(14000));
                }
            }

            if (this.isSprinting() && isMature()) {
                stompEffect(3f, 0.5f, checkEquipment(ErsItems.SCRATCHING_BOARD.get()) ? 6f : 4f);
            }
        } else {
            boolean flag2 = onGround() && !isMoving() && this.getAttackState().getType() == AttackType.EMPTY;

            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME)) {
                this.entityData.set(
                        NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200, 400));
                int newState = RandomSource.create().nextInt(4);
                if (newState != 0 && flag2) {
                    if (getCommand() == 1) {
                        if (newState == 1) {
                            triggerAnim("extra", "idle_sit2");
                        } else if (newState == 2) {
                            triggerAnim("extra", "idle_sit3");
                        } else {
                            triggerAnim("extra", "idle_sit3");
                        }
                    } else if (updateSkyBrightness() < 8) {
                        if (newState == 1) {
                            triggerAnim("extra", "idle2");
                        } else {
                            triggerAnim("extra", "idle3");
                        }
                    }
                }
            }
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
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.type().msgId().equals("sweetBerryBush")
                || source.getEntity() instanceof Pufferfish
                || source.type().msgId().equals("cactus")) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {}

    @Override
    public void aiStep() {
        super.aiStep();

        float height = isMature() ? 2.7f : 1f;
        waterAiStep(height * getRenderSize());

        if (isInWater()) {
            if (onGround()) {
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(8f);
            } else this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4f);
        }
    }

    public void updateAgeFromServer() {
        if (getAgeInDays() >= 20 && !this.isMature()) {
            this.setMature(true);
            getAnimatableInstanceCache()
                    .getManagerForId(getId())
                    .getAnimationControllers()
                    .get("main")
                    .stop();
        }

        if (getAgeInDays() >= 38 && this.canBeElite()) {
            this.setElite(true);
        }

        float scale = calculateScale();
        if (isMature()) this.setRenderSize(scale);
        else {
            this.setRenderSize(ErsUtils.calculateBabyRenderSize(getAgeInDays()));
        }
        this.refreshDimensions();

        if (!isElite()) scale *= 0.7f;
        if (!isMature()) scale *= 0.4f;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(325 * scale);
        setBaseHealth(325 * scale);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(35 * scale);
        if (!isMature()) scale *= 2.2f;
        if (!this.isElite() && isMature()) scale /= 0.7f;
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.18 * scale);
        containerChanged(this.getInventory());
    }

    protected void setBaseHealth(float baseHealth) {
        this.baseHealth = baseHealth;
    }

    private float baseHealth = 325;

    @Override
    protected float getBaseHealthValue() {
        return baseHealth;
    }

    private void placeNest() {
        if (this.level().isClientSide) return;
        BlockState state = ErsBlocks.SAEVUS_NEST.get().defaultBlockState();
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        double yRot = Math.toRadians(this.getYRot());
        int offsetX = (int) (-Math.sin(yRot) * 1.5);
        int offsetZ = (int) (Math.cos(yRot) * 1.5);

        BlockPos targetPos = pos.offset(offsetX, 0, offsetZ);
        if (level.getBlockState(targetPos).isAir()
                && level.getBlockState(targetPos.below()).isSolidRender(level, targetPos.below())) {
            level.setBlock(targetPos, state, 3);
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
                if ($$6.nextFloat() < 0.1f) {
                    pSpawnData = new ErsGroupData<>(false, SpawnVariant.getRareSpawnVariant(Variant.values(), $$6));
                } else {
                    pSpawnData = new ErsGroupData<>(
                            false,
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), $$6),
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), $$6),
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), $$6),
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), $$6));
                }
            }

            this.setVariant(((ErsGroupData<Variant>) pSpawnData).getVariant($$6));

            this.setAgeInDays(this.random.nextIntBetweenInclusive(15, 50));
            if (this.random.nextFloat() < 0.3f) {
                this.setCanBeElite(true);
            }
            updateAgeFromServer();
            this.setHealth(this.getMaxHealth());
            if (isMature()) {
                if (this.random.nextFloat() < 0.15f) {
                    TickHelper.tickLater(level(), 20, this::placeNest);
                }
            }
            if (this.random.nextFloat() < 0.005f) {
                var customNameList =
                        new String[] {"Icarian", "Halgus_DEVIL", "remake", "Guardian", "Emperor", "Taroth"};
                this.setCustomName(Component.literal(customNameList[this.random.nextInt(customNameList.length)]));
            }

            return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        }
    }

    public @NotNull Variant getVariant() {
        return Variant.byId(getVariantId());
    }

    public void setVariant(Variant pVariant) {
        setVariantId(pVariant.getId());
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        if (isSoul()) return;

        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        if (isMature() && random.nextFloat() < 0.3f) {
            ItemStack stack = new ItemStack(ErsItems.SAEVUS_EGG.get());
            this.spawnAtLocation(stack);
        }
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {}

    private void generateFeces() {
        if (this.level().isClientSide) return;
        List<BlockState> fecesBlocks = List.of(
                ErsBlocks.BONE_FECES.get().defaultBlockState(),
                ErsBlocks.SMALL_FECES.get().defaultBlockState(),
                ErsBlocks.LARGE_FECES.get().defaultBlockState(),
                ErsBlocks.GLASSES_FECES.get().defaultBlockState());
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        double yRot = Math.toRadians(this.getYRot());
        int offsetX = (int) (-Math.sin(yRot) * 1.5);
        int offsetZ = (int) (Math.cos(yRot) * 1.5);

        BlockPos targetPos = pos.offset(offsetX, 0, offsetZ);
        if (level.getBlockState(targetPos).isAir()
                && level.getBlockState(targetPos.below()).isSolidRender(level, targetPos.below())) {
            BlockState state = fecesBlocks.get(this.level().random.nextInt(fecesBlocks.size()));
            level.setBlock(targetPos, state, 3);
            level.playSound(
                    null,
                    targetPos.getX() + 0.5,
                    targetPos.getY() + 0.5,
                    targetPos.getZ() + 0.5,
                    SoundEvents.SLIME_BLOCK_PLACE,
                    SoundSource.BLOCKS,
                    0.5f,
                    1.0f);
        }
    }

    @Override
    public float getSoundRange() {
        return 81;
    }

    public enum Variant implements StringRepresentable, SpawnVariant {
        ORIGINAL(0, "original", true),
        BLACK(1, "black", false),
        LACK_YELLOW(2, "lack_yellow", false),
        WHITE(3, "white", false);

        private static final IntFunction<Variant> BY_ID =
                ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        private final int id;
        private final String name;
        private final boolean common;

        Variant(int pId, String pName, boolean pCommon) {
            this.id = pId;
            this.name = pName;
            this.common = pCommon;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public boolean isCommon() {
            return this.common;
        }

        public @NotNull String getSerializedName() {
            return this.name;
        }

        public static Variant byId(int pId) {
            return BY_ID.apply(pId);
        }
    }
}
