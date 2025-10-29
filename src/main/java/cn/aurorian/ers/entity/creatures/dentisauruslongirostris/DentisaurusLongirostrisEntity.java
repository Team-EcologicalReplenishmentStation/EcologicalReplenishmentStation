package cn.aurorian.ers.entity.creatures.dentisauruslongirostris;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.SwampDragonAnimator;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.entity.ai.goal.MobFollowOwnerGoal;
import cn.aurorian.ers.entity.ai.goal.MobWanderGoal;
import cn.aurorian.ers.entity.ai.movecontrol.AquaticMoveControl;
import cn.aurorian.ers.entity.ai.movecontrol.LimitedMoveControl;
import cn.aurorian.ers.entity.ai.navigation.MMGroundPathNavigation;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.ai.*;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.invertory.DentisaurusLongirostrisMenuProvider;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.item.equipment.MountEquipment;
import cn.aurorian.ers.packet.MobSyncDimPacket;
import cn.aurorian.ers.packet.MobTurnPacket;
import cn.aurorian.ers.packet.VehicleJumpPacket;
import cn.aurorian.ers.util.ErsUtils;
import com.mojang.serialization.Codec;
import net.minecraft.Util;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
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
import net.minecraft.world.*;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
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
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.BlockPathTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;
import org.joml.Math;
import org.joml.Vector3f;
import software.bernie.geckolib.core.animation.AnimatableManager.ControllerRegistrar;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.List;
import java.util.function.IntFunction;

public class DentisaurusLongirostrisEntity extends ErsTamableVehicle<DentisaurusLongirostrisEntity> implements ContainerListener, HasCustomInventoryScreen, Bucketable, VariantHolder<DentisaurusLongirostrisEntity.Variant>{
    private final GeneralAnimator<DentisaurusLongirostrisEntity> animator;
    private static final float BASE_MOVE_SPEED = 0.25f;
    private static final float BASE_HEALTH = 150.0f;
    private static final float BASE_ATTACK_DAMAGE = 15.0f;
    public static final float BASE_BOUNDING_BOX_WIDTH = 3.5f;
    public static final float BASE_BOUNDING_BOX_HEIGHT = 2.75f;
    private static final float ACCELERATION = 0.08f;
    private boolean init = false;

    private static final EntityDataAccessor<Float> RENDER_SIZE = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Integer> NEXT_POOP_TIME = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Vector3f> FOOD_POSITION = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.VECTOR3);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Float> HUNGER = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.FLOAT);
    public static final EntityDataAccessor<Integer> NEXT_CHANGE_TIME = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.INT);
    public static final EntityDataAccessor<Boolean> BLOODY = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Boolean> ARMORED = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.BOOLEAN);
    public static final EntityDataAccessor<Integer> FILLED_FISH = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> ELITE = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> CAN_BE_ELITE = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Boolean> MATURE = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COMMAND = SynchedEntityData.defineId(DentisaurusLongirostrisEntity.class, EntityDataSerializers.INT);
    public DentisaurusLongirostrisEntity(EntityType<? extends DentisaurusLongirostrisEntity> pEntityType, Level pLevel) {
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
        if(!isInWater() || onGround()){
            if(isMoving()){
                if(isSprinting()){
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_RUN);
                }else {
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_WALK);
                }
            }else{
                startAttack(AttackType.SWAMP_DRAGON_ATTACK);
            }
        }else {
            if(isSprinting()){
                startAttack(AttackType.SWAMP_DRAGON_ATTACK_QUICK_SWIMMING);
            }else {
                if(getWaterDepth() >= 3 || !onGround()){
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_SWIM_MID);
                }else {
                    startAttack(AttackType.SWAMP_DRAGON_ATTACK_SWIM_SHALLOW);
                }
            }
        }
    }

    @Override
    public void executeSpecialAttackType() {
        if(isInWater() && !onGround()){
            startAttack(AttackType.SWAMP_DRAGON_SWIM_JUDGEMENT);
        }else {
            startAttack(AttackType.SWAMP_DRAGON_JUDGEMENT);
        }
    }

    @Override
    public void executeJudgementAttackType() {
        if(isInWater() && !onGround()){
            startAttack(AttackType.SWAMP_DRAGON_SWIM_SPECIAL_ATTACK);
        }else {
            startAttack(AttackType.SWAMP_DRAGON_SPECIAL_ATTACK);
        }
    }

    @Override
    public void executeTurnAttackType() {
        if(isSprinting())
            return;
        if(isInWater() && !onGround()) {
            startAttack(AttackType.SWAMP_DRAGON_ATTACK_SWIM_TURN);
        }else {
            startAttack(AttackType.SWAMP_DRAGON_ATTACK_TURN);
        }
    }

    @Override
    public boolean isPushable() {
        boolean mightBeSleeping = updateSkyBrightness() < 4 && this.getControllingPassenger() == null && getCommand() == 0 && this.getAttackState().getType() == AttackType.EMPTY && this.getTarget() == null && !this.isInWater();
        return !isVehicle() && !mightBeSleeping && getCommand() != 1 && getAttackState().getType().canMove();
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new DentisaurusLongirostrisBodyControl(this);
    }

    @Override
    protected void registerGoals() {
        goalSelector.addGoal(1,new DentisaurusLongirostrisMeleeAttackGoal(this,2,false));
        goalSelector.addGoal(2,new MobFollowOwnerGoal(this,2.2D,7.0F,4.0F,64F,false));
        goalSelector.addGoal(5, new DentisaurusLongirostrisRandomSwimGoal(this, 1));
        goalSelector.addGoal(5, new MobWanderGoal(this, 1.2){
            @Override
            public boolean canUse() {
                if (this.mob.isInWater() || !((DentisaurusLongirostrisEntity)this.mob).getAttackState().getType().canMove()) {
                    return false;
                }
                return super.canUse() && ((DentisaurusLongirostrisEntity)this.mob).updateSkyBrightness() > 4 && ((DentisaurusLongirostrisEntity)this.mob).getCommand() == 0;
            }
        });
        goalSelector.addGoal(6, new DentisaurusLongirostrisLookAtPlayerGoal(this, Player.class, 6.0F));
        targetSelector.addGoal(1, new DentisaurusLongirostrisHurtByTargetGoal(this));
        targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,Player.class, true){
            @Override
            public boolean canUse() {
                return super.canUse() && !isTame() && isMature();
            }
        });
        targetSelector.addGoal(2, new DentisaurusLongirostrisOwnerHurtByTargetGoal(this));
        targetSelector.addGoal(3, new DentisaurusLongirostrisOwnerHurtTargetGoal(this));
        targetSelector.addGoal(4, new AttackFishGoal(this, AbstractFish.class, Boolean.TRUE));
    }
    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level level) {
        return new AmphibiousPathNavigation(this, level){

            @Override
            public boolean isStableDestination(@NotNull BlockPos p_217799_) {
                return true;
            }
        };
    }

    @Override
    public void tick() {
        super.tick();
        if (isClientSide())
        {
            boolean flag2 = onGround() && !isMoving() && this.getAttackState().getType() == AttackType.EMPTY;

            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME) && isMature()) {
                this.entityData.set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200,400));
                int newState = RandomSource.create().nextInt(4);
                if(newState != 0 && flag2) {
                    if(getCommand() == 1){
                        if(newState == 1) {
                            triggerAnim("extra", "idle_yawn");
                        }else if(newState == 2) {
                            triggerAnim("extra", "idle_sit2");
                        }else {
                            triggerAnim("extra", "idle_sit3");
                        }
                    }else if(updateSkyBrightness() > 4) {
                        if(newState == 1) {
                            triggerAnim("extra", "idle2");
                        }else {
                            triggerAnim("extra", "idle3");
                        }
                    }
                }
            }
            if(this.isMoving()){
                updateMount();
            }

            if(entityData.get(BLOODY)){
                if(tickCount % 2400 == 0 || isInWater()){
                    entityData.set(BLOODY, false);
                }
            }
        }

        if (!isClientSide()) {
            ErsNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> this), new MobTurnPacket(this.getId(),getRotDirection()));

            if (this.tickCount > this.entityData.get(NEXT_CHANGE_TIME) && isMature()) {
                boolean flag2 = onGround() && !isMoving() && this.getAttackState().getType() == AttackType.EMPTY && getTarget() == null;
                if(flag2 && getCommand() == 0 && isInWater() && onGround() && getWaterDepth() < 3){
                    if(level().random.nextFloat() < 0.25f){
                        this.navigation.stop();
                        this.entityData.set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(1200,1600));
                        startAttack(AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL);
                    }else if(level().random.nextFloat() < 0.5f) {
                        this.navigation.stop();
                        this.entityData.set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(1200, 1600));
                        startAttack(AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE);
                    }
                }
            }

            if(staminaCount > 40 && this.getCommand() == 1){
                setStamina(getStamina() + 0.4f);
            }

            if(tickCount % 20 == 0){
                if(tickCount % 400 ==0){
                    this.heal(1);
                }

                //饥饿机制
                float hunger = this.entityData.get(HUNGER);
                if(tickCount % 100 == 0){
                    if(isElite())
                        hunger -= 0.1f;
                    if(isMature())
                        hunger -= 0.1f;
                    setHunger(hunger - 0.1f);
                }

                if((hunger < 50 || this.getHealth() < this.getMaxHealth()) && tickCount % 200 == 0){
                    for (int slot = 1; slot <= 3; slot++) {
                        if (!this.getInventory().getItem(slot).isEmpty()) {
                            this.getInventory().getItem(slot).shrink(1);
                            this.feed(3);
                            this.heal(5);
                            break;
                        }
                    }
                }

                if(this.getAgeInTicks() % 24000 == 0 || !init)
                {
                    this.updateFromAgeServer();
                    // 发送同步数据包
                    ErsNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY.with(() -> this), new MobSyncDimPacket(this.getId()));
                    init = true;
                }

                //装备更新
                int filledSlots = 0;
                for (int slot = 1; slot <= 3; slot++) {
                    if (!getInventory().getItem(slot).isEmpty()) {
                        filledSlots++;
                    }
                }
                entityData.set(FILLED_FISH,filledSlots);

                if(hunger > 0){
                    this.setAgeInTicks(this.getAgeInTicks() + 20);
                }

                int nextPoopTime = this.entityData.get(NEXT_POOP_TIME);
                if (nextPoopTime > 0) {
                    this.entityData.set(NEXT_POOP_TIME, nextPoopTime - 20);
                } else {
                    this.generateFeces();
                    this.entityData.set(NEXT_POOP_TIME, 10000 + RandomSource.create().nextInt(14000));
                }
            }

            boolean hasScratchingBoard = false;
            for (int i = 5; i <= 7; i++) {
                ItemStack itemStack = this.inventory.getItem(i);
                if(!itemStack.isEmpty()){
                    if(itemStack.getItem() instanceof MountEquipment equipment){
                        equipment.tickEquip(itemStack,this);
                    }
                    if(itemStack.is(Items.TURTLE_HELMET)){
                        if(this.getControllingPassenger()!=null){
                            turtleHelmetTick();
                        }
                    }
                    if(itemStack.is(ErsItems.SCRATCHING_BOARD.get()))
                        hasScratchingBoard = true;
                }
            }

            //践踏
            if(this.isSprinting()){
                stompEffect(3f, 3f, hasScratchingBoard ? 4f : 2f);
            }
        }
    }

    private void turtleHelmetTick() {
        if (!this.getControllingPassenger().isEyeInFluid(FluidTags.WATER)) {
            this.getControllingPassenger().addEffect(new MobEffectInstance(MobEffects.WATER_BREATHING, 200, 0, false, false, true));
        }
    }

    public void updateFromAgeServer() {
        if(getAgeInDays() >= 20 && !this.isMature()){
            this.setMature(true);
        }

        if(getAgeInDays() >= 38 && this.canBeElite()){
            this.setElite(true);
        }

        float scale = calculateScale();
        if(isMature())
            this.setRenderSize(scale);
        else {
            this.setRenderSize(ErsUtils.calculateBabyRenderSize(getAgeInDays()));
        }
        // 修改碰撞箱
        this.refreshDimensions();

        if(!this.isElite())
            scale *= 0.7f;
        if(!this.isMature())
            scale *= 0.4f;
        this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH * scale);
        this.getAttribute(Attributes.ATTACK_DAMAGE).setBaseValue(BASE_ATTACK_DAMAGE * scale);
        if(!this.isMature())
            scale *= 2.1f;
        else if(!this.isElite())
            scale *= 1.3f;
        this.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(BASE_MOVE_SPEED * scale);
        containerChanged(this.getInventory());
        this.getAnimatableInstanceCache().getManagerForId(getId()).clearSnapshotCache();
    }

    @Override
    public @NotNull EntityDimensions getDimensions(@NotNull Pose pPose) {
        int age = getAgeInDays();
        float scale;
        if(this.isElite()){
            age -= 18;
        }
        if(this.isMature()){
            age -= 20;
        }
        scale = ErsUtils.calculateRenderSize(age);
        if(!this.isMature()){
            scale *= 0.4F;
        }

        return this.getType().getDimensions().scale(scale);
    }

    public float getHunger() {
        return this.entityData.get(HUNGER);
    }

    public void setHunger(float hunger) {
        entityData.set(HUNGER, java.lang.Math.max(java.lang.Math.min(hunger, 100),0));
    }

    public void feed(int foodAmount) {
        setHunger(getHunger() + foodAmount);
    }

    @Override
    public boolean isFood(@NotNull ItemStack itemStack) {
        return itemStack.is(ItemTags.FISHES);
    }

    public int getCommand() {
        return this.entityData.get(COMMAND);
    }

    public void setCommand(int command) {
        this.entityData.set(COMMAND, command);
    }
    public boolean canBeElite() {
        return this.entityData.get(CAN_BE_ELITE);
    }
    public void setCanBeElite(boolean canBeElite) {
        this.entityData.set(CAN_BE_ELITE, canBeElite);
    }
    public boolean isElite() {
        return this.entityData.get(ELITE);
    }
    public void setElite(boolean elite) {
        this.entityData.set(ELITE, elite);
    }
    public boolean isMature() {
        return this.entityData.get(MATURE);
    }
    public void setMature(boolean mature) {
        this.entityData.set(MATURE, mature);
    }

    @Override
    public @NotNull InteractionResult mobInteract(Player player, @NotNull InteractionHand pHand) {
        if(player.getMainHandItem().getItem() == ErsItems.LARGE_WATER_BUCKET.get() && this.isTame() && this.isOwnedBy(player))
        {
            return ErsMobLargeBucket.bucketMobPickup(player, pHand, this).orElse(InteractionResult.PASS);
        }
        if (isMature() && isTame() && !isVehicle() && this.isOwnedBy(player) && !player.isCrouching() && !player.getMainHandItem().is(ItemTags.FISHES)) {
            player.setYRot(getYRot());
            player.setXRot(getXRot());
            player.startRiding(this);
        }

        if (player.isCrouching() && this.isTame() && this.isOwnedBy(player) && !player.getMainHandItem().is(ItemTags.FISHES))
        {
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
            } else if (this.getCommand() == 2) {
                commandText = "follow";
                this.setTarget(null);
            }
            player.displayClientMessage(Component.translatable("ers.command." + commandText), true);
            if (isOrderedToSit()) setTarget(null);
        }

        if (!isClientSide() && (player.getMainHandItem().is(ItemTags.FISHES) || player.getMainHandItem().is(ErsItems.PISCIVORES_FEED.get()))) {
            // 驯服
            if(!this.isTame() && this.getAgeInDays() < 1 && player.getMainHandItem().is(ErsItems.PISCIVORES_FEED.get())){
                if (this.random.nextInt(3) == 0 && !ForgeEventFactory.onAnimalTame(this, player)){
                    this.tame(player);
                    this.level().broadcastEntityEvent(this, (byte)7);
                }else{
                    this.level().broadcastEntityEvent(this, (byte)6);
                }
            }else if(this.isTame()) {
                this.feed(player.getMainHandItem().is(ItemTags.FISHES)? 1 : 3);
                this.heal(player.getMainHandItem().is(ItemTags.FISHES)? 3 : 5);
                player.getMainHandItem().shrink(1);
            }

        }
        return super.mobInteract(player, pHand);
    }

    private void generateFeces(){
        if (this.level().isClientSide) return;
        List<BlockState> fecesBlocks = List.of(ErsBlocks.SWAMP_DRAGON_BONE_FECES.get().defaultBlockState(),
                ErsBlocks.SWAMP_DRAGON_SMALL_FECES.get().defaultBlockState(),
                ErsBlocks.SWAMP_DRAGON_LARGE_FECES.get().defaultBlockState(),
                ErsBlocks.SWAMP_DRAGON_GLASSES_FECES.get().defaultBlockState(),
                ErsBlocks.SWAMP_DRAGON_TEL_FECES.get().defaultBlockState());
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        double yRot = Math.toRadians(this.getYRot());
        int offsetX = (int) (-Math.sin(yRot) * 1.5); // 向后偏移1.5格
        int offsetZ = (int) (Math.cos(yRot) * 1.5);

        BlockPos targetPos = pos.offset(offsetX, 0, offsetZ);
        if (level.getBlockState(targetPos).isAir() &&
            level.getBlockState(targetPos.below()).isSolidRender(level, targetPos.below())) {
            BlockState state = fecesBlocks.get(this.level().random.nextInt(fecesBlocks.size()));
            if(state.is(ErsBlocks.SWAMP_DRAGON_TEL_FECES.get())){
                if(this.level().random.nextFloat() < 0.1f){
                    return;
                }
            }
            level.setBlock(targetPos, state, 3);
            level.playSound(null,
                targetPos.getX() + 0.5,
                targetPos.getY() + 0.5,
                targetPos.getZ() + 0.5,
                SoundEvents.SLIME_BLOCK_PLACE,
                SoundSource.BLOCKS,
                0.5f,
                1.0f);
        }
    }

    private void placeNest(){
        if (this.level().isClientSide) return;
        BlockState state = ErsBlocks.SWAMP_DRAGON_NEST.get().defaultBlockState();
        BlockPos pos = this.blockPosition();
        Level level = this.level();

        double yRot = Math.toRadians(this.getYRot());
        int offsetX = (int) (-Math.sin(yRot) * 1.5);
        int offsetZ = (int) (Math.cos(yRot) * 1.5);

        BlockPos targetPos = pos.offset(offsetX, 0, offsetZ);
        if (level.getBlockState(targetPos).isAir() &&
                level.getBlockState(targetPos.below()).isSolidRender(level, targetPos.below())) {
            level.setBlock(targetPos, state, 3);
        }
    }

    @Override
    public void setSprinting(boolean pSprinting) {
        this.setSharedFlag(3, pSprinting);
    }

    @Override
    protected @NotNull Vec3 getRiddenInput(@NotNull Player pPlayer, @NotNull Vec3 pTravelVector) {
        if(this.rideSpeed != 0 || pPlayer.jumping){
            updateMount();
            if(getCommand() == 1)
                this.setCommand(0);
        }
        if(this.isInFluidType()){
            if(this.isSprinting())
            {
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(12);
            }
            else
            {
                this.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(8);
            }

            float y = 0;
            if(pPlayer.jumping && getSwimState() != 1){
                y = 0.4f;
                if(!wasEyeInWater && getSwimState() != 3)
                    setSwimState(1);
            }else if(entityData.get(IS_DIVING) && getSwimState() != 1) {
                y = -0.4f;
            }

            if (rideSpeed > 0.1 && getSwimState() == 1) {
                if (this.horizontalCollision && !onGround()) {
                    y = 0.8f;
                }
            }

            if (!getAttackState().getType().canMove())
                y = 0;
            return new Vec3(0, y, this.rideSpeed);
        }
        else
        {
            return new Vec3(0, 0, this.rideSpeed);
        }
    }

    @Override
    protected float getRiddenSpeed(@NotNull Player pPlayer) {
        return this.rideSpeed + 0.2f;
    }

    @Override
    protected void tickRidden(@NotNull Player driver, @NotNull Vec3 move)
    {
        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        tickStableHead();

        if(this.isInFluidType() && pitch > 20 && isSprinting() && getWaterDepth() >= 4)
        {
            jumpFromGround();
        }
        //只有当非大转向并且按下键并且有鞍具的时候rideSpeed才会增加
        if(this.getAttackState().getType().canMove() && this.isSaddled())
        {
            if((!this.getRotDirection().isLargeTurn() || this.isSprinting()) && (Math.abs(driver.zza) > 0 || Math.abs(driver.xxa) > 0))
            {
                float baseSpeed = (float) this.getAttribute(Attributes.MOVEMENT_SPEED).getValue();
                this.rideSpeed = Mth.approach(this.rideSpeed, isSprinting() ? baseSpeed * 2.2f : baseSpeed, ACCELERATION);
            }else{
                this.rideSpeed = Mth.approach(this.rideSpeed, 0, ACCELERATION * 2);
            }
        }
        else{
            this.rideSpeed = Mth.approach(this.rideSpeed, 0, ACCELERATION * 3);
        }

    }

    @Override
    protected void jumpFromGround() {
        // 基础跳跃速度
        double jumpVelocity = 0.2;
        // 设置新的运动速度
        this.setDeltaMovement(this.getDeltaMovement().add(0, jumpVelocity, 0));
        this.hasImpulse = true;
        ForgeHooks.onLivingJump(this);
        if(this.level().isClientSide)
            ErsNetwork.INSTANCE.sendToServer(new VehicleJumpPacket(this.getId()));
    }

    public int updateSkyBrightness() {
        return ErsUtils.updateSkyBrightness(level());
    }

    @Override
    public void registerControllers(ControllerRegistrar controllers) {
        AnimationController<DentisaurusLongirostrisEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            boolean flag1 = false;
            for(int y = 1; y <= 8; y++) {
                BlockPos checkPos = new BlockPos(this.blockPosition().below(y));
                BlockState blockstate = this.level().getBlockState(checkPos);

                if(blockstate.isAir()) {
                    continue;
                }
                flag1 = isWaterBlock(this.level(), this.blockPosition().below(y));
                break;
            }

            if (isFalling() && !flag1) {
                builder.thenLoop("animation.fall");
            }
            if (isInWater() && !onGround()) {
                if (isMoving() && isSprinting()) {
                    builder.thenLoop("animation.quickly_swimming");
                } else if (isMoving() && !isSprinting() && (getWaterDepth() <= 3 || this.onGround() || !this.isMature())) {
                    builder.thenLoop("animation.swim_shallow");
                } else if (isMoving() && !isSprinting()) {
                    builder.thenLoop("animation.swim_mid");
                } else if (!this.getRotDirection().isNone() && isMature()) {
                    if (this.getRotDirection().isLeft()) {
                            builder.thenLoop("animation.+w_left");
                    } else {
                        builder.thenLoop("animation.-w_right");
                    }
                } else {
                    if(this.getControllingPassenger() instanceof Player player &&
                            getRiddenInput(player,new Vec3(0,0,0)).y < 0){
                        builder.thenLoop("animation.swim_down");
                    }
                    else if(this.getControllingPassenger()!=null && this.getControllingPassenger().jumping){
                        builder.thenLoop("animation.swim_up");
                    }else {
                        builder.thenLoop("animation.idle_shallow");
                    }
                }
            } else {
                if (!this.onGround() && flag1) {
                    builder.thenLoop("animation.swim_shallow");
                }
                if (isMoving() && isSprinting()) {
                    builder.thenLoop("animation.run");
                } else if (isMoving()) {
                    builder.thenLoop("animation.walk");
                } else if (!this.getRotDirection().isNone()) {
                    if (this.getRotDirection().isLeft()) {
                        if(isVehicle())
                            builder.thenLoop("animation.+left");
                        else
                            builder.thenLoop("animation.+left_ai");
                    } else {
                        if(isVehicle())
                            builder.thenLoop("animation.-right");
                        else
                            builder.thenLoop("animation.-right_ai");
                    }
                } else {
                    if(getCommand() == 1){
                        builder.thenLoop("animation.idle_sit");
                    }
                    else if(updateSkyBrightness() < 4 && this.getControllingPassenger() == null && getCommand() == 0 && this.getAttackState().getType() == AttackType.EMPTY && this.getTarget() == null && !this.isInWater()){
                        if(isMature() && this.level().getRawBrightness(this.blockPosition(),0) > 8){
                            builder.thenLoop("animation.sleep-sunlight");
                        }else {
                            builder.thenLoop("animation.sleep");
                        }
                    }else {
                        builder.thenLoop("animation.idle");
                    }
                }
            }

            return state.setAndContinue(builder);
            });

        AnimationController<DentisaurusLongirostrisEntity> attack = new AnimationController<>(this, "attack",4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack-idle"))
                .triggerableAnim("attack-walk", RawAnimation.begin().thenPlay("animation.attack-walk"))
                .triggerableAnim("attack-run", RawAnimation.begin().thenPlay("animation.attack-run"))
                .triggerableAnim("attack_swim_shallow", RawAnimation.begin().thenPlay("animation.attack_swim_shallow"))
                .triggerableAnim("attack_swim_mid", RawAnimation.begin().thenPlay("animation.attack_swim_mid"))
                .triggerableAnim("attack_quickly_swimming", RawAnimation.begin().thenPlay("animation.attack_quickly_swimming"))
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
                .triggerableAnim("catch_fish-middle", RawAnimation.begin().thenPlay("animation.catch_fish-middle"));

        AnimationController<DentisaurusLongirostrisEntity> extra = new AnimationController<>(this, "extra",0, state -> PlayState.STOP)
                .triggerableAnim("idle2", RawAnimation.begin().thenPlay("animation.idle2"))
                .triggerableAnim("idle3", RawAnimation.begin().thenPlay("animation.idle3"))
                .triggerableAnim("idle_sit2", RawAnimation.begin().thenPlay("animation.idle_sit2"))
                .triggerableAnim("idle_sit3", RawAnimation.begin().thenPlay("animation.idle_sit3"))
                .triggerableAnim("idle_yawn", RawAnimation.begin().thenPlay("animation.idle_yawn"));

            controllers.add(main,attack,extra);
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
        int maxCheckHeight = 16; // 设置一个最大检查高度，避免无限循环
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

    public void tickStableHead(){
        float pitch = this.getAnimator().getModelPitch(this.getAnimator().getPartialTick());
        if(this.isInWater())
        {
            setStableHead(pitch < 20);
        }
        else if(this.onGround())
        {
            setStableHead(true);
        }else if(isFalling()){
            setStableHead(false);
        }
    }
    @Override
    protected boolean canAddPassenger(@NotNull Entity pPassenger) {

        return this.getPassengers().size() <= 3;
    }

    @Override
    protected void dropCustomDeathLoot(@NotNull DamageSource pSource, int pLooting, boolean pRecentlyHit) {
        super.dropCustomDeathLoot(pSource, pLooting, pRecentlyHit);
        if(isMature() && random.nextFloat() < 0.15f){
            ItemStack stack = new ItemStack(ErsItems.SWAMP_DRAGON_EGG.get());
            this.spawnAtLocation(stack);
        }

        for (int i = 0; i < this.inventory.getContainerSize(); i++) {
            ItemStack stack = this.inventory.getItem(i);
            this.spawnAtLocation(stack);
            this.inventory.setItem(i, ItemStack.EMPTY);
        }
    }
    @Override
    public void containerChanged(@NotNull Container pContainer) {
        this.setSaddled(!this.inventory.getItem(0).isEmpty());
        this.entityData.set(ARMORED,!getInventory().getItem(4).isEmpty());

        boolean healthBoost = false;
        boolean armorBoost = false;
        for (int i = 5; i <= 7; i++) {
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

        float scale = calculateScale();

        if(!this.isElite())
            scale *= 0.7f;
        if(!this.isMature())
            scale *= 0.4f;

        if(healthBoost){
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH * scale + 20);
        }else {
            this.getAttribute(Attributes.MAX_HEALTH).setBaseValue(BASE_HEALTH * scale);
        }

        if (armorBoost) {
            this.getAttribute(Attributes.ARMOR).setBaseValue(16);
        } else {
            this.getAttribute(Attributes.ARMOR).setBaseValue(12);
        }
    }

    private float calculateScale() {
        int age = getAgeInDays();
        if(this.isMature()){
            age -= 20;
        }
        if(this.isElite()){
            age -= 18;
        }
        return ErsUtils.calculateRenderSize(age);
    }

    @Override
    public void openCustomInventoryScreen(@NotNull Player player) {
        if (!isClientSide()) {
            NetworkHooks.openScreen(
                (ServerPlayer) player,
                new DentisaurusLongirostrisMenuProvider(this),
                // 写入实体ID到数据缓冲区
                buf -> buf.writeInt(this.getId())
            );
        }
    }

    @Override
    public void startAttack(AttackType type) {
        super.startAttack(type);
        if(type != AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL && type != AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE && this.getCommand() == 1)
            this.setCommand(0);
    }
    @Override
    protected void defineSynchedData()
    {
        super.defineSynchedData();
        entityData.define(FOOD_POSITION, new Vector3f(0, 0, 0));
        entityData.define(FROM_BUCKET, false);
        entityData.define(NEXT_POOP_TIME, RandomSource.create().nextInt(24000));
        entityData.define(RENDER_SIZE, 1.0f);
        entityData.define(HUNGER,100f);
        entityData.define(NEXT_CHANGE_TIME, this.tickCount + random.nextIntBetweenInclusive(200,400));
        entityData.define(BLOODY, false);
        entityData.define(ARMORED,false);
        entityData.define(FILLED_FISH,0);
        entityData.define(COMMAND, 0);
        entityData.define(CAN_BE_ELITE, false);
        entityData.define(ELITE, false);
        entityData.define(MATURE,false);
    }
    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound)
    {
        super.addAdditionalSaveData(compound);
        compound.putInt("AgeTicks", this.getAgeInTicks());
        compound.putInt("NextPoopTime", this.getNextPoopTime());
        compound.putFloat("RenderSize", this.getRenderSize());
        compound.putFloat("Hunger", this.getHunger());
        compound.putInt("Command", this.getCommand());
        compound.putBoolean("Armored",entityData.get(ARMORED));
        compound.putBoolean("Elite", this.isElite());
        compound.putBoolean("CanBeElite", this.canBeElite());
        compound.putBoolean("Mature", this.isMature());
        if(hasCustomName())
            compound.putString("CustomName", Component.Serializer.toJson(this.getCustomName()));

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
        compound.put("Items", itemsList);
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound)
    {
        super.readAdditionalSaveData(compound);
        this.setAgeInTicks(compound.getInt("AgeTicks"));
        this.setNextPoopTime(compound.getInt("NextPoopTime"));
        this.setRenderSize(compound.getFloat("RenderSize"));
        this.setHunger(compound.getFloat("Hunger"));
        this.setCommand(compound.getInt("Command"));
        this.entityData.set(ARMORED,compound.getBoolean("Armored"));
        this.setElite(compound.getBoolean("Elite"));
        this.setCanBeElite(compound.getBoolean("CanBeElite"));
        this.setMature(compound.getBoolean("Mature"));
        if(compound.contains("CustomName"))
            this.setCustomName(Component.Serializer.fromJson(compound.getString("CustomName")));

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

    @Override
    public void updateMount() {
        this.getEntityData().set(NEXT_CHANGE_TIME, this.tickCount + RandomSource.create().nextIntBetweenInclusive(200,400));
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle2");
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle3");
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle_yawn");
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle_sit2");
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("idle_sit3");

        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("catch_fish-small");
        this.getAnimatableInstanceCache().getManagerForId(this.getId()).stopTriggeredAnimation("catch_fish-middle");
        if(this.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL || this.getAttackState().getType() == AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE){
            this.getAttackState().isSyncInstance = true;
            this.setAttackState(new MobAttack(AttackType.EMPTY,this));
        }
    }

    @Override
    public boolean canBreatheUnderwater() {
        return true;
    }

    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource)
    {
        return false;
    }

    public GeneralAnimator<DentisaurusLongirostrisEntity> getAnimator(){
        return animator;
    }

    public void setFoodPosition(Vector3f foodPosition){
        entityData.set(FOOD_POSITION, foodPosition);
    }
    public Vector3f getFoodPosition(){
        return entityData.get(FOOD_POSITION);
    }
    public float getRenderSize()
    {
        return entityData.get(RENDER_SIZE);
    }
    public void setRenderSize(float renderSize){
        entityData.set(RENDER_SIZE, renderSize);
    }
    public int getInventorySize(){
        return 8;
    }
    public int getNextPoopTime(){
        return this.entityData.get(NEXT_POOP_TIME);
    }
    public void setNextPoopTime(int time){
        this.entityData.set(NEXT_POOP_TIME, time);
    }

    @Override
    public boolean isSaddleable()
    {
        return isAlive() && isTame() && isMature();
    }
    public static AttributeSupplier.Builder createAttributes()
    {
        return Mob.createMobAttributes()
                .add(Attributes.MOVEMENT_SPEED, BASE_MOVE_SPEED)
                .add(Attributes.MAX_HEALTH, BASE_HEALTH * 0.7)
                .add(Attributes.FOLLOW_RANGE, 16)
                .add(Attributes.KNOCKBACK_RESISTANCE, 1)
                .add(Attributes.ATTACK_DAMAGE, BASE_ATTACK_DAMAGE * 0.7)
                .add(Attributes.ARMOR,12)
                .add(ForgeMod.SWIM_SPEED.get(),8);
    }

    @Override
    public void equipSaddle(@Nullable SoundSource source)
    {
        this.inventory.setItem(0, new ItemStack(ErsItems.SWAMP_DRAGON_SADDLE.get()));
        setSaddled(true);
        level().playSound(null, getX(), getY(), getZ(), SoundEvents.HORSE_SADDLE, getSoundSource(), 1, 1);
    }
    public boolean isClientSide(){
        return level().isClientSide;
    }
    private static boolean isWaterBlock(Level level, BlockPos pos) {
        BlockState blockState = level.getBlockState(pos);
        // 检查是否为水方块（包括流动的水）
        return blockState.getFluidState().is(FluidTags.WATER) &&
            !blockState.getFluidState().isEmpty();
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
        return new ItemStack(ErsItems.SWAMP_DRAGON_LARGE_BUCKET.get());
    }

     public boolean isFalling() {
        return !this.onGround() && 
               !this.isInWater() && 
               !this.isInLava() && 
               this.getDeltaMovement().y < -0.8;
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
        if(getControllingPassenger() != null){
            float i = 1.7f;
            if(getAgeInDays() > 50){
                i = 1.8f;
            }
            waterAiStep(i);
        }else {
            this.setNoGravity(false);
        }
    }

    @Override
    public @NotNull Vec3 getFluidFallingAdjustedMovement(double pGravity, boolean pIsFalling, @NotNull Vec3 pDeltaMovement) {
        if (!this.isNoGravity()) {
            double d0;
            if (pIsFalling && java.lang.Math.abs(pDeltaMovement.y - 0.005) >= 0.003 && java.lang.Math.abs(pDeltaMovement.y - pGravity / 16.0) < 0.003) {
                d0 = -0.003;
            } else {
                d0 = pDeltaMovement.y - pGravity / 16.0;
            }

            return new Vec3(pDeltaMovement.x, d0, pDeltaMovement.z);
        } else {
            return pDeltaMovement;
        }
    }

    @Override
    public boolean isInvulnerableTo(DamageSource source) {
        if(source.type().msgId().equals("sweetBerryBush") || source.getEntity() instanceof Pufferfish || source.type().msgId().equals("cactus")) {
            return true;
        }
        return super.isInvulnerableTo(source);
    }

    @Override
    public void onInsideBubbleColumn(boolean pDownwards) {}

    @Override
    public void makeStuckInBlock(@NotNull BlockState pState, @NotNull Vec3 pMotionMultiplier) {}

    @Override
    public SpawnGroupData finalizeSpawn(@NotNull ServerLevelAccessor pLevel, @NotNull DifficultyInstance pDifficulty, @NotNull MobSpawnType pReason, @org.jetbrains.annotations.Nullable SpawnGroupData pSpawnData, @org.jetbrains.annotations.Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            return pSpawnData;
        } else {
            RandomSource $$6 = pLevel.getRandom();
            if (pSpawnData == null) {
                if($$6.nextFloat() < 0.1f) {
                    pSpawnData = new SwampDragonGroupData(Variant.getRareSpawnVariant($$6));
                }else {
                    pSpawnData = new SwampDragonGroupData(Variant.getCommonSpawnVariant($$6),Variant.getCommonSpawnVariant($$6),
                            Variant.getCommonSpawnVariant($$6),Variant.getCommonSpawnVariant($$6));
                }
            }

            this.setVariant(((SwampDragonGroupData)pSpawnData).getVariant($$6));

            if(this.random.nextFloat() < 0.05f){
                this.setCanBeElite(true);
            }

            this.setAgeInDays(this.random.nextIntBetweenInclusive(15,50));
            updateFromAgeServer();
            if(isMature()){
                if(this.random.nextFloat() < 0.15f){
                    placeNest();
                }
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

    public enum Variant implements StringRepresentable {
        ORIGINAL(0, "original", true),
        ORIGINAL_LIGHT(1,"original_light",true),
        ORIGINAL_DEEP(2,"original_deep",true),
        LACK_YELLOW(3, "lack_yellow", false),
        LACK_YELLOW_LIGHT(4,"lack_yellow_light",false),
        LACK_YELLOW_DEEP(5,"lack_yellow_deep",false),
        BLACK(6, "black", false),
        BLACK_LIGHT(7,"black_light",false),
        BLACK_DEEP(8,"black_deep",false),
        WHITE(9, "white", false),
        WHITE_LIGHT(10,"white_light",false),
        WHITE_DEEP(11,"white_deep",false);

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
    public static class SwampDragonGroupData extends AgeableMobGroupData {
        public final Variant[] types;

        public SwampDragonGroupData(Variant... pTypes) {
            super(false);
            this.types = pTypes;
        }

        public Variant getVariant(RandomSource pRandom) {
            return this.types[pRandom.nextInt(this.types.length)];
        }
    }
}
