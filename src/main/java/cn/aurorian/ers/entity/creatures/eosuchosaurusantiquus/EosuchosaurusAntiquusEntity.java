package cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus;

import cn.aurorian.ers.client.animator.EosuchosaurusAntiquusAnimator;
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
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.ai.EosuchosaurusAntiquusAttackGoal;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.inventory.EosuchosaurusAntiquusMenuProvider;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.ers.util.SimpleAutoPlayingSoundKeyFrameHandler;
import cn.aurorian.ers.util.TickHelper;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.Set;
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
import net.minecraft.world.entity.animal.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.food.FoodProperties;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
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

public class EosuchosaurusAntiquusEntity extends ErsTamableVehicle<EosuchosaurusAntiquusEntity>
        implements ContainerListener, HasCustomInventoryScreen, VariantHolder<EosuchosaurusAntiquusEntity.Variant> {

    private static final Set<Class<? extends Entity>> PREY_TYPES =
            Set.of(Pig.class, Cow.class, Sheep.class, Chicken.class, AquicornisDinosauriformisEntity.class);

    public EosuchosaurusAntiquusEntity(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        switchNavigator(true);
        animator = new EosuchosaurusAntiquusAnimator(this);
        this.setMaxUpStep(1.5f);
        this.createInventory();
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.SWIM_COST = 0.1f;
        this.SPRINT_COST = 0.05f;
        this.RECOVER = 0.35f;

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

    private final GeneralAnimator<EosuchosaurusAntiquusEntity> animator;
    private int waterStepBoostTicks;
    private static final EntityDataAccessor<Integer> NEXT_POOP_TIME =
            SynchedEntityData.defineId(EosuchosaurusAntiquusEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME =
            SynchedEntityData.defineId(EosuchosaurusAntiquusEntity.class, EntityDataSerializers.INT);

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
                .add(Attributes.MOVEMENT_SPEED, 0.2)
                .add(Attributes.MAX_HEALTH, 100)
                .add(Attributes.FOLLOW_RANGE, 36)
                .add(Attributes.ATTACK_DAMAGE, 12)
                .add(Attributes.ARMOR, 8)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.5)
                .add(ForgeMod.SWIM_SPEED.get(), 4);
    }

    @Override
    public GeneralAnimator<EosuchosaurusAntiquusEntity> getAnimator() {
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
        startAttack(AttackType.ANTIQUUS_ATTACK);
    }

    @Override
    public void executeTurnAttackType() {
        startAttack(AttackType.ANTIQUUS_ATTACK_TURN);
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
        return pStack.is(Items.CHICKEN)
                || pStack.is(Items.BEEF)
                || pStack.is(Items.PORKCHOP)
                || pStack.is(Items.MUTTON)
                || pStack.is(ErsItems.MEAT_FEED.get());
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

        if (!level().isClientSide && (isFood(player.getMainHandItem()))) {
            if (!this.isTame() && (this.getHealth() / this.getMaxHealth() < 0.4f || this.getAgeInDays() < 1)) {
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            } else if (this.isTame() && player.getMainHandItem().is(ErsItems.MEAT_FEED.get())) {
                this.feed(10);
                this.heal(10);
                player.getMainHandItem().shrink(1);
            }
        }

        if (!level().isClientSide) {
            FoodProperties foodProperties = player.getMainHandItem().getFoodProperties(this);
            if (foodProperties != null && foodProperties.isMeat()) {
                this.feed(3);
                this.heal(3);
                player.getMainHandItem().shrink(1);
            }
        }
        return super.mobInteract(player, pHand);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new EosuchosaurusAntiquusAttackGoal(this, 2, false));
        goalSelector.addGoal(2, new MobFollowOwnerGoal(this, 2.4D, 7.0F, 4.0F, 64F, false));
        goalSelector.addGoal(5, new WaterAvoidingRandomStrollGoal(this, 1.2) {
            @Override
            public boolean canUse() {
                if (!((EosuchosaurusAntiquusEntity) this.mob)
                        .getAttackState()
                        .getType()
                        .canMove()) {
                    return false;
                }
                if (((ErsTamable<?>) this.mob).updateSkyBrightness() > 8) return false;
                return super.canUse() && ((EosuchosaurusAntiquusEntity) this.mob).getCommand() != 1;
            }
        });
        goalSelector.addGoal(6, new ErsTamableLookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(1, new ErsTamableHurtByTargetGoal(this));
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
                        && PREY_TYPES.contains(target.getClass());
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<EosuchosaurusAntiquusEntity> main = new AnimationController<>(this, "main", 2, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    if (isInWater() && !onGround()) {
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

        AnimationController<EosuchosaurusAntiquusEntity> extra = new AnimationController<>(
                        this, "extra", 10, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle2"))
                .triggerableAnim("idle_sit2", RawAnimation.begin().thenPlay("animation.idle_sit2"))
                .triggerableAnim("idle_sit3", RawAnimation.begin().thenPlay("animation.idle_sit3"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<EosuchosaurusAntiquusEntity> attack = new AnimationController<>(
                        this, "attack", 10, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"))
                .triggerableAnim("left_attack", RawAnimation.begin().thenPlay("animation.left_attack"))
                .triggerableAnim("right_attack", RawAnimation.begin().thenPlay("animation.right_attack"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        controllerRegistrar.add(main, attack, extra);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new EosuchosaurusAntiquusBodyControl(this);
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
        if (this.isInFluidType()) {
            float y = 0;

            if (pPlayer.jumping && (getSwimState() != 1 || wasEyeInWater)) {
                y = 0.4f;
                if (!wasEyeInWater) setSwimState(1);
            }

            if (rideSpeed > 0.05F) {
                if (this.horizontalCollision) {
                    y = 0.6f;
                    setSwimState(1);
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
                this.rideSpeed = Mth.approach(this.rideSpeed, isSprinting() ? baseSpeed * 2.4f : baseSpeed, 0.08f);
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
        this.inventory.setItem(0, new ItemStack(ErsItems.ANTIQUUS_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    @Override
    protected float getBaseArmorValue() {
        return 8;
    }

    @Override
    protected float getBoostedArmorValue() {
        return 12;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!level().isClientSide() && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new EosuchosaurusAntiquusMenuProvider(this),
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
        } else {
            boolean flag2 = onGround() && !isMoving() && this.getAttackState().getType() == AttackType.EMPTY;

            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME)) {
                this.entityData.set(
                        NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200, 400));
                int newState = RandomSource.create().nextInt(2);
                if (newState != 0 && flag2) {
                    if (getCommand() == 1) {
                        if (newState == 1) {
                            triggerAnim("extra", "idle_sit2");
                        } else if (newState == 2) {
                            triggerAnim("extra", "idle_sit3");
                        }
                    } else if (updateSkyBrightness() < 8) {
                        triggerAnim("extra", "idle2");
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
    public void aiStep() {
        super.aiStep();

        float height = isMature() ? 1.7f : 0.9f;
        waterAiStep(height * getRenderSize());

        if (isInWater()) {
            if (onGround()) {
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(8f);
            } else this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4f);
        }
        tickWaterStepBoost();
        boostOverWaterStep();
    }

    private void tickWaterStepBoost() {
        if (waterStepBoostTicks <= 0) {
            return;
        }

        waterStepBoostTicks--;
        if (waterStepBoostTicks == 0 && getSwimState() == 3) {
            setSwimState(wasEyeInWater ? 2 : 1);
        }
    }

    private void boostOverWaterStep() {
        if (getControllingPassenger() == null
                || !isInFluidType()
                || !horizontalCollision
                || rideSpeed <= 0.05F
                || isDiving()) {
            return;
        }

        Vec3 delta = getDeltaMovement();
        if (delta.y < 0.36D) {
            setDeltaMovement(delta.x, 0.36D, delta.z);
            setSwimState(3);
            waterStepBoostTicks = 8;
            hasImpulse = true;
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

        float scale = calculateScale();
        if (isMature()) this.setRenderSize(scale);
        else {
            this.setRenderSize(ErsUtils.calculateBabyRenderSize(getAgeInDays()));
        }
        this.refreshDimensions();

        if (!isMature()) scale *= 0.4f;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(100 * scale);
        setBaseHealth(100 * scale);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(12 * scale);
        if (!isMature()) scale *= 2.2f;
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.2 * scale);
        containerChanged(this.getInventory());
    }

    protected void setBaseHealth(float baseHealth) {
        this.baseHealth = baseHealth;
    }

    private float baseHealth = 100;

    @Override
    protected float getBaseHealthValue() {
        return baseHealth;
    }

    private void placeNest() {
        if (this.level().isClientSide) return;
        BlockState state = ErsBlocks.ANTIQUUS_NEST.get().defaultBlockState();
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        double yRot = Math.toRadians(this.getYRot());
        int offsetX = (int) (-Math.sin(yRot) * 1.3);
        int offsetZ = (int) (Math.cos(yRot) * 1.3);

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
            RandomSource random = pLevel.getRandom();
            if (pSpawnData == null) {
                if (random.nextFloat() < 0.1f) {
                    pSpawnData = new ErsGroupData<>(false, SpawnVariant.getRareSpawnVariant(Variant.values(), random));
                } else {
                    pSpawnData =
                            new ErsGroupData<>(false, SpawnVariant.getCommonSpawnVariant(Variant.values(), random));
                }
            }

            this.setVariant(((ErsGroupData<Variant>) pSpawnData).getVariant(random));

            this.setAgeInDays(this.random.nextIntBetweenInclusive(15, 50));

            updateAgeFromServer();
            this.setHealth(this.getMaxHealth());
            if (isMature()) {
                if (this.random.nextFloat() < 0.15f) {
                    TickHelper.tickLater(level(), 20, this::placeNest);
                }
            }
            //            if (this.random.nextFloat() < 0.005f) {
            //                var customNameList =
            //                        new String[] {};
            //
            // this.setCustomName(Component.literal(customNameList[this.random.nextInt(customNameList.length)]));
            //            }

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
            ItemStack stack = new ItemStack(ErsItems.ANTIQUUS_EGG.get());
            this.spawnAtLocation(stack);
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if (source.type().msgId().equals("sweetBerryBush")) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        return false;
    }

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {
        if (!pState.is(Blocks.SWEET_BERRY_BUSH)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }
    }

    private void generateFeces() {
        if (this.level().isClientSide) return;
        List<BlockState> fecesBlocks = List.of(
                ErsBlocks.BONE_FECES.get().defaultBlockState(),
                ErsBlocks.SMALL_FECES.get().defaultBlockState());
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

    public enum Variant implements StringRepresentable, SpawnVariant {
        ORIGINAL(0, "original", true),
        WHITE(1, "white", false),
        BLACK(2, "black", false);

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
