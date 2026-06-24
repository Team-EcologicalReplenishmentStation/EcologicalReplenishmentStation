package cn.aurorian.ers.entity.creatures.dentisauruslongirostris;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.SwampDragonAnimator;
import cn.aurorian.ers.entity.*;
import cn.aurorian.ers.entity.ai.AttackFishGoal;
import cn.aurorian.ers.entity.ai.ErsTamableLookAtPlayerGoal;
import cn.aurorian.ers.entity.ai.ErsTamableVehicleRandomSwimGoal;
import cn.aurorian.ers.entity.ai.goal.*;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.ai.DentisaurusLongirostrisMeleeAttackGoal;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.inventory.DentisaurusLongirostrisMenuProvider;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.init.ErsSounds;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.packet.VehicleJumpInWaterPacket;
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
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.tags.ItemTags;
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
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Pufferfish;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class DentisaurusLongirostrisEntity extends ErsTamableVehicle<DentisaurusLongirostrisEntity>
        implements ContainerListener,
                HasCustomInventoryScreen,
                Bucketable,
                VariantHolder<DentisaurusLongirostrisEntity.Variant> {
    private final GeneralAnimator<DentisaurusLongirostrisEntity> animator;

    private static final float BASE_MOVE_SPEED = 0.25f;

    private static final float BASE_HEALTH = 250.0f;

    private static final float BASE_ATTACK_DAMAGE = 15.0f;

    private static final float ACCELERATION = 0.08f;

    private static final EntityDataAccessor<Integer> NEXT_POOP_TIME =
            SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Boolean> FROM_BUCKET =
            SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.BOOLEAN);

    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME =
            SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.INT);

    public static final EntityDataAccessor<Integer> FILLED_FISH =
            SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.INT);

    public DentisaurusLongirostrisEntity(
            EntityType<? extends DentisaurusLongirostrisEntity> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        animator = new SwampDragonAnimator(this);
        this.createInventory();
        switchNavigator(true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.setMaxUpStep(2f);
        this.SWIM_COST = 0.1f;
        this.SPRINT_COST = 0.15f;
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
        if (!isInWater() || onGround()) {
            if (isMoving()) {
                if (isSprinting()) {
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_RUN);
                } else {
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_WALK);
                }
            } else {
                startAttack(AttackType.SWAMP_DRAGON_ATTACK);
            }
        } else {
            if (isSprinting()) {
                startAttack(AttackType.SWAMP_DRAGON_ATTACK_QUICK_SWIMMING);
            } else {
                if (getWaterDepth() >= 3 || !onGround()) {
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_SWIM_MID);
                } else {
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_SWIM_SHALLOW);
                }
            }
        }
    }

    @Override
    public void executeSpecialAttackType() {
        if (isInWater() && !onGround()) {
            startAttack(AttackType.SWAMP_DRAGON_SWIM_JUDGEMENT);
        } else {
            startAttack(AttackType.SWAMP_DRAGON_JUDGEMENT);
        }
    }

    @Override
    public void executeJudgementAttackType() {
        if (isInWater() && !onGround()) {
            startAttack(AttackType.SWAMP_DRAGON_SWIM_SPECIAL_ATTACK);
        } else {
            startAttack(AttackType.SWAMP_DRAGON_SPECIAL_ATTACK);
        }
    }

    @Override
    public void executeTurnAttackType() {
        if (isSprinting()) return;
        if (isInWater() && !onGround()) {
            startAttack(AttackType.SWAMP_DRAGON_ATTACK_SWIM_TURN);
        } else {
            startAttack(AttackType.SWAMP_DRAGON_ATTACK_TURN);
        }
    }

    @Override
    public void executeJumpAttackType() {
        if (getAttackState().isEmpty() && !onGround() && !isInWater()) {
            this.startAttack(AttackType.SWAMP_DRAGON_JUMP_ATTACK);
            this.setDeltaMovement(this.getDeltaMovement().scale(0.5f));
        }
    }

    @Override
    public boolean isPushable() {
        return (isMoving() || isSprinting()) && isVehicle();
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new DentisaurusLongirostrisBodyControl(this);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new DentisaurusLongirostrisMeleeAttackGoal(this, 2, false));
        goalSelector.addGoal(2, new MobFollowOwnerGoal(this, 2.2D, 7.0F, 4.0F, 64F, false));
        goalSelector.addGoal(5, new ErsTamableVehicleRandomSwimGoal(this, 1));
        goalSelector.addGoal(5, new MobWanderGoal(this, 1.2) {
            @Override
            public boolean canUse() {
                return super.canUse() && ((ErsTamable<?>) this.mob).updateSkyBrightness() > 4;
            }
        });
        goalSelector.addGoal(6, new ErsTamableLookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(1, new ErsTamableHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Player.class, true) {
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame() && isMature() && !mightBeSleeping();
            }
        });
        targetSelector.addGoal(2, new ErsTamableOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new ErsTamableOwnerHurtTargetGoal(this));
        targetSelector.addGoal(4, new AttackFishGoal(this, AbstractFish.class, Boolean.TRUE));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
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

    @Override
    public boolean mightBeSleeping() {
        return updateSkyBrightness() < 4
                && this.getControllingPassenger() == null
                && getCommand() == 0
                && this.getAttackState().getType() == AttackType.EMPTY
                && this.getTarget() == null
                && !this.isInWater()
                && !isBloody();
    }

    @Override
    public void tick() {
        super.tick();
        if (isClientSide()) {
            boolean flag2 = onGround() && !isMoving() && this.getAttackState().getType() == AttackType.EMPTY;

            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME)) {
                this.entityData.set(
                        NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200, 400));
                int newState = RandomSource.create().nextInt(4);
                if (newState != 0 && flag2) {
                    if (getCommand() == 1) {
                        if (newState == 1) {
                            triggerAnim("extra", "idle_sit4");
                        } else if (newState == 2) {
                            triggerAnim("extra", "idle_sit2");
                        } else {
                            triggerAnim("extra", "idle_sit3");
                        }
                    } else if (updateSkyBrightness() > 4) {
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

        if (!isClientSide()) {
            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME) && isMature()) {
                boolean flag2 = onGround()
                        && !isMoving()
                        && this.getAttackState().getType() == AttackType.EMPTY
                        && getTarget() == null;
                if (flag2 && getCommand() == 0 && isInWater() && onGround() && getWaterDepth() < 3) {
                    if (level().random.nextFloat() < 0.25f) {
                        this.navigation.stop();
                        this.entityData.set(
                                NEXT_CHANGE_TIME,
                                this.tickCount + RandomSource.create().nextIntBetweenInclusive(1200, 1600));
                        startAttack(AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL);
                    } else if (level().random.nextFloat() < 0.5f) {
                        this.navigation.stop();
                        this.entityData.set(
                                NEXT_CHANGE_TIME,
                                this.tickCount + RandomSource.create().nextIntBetweenInclusive(1200, 1600));
                        startAttack(AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE);
                    }
                }
            }

            tickCommonServer();

            if (tickCount % 20 == 0) {
                int filledSlots = 0;
                for (int slot = 5; slot <= 7; slot++) {
                    if (!getInventory().getItem(slot).isEmpty()) {
                        filledSlots++;
                    }
                }
                setFilledFish(filledSlots);

                int nextPoopTime = getNextPoopTime();
                if (nextPoopTime > 0) {
                    setNextPoopTime(nextPoopTime - 20);
                } else {
                    this.generateFeces();
                    setNextPoopTime(10000 + RandomSource.create().nextInt(14000));
                }
            }

            if (isMature() && this.isSprinting()) {
                stompEffect(3f, 3f, checkEquipment(ErsItems.SCRATCHING_BOARD.get()) ? 4f : 2f);
            }
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

        if (!this.isElite()) scale *= 0.7f;
        if (!this.isMature()) scale *= 0.4f;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH * scale);
        setBaseHealth(BASE_HEALTH * scale);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BASE_ATTACK_DAMAGE * scale);
        if (!this.isMature()) scale *= 2.1f;
        else if (!this.isElite()) scale *= 1.3f;
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_MOVE_SPEED * scale);
        containerChanged(this.getInventory());
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
    protected float calculateScale() {
        int age = getAgeInDays();
        if (this.isMature()) {
            age -= 20;
        }
        if (this.isElite()) {
            age -= 18;
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

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return itemStack.is(ItemTags.FISHES) || itemStack.is(ErsItems.FISH_FEED.get());
    }

    public void setFilledFish(int count) {
        this.entityData.set(FILLED_FISH, count);
    }

    public int getFilledFish() {
        return this.entityData.get(FILLED_FISH);
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
                && !(player.getMainHandItem().is(ItemTags.FISHES)
                        || player.getMainHandItem().is(ErsItems.FISH_FEED.get()))) {
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
        }

        if (!isClientSide() && (isFood(player.getMainHandItem()))) {
            if (!this.isTame()
                    && this.getAgeInDays() < 1
                    && player.getMainHandItem().is(ErsItems.FISH_FEED.get())) {
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            } else if (this.isTame()) {
                this.feed(player.getMainHandItem().is(ItemTags.FISHES) ? 1 : 5);
                this.heal(player.getMainHandItem().is(ItemTags.FISHES) ? 3 : 5);
                player.getMainHandItem().shrink(1);
            }
        }
        return super.mobInteract(player, pHand);
    }

    private void generateFeces() {
        if (this.level().isClientSide) return;
        List<BlockState> fecesBlocks = List.of(
                ErsBlocks.BONE_FECES.get().defaultBlockState(),
                ErsBlocks.SMALL_FECES.get().defaultBlockState(),
                ErsBlocks.LARGE_FECES.get().defaultBlockState(),
                ErsBlocks.GLASSES_FECES.get().defaultBlockState(),
                ErsBlocks.TEL_FECES.get().defaultBlockState());
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        double yRot = Math.toRadians(this.getYRot());
        int offsetX = (int) (-Math.sin(yRot) * 1.5); // 向后偏移1.5格
        int offsetZ = (int) (Math.cos(yRot) * 1.5);

        BlockPos targetPos = pos.offset(offsetX, 0, offsetZ);
        if (level.getBlockState(targetPos).isAir()
                && level.getBlockState(targetPos.below()).isSolidRender(level, targetPos.below())) {
            BlockState state = fecesBlocks.get(this.level().random.nextInt(fecesBlocks.size()));
            if (state.is(ErsBlocks.TEL_FECES.get())) {
                if (this.level().random.nextFloat() < 0.1f) {
                    return;
                }
            }
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

    private void placeNest() {
        if (this.level().isClientSide) return;
        BlockState state = ErsBlocks.SWAMP_DRAGON_NEST.get().defaultBlockState();
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        if (level.getBlockState(pos).isAir() && level.getBlockState(pos.below()).isSolidRender(level, pos.below())) {
            level.setBlock(pos, state, 3);
        }
    }

    @Override
    protected @Nullable SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return ErsSounds.LONGIROSTRIS_HURT.get();
    }

    @Override
    public void setSprinting(boolean pSprinting) {
        this.setSharedFlag(3, pSprinting);
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
                startAttack(AttackType.SWAMP_DRAGON_JUMP);
                this.setDeltaMovement(this.getDeltaMovement().scale(2).add(0, 1, 0));
            }
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return this.rideSpeed + 0.2f;
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
                        isSprinting() ? baseSpeed * 2.2f : isInWater() ? 1.25f * baseSpeed : baseSpeed,
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
        double jumpVelocity = 0.36;
        this.setDeltaMovement(this.getDeltaMovement().add(0, jumpVelocity, 0));
        this.hasImpulse = true;
        ForgeHooks.onLivingJump(this);
        if (this.level().isClientSide) ErsNetwork.INSTANCE.sendToServer(new VehicleJumpInWaterPacket(this.getId()));
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        AnimationController<DentisaurusLongirostrisEntity> main = new AnimationController<>(this, "main", 5, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    boolean flag1 = false;
                    for (int y = 1; y <= 8; y++) {
                        BlockPos checkPos = new BlockPos(this.blockPosition().below(y));
                        BlockState blockstate = this.level().getBlockState(checkPos);

                        if (blockstate.isAir()) {
                            continue;
                        }
                        flag1 = isWaterBlock(this.level(), this.blockPosition().below(y));
                        break;
                    }

                    if (isFalling() && !flag1) {
                        builder.thenLoop("animation.fall");
                    }
                    if (isInWater() && !onGround()) {
                        if (isMoving()) {
                            if (isSprinting()) {
                                builder.thenLoop("animation.quickly_swimming");
                            } else if (!isSprinting()
                                    && (getWaterDepth() <= 3 || this.onGround() || !this.isMature())) {
                                builder.thenLoop("animation.swim_shallow");
                            } else {
                                builder.thenLoop("animation.swim_mid");
                            }
                        } else if (!this.getRotDirection().isNone() && isMature()) {
                            if (this.getRotDirection().isLeft()) {
                                builder.thenLoop("animation.+w_left");
                            } else {
                                builder.thenLoop("animation.-w_right");
                            }
                        } else {
                            if (this.getControllingPassenger() instanceof Player player
                                    && getRiddenInput(player, new Vec3(0, 0, 0)).y < 0) {
                                builder.thenLoop("animation.swim_down");
                            } else if (this.getControllingPassenger() != null
                                    && this.getControllingPassenger().jumping) {
                                builder.thenLoop("animation.swim_up");
                            } else {
                                builder.thenLoop("animation.idle_shallow");
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
                                builder.thenLoop("animation.+left");
                            } else {
                                builder.thenLoop("animation.-right");
                            }
                        } else {
                            if (getCommand() == 1) {
                                builder.thenLoop("animation.idle_sit");
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

        AnimationController<DentisaurusLongirostrisEntity> attack = new AnimationController<>(
                        this, "attack", 5, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack-idle"))
                .triggerableAnim("attack2", RawAnimation.begin().thenPlay("animation.attack-idle2"))
                .triggerableAnim("attack-walk", RawAnimation.begin().thenPlay("animation.attack-walk"))
                .triggerableAnim("attack-walk2", RawAnimation.begin().thenPlay("animation.attack-walk2"))
                .triggerableAnim("attack-run", RawAnimation.begin().thenPlay("animation.attack-run"))
                .triggerableAnim("attack-run2", RawAnimation.begin().thenPlay("animation.attack-run2"))
                .triggerableAnim("attack_swim_shallow", RawAnimation.begin().thenPlay("animation.attack_swim_shallow"))
                .triggerableAnim("attack_swim_mid", RawAnimation.begin().thenPlay("animation.attack_swim_mid"))
                .triggerableAnim(
                        "attack_quickly_swimming", RawAnimation.begin().thenPlay("animation.attack_quickly_swimming"))
                .triggerableAnim("attack_turn", RawAnimation.begin().thenPlay("animation.attack_turn"))
                .triggerableAnim("attack_swim_turn", RawAnimation.begin().thenPlay("animation.attack_swim_turn"))
                .triggerableAnim("attack2-small", RawAnimation.begin().thenPlay("animation.attack2-small"))
                .triggerableAnim("attack2-middle", RawAnimation.begin().thenPlay("animation.attack2-middle"))
                .triggerableAnim("attack2-miss", RawAnimation.begin().thenPlay("animation.attack2-miss"))
                .triggerableAnim("attack3", RawAnimation.begin().thenPlay("animation.attack3"))
                .triggerableAnim("swim_attack", RawAnimation.begin().thenPlay("animation.swim_attack"))
                .triggerableAnim("swim_attack-miss", RawAnimation.begin().thenPlay("animation.swim_attack-miss"))
                .triggerableAnim("swim_attack2", RawAnimation.begin().thenPlay("animation.swim_attack2"))
                .triggerableAnim("catch_fish-small", RawAnimation.begin().thenPlay("animation.catch_fish-small"))
                .triggerableAnim("catch_fish-middle", RawAnimation.begin().thenPlay("animation.catch_fish-middle"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"))
                .triggerableAnim("jump", RawAnimation.begin().thenPlay("animation.jump"))
                .triggerableAnim("jump_attack", RawAnimation.begin().thenPlay("animation.jump_attack"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<DentisaurusLongirostrisEntity> extra = new AnimationController<>(
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
            ItemStack stack = new ItemStack(ErsItems.SWAMP_DRAGON_EGG.get());
            this.spawnAtLocation(stack);
        }
    }

    @Override
    protected float getBaseArmorValue() {
        return 12;
    }

    @Override
    protected float getBoostedArmorValue() {
        return 16;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!isClientSide() && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new DentisaurusLongirostrisMenuProvider(this),
                    buf -> buf.writeInt(this.getId()));
        }
    }

    @Override
    public void startAttack(ErsAttackType type) {
        super.startAttack(type);
        if (type != AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL
                && type != AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE
                && this.getCommand() == 1) this.setCommand(0);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(FROM_BUCKET, false);
        entityData.define(NEXT_POOP_TIME, RandomSource.create().nextInt(24000));
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(200, 400));
        entityData.define(FILLED_FISH, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Command", this.getCommand());
        compound.putInt("FilledFish", this.getFilledFish());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setCommand(compoundTag.getInt("Command"));
        this.setFilledFish(compoundTag.getInt("FilledFish"));
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

        stopTriggeredAnimation("attack", "catch_fish-small");
        stopTriggeredAnimation("attack", "catch_fish-middle");
        if (this.getAttackState().isEmpty()) {
            stopTriggeredAnimation("attack", "knockdown_left");
            stopTriggeredAnimation("attack", "knockdown_right");
        }
        if (this.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL
                || this.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE) {
            this.getAttackState().isSyncInstance = true;
            this.setAttackState(new MobAttack(AttackType.EMPTY, this));
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

    public GeneralAnimator<DentisaurusLongirostrisEntity> getAnimator() {
        return animator;
    }

    public int getInventorySize() {
        return 8;
    }

    public int getNextPoopTime() {
        return this.entityData.get(NEXT_POOP_TIME);
    }

    public void setNextPoopTime(int time) {
        this.entityData.set(NEXT_POOP_TIME, time);
    }

    @Override
    public boolean isSaddleable() {
        return super.isSaddleable() && isMature();
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVE_SPEED)
                .add(Attributes.MAX_HEALTH, BASE_HEALTH * 0.7)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE * 0.7)
                .add(Attributes.ARMOR, 12)
                .add(ForgeMod.SWIM_SPEED.get(), 10);
    }

    @Override
    public void equipSaddle() {
        this.inventory.setItem(0, new ItemStack(ErsItems.SWAMP_DRAGON_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    public boolean isClientSide() {
        return level().isClientSide;
    }

    private static boolean isWaterBlock(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        // 检查是否为水方块（包括流动的水）
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
        return new ItemStack(ErsItems.DENTISARUS_LONGIROSTRIS_LARGE_BUCKET.get());
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
        if (getControllingPassenger() != null) {
            waterAiStep(isElite() ? 2.6f * getRenderSize() : 2.4f * getRenderSize());
        } else {
            this.setNoGravity(false);
        }
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
    public void onInsideBubbleColumn(boolean pDownwards) {}

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {}

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {}

    @Override
    protected void playSwimSound(float pVolume) {}

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
                    pSpawnData = new ErsGroupData<>(
                            false,
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), random),
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), random),
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), random),
                            SpawnVariant.getCommonSpawnVariant(Variant.values(), random));
                }
            }

            this.setVariant(((ErsGroupData<Variant>) pSpawnData).getVariant(random));

            if (this.random.nextFloat() < 0.25f) {
                this.setCanBeElite(true);
            }

            this.setAgeInDays(this.random.nextIntBetweenInclusive(15, 50));
            updateAgeFromServer();
            this.setHealth(this.getMaxHealth());
            if (isMature()) {
                if (this.random.nextFloat() < 0.15f) {
                    TickHelper.tickLater(level(), 20, this::placeNest);
                }
            }

            if (this.random.nextFloat() < 0.01f) {
                var customNameList = new String[] {
                    "Ladon",
                    "Forsaken",
                    "Acheron_Pollux",
                    "profound",
                    "Nakishimo",
                    "Carpodacus dubius",
                    "sunfyre",
                    "Nekorizu",
                    "U3UUU",
                    "Kai_Sylph",
                    "Skadi",
                    "Bearer of the flowing red train"
                };
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

    public enum Variant implements StringRepresentable, SpawnVariant {
        ORIGINAL(0, "original", true),
        ORIGINAL_LIGHT(1, "original_light", true),
        ORIGINAL_DEEP(2, "original_deep", true),
        LACK_YELLOW(3, "lack_yellow", false),
        LACK_YELLOW_LIGHT(4, "lack_yellow_light", false),
        LACK_YELLOW_DEEP(5, "lack_yellow_deep", false),
        BLACK(6, "black", false),
        BLACK_LIGHT(7, "black_light", false),
        BLACK_DEEP(8, "black_deep", false),
        WHITE(9, "white", false),
        WHITE_LIGHT(10, "white_light", false),
        WHITE_DEEP(11, "white_deep", false);

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
