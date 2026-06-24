package cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus;

import cn.aurorian.ers.client.animator.CristatodromeusBrachypterusAnimator;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsGroupData;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.ai.ErsTamableLookAtPlayerGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtByTargetGoal;
import cn.aurorian.ers.entity.ai.goal.ErsTamableOwnerHurtTargetGoal;
import cn.aurorian.ers.entity.ai.goal.MobAvodingEntityGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowOwnerGoal;
import cn.aurorian.ers.entity.ai.goal.MobWanderGoal;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.ai.CristatodromeusBrachypterusMeleeAttackGoal;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.inventory.CristatodromeusBrachypterusMenuProvider;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.init.ErsItems;
import com.mojang.serialization.Codec;
import java.util.List;
import java.util.function.IntFunction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.HasCustomInventoryScreen;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.SpawnGroupData;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.VariantHolder;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class CristatodromeusBrachypterusEntity extends ErsTamableVehicle<CristatodromeusBrachypterusEntity>
        implements HasCustomInventoryScreen, VariantHolder<CristatodromeusBrachypterusEntity.Variant> {
    private static final float BASE_MOVE_SPEED = 0.25f;
    private static final float BASE_HEALTH = 35.0f;
    private static final float BASE_ATTACK_DAMAGE = 9.0f;
    private static final double PREDATOR_RANGE = 16.0D;

    private final GeneralAnimator<CristatodromeusBrachypterusEntity> animator;
    private int waterStepBoostTicks;

    public CristatodromeusBrachypterusEntity(EntityType<? extends TamableAnimal> entityType, Level level) {
        super(entityType, level);
        this.animator = new CristatodromeusBrachypterusAnimator(this);
        this.createInventory();
        this.setMaxUpStep(1.0f);
        this.SPRINT_COST = 0.08f;
        this.RECOVER = 0.2f;
        this.healInterval = 300;
        this.doHunger = true;
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1, new AvoidPredatorGoal<>(this, TerridensaurusSaevusEntity.class));
        goalSelector.addGoal(1, new AvoidPredatorGoal<>(this, DentisaurusLongirostrisEntity.class));
        goalSelector.addGoal(1, new AvoidPredatorGoal<>(this, EosuchosaurusAntiquusEntity.class));
        goalSelector.addGoal(2, new CristatodromeusBrachypterusMeleeAttackGoal(this, 1.35D, false));
        goalSelector.addGoal(3, new MobFollowOwnerGoal(this, 1.6D, 7.0F, 4.0F, 64.0F, false));
        goalSelector.addGoal(4, new MobWanderGoal(this, 1.0D) {
            @Override
            public boolean canUse() {
                return !CristatodromeusBrachypterusEntity.this.mightBeSleeping() && super.canUse();
            }

            @Override
            public boolean canContinueToUse() {
                return !CristatodromeusBrachypterusEntity.this.mightBeSleeping() && super.canContinueToUse();
            }
        });
        goalSelector.addGoal(5, new ErsTamableLookAtPlayerGoal(this, Player.class, 6.0F));

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
        for (int slot = 5; slot <= 6; slot++) {
            if (!this.getInventory().getItem(slot).isEmpty()) {
                this.getInventory().getItem(slot).shrink(1);
                this.feed(10);
                this.heal(10);
                break;
            }
        }
    }

    @Override
    public boolean isFood(@NotNull ItemStack stack) {
        return stack.is(ErsItems.VEGETABLE_FEED.get()) || stack.is(ErsItems.HAY_FEED.get());
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        ItemStack handItem = player.getItemInHand(hand);

        if (isTame()
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

        if (!level().isClientSide && hand == InteractionHand.MAIN_HAND && isFood(handItem)) {
            handItem.shrink(1);
            if (!this.isTame()) {
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)) {
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte) 7);
                } else {
                    this.level().broadcastEntityEvent(this, (byte) 6);
                }
            } else {
                this.feed(10);
                this.heal(10);
            }
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }

    @Override
    public void executeTurnAttackType() {
        startAttack(AttackType.CRISTATODROMEUS_KICK);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new CristatodromeusBrachypterusBodyControl(this);
    }

    @Override
    public void updateMount() {
        if (this.getCommand() == 1) {
            this.setCommand(0);
        }
        if (this.getAttackState().isEmpty()) {
            stopTriggeredAnimation("attack", "knockdown_left");
            stopTriggeredAnimation("attack", "knockdown_right");
        }
    }

    @Override
    public void tickStamina(boolean waterAnimal, boolean recoverWhenWalk) {
        if (hasNearbyPredator() && getControllingPassenger() instanceof Player && isSprinting()) {
            staminaCount = 0;
            return;
        }
        super.tickStamina(waterAnimal, recoverWhenWalk);
    }

    public boolean hasNearbyPredator() {
        AABB area = this.getBoundingBox().inflate(PREDATOR_RANGE);
        List<LivingEntity> predators = this.level()
                .getEntitiesOfClass(
                        LivingEntity.class,
                        area,
                        entity -> entity instanceof TerridensaurusSaevusEntity
                                || entity instanceof DentisaurusLongirostrisEntity
                                || entity instanceof EosuchosaurusAntiquusEntity);
        return !predators.isEmpty();
    }

    public void performKickDamage() {
        Vec3 center = this.position().add(new Vec3(0, 0, 1.05D).yRot(-this.yBodyRot * ((float) Math.PI / 180F)));
        AABB area = new AABB(center.subtract(1.25D, 0.65D, 1.25D), center.add(1.25D, 0.85D, 1.25D));
        float damage = (float) this.getAttributeValue(Attributes.ATTACK_DAMAGE);

        for (LivingEntity living :
                level().getEntitiesOfClass(LivingEntity.class, area, EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {
            if (living.is(this)
                    || living.getVehicle() == this
                    || living.getType() == this.getType()
                    || this.isAlliedTo(living)
                    || living.distanceToSqr(center) > 1.25D * 1.25D) {
                continue;
            }

            if (living.hurt(this.damageSources().mobAttack(this), damage)) {
                Vec3 knockback =
                        living.position().subtract(this.position()).normalize().scale(0.55D);
                living.setDeltaMovement(knockback.x, Math.min(0.45D, living.getDeltaMovement().y + 0.35D), knockback.z);
            }
        }
    }

    @Override
    public void equipSaddle() {
        this.inventory.setItem(0, new ItemStack(ErsItems.CRISTATODROMEUS_BRACHYPTERUS_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    @Override
    public int getInventorySize() {
        return 7;
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!level().isClientSide() && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new CristatodromeusBrachypterusMenuProvider(this),
                    buf -> buf.writeInt(this.getId()));
        }
    }

    @Override
    protected float getBaseHealthValue() {
        return BASE_HEALTH;
    }

    @Override
    protected float getBaseArmorValue() {
        return 2;
    }

    @Override
    protected float getBoostedArmorValue() {
        return 5;
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player player, @NotNull Vec3 travelVector) {
        if (this.rideSpeed != 0 || player.jumping) {
            updateMount();
            if (getCommand() == 1) this.setCommand(0);
        }
        if (this.isInFluidType()) {
            float y = 0;

            if (player.jumping && (getSwimState() != 1 || wasEyeInWater)) {
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
    public boolean mightBeSleeping() {
        return updateSkyBrightness() <= 8
                && this.getControllingPassenger() == null
                && getCommand() == 0
                && this.getAttackState().getType() == AttackType.EMPTY
                && this.getTarget() == null
                && !this.isInWater()
                && !isBloody();
    }

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 travelVector) {
        tickStableHead();
        if (this.getAttackState().getType().canMove() && this.isSaddled()) {
            if (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0 || this.isSprinting()) {
                float baseSpeed =
                        (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.rideSpeed = Mth.approach(this.rideSpeed, isSprinting() ? baseSpeed * 2.0f : baseSpeed, 0.08f);
            } else {
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.16f);
            }
        } else {
            this.rideSpeed = Mth.approach(this.rideSpeed, 0, 0.24f);
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player player) {
        return this.rideSpeed + 0.1f;
    }

    public void tickStableHead() {
        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        if (this.onGround()) {
            setStableHead(true);
        } else if (isFalling()) {
            setStableHead(false);
        } else {
            setStableHead(pitch < 20);
        }
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Command", this.getCommand());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setCommand(compound.getInt("Command"));
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor level,
            @NotNull net.minecraft.world.DifficultyInstance difficulty,
            @NotNull MobSpawnType reason,
            @Nullable SpawnGroupData spawnData,
            @Nullable CompoundTag dataTag) {
        RandomSource random = level.getRandom();
        if (spawnData == null) {
            Variant variant = random.nextFloat() < 0.7f ? Variant.YELLOW : Variant.ORIGINAL;
            spawnData = new ErsGroupData<>(false, variant);
        }

        this.setVariant(((ErsGroupData<Variant>) spawnData).getVariant(random));
        this.setHealth(this.getMaxHealth());
        return super.finalizeSpawn(level, difficulty, reason, spawnData, dataTag);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        AnimationController<CristatodromeusBrachypterusEntity> main =
                new AnimationController<>(this, "main", 5, state -> {
                    RawAnimation animation = RawAnimation.begin();
                    if (isInWater()) {
                        animation.thenLoop("animation.swim");
                    } else if (isMoving() && isSprinting()) {
                        animation.thenLoop("animation.run");
                    } else if (isMoving()) {
                        animation.thenLoop("animation.walk");
                    } else if (!this.getRotDirection().isNone()) {
                        animation.thenLoop(
                                this.getRotDirection().isLeft() ? "animation.turn_left" : "animation.turn_right");
                    } else if (getCommand() == 1) {
                        animation.thenLoop("animation.idle_sit");
                    } else if (mightBeSleeping()) {
                        animation.thenLoop("animation.sleep");
                    } else {
                        animation.thenLoop("animation.idle");
                    }
                    return state.setAndContinue(animation);
                });

        AnimationController<CristatodromeusBrachypterusEntity> attack = new AnimationController<>(
                        this, "attack", 5, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"));

        controllers.add(main, attack);
    }

    @Override
    public GeneralAnimator<CristatodromeusBrachypterusEntity> getAnimator() {
        return animator;
    }

    @Override
    public @NotNull Variant getVariant() {
        return Variant.byId(getVariantId());
    }

    @Override
    public void setVariant(Variant variant) {
        setVariantId(variant.getId());
    }

    public static AttributeSupplier.Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVE_SPEED)
                .add(Attributes.MAX_HEALTH, BASE_HEALTH)
                .add(Attributes.FOLLOW_RANGE, 32)
                .add(Attributes.KNOCKBACK_RESISTANCE, 0.35)
                .add(Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE)
                .add(Attributes.ARMOR, 2)
                .add(ForgeMod.SWIM_SPEED.get(), 2.5);
    }

    @Override
    public void aiStep() {
        super.aiStep();

        waterAiStep(this.getBbHeight() * 0.65f);
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

    private static class AvoidPredatorGoal<T extends LivingEntity> extends MobAvodingEntityGoal<T> {
        private final CristatodromeusBrachypterusEntity entity;

        AvoidPredatorGoal(CristatodromeusBrachypterusEntity entity, Class<T> predatorClass) {
            super(entity, predatorClass, 16.0F, 1.2D, 1.8D);
            this.entity = entity;
        }

        @Override
        public boolean canUse() {
            return entity.getControllingPassenger() == null
                    && entity.getCommand() != 1
                    && !entity.mightBeSleeping()
                    && super.canUse();
        }

        @Override
        public boolean canContinueToUse() {
            return entity.getControllingPassenger() == null
                    && entity.getCommand() != 1
                    && !entity.mightBeSleeping()
                    && super.canContinueToUse();
        }
    }

    public enum Variant implements StringRepresentable {
        ORIGINAL(0, "original"),
        YELLOW(1, "yellow");

        private static final IntFunction<Variant> BY_ID =
                ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
        public static final Codec<Variant> CODEC = StringRepresentable.fromEnum(Variant::values);
        private final int id;
        private final String name;

        Variant(int id, String name) {
            this.id = id;
            this.name = name;
        }

        public int getId() {
            return this.id;
        }

        public String getName() {
            return this.name;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        public static Variant byId(int id) {
            return BY_ID.apply(id);
        }
    }
}
