package cn.aurorian.oasis.entity.tubunasusdurovela;

import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.HasGender;
import cn.aurorian.ers.entity.ai.goal.MobAlertLeaderGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowLeaderGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowParentGoal;
import cn.aurorian.ers.entity.ai.goal.MobWanderGoal;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsMobEffects;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.ers.util.SimpleAutoPlayingSoundKeyFrameHandler;
import cn.aurorian.ers.util.TickHelper;
import cn.aurorian.oasis.client.animator.TubunasusDurovelaAnimator;
import cn.aurorian.oasis.entity.ai.OasisBreedGoal;
import cn.aurorian.oasis.entity.tubunasusdurovela.ai.TubunasusDurovelaSailGoal;
import cn.aurorian.oasis.entity.tubunasusdurovela.ai.TubunasusMeleeAttackGoal;
import cn.aurorian.oasis.entity.tubunasusdurovela.invertory.TubunasusDurovelaMenuProvider;
import cn.aurorian.oasis.init.OasisEntities;
import cn.aurorian.oasis.init.OasisItems;
import cn.aurorian.oasis.init.OasisSounds;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
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
import org.joml.Vector3d;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.Arrays;
import java.util.UUID;
import java.util.function.IntFunction;

public class TubunasusDurovelaEntity extends ErsTamableVehicle<TubunasusDurovelaEntity> implements HasGender, HasCustomInventoryScreen, Bucketable, VariantHolder<TubunasusDurovelaEntity.Variant>{
    private final GeneralAnimator<TubunasusDurovelaEntity> animator;
    private int rushTimer = 0;
    private boolean crush = false;
    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME = SynchedEntityData.defineId(TubunasusDurovelaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SAIL = SynchedEntityData.defineId(TubunasusDurovelaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(TubunasusDurovelaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TubunasusDurovelaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> GENDER = SynchedEntityData.defineId(TubunasusDurovelaEntity.class, EntityDataSerializers.BOOLEAN);
    public TubunasusDurovelaEntity(EntityType<? extends ErsTamableVehicle> type, Level level) {
        super(type, level);
        animator = new TubunasusDurovelaAnimator(this);
        switchNavigator(true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.createInventory();
        this.setMaxUpStep(2f);
        this.SWIM_COST = 0.15f;
        this.SPRINT_COST = 0.05f;
        this.RECOVER = 0.25f;
        this.WATER_ANIMAL = true;
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            moveControl = new LimitedMoveControl(this);
            navigation = new MMGroundPathNavigation(this, level());
        } else {
            moveControl = new AquaticMoveControl(this,1).applyGravity(false);
            navigation = new AmphibiousPathNavigation(this,level());
        }
    }

    @Override
    public void executeDefaultAttackType() {
        startAttack(AttackType.TUBUNASUS_ATTACK);
    }

    @Override
    public void executeJudgementAttackType() {
        setSailUp(!isSailUp());
    }

    @Override
    public void executeTurnAttackType() {
        if(isSprinting())
            return;
        if(getAttackState().animatorTick == 0 && onGround())
            startAttack(AttackType.TUBUNASUS_ATTACK_TURN);
    }

    @Override
    public boolean isFood(@NotNull ItemStack pStack) {
        return pStack.is(OasisItems.TEASELGOURD.get());
    }

    public int getRushTimer() {
        return rushTimer;
    }
    @Override
    public GeneralAnimator<TubunasusDurovelaEntity> getAnimator() {
        return animator;
    }
    public boolean isSailUp(){
        return entityData.get(SAIL);
    }
    public void setSailUp(boolean sail){
        entityData.set(SAIL, sail);
    }
    public float getScale(){
        return entityData.get(SCALE);
    }
    public void setScale(float scale){
        entityData.set(SCALE, Math.clamp(scale, 0.8f, 1.1f));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(200,400));
        entityData.define(SAIL, true);
        entityData.define(SCALE, 1.0f);
        entityData.define(FROM_BUCKET, false);
        entityData.define(GENDER, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("Variant", this.getVariant().getId());

        compound.putFloat("Scale", getScale());
        compound.putBoolean("Gender", getGender());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        this.setVariant(Variant.byId(compound.getInt("Variant")));

        setScale(compound.getFloat("Scale"));
        setGender(compound.getBoolean("Gender"));
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!level().isClientSide() && isOwnedBy(player) && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new TubunasusDurovelaMenuProvider(this),
                    buf -> buf.writeInt(this.getId())
            );
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TubunasusMeleeAttackGoal(this, 2,true));
        this.goalSelector.addGoal(2, new OasisBreedGoal(this,1));
        this.goalSelector.addGoal(3, new MobWanderGoal(this,1.1,40){
            @Override
            public boolean canUse(){
                return super.canUse() && !TubunasusDurovelaEntity.this.isBaby();
            }
        }.setWaterVerticalRange(0));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, TubunasusDurovelaEntity.class, 10, 1.1, 2.1){
            @Override
            public boolean canUse() {
                return super.canUse() && !TubunasusDurovelaEntity.this.isBaby();
            }
        });
        this.goalSelector.addGoal(4, new MobFollowLeaderGoal(this,2.1,14,10));
        this.goalSelector.addGoal(4, new MobFollowParentGoal(this,1));
        this.goalSelector.addGoal(5, new TubunasusDurovelaSailGoal(this));

        this.goalSelector.addGoal(1, new MobAlertLeaderGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, true,
                (mob) -> leader != null && leader.getTarget() == mob
        ));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<TubunasusDurovelaEntity> main = new AnimationController<>(this, "main", 2 , state -> {
            RawAnimation builder = RawAnimation.begin();
            if(isInWater() && !onGround()){
                if(isSprinting() || (isBaby() && ErsUtils.isMoving(this))){
                    builder.thenLoop("animation.swim");
                }
                else if(ErsUtils.isMoving(this) && !isBaby()){
                    builder.thenLoop("animation.swim2");
                }else {
                    builder.thenLoop("animation.swim_idle");
                }
            }else {
                if (isSprinting()) {
                    if(this.rushTimer < 75){
                        builder.thenLoop("animation.run");
                    }
                    else if(this.rushTimer < 167){
                        builder.thenLoop("animation.run2");
                    }
                    else{
                        builder.thenLoop("animation.run3");
                    }
                } else if (state.isMoving() || ErsUtils.isMoving(this)) {
                    builder.thenLoop("animation.walk");
                } else if (!this.getRotDirection().isNone()) {
                    if (this.getRotDirection().isLeft()) {
                        builder.thenLoop("animation.turn_-left");
                    } else {
                        builder.thenLoop("animation.turn_+right");
                    }
                } else {
                    builder.thenLoop("animation.idle");
                }
            }
            return state.setAndContinue(builder);
        });

        AnimationController<TubunasusDurovelaEntity> extra = new AnimationController<>(this, "extra",2, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle_eat"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle_lookAround"))
                .triggerableAnim("taming", RawAnimation.begin().thenPlay("animation.taming"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<TubunasusDurovelaEntity> attack = new AnimationController<>(this, "attack", 4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("attack_turn", RawAnimation.begin().thenPlay("animation.attack_turn"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"));

        AnimationController<TubunasusDurovelaEntity> control = new AnimationController<>(this, "control", 0, state -> {
            RawAnimation builder = RawAnimation.begin();
            if(isBaby())
                return PlayState.STOP;

            if(isSailUp()){
                builder.thenPlayAndHold("animation.open");
            }else {
                builder.thenPlayAndHold("animation.close");
            }
            return state.setAndContinue(builder);
        });

        controllerRegistrar.add(main,extra,attack,control);
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if(isBaby()){
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50);
        }
        else
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(350);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if(player.getMainHandItem().getItem() == ErsItems.LARGE_WATER_BUCKET.get() && this.isTame() && this.isOwnedBy(player))
        {
            return ErsMobLargeBucket.bucketMobPickup(player, pHand, this).orElse(InteractionResult.PASS);
        }

        if (!player.isCrouching() && !isBaby() && !isFood(player.getItemInHand(pHand))) {
            if((this.isTame() || !isInLove()) && !this.isOwnedBy(player) && getPassengers().isEmpty())
                return InteractionResult.PASS;
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
        }
        return super.mobInteract(player, pHand);
    }

    @Override
    protected void positionRider(@NotNull Entity pPassenger, @NotNull MoveFunction pCallback) {
        if (level().isClientSide) {
            float f = 0;
            if (this.getPassengers().size() > 1) {
                int i = this.getPassengers().indexOf(pPassenger);
                if (i == 0) {
                    f = 0F;
                } else {
                    f = -0.6F;
                }
                if (pPassenger instanceof Animal) {
                    f += 0.2F;
                }
            }
            float d0 = 0;

            if(!isSaddled())
                d0 = -0.1f;

            Vec3 vec3 = (new Vec3(f, 0.0, 0.0)).yRot(-this.getYRot() * 0.017453292F - 1.5707964F);

            Vector3d vector3d = this.getAnimData(ErsDataTickets.SADDLE_POS);
            if (vector3d != null) {
                pCallback.accept(pPassenger,
                        this.getX() + vector3d.x + vec3.x,
                        this.getY() + vector3d.y - 0.5f + d0,
                        this.getZ() + vector3d.z + vec3.z);
            }
        } else {
            pCallback.accept(pPassenger,
                    this.getX() + getRiderPos().getX(),
                    this.getY() + getRiderPos().getY() - 0.5f,
                    this.getZ() + getRiderPos().getZ());
        }
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(OasisSounds.DUROVELA_FOOTSTEP.get(), 0.15F, 1.0F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new TubunasusDurovelaBodyControl(this);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 350)
                .add(Attributes.ARMOR,6)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.KNOCKBACK_RESISTANCE,1)
                .add(Attributes.FOLLOW_RANGE,48)
                .add(ForgeMod.SWIM_SPEED.get(),4)
                .add(Attributes.MOVEMENT_SPEED, 0.15);
    }

    @Override
    protected boolean canAddPassenger(@NotNull Entity pPassenger) {

        return this.getPassengers().size() < 2 && !isSprinting();
    }

    @Override
    public boolean canBeCollidedWith() {
        return !(isSprinting() && isVehicle());
    }

    @Override
    public boolean canCollideWith(@NotNull Entity pEntity) {
        if(pEntity instanceof TubunasusDurovelaEntity)
            return false;
        return super.canCollideWith(pEntity);
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        LivingEntity passenger = (LivingEntity) getFirstPassenger();
        if(passenger == null)
            return null;
        return this.isOwnedBy(passenger) ? passenger : null;
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

            if(isSailUp()){
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(10);
            }else {
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(4);
            }

            return new Vec3(0, y, this.rideSpeed);
        }
        else
        {
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    private static final float ACCELERATION = 0.08f;

    private int buckingTimer = -1;

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 move)
    {
        tickStableHead();
        if(this.getAttackState().getType().canMove() && this.isSaddled()){
            if(((!this.getRotDirection().isLargeTurn() || isInWater()) && (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0)) || this.isSprinting())
            {
                float baseSpeed = (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();

                if(rushTimer > 75){
                    baseSpeed *= 1.2f;
                    if(rushTimer > 167){
                        baseSpeed *= 1.2f;
                    }
                    level().addParticle(ParticleTypes.POOF, this.getRandomX(0.5), this.getRandomY(), this.getRandomZ(0.5), 0, 0, 0);
                }

                if(isSprinting() && !isInWater() && !isSailUp())
                    baseSpeed += 0.03f;

                this.rideSpeed = Mth.approach(this.rideSpeed, isSprinting() ? baseSpeed * 2.2f : baseSpeed, ACCELERATION);
            }else{
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, ACCELERATION * 2);
            }
        }
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
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return isOwnedBy(pPlayer) ? this.rideSpeed + 0.1f :
                (float)this.getAttributeValue(Attributes.MOVEMENT_SPEED);
    }

    @Override
    protected void customServerAiStep() {
        super.customServerAiStep();
        if (isInWater() && navigation instanceof MMGroundPathNavigation) {
            switchNavigator(false);
        } else if (!isInWater() && navigation instanceof AmphibiousPathNavigation) {
            switchNavigator(true);
        }
    }

    @Override
    public void aiStep() {
        super.aiStep();
        if(!isBaby())
            waterAiStep(2.4f);
        else
            waterAiStep(1.1f);
        breakBlock();
        checkAndBreakBlocksAhead();
    }

    @Override
    public void tick() {
        super.tick();

        if(level().isClientSide){
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
        }

        boolean hasHorseShoe = false;
        for (int i = 4; i <= 6; i++) {
            ItemStack itemStack = this.inventory.getItem(i);
            if(!itemStack.isEmpty()){
                if(itemStack.getItem() instanceof MountEquipment equipment){
                    equipment.tickEquip(itemStack,this);
                }
                if(itemStack.is(OasisItems.HORSESHOE.get()))
                    hasHorseShoe = true;
            }
        }

        if(isSprinting() && !isInWater() && !isBaby()){
            rushTimer++;
        }else {
            rushTimer = 0;
        }


        if(isSprinting()){
            float damage = rushTimer > 75 ? 6f : 3f;
            if(rushTimer > 167)
                damage = 21;
            if(hasHorseShoe)
                damage += 4;
            stompEffect(3f, 3f, damage);
        }

        if(crush){
            this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0);
            if(this.getControllingPassenger() != null)
                this.getControllingPassenger().setSprinting(false);
            if(tickCount % 80 == 0){
                crush = false;
                this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.15);
            }

        }

        if(!level().isClientSide()){
            if(tickCount % 200 == 0){
                heal(1);
                updateLeader();
            }

            if(isTame())
                return;
            
            Player player = null;
            if(getFirstPassenger() instanceof Player)
                player = (Player) this.getFirstPassenger();

            if(player != null  && !this.isOwnedBy(player)){
                if (buckingTimer == -1) {
                    buckingTimer = Mth.randomBetweenInclusive(this.random, 100, 200);
                }

                buckingTimer--;

                if (buckingTimer % 20 == 0) {
                    if (this.random.nextFloat() < 0.05){
                        this.tame(player);
                        this.level().broadcastEntityEvent(this, (byte)7);
                    }
                }

                if (buckingTimer <= 0 && !isTame()) {
                    player.stopRiding();
                    this.setTarget(player);
                    player.setDeltaMovement(0,10,0);
                    TickHelper.tickLater(level(),3, ()-> this.triggerAnim("extra","taming"));

                    this.playSound(SoundEvents.HORSE_ANGRY, 1.0f, 1.0f);
                    if(leader != this){
                        TickHelper.tickLater(level(),20, ()-> {
                            if(leader != null)
                                this.setTarget(null);
                        });
                    }
                    buckingTimer = -1;
                }
            }else {
                buckingTimer = -1;
            }
        }

        if(ErsUtils.isMoving(this)){
            updateMount();
        }
    }

    @Override
    public void updateMount() {
        this.getEntityData().set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200,400));
        stopTriggeredAnimation("extra","idle2");
        stopTriggeredAnimation("extra","idle3");
    }

    @Override
    public void onInsideBubbleColumn(boolean pDownwards) {}

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {}

    @Override
    protected void stompDamage(float damage, LivingEntity living) {
        if (living.hurt(this.damageSources().mobAttack(this), damage)) {

            Vec3 vec31 = (new Vec3(this.getX() - living.getX(), 0.0D, this.getZ() - living.getZ())).normalize().scale(0.5F);
            if(rushTimer > 167)
                living.addEffect(new MobEffectInstance(ErsMobEffects.FRACTURE.get(),600));

            living.setDeltaMovement(
                    living.getDeltaMovement().x / 2.0D - vec31.x,
                    living.onGround() ? Math.min(0.4D, living.getDeltaMovement().y / 2.0D + (double) 0.5F) : living.getDeltaMovement().y,
                    living.getDeltaMovement().z / 2.0D - vec31.z
            );
        }
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.type().msgId().equals("sweetBerryBush")) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    private void checkAndBreakBlocksAhead() {
        if (level().isClientSide) return;
        if (!ForgeEventFactory.getMobGriefingEvent(this.level(), this))
            return;

        if(rushTimer < 75)
            return;

        Direction facing = this.getDirection();
        BlockPos centerPos = this.blockPosition().relative(facing, 2);

        boolean shouldStop = false;
        float stoneDestroyTime = Blocks.DEEPSLATE.defaultBlockState().getDestroySpeed(level(), BlockPos.ZERO);

        for (int xOffset = 0; xOffset < 2; xOffset++) {
            for (int yOffset = 1; yOffset < 3; yOffset++) {
                BlockPos checkPos = centerPos.offset(
                        facing.getAxis() == Direction.Axis.X ? 0 : xOffset,
                        yOffset,
                        facing.getAxis() == Direction.Axis.Z ? 0 : xOffset
                );

                BlockState state = level().getBlockState(checkPos);
                Block block = state.getBlock();

                float destroyTime = state.getDestroySpeed(level(), checkPos);

                if (destroyTime < stoneDestroyTime && destroyTime > 0 &&
                        block.getExplosionResistance() <= 1200.0F) {
                    if(ForgeEventFactory.getMobGriefingEvent(this.level(), this)){
                        shouldStop = level().destroyBlock(checkPos, true, this);
                    }else {
                        shouldStop = true;
                    }
                }
            }
        }

        if (shouldStop) {
            crush = true;
            this.setSprinting(false);
        }
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            if (pDataTag != null && pDataTag.contains("UUID")) {
                setUUID(UUID.fromString(pDataTag.getString("UUID")));
            }
            return pSpawnData;
        } else {
            RandomSource random = pLevel.getRandom();
            var throwedSpawnData = pSpawnData;
            if(random.nextFloat() < 0.05f){
                throwedSpawnData = new DuravelaTubunasusGroupData(Variant.getRareSpawnVariant(random));
            }else {
                throwedSpawnData = new DuravelaTubunasusGroupData(
                        Variant.getCommonSpawnVariant(random),
                        Variant.getCommonSpawnVariant(random),
                        Variant.getCommonSpawnVariant(random)
                );
            }

            if (pSpawnData == null) {
              pSpawnData = throwedSpawnData;
            }

            this.setVariant(((DuravelaTubunasusGroupData)throwedSpawnData).getVariant(random));
        }

        if(this.random.nextFloat() < 0.2f)
            this.setGender(true);

        if(getGender())
            this.setScale(Mth.randomBetween(this.random, 1f, 1.1f));
        else
            this.setScale(Mth.randomBetween(this.random, 0.8f, 1f));
        this.refreshDimensions();

        if(this.random.nextFloat() < 0.002f){
            var customNameList = new String[]{"dark", "D3WOJDIWLANLAND"};
            this.setCustomName(Component.literal(customNameList[this.random.nextInt(customNameList.length)]));
        }

        SpawnGroupData returnValue = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if(isBaby()){
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50);
            this.setHealth(this.getMaxHealth());
        }
        return returnValue;
    }

    @Override
    public ErsTamable<?> getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        TubunasusDurovelaEntity otherParent1 = (TubunasusDurovelaEntity) otherParent;
        TubunasusDurovelaEntity child = OasisEntities.TUBUNASUS_DUROVELA.get().create(level);
        if (child != null) {
            int i = this.random.nextInt(9);
            Variant variant;
            if (i < 4) {
                variant = this.getVariant();
            } else if (i < 8) {
                variant = otherParent1.getVariant();
            } else {
                variant = Util.getRandom(Variant.values(), this.random);
            }

            child.setVariant(variant);
        }

        return child;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        float scale = getScale();
        if(isBaby())
            scale *= 0.5f;
        return this.getType().getDimensions().scale(scale);
    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }
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
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(OasisItems.TUBUNASUS_DUROVELA_LARGE_BUCKET.get());
    }

    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return OasisSounds.DUROVELA_HURT.get();
    }

    public @NotNull Variant getVariant() {
        return Variant.byId(getVariantId());
    }

    public void setVariant(Variant pVariant) {
        setVariantId(pVariant.getId());
    }

    @Override
    public void containerChanged(@NotNull Container container) {
        this.setSaddled(!this.inventory.getItem(0).isEmpty());

        boolean healthBoost = false;
        boolean armorBoost = false;
        for (int i = 1; i <= 3; i++) {
            ItemStack itemStack = this.inventory.getItem(i);
            if(!itemStack.isEmpty()){
                if(itemStack.is(Items.ENCHANTED_GOLDEN_APPLE)){
                    healthBoost = true;
                }
                if(itemStack.is(Items.SHIELD)){
                    armorBoost = true;
                }
            }
        }

        if(healthBoost){
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(350 + 20);
        }else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(350);
        }

        if (armorBoost) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(10);
        } else {
            this.getAttribute(Attributes.ARMOR).setBaseValue(6);
        }
    }

    @Override
    public int getInventorySize() {
        return 19;
    }

    @Override
    public boolean isSaddleable() {
        return super.isSaddleable() && !isBaby();
    }

    @Override
    public void equipSaddle() {
        this.inventory.setItem(0, new ItemStack(OasisItems.TUBUNASUS_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    @Override
    public void setGender(boolean gender) {
        this.entityData.set(GENDER,gender);
    }

    @Override
    public boolean getGender() {
        return this.entityData.get(GENDER);
    }

    @Override
    public boolean canMate(@NotNull Animal pOtherAnimal) {
        if (pOtherAnimal == this) {
            return false;
        } else if (pOtherAnimal.getClass() != this.getClass()) {
            return false;
        } else {
            return this.isInLove() && pOtherAnimal.isInLove() && this.getGender() != ((TubunasusDurovelaEntity) pOtherAnimal).getGender();
        }
    }

    public enum Variant implements StringRepresentable {
        ORIGINAL(0, "original", true),
        BLUE(1, "blue", true),
        GREEN(2, "green", true),
        WHITE(3, "white", false);

        private static final IntFunction<Variant> BY_ID = ByIdMap.continuous(Variant::getId, values(), ByIdMap.OutOfBoundsStrategy.ZERO);
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

        public @NotNull String getSerializedName() {
            return this.name;
        }

        public static Variant byId(int pId) {
            return BY_ID.apply(pId);
        }

        public static Variant getCommonSpawnVariant(RandomSource pRandom) {
            return getSpawnVariant(pRandom, true);
        }

        public static Variant getRareSpawnVariant(RandomSource pRandom) {
            return getSpawnVariant(pRandom, false);
        }

        private static Variant getSpawnVariant(RandomSource pRandom, boolean pCommon) {
           Variant[] $$2 = Arrays.stream(values()).filter((p_149252_) -> p_149252_.common == pCommon).toArray(Variant[]::new);
            return Util.getRandom($$2, pRandom);
        }
    }
    public static class DuravelaTubunasusGroupData extends AgeableMobGroupData {
        public final Variant[] types;

        public DuravelaTubunasusGroupData(Variant... pTypes) {
            super(1f);
            this.types = pTypes;
        }

        public Variant getVariant(RandomSource pRandom) {
            return this.types[pRandom.nextInt(this.types.length)];
        }
    }
}
