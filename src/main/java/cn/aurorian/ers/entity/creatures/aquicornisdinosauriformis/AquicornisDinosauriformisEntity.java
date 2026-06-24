package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis;

import cn.aurorian.ers.client.animator.AquicornisDinosauriformisAnimator;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.*;
import cn.aurorian.ers.entity.ai.ErsTamableLookAtPlayerGoal;
import cn.aurorian.ers.entity.ai.ErsTamableVehicleRandomSwimGoal;
import cn.aurorian.ers.entity.ai.goal.*;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.ai.AquicornisDinosauriformisEatBerryGoal;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.ai.AquicornisDinosauriformisForageGoal;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.ai.AquicornisDinosauriformisMeleeAttackGoal;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.inventory.AquicornisDinosauriformisMenuProvider;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.packet.VehicleJumpInWaterPacket;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.ers.util.SimpleAutoPlayingSoundKeyFrameHandler;
import cn.aurorian.ers.util.TickHelper;
import com.mojang.serialization.Codec;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.FluidTags;
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
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class AquicornisDinosauriformisEntity extends ErsTamableVehicle<AquicornisDinosauriformisEntity>
        implements ContainerListener,
                HasCustomInventoryScreen,
                Bucketable,
                VariantHolder<AquicornisDinosauriformisEntity.Variant> {
    private final GeneralAnimator<AquicornisDinosauriformisEntity> animator;

    private static final float BASE_MOVE_SPEED = 0.22f;

    private static final float BASE_HEALTH = 45.0f;

    private static final float BASE_ATTACK_DAMAGE = 6.0f;

    private static final float ACCELERATION = 0.08f;

    private static final EntityDataAccessor<Integer> NEXT_POOP_TIME =
            SynchedEntityData.defineId(AquicornisDinosauriformisEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(AquicornisDinosauriformisEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME =
            SynchedEntityData.defineId(AquicornisDinosauriformisEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> NEXT_WANTED =
            SynchedEntityData.defineId(AquicornisDinosauriformisEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> TAMING_PROGRESS =
            SynchedEntityData.defineId(AquicornisDinosauriformisEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> LAST_DIG_TIME =
            SynchedEntityData.defineId(AquicornisDinosauriformisEntity.class, EntityDataSerializers.INT);

    public AquicornisDinosauriformisEntity(
            EntityType<? extends AquicornisDinosauriformisEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        animator = new AquicornisDinosauriformisAnimator(this);
        this.createInventory();
        switchNavigator(true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setMaxUpStep(1f);
        this.SWIM_COST = 0.1f;
        this.SPRINT_COST = 0.1f;
        this.RECOVER = 0.25f;
        this.WATER_ANIMAL = true;

        this.doAgeTick = true;
        this.doHunger = true;
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            moveControl = new LimitedMoveControl(this);
            navigation = new MMGroundPathNavigation(this, level());
        } else {
            moveControl = new AquaticMoveControl(this, 1.2F);
            navigation = createNavigation(level());
        }
    }

    @Override
    public void executeDefaultAttackType() {
        startAttack(AttackType.DINOSAURIFORMIS_ATTACK);
    }

    @Override
    public void executeTurnAttackType() {
        //        if (isSprinting()) return;
        //        if (isInWater() && !onGround()) {
        //            startAttack(AttackType.SWAMP_DRAGON_ATTACK_SWIM_TURN);
        //        } else {
        //            startAttack(AttackType.SWAMP_DRAGON_ATTACK_TURN);
        //        }
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new AquicornisDinosauriformisBodyControl(this);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new AquicornisDinosauriformisMeleeAttackGoal(this, 1, false));
        goalSelector.addGoal(2, new MobFollowOwnerGoal(this, 2.2D, 7.0F, 4.0F, 64F, false));
        goalSelector.addGoal(3, new AquicornisDinosauriformisForageGoal(this));
        goalSelector.addGoal(4, new AquicornisDinosauriformisEatBerryGoal(this));
        goalSelector.addGoal(5, new ErsTamableVehicleRandomSwimGoal(this, 1));
        goalSelector.addGoal(5, new MobWanderGoal(this, 1.2));
        goalSelector.addGoal(6, new ErsTamableLookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(1, new ErsTamableHurtByTargetGoal(this));
        targetSelector.addGoal(2, new ErsTamableOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new ErsTamableOwnerHurtTargetGoal(this));
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level) {

            @Override
            public boolean isStableDestination(@NotNull BlockPos p_217799_) {
                return true;
            }
        };
    }

    //    @Override
    //    public boolean mightBeSleeping() {
    //        return updateSkyBrightness() < 4
    //                && this.getControllingPassenger() == null
    //                && getCommand() == 0
    //                && this.getAttackState().getType() == AttackType.EMPTY
    //                && this.getTarget() == null
    //                && !this.isInWater()
    //                && !isBloody();
    //    }

    @Override
    public void tick() {
        super.tick();
        if (isClientSide()) {
            boolean flag2 = onGround() && !isMoving() && this.getAttackState().getType() == AttackType.EMPTY;

            //            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME)) {
            //                this.entityData.set(
            //                        NEXT_CHANGE_TIME, this.tickCount +
            // RandomSource.create().nextIntBetweenInclusive(200, 400));
            //                int newState = RandomSource.create().nextInt(4);
            //                if (newState != 0 && flag2) {
            //                    if (getCommand() == 1) {
            //                        if (newState == 1) {
            //                            triggerAnim("extra", "idle_sit4");
            //                        } else if (newState == 2) {
            //                            triggerAnim("extra", "idle_sit2");
            //                        } else {
            //                            triggerAnim("extra", "idle_sit3");
            //                        }
            //                    } else if (updateSkyBrightness() > 4) {
            //                        if (newState == 1) {
            //                            triggerAnim("extra", "idle2");
            //                        } else {
            //                            triggerAnim("extra", "idle3");
            //                        }
            //                    }
            //                }
            //            }
            if (this.isMoving()) {
                updateMount();
            }
        }

        if (!isClientSide()) {
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
        }
    }

    @Override
    protected void tickAutoFeed() {
        if (this.getInventory() == null) return;
        if (!this.getInventory().getItem(2).isEmpty()) {
            this.getInventory().getItem(2).shrink(1);
            this.feed(10);
            this.heal(10);
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

        if (!this.isMature()) scale *= 0.4f;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH * scale);
        setBaseHealth(BASE_HEALTH * scale);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BASE_ATTACK_DAMAGE * scale);
        if (!this.isMature()) scale *= 1.5f;
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

    public Predicate<ItemStack> foodPredicate = item -> item.is(ErsItems.FRUIT_FEED.get())
            || item.is(ErsItems.FISH_FEED.get())
            || item.is(ErsItems.WORM_FEED.get())
            || item.is(ErsItems.VEGETABLE_FEED.get())
            || item.is(ErsItems.HAY_FEED.get());

    private ItemStack getWantedFood() {
        return switch (entityData.get(NEXT_WANTED)) {
            case 0 -> ErsItems.FRUIT_FEED.get().getDefaultInstance();
            case 1 -> ErsItems.FISH_FEED.get().getDefaultInstance();
            case 2 -> ErsItems.WORM_FEED.get().getDefaultInstance();
            case 3 -> ErsItems.VEGETABLE_FEED.get().getDefaultInstance();
            case 4 -> ErsItems.HAY_FEED.get().getDefaultInstance();
            default -> ItemStack.EMPTY;
        };
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        ItemStack wanted = getWantedFood();
        return itemStack.is(wanted.getItem());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if (player.getMainHandItem().getItem() == ErsItems.LARGE_WATER_BUCKET.get()
                && this.isTame()
                && this.isOwnedBy(player)) {
            return ErsMobLargeBucket.bucketMobPickup(player, pHand, this).orElse(InteractionResult.PASS);
        }
        if (isMature()
                && isTame()
                && !isVehicle()
                && this.isOwnedBy(player)
                && !player.isCrouching()
                && !foodPredicate.test(player.getMainHandItem())) {
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
        }

        if (pHand == InteractionHand.MAIN_HAND
                && player.isCrouching()
                && this.isTame()
                && this.isOwnedBy(player)
                && !foodPredicate.test(player.getMainHandItem())) {
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
        }

        if (!isClientSide() && pHand == InteractionHand.MAIN_HAND) {
            if (!this.isTame()) {
                if (!isInLove()) {
                    if (isFood(player.getMainHandItem())) {
                        setInLove(player);
                        entityData.set(NEXT_WANTED, Mth.randomBetweenInclusive(random, 0, 4));
                        entityData.set(TAMING_PROGRESS, entityData.get(TAMING_PROGRESS) + 1);
                        player.getMainHandItem().shrink(1);
                        if (entityData.get(TAMING_PROGRESS).intValue() >= 12
                                && !ForgeEventFactory.onAnimalTame(this, player)) {
                            this.tame(player);
                            this.level().broadcastEntityEvent(this, (byte) 7);
                        }
                    } else {
                        player.displayClientMessage(
                                Component.translatable(
                                        "ers.taming.dinosauriformis.tip",
                                        getWantedFood().getDisplayName()),
                                true);
                    }
                } else {
                    player.displayClientMessage(Component.translatable("ers.taming.dinosauriformis.wait"), true);
                }
            } else if (this.isTame() && foodPredicate.test(player.getMainHandItem())) {
                this.feed(10);
                this.heal(10);
                player.getMainHandItem().shrink(1);

                if (isFood(player.getMainHandItem())) {
                    setInLove(player);
                    entityData.set(NEXT_WANTED, Mth.randomBetweenInclusive(random, 0, 4));
                } else {
                    player.displayClientMessage(
                            Component.translatable(
                                    "ers.taming.dinosauriformis.tip",
                                    getWantedFood().getDisplayName()),
                            true);
                }
            }
        }
        return super.mobInteract(player, pHand);
    }

    private void generateFeces() {
        //        if (this.level().isClientSide) return;
        //        List<BlockState> fecesBlocks = List.of(
        //                ErsBlocks.BONE_FECES.get().defaultBlockState(),
        //                ErsBlocks.SMALL_FECES.get().defaultBlockState(),
        //                ErsBlocks.LARGE_FECES.get().defaultBlockState(),
        //                ErsBlocks.GLASSES_FECES.get().defaultBlockState(),
        //                ErsBlocks.TEL_FECES.get().defaultBlockState());
        //        BlockPos pos = this.blockPosition();
        //        Level level = this.level();
        //
        //        double yRot = Math.toRadians(this.getYRot());
        //        int offsetX = (int) (-Math.sin(yRot) * 1.5); // 向后偏移1.5格
        //        int offsetZ = (int) (Math.cos(yRot) * 1.5);
        //
        //        BlockPos targetPos = pos.offset(offsetX, 0, offsetZ);
        //        if (level.getBlockState(targetPos).isAir()
        //                && level.getBlockState(targetPos.below()).isSolidRender(level, targetPos.below())) {
        //            BlockState state = fecesBlocks.get(this.level().random.nextInt(fecesBlocks.size()));
        //            if (state.is(ErsBlocks.TEL_FECES.get())) {
        //                if (this.level().random.nextFloat() < 0.1f) {
        //                    return;
        //                }
        //            }
        //            level.setBlock(targetPos, state, 3);
        //            level.playSound(
        //                    null,
        //                    targetPos.getX() + 0.5,
        //                    targetPos.getY() + 0.5,
        //                    targetPos.getZ() + 0.5,
        //                    SoundEvents.SLIME_BLOCK_PLACE,
        //                    SoundSource.BLOCKS,
        //                    0.5f,
        //                    1.0f);
        //        }
    }

    private void placeNest() {
        if (this.level().isClientSide) return;
        BlockState state = ErsBlocks.DINOSAURIFORMIS_NEST.get().defaultBlockState();
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
            level.setBlock(pos, state, 3);
        }
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
            } else if (isDiving() && getSwimState() != 1) {
                y = -0.4f;
            }

            if (rideSpeed > 0.1 && getSwimState() == 1) {
                if (this.horizontalCollision && !onGround()) {
                    y = 0.8f;
                }
            }

            if (!getAttackState().getType().canMove()) y = 0;
            return new Vec3(0, y, this.rideSpeed);
        } else {
            if (onGround() && player.jumping && getAttackState().isEmpty()) {
                startAttack(AttackType.DINOSAURIFORMIS_JUMP);
                this.setDeltaMovement(this.getDeltaMovement().scale(2).add(0, 0.6, 0));
            }
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return this.rideSpeed + 0.1f;
    }

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 move) {
        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        tickStableHead();

        if (this.isInFluidType() && pitch > 20 && isSprinting() && getWaterDepth() >= 4) {
            jumpFromGround();
        }

        if (this.getAttackState().getType().canMove() && this.isSaddled()) {
            if ((!this.getRotDirection().isLargeTurn() || this.isSprinting())
                    && (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0)) {
                float baseSpeed =
                        (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.rideSpeed = Mth.approach(
                        this.rideSpeed,
                        isSprinting() ? baseSpeed * 2f : isInWater() ? 1.25f * baseSpeed : baseSpeed,
                        ACCELERATION);
            } else {
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, ACCELERATION * 2);
            }
        } else {
            this.rideSpeed = Mth.approach(this.rideSpeed, 0, ACCELERATION * 3);
        }
    }

    @Override
    protected void jumpFromGround() {
        double jumpVelocity = 0.25;
        this.setDeltaMovement(this.getDeltaMovement().add(0, jumpVelocity, 0));
        this.hasImpulse = true;
        ForgeHooks.onLivingJump(this);
        if (this.level().isClientSide) ErsNetwork.INSTANCE.sendToServer(new VehicleJumpInWaterPacket(this.getId()));
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        AnimationController<AquicornisDinosauriformisEntity> main = new AnimationController<>(
                        this, "main", 5, state -> {
                            RawAnimation builder = RawAnimation.begin();
                            boolean flag1 = false;
                            for (int y = 1; y <= 8; y++) {
                                BlockPos checkPos =
                                        new BlockPos(this.blockPosition().below(y));
                                BlockState blockstate = this.level().getBlockState(checkPos);

                                if (blockstate.isAir()) {
                                    continue;
                                }
                                flag1 = isWaterBlock(
                                        this.level(), this.blockPosition().below(y));
                                break;
                            }

                            if (isFalling() && !flag1) {
                                builder.thenLoop("animation.fall");
                            }
                            if (isInWater() && !onGround()) {
                                if (isMoving()) {
                                    if (isSprinting()) {
                                        builder.thenLoop("animation.quickly_swim_mid");
                                    } else if (!isSprinting()
                                            && (getWaterDepth() <= 3 || this.onGround() || !this.isMature())) {
                                        builder.thenLoop("animation.swim");
                                    } else {
                                        builder.thenLoop("animation.swim_mid");
                                    }
                                } else if (!this.getRotDirection().isNone() && isMature()) {
                                    if (this.getRotDirection().isLeft()) {
                                        builder.thenLoop("animation.-w_left");
                                    } else {
                                        builder.thenLoop("animation.+w_right");
                                    }
                                } else {
                                    if (this.getControllingPassenger() instanceof Player player
                                            && getRiddenInput(player, new Vec3(0, 0, 0)).y < 0) {
                                        builder.thenLoop("animation.swim_down");
                                    } else if (this.getControllingPassenger() != null
                                            && this.getControllingPassenger().jumping) {
                                        builder.thenLoop("animation.swim_up");
                                    } else {
                                        builder.thenLoop("animation.swim_idle");
                                    }
                                }
                            } else {
                                if (isMoving()) {
                                    if (isSprinting()) {
                                        builder.thenLoop("animation.run");
                                    } else {
                                        builder.thenLoop("animation.walk");
                                    }
                                } else if (!this.getRotDirection().isNone()) {
                                    if (this.getRotDirection().isLeft()) {
                                        builder.thenLoop("animation.-left");
                                    } else {
                                        builder.thenLoop("animation.+right");
                                    }
                                } else {
                                    if (getCommand() == 1) {
                                        builder.thenLoop("animation.sit");
                                    } else if (mightBeSleeping()) {
                                        if (this.level().getRawBrightness(this.blockPosition(), 0) > 8 && isMature()) {
                                            builder.thenLoop("animation.sleep-sunlight");
                                        } else {
                                            builder.thenLoop("animation.sleep");
                                        }
                                    } else if (onGround()) {
                                        builder.thenLoop("animation.idle");
                                    }
                                }
                            }

                            return state.setAndContinue(builder);
                        })
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<AquicornisDinosauriformisEntity> attack = new AnimationController<>(
                        this, "attack", 5, state -> PlayState.STOP)
                .triggerableAnim("dig", RawAnimation.begin().thenPlay("animation.dig"))
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("attack2", RawAnimation.begin().thenPlay("animation.attack2"))
                .triggerableAnim("attack_turn", RawAnimation.begin().thenPlay("animation.attack_turn"))
                .triggerableAnim("attack_swim_turn", RawAnimation.begin().thenPlay("animation.attack_swim_turn"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"))
                .triggerableAnim("jump", RawAnimation.begin().thenPlay("animation.jump"))
                .triggerableAnim("jump_attack", RawAnimation.begin().thenPlay("animation.jump_attack"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<AquicornisDinosauriformisEntity> extra = new AnimationController<>(
                        this, "extra", 0, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle2"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle3"))
                .triggerableAnim("idle_sit2", RawAnimation.begin().thenPlay("animation.idle_sit2"))
                .triggerableAnim("idle_sit3", RawAnimation.begin().thenPlay("animation.idle_sit3"))
                .triggerableAnim("idle_sit4", RawAnimation.begin().thenPlay("animation.idle_sit4"));

        controllers.add(main, attack, extra);
    }

    public int getWaterDepth() {
        if (!this.isInWater()) {
            return 0;
        }
        Level level = this.level();
        BlockPos entityPos = this.blockPosition();
        double entityYInBlock = this.getY() - Math.floor(this.getY());
        // 如果实体在方块上半部分且当前方块不是水，从上一个方块开始计算
        if (entityYInBlock > 0.5D && !isWaterBlock(level, entityPos)) {
            entityPos = entityPos.above();
        }
        // 从实体当前位置向上检查
        int depth = 0;
        int maxCheckHeight = 12; // 设置一个最大检查高度，避免无限循环
        for (int i = 0; i < maxCheckHeight; i++) {
            BlockPos checkPos = entityPos.above(i);
            // 检查世界边界
            if (checkPos.getY() > level.getMaxBuildHeight() || checkPos.getY() < level.getMinBuildHeight()) {
                break;
            }
            // 检查当前方块
            if (isWaterBlock(level, checkPos)) {
                depth++;
            } else {
                // 找到第一个非水方块，结束循环
                break;
            }
        }
        return depth;
    }

    public void tickStableHead() {
        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        if (this.isInWater()) {
            setStableHead(pitch < 20);
        } else if (this.onGround()) {
            setStableHead(true);
        } else if (isFalling()) {
            setStableHead(false);
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        if (isSoul()) return;

        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        if (isMature() && random.nextFloat() < 0.15f) {
            ItemStack stack = new ItemStack(ErsItems.DINOSAURIFORMIS_EGG.get());
            this.spawnAtLocation(stack);
        }
    }

    protected void setBaseHealth(float baseHealth) {
        this.baseHealth = baseHealth;
    }

    private float baseHealth = BASE_HEALTH;

    @Override
    protected float getBaseHealthValue() {
        return baseHealth;
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
        if (!isClientSide() && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new AquicornisDinosauriformisMenuProvider(this),
                    buf -> buf.writeInt(this.getId()));
        }
    }

    @Override
    public void startAttack(ErsAttackType type) {
        super.startAttack(type);
        if (this.getCommand() == 1) this.setCommand(0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
        entityData.define(NEXT_POOP_TIME, RandomSource.create().nextInt(24000));
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(200, 400));
        entityData.define(NEXT_WANTED, 0);
        entityData.define(TAMING_PROGRESS, 0);
        entityData.define(LAST_DIG_TIME, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Command", this.getCommand());

        if (!isTame()) {
            compound.putInt("NextWanted", this.entityData.get(NEXT_WANTED));
            compound.putInt("TamingProgress", this.entityData.get(TAMING_PROGRESS));
        }
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setCommand(compoundTag.getInt("Command"));

        if (!isTame() && compoundTag.contains("NextWanted")) {
            this.entityData.set(NEXT_WANTED, compoundTag.getInt("NextWanted"));
        }

        if (!isTame() && compoundTag.contains("TamingProgress")) {
            this.entityData.set(TAMING_PROGRESS, compoundTag.getInt("TamingProgress"));
        }
        updateAgeFromServer();
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

        if (horizontalCollision || onGround()) {
            stopTriggeredAnimation("attack", "jump");
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        return false;
    }

    @Override
    public boolean checkSpawnObstruction(@NotNull LevelReader pLevel) {
        return pLevel.isUnobstructed(this);
    }

    public GeneralAnimator<AquicornisDinosauriformisEntity> getAnimator() {
        return animator;
    }

    public int getInventorySize() {
        return 3;
    }

    public int getNextPoopTime() {
        return this.entityData.get(NEXT_POOP_TIME);
    }

    public void setNextPoopTime(int time) {
        this.entityData.set(NEXT_POOP_TIME, time);
    }

    public int getLastDigTime() {
        return this.entityData.get(LAST_DIG_TIME);
    }

    public void setLastDigTime(int time) {
        this.entityData.set(LAST_DIG_TIME, time);
    }

    @Override
    public boolean isSaddleable() {
        return super.isSaddleable() && isMature();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVE_SPEED)
                .add(Attributes.MAX_HEALTH, BASE_HEALTH)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE)
                .add(Attributes.ARMOR, 12)
                .add(ForgeMod.SWIM_SPEED.get(), 6);
    }

    @Override
    public void equipSaddle() {
        this.inventory.setItem(0, new ItemStack(ErsItems.DINOSAURIFORMIS_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    public boolean isClientSide() {
        return level().isClientSide;
    }

    private static boolean isWaterBlock(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        return blockState.getFluidState().is(FluidTags.WATER)
                && !blockState.getFluidState().isEmpty();
    }

    @Override
    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    @Override
    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    @Override
    public void saveToBucketTag(ItemStack pStack) {
        this.addAdditionalSaveData(pStack.getOrCreateTag());
    }

    @Override
    public void loadFromBucketTag(@NotNull CompoundTag pTag) {
        this.readAdditionalSaveData(pTag);
    }

    @Override
    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL;
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.AQUICORNIS_DINOSAURIFORMIS_LARGE_BUCKET.get());
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (isInWater() && !(this.moveControl instanceof AquaticMoveControl)) {
            switchNavigator(false);
        } else if (!isInWater() && this.moveControl instanceof AquaticMoveControl) {
            switchNavigator(true);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if (getControllingPassenger() != null) {
            waterAiStep(1.15f * getRenderSize());
        } else {
            this.setNoGravity(false);
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
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {
        if (!pState.is(Blocks.SWEET_BERRY_BUSH)) {
            super.makeStuckInBlock(pState, pMotionMultiplier);
        }
    }

    //    @Override
    //    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {}
    //
    //    @Override
    //    protected void playSwimSound(float pVolume) {}

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor pLevel,
            @NotNull DifficultyInstance pDifficulty,
            @NotNull MobSpawnType pReason,
            @org.jetbrains.annotations.Nullable SpawnGroupData pSpawnData,
            @org.jetbrains.annotations.Nullable CompoundTag pDataTag) {
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

            //            if (this.random.nextFloat() < 0.01f) {
            //                var customNameList = new String[] {""};
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
