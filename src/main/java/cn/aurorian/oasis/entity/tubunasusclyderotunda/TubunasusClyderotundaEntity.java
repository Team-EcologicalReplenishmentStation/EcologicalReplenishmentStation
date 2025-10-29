package cn.aurorian.oasis.entity.tubunasusclyderotunda;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsSaddleable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.ai.goal.MobAlertLeaderGoal;
import cn.aurorian.ers.entity.ai.goal.MobFollowLeaderGoal;
import cn.aurorian.ers.entity.ai.goal.MobWanderGoal;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.ers.util.ErsUtils;
import cn.aurorian.ers.util.TickHelper;
import cn.aurorian.oasis.client.animator.ClyderotundaTubunasusAnimator;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.ai.TubunasusClyderotundaMeleeAttackGoal;
import cn.aurorian.oasis.init.OasisItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.AmphibiousPathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
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

public class TubunasusClyderotundaEntity extends ErsTamableVehicle<TubunasusClyderotundaEntity> implements ErsSaddleable, ContainerListener, HasCustomInventoryScreen, Bucketable{
    private final GeneralAnimator<TubunasusClyderotundaEntity> animator;
    private boolean stableHead;
    private int rushTimer = 0;
    private boolean crush = false;
    private static final EntityDataAccessor<Boolean> DATA_SADDLED = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> SAIL = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Float> SCALE = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DEAD_PROGRESS = SynchedEntityData.defineId(TubunasusClyderotundaEntity.class, EntityDataSerializers.INT);
    public TubunasusClyderotundaEntity(EntityType<? extends ErsTamableVehicle> type, Level level) {
        super(type, level);
        animator = new ClyderotundaTubunasusAnimator(this);
        stableHead = false;
        this.setMaxUpStep(1f);
        this.moveControl = new LimitedMoveControl(this);
        switchNavigator(true);
        this.setPathfindingMalus(BlockPathTypes.WATER, 0.0F);
        this.createInventory();
    }

    protected void switchNavigator(boolean onLand) {
        if (onLand) {
            navigation = new MMGroundPathNavigation(this, level());
        } else {
            navigation = new AmphibiousPathNavigation(this,level());
        }
    }

    public int getRushTimer() {
        return rushTimer;
    }
    public void setStableHead(boolean stableHead){
        this.stableHead = stableHead;
    }
    public boolean getStableHead(){
        return stableHead;
    }
    @Override
    public GeneralAnimator<TubunasusClyderotundaEntity> getAnimator() {
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
        entityData.set(SCALE, Math.clamp(scale, 0.9f, 1.1f));
    }
    protected SimpleContainer inventory;
    public int getInventorySize(){
        return 7;
    }
    public int getDeadProgress(){
        return entityData.get(DEAD_PROGRESS);
    }
    public void setDeadProgress(int progress){
        entityData.set(DEAD_PROGRESS, progress);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(200,400));
        entityData.define(SAIL, true);
        entityData.define(SCALE, 1.0f);
        entityData.define(FROM_BUCKET, false);
        entityData.define(DEAD_PROGRESS,0);
        entityData.define(DATA_SADDLED, false);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        if(getDeadProgress() == 0){
            ListTag itemsList = new ListTag();
            for (int i = 0; i < this.inventory.getContainerSize(); i++) {
                ItemStack stack = this.inventory.getItem(i);
                if (!stack.isEmpty()) {
                    CompoundTag itemTag = new CompoundTag();
                    itemTag.putInt("Slot", i);
                    stack.save(itemTag);
                    itemsList.add(itemTag);
                }
            }
            compoundTag.put("Items", itemsList);
        }

        if(hasCustomName())
            compoundTag.putString("CustomName", Component.Serializer.toJson(this.getCustomName()));

        compoundTag.putBoolean("saddled", this.isSaddled());
        compoundTag.putFloat("Scale", getScale());
        compoundTag.putInt("DeadProgress", getDeadProgress());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if(getDeadProgress() == 0){

            this.inventory.clearContent();
            ListTag itemsList = compound.getList("Items", 10);
            for (int i = 0; i < itemsList.size(); i++) {
                CompoundTag itemTag = itemsList.getCompound(i);
                int slot = itemTag.getInt("Slot");
                ItemStack stack = ItemStack.of(itemTag);
                if (!stack.isEmpty() && slot >= 0 && slot < this.inventory.getContainerSize()) {
                    this.inventory.setItem(slot, stack);
                }
            }
        }

        if(compound.contains("CustomName"))
            this.setCustomName(Component.Serializer.fromJson(compound.getString("CustomName")));

        setSaddled(compound.getBoolean("saddled"));
        setScale(compound.getFloat("Scale"));
        setDeadProgress(compound.getInt("DeadProgress"));

    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
//        if (!level().isClientSide()) {
//            NetworkHooks.openScreen(
//                    (ServerPlayer) player,
//                    new DurovelaTubunasusMenuProvider(this),
//                    buf -> buf.writeInt(this.getId())
//            );
//        }
    }

    @Override
    protected void tickDeath() {
        if(this.getDeadProgress() == 0)
            this.setDeadProgress(1);
        this.ejectPassengers();
        if (this.getDeadProgress() >= 8){
            this.remove(RemovalReason.KILLED);
        }
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack stack = this.inventory.getItem(i);
            this.spawnAtLocation(stack);
            this.inventory.setItem(i, ItemStack.EMPTY);
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new TubunasusClyderotundaMeleeAttackGoal(this, 2,true));
        this.goalSelector.addGoal(2, new MobWanderGoal(this,1.1,40){
            @Override
            public boolean canUse() {
                return super.canUse() && ((TubunasusClyderotundaEntity)this.mob).leader == this.mob;
            }
        }.setWaterVerticalRange(0));
        this.goalSelector.addGoal(2, new AvoidEntityGoal<>(this, TubunasusClyderotundaEntity.class, 8, 1.1, 2.1));
        this.goalSelector.addGoal(3, new MobFollowLeaderGoal(this,2.1,14,10));

        this.goalSelector.addGoal(1, new MobAlertLeaderGoal(this).setAlertOthers());
        this.goalSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Mob.class, true,
                (mob) -> leader != null && leader.getTarget() == mob
        ));
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<TubunasusClyderotundaEntity> main = new AnimationController<>(this, "main", 2 , state -> {
            RawAnimation builder = RawAnimation.begin();
            if(getDeadProgress() != 0)
                return PlayState.STOP;
            if(isInWater() && !onGround()){
                if(isSprinting()){
                    builder.thenLoop("animation.swim");
                }
                else if(ErsUtils.isMoving(this)){
                    builder.thenLoop("animation.swim2");
                }else {
                    builder.thenLoop("animation.swim_idle");
                }
            }else {
                if (isSprinting()) {
                    builder.thenLoop("animation.run");
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

        AnimationController<TubunasusClyderotundaEntity> extra = new AnimationController<>(this, "extra",2, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle_eat"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle_lookAround"))
                .triggerableAnim("taming", RawAnimation.begin().thenPlay("animation.taming"));

        AnimationController<TubunasusClyderotundaEntity> attack = new AnimationController<>(this, "attack", 4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("attack_turn", RawAnimation.begin().thenPlay("animation.attack_turn"));

        AnimationController<TubunasusClyderotundaEntity> control = new AnimationController<>(this, "control", 0, state -> {
            RawAnimation builder = RawAnimation.begin();
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
    public @NotNull InteractionResult interactAt(@NotNull Player player, @NotNull Vec3 pVec, @NotNull InteractionHand pHand) {
        if(this.getDeadProgress() != 0){
            if(player.getItemInHand(pHand).getItem() instanceof SwordItem || player.getItemInHand(pHand).getItem() instanceof AxeItem){
                getRandomDrop(getDeadProgress());
                playSound(SoundEvents.BONE_BLOCK_HIT);
                this.setDeadProgress(getDeadProgress() + 1);
                return InteractionResult.SUCCESS;
            }else
                return InteractionResult.PASS;
        }else {
            return super.interactAt(player, pVec, pHand);
        }
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand pHand) {
        if(player.getMainHandItem().getItem() == ErsItems.LARGE_WATER_BUCKET.get() && this.isTame() && this.isOwnedBy(player))
        {
            return ErsMobLargeBucket.bucketMobPickup(player, pHand, this).orElse(InteractionResult.PASS);
        }

        if (!player.isCrouching()) {
            if(this.isTame() && !this.isOwnedBy(player) && getPassengers().isEmpty())
                return InteractionResult.PASS;
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
        }
        return super.mobInteract(player, pHand);
    }

    private void getRandomDrop(int progress) {
        if (level().isClientSide)
            return;

        switch (progress) {
            case 2 -> spawnLeather(1, 2);
            case 3 -> {
                spawnAtLocation(new ItemStack(OasisItems.HEART.get(),1), 1);
                spawnChanceLeather(0.667f);
            }
            case 4 -> {
                spawnAtLocation(new ItemStack(OasisItems.INTESTINES.get(),1), 1);
                spawnAtLocation(new ItemStack(OasisItems.KIDNEY.get(),1), 1);
                spawnChanceLeather(0.667f);
            }
            case 5 -> {
                spawnAtLocation(new ItemStack(OasisItems.LUNG.get(),1), 1);
                spawnChanceLeather(0.667f);
            }
            case 6 -> {
                spawnAtLocation(new ItemStack(OasisItems.LIVER.get(), random.nextIntBetweenInclusive(2, 3)), 1);
                spawnChanceLeather(0.667f);
            }
            case 7 -> spawnAtLocation(new ItemStack(OasisItems.BONE.get(), random.nextIntBetweenInclusive(3, 5)), 1);
            default -> {}
        }
    }

    private void spawnChanceLeather(float chance) {
        if (random.nextFloat() < chance) {
            spawnLeather(1, 2);
        }
    }

    private void spawnLeather(int min, int max) {
        ItemStack drop = new ItemStack(OasisItems.LEATHER.get(), random.nextIntBetweenInclusive(min, max));
        this.spawnAtLocation(drop, 1);
    }

    @Override
    protected void playStepSound(@NotNull BlockPos pPos, @NotNull BlockState pState) {
        this.playSound(SoundEvents.COW_STEP, 0.15F, 1.0F);
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new TubunasusClyderotundaBodyControl(this);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 250.0)
                .add(Attributes.ARMOR,6)
                .add(Attributes.ATTACK_DAMAGE, 12)
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
    public boolean isPushable() {
        return !onGround();
    }

    @Override
    public boolean isNoAi() {
        return super.isNoAi() || !isAlive();
    }

    @Override
    public boolean canBeCollidedWith() {
        return true;
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
            if(crush)
                return new Vec3(0,0,0);
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
        return this.rideSpeed + 0.2f;
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
        waterAiStep(2.4f);
        breakBlock();
        checkAndBreakBlocksAhead();
    }

    @Override
    public boolean isAlive() {
        return super.isAlive() || this.getDeadProgress() == 0;
    }

    @Override
    public void tick() {
        super.tick();
        if(!isAlive())
            return;

        if(level().isClientSide){
            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME)) {
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
//                    equipment.tickEquip(itemStack,this);
                }
                if(itemStack.is(OasisItems.HORSESHOE.get()))
                    hasHorseShoe = true;
            }
        }

        if(isSprinting() && !isInWater()){
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

        if(crush && this.tickCount % 40 == 0){
            crush = false;
        }

        if(!level().isClientSide()){
            if(tickCount % 200 == 0){
                heal(1);
                updateLeader();
            }

            if(isTame())
                return;
            Player player = (Player) this.getFirstPassenger();

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
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle2");
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle3");
    }

    @Override
    public void onInsideBubbleColumn(boolean pDownwards) {}

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {}

    @Override
    public int getMaxSpawnClusterSize() {
        return 5;
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
                if (state.canOcclude() && state.isSolidRender(level(), checkPos)) {
                    shouldStop = true;
                }

                if (destroyTime < stoneDestroyTime && destroyTime > 0 &&
                        block.getExplosionResistance() <= 1200.0F) {
                    level().destroyBlock(checkPos, true, this);
                }
            }
        }

        if (shouldStop) {
            crush = true;
            this.setSprinting(false);
        }
    }

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @Nullable SpawnGroupData pSpawnData, @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            return pSpawnData;
        } else {
            var throwedSpawnData = pSpawnData;

            if (pSpawnData == null) {
              pSpawnData = throwedSpawnData;
            }
        }
        this.setScale(Mth.randomBetween(this.random, 0.9f, 1.1f));
        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        float scale = getScale();
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

    @Override
    public void containerChanged(@NotNull Container container) {
        this.setSaddled(!this.inventory.getItem(0).isEmpty());

        boolean healthBoost = false;
        boolean armorBoost = false;
        for (int i = 4; i <= 6; i++) {
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
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(270);
        }else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(250);
        }

        if (armorBoost) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(10);
        } else {
            this.getAttribute(Attributes.ARMOR).setBaseValue(6);
        }
    }

    @Override
    public boolean isSaddleable() {
        return isAlive() && isTame();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource soundSource) {
        this.inventory.setItem(0, new ItemStack(OasisItems.TUBUNASUS_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }

    @Override
    public boolean isSaddled() {
        return entityData.get(DATA_SADDLED);
    }

    public void setSaddled(boolean saddled)
    {
        entityData.set(DATA_SADDLED, saddled);
    }
}
