package cn.aurorian.oasis.entity.tubunasusclyderotunda;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.HasGender;
import cn.aurorian.ers.entity.ai.goal.MobAlertLeaderGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowLeaderGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowParentGoal;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.ers.util.SimpleAutoPlayingSoundKeyFrameHandler;
import cn.aurorian.ers.util.TickHelper;
import cn.aurorian.oasis.client.animator.ClyderotundaTubunasusAnimator;
import cn.aurorian.oasis.entity.ai.OasisBreedGoal;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.invertory.TubunasusClyderotundaMenuProvider;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import cn.aurorian.oasis.entity.tubunasusdurovela.ai.TubunasusMeleeAttackGoal;
import cn.aurorian.oasis.init.OasisEntities;
import cn.aurorian.oasis.init.OasisItems;
import cn.aurorian.oasis.init.OasisSounds;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
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

import java.util.UUID;

public class TubunasusClyderotundaEntity extends ErsTamableVehicle<TubunasusClyderotundaEntity> implements HasGender,HasCustomInventoryScreen{
    private final GeneralAnimator<TubunasusClyderotundaEntity> animator;

    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.FLOAT);

    private static final EntityDataAccessor<Boolean> GENDER = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.BOOLEAN);

    public TubunasusClyderotundaEntity(EntityType<? extends ErsTamableVehicle> type, Level level) {
        super(type, level);
        animator = new ClyderotundaTubunasusAnimator(this);
        switchNavigator(true);
        this.createInventory();
        this.setMaxUpStep(2f);
        this.SWIM_COST = 0.15f;
        this.SPRINT_COST = 0.025f;
        this.RECOVER = 0.25f;
        this.WATER_ANIMAL = false;
    }

    @Override
    public void executeDefaultAttackType() {
        startAttack(AttackType.TUBUNASUS_ATTACK);
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
        return pStack.is(Items.WHEAT);
    }

    @Override
    public GeneralAnimator<TubunasusClyderotundaEntity> getAnimator() {
        return animator;
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
        entityData.define(SCALE, 1.0f);
        entityData.define(GENDER, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);

        compound.putFloat("Scale", getScale());
        compound.putBoolean("Gender", getGender());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        setScale(compound.getFloat("Scale"));
        setGender(compound.getBoolean("Gender"));
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TubunasusMeleeAttackGoal(this, 2,true));
        this.goalSelector.addGoal(2, new OasisBreedGoal(this,1));
        this.goalSelector.addGoal(3, new WaterAvoidingRandomStrollGoal(this,1.1,40));
        this.goalSelector.addGoal(3, new AvoidEntityGoal<>(this, TubunasusClyderotundaEntity.class, 10, 1.1, 2.1){
            @Override
            public boolean canUse() {
                return super.canUse() && !TubunasusClyderotundaEntity.this.isBaby();
            }
        });
        this.goalSelector.addGoal(4, new MobFollowLeaderGoal(this,2.1,14,10));
        this.goalSelector.addGoal(4, new MobFollowParentGoal(this,1));

        this.goalSelector.addGoal(1, new MobAlertLeaderGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, true,
                (mob) -> leader != null && leader.getTarget() == mob
        ));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<TubunasusClyderotundaEntity> main = new AnimationController<>(this, "main", 2 , state -> {
            RawAnimation builder = RawAnimation.begin();
            if(isInWater() && !onGround()){
               if(ErsUtils.isMoving(this)){
                    builder.thenLoop("animation.swim");
                }else {
                    builder.thenLoop("animation.swim_idle");
                }
            }else {
                if (isSprinting()) {
                    builder.thenLoop("animation.run");
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

        AnimationController<TubunasusClyderotundaEntity> extra = new AnimationController<>(this, "extra",2, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle_eat"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle_lookAround"))
                .triggerableAnim("taming", RawAnimation.begin().thenPlay("animation.taming"))
                .setSoundKeyframeHandler(new SimpleAutoPlayingSoundKeyFrameHandler<>());

        AnimationController<TubunasusClyderotundaEntity> attack = new AnimationController<>(this, "attack", 4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("attack_turn", RawAnimation.begin().thenPlay("animation.attack_turn"))
                .triggerableAnim("knockdown_left", RawAnimation.begin().thenPlay("animation.knockdown_left"))
                .triggerableAnim("knockdown_right", RawAnimation.begin().thenPlay("animation.knockdown_right"));

        controllerRegistrar.add(main,extra,attack);
    }

    @Override
    protected void ageBoundaryReached() {
        super.ageBoundaryReached();
        if(isBaby()){
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50);
        }
        else
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(275);
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if (!player.isCrouching() && !isBaby() && !isFood(player.getItemInHand(pHand))) {
            if((this.isTame() || !isInLove()) && !this.isOwnedBy(player))
                return InteractionResult.PASS;
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
        }
        return super.mobInteract(player, pHand);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(OasisSounds.DUROVELA_FOOTSTEP.get(), 0.10F, 1.0F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new TubunasusClyderotundaBodyControl(this);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 275)
                .add(Attributes.ARMOR,6)
                .add(Attributes.ATTACK_DAMAGE, 8)
                .add(Attributes.KNOCKBACK_RESISTANCE,1)
                .add(Attributes.FOLLOW_RANGE,48)
                .add(ForgeMod.SWIM_SPEED.get(),3)
                .add(Attributes.MOVEMENT_SPEED, 0.22);
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
        if(this.getAttackState().getType().canMove()){
            if(((!this.getRotDirection().isLargeTurn() || isInWater()) && (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0)) || this.isSprinting())
            {
                float baseSpeed = (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
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
    public void aiStep() {
        super.aiStep();
        if(!isBaby())
            waterAiStep(2.3f);
        else
            waterAiStep(1.1f);
        breakBlock();
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


        if(isSprinting()){
            float damage = 1f;
            if(hasHorseShoe)
                damage += 4;
            stompEffect(2f, 2f, damage);
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

            ErsBleedingEffect.giveBleedingEffect(living,2);

            living.setDeltaMovement(
                    living.getDeltaMovement().x / 2.0D - vec31.x,
                    living.onGround() ? Math.min(0.4D, living.getDeltaMovement().y / 2.0D + (double) 0.5F) : living.getDeltaMovement().y,
                    living.getDeltaMovement().z / 2.0D - vec31.z
            );
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.type().msgId().equals("sweetBerryBush")) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public @NotNull SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            if (pDataTag != null && pDataTag.contains("UUID")) {
                setUUID(UUID.fromString(pDataTag.getString("UUID")));
            }
            return pSpawnData;
        }

        if(this.random.nextFloat() < 0.2f)
            this.setGender(true);

        if(getGender())
            this.setScale(Mth.randomBetween(this.random, 1f, 1.1f));
        else
            this.setScale(Mth.randomBetween(this.random, 0.8f, 1f));
        this.refreshDimensions();

        SpawnGroupData returnValue = super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
        if(isBaby()){
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(50);
            this.setHealth(this.getMaxHealth());
        }
        return returnValue;
    }

    @Override
    public ErsTamable<?> getBreedOffspring(@NotNull ServerLevel level, @NotNull AgeableMob otherParent) {
        TubunasusClyderotundaEntity child = OasisEntities.TUBUNASUS_CLYDEROTUNDA.get().create(level);
        return child;
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        float scale = getScale();
        if(isBaby())
            scale *= 0.5f;
        return this.getType().getDimensions().scale(scale);
    }

    @Nullable
    @Override
    protected SoundEvent getHurtSound(@NotNull DamageSource pDamageSource) {
        return OasisSounds.DUROVELA_HURT.get();
    }

    @Override
    public int getInventorySize() {
        return 13;
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
            return this.isInLove() && pOtherAnimal.isInLove() && this.getGender() != ((TubunasusClyderotundaEntity) pOtherAnimal).getGender();
        }
    }

    @Override
    public void containerChanged(@NotNull Container pContainer) {
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
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(275 + 20);
        }else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(275);
        }

        if (armorBoost) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(10);
        } else {
            this.getAttribute(Attributes.ARMOR).setBaseValue(6);
        }
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!level().isClientSide() && isOwnedBy(player) && !isSoul()) {
            NetworkHooks.openScreen(
                    (ServerPlayer) player,
                    new TubunasusClyderotundaMenuProvider(this),
                    buf -> buf.writeInt(this.getId())
            );
        }
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
    public boolean canCollideWith(@NotNull Entity pEntity) {
        if(pEntity instanceof TubunasusDurovelaEntity)
            return false;
        return super.canCollideWith(pEntity);
    }
}
