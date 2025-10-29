package cn.aurorian.ers.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.client.ErsDataTickets;
import cn.aurorian.ers.client.MountCameraManager;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.init.ErsSerializers;
import cn.aurorian.ers.packet.MobTurnPacket;
import cn.aurorian.ers.util.TickHelper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.tags.BlockTags;
import net.minecraft.world.ContainerListener;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.event.ForgeEventFactory;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.joml.Math;
import org.joml.Vector3d;
import superlord.prehistoricfauna.init.PFBlocks;

import java.util.List;

public abstract class ErsTamableVehicle<T extends ErsTamableVehicle<?>> extends ErsTamable<T> implements ErsPlayerRideable,ErsSaddleable, ContainerListener {
    protected ErsTamableVehicle(EntityType<? extends TamableAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        stableHead = false;
    }

    private static final EntityDataAccessor<MobRotDirection> ROT_DIRECTION = SynchedEntityData.defineId(ErsTamableVehicle.class, ErsSerializers.MOB_ROTATION_SERIALIZER.get());
    public static final EntityDataAccessor<Boolean> IS_DIVING = SynchedEntityData.defineId(ErsTamableVehicle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> SWIM_STATE = SynchedEntityData.defineId(ErsTamableVehicle.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<MobAttack> ATTACK_STATE = SynchedEntityData.defineId(ErsTamableVehicle.class, ErsSerializers.MOB_ATTACK_SERIALIZER.get());
    private static final EntityDataAccessor<Boolean> IS_IN_SCREEN = SynchedEntityData.defineId(ErsTamableVehicle.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> DATA_VARIANT = SynchedEntityData.defineId(ErsTamableVehicle.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Float> STAMINA = SynchedEntityData.defineId(ErsTamableVehicle.class, EntityDataSerializers.FLOAT);
    private static final EntityDataAccessor<Boolean> DATA_SADDLED = SynchedEntityData.defineId(ErsTamableVehicle.class, EntityDataSerializers.BOOLEAN);
    protected float rideSpeed;
    public ErsTamableVehicle<?> leader;
    protected SimpleContainer inventory;
    protected boolean stableHead;

    public MobRotDirection getRotDirection(){
        return entityData.get(ROT_DIRECTION);
    }
    public  void setRotDirection(MobRotDirection rotDirection) {
        setRotDirection(rotDirection, false);
    }
    public void setRotDirection(MobRotDirection rotDirection, boolean sync) {
        entityData.set(ROT_DIRECTION, rotDirection);
        if (sync)
            ErsNetwork.INSTANCE.sendToServer(new MobTurnPacket(this.getId(),rotDirection));
    }

    public void onDiveKeyUpdate(boolean pressed) {
        if (!level().isClientSide) {
            entityData.set(IS_DIVING,pressed);
        }
    }

    public int getSwimState(){
        return entityData.get(SWIM_STATE);
    }
    public void setSwimState(int state){
        entityData.set(SWIM_STATE, state);
    }
    public void setAttackState(MobAttack mobAttack)
    {
        this.entityData.set(ATTACK_STATE, mobAttack);
    }
    public MobAttack getAttackState()
    {
        return this.entityData.get(ATTACK_STATE);
    }
    public boolean isInScreen() {
        return this.entityData.get(IS_IN_SCREEN);
    }

    public void setIsInScreen(boolean isInScreen) {
        entityData.set(IS_IN_SCREEN,isInScreen);
    }

    public float getStamina(){
        return this.entityData.get(STAMINA);
    }

    public void setStamina(float i){
        entityData.set(STAMINA, Math.min(100f,i));
    }
    public boolean getStableHead(){
        return stableHead;
    }
    public void setStableHead(boolean stableHead){
        this.stableHead = stableHead;
    }
    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(ROT_DIRECTION, MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
        this.entityData.define(IS_DIVING,false);
        this.entityData.define(SWIM_STATE, 0);
        this.entityData.define(ATTACK_STATE, new MobAttack(AttackType.EMPTY,this));
        this.entityData.define(IS_IN_SCREEN,false);
        this.entityData.define(DATA_VARIANT, 0);
        this.entityData.define(STAMINA,100f);
        this.entityData.define(DATA_SADDLED, false);
    }

    @Override
    public void tick() {
        super.tick();
        if(!this.level().isClientSide){
            this.getAttackState().tick();
            tickStamina(WATER_ANIMAL,RECOVER_WHEN_WALK);
        }
        else
            this.getAnimator().tick();
    }

    public int staminaCount = 0;

    public float SWIM_COST = 0.1f;
    public float SPRINT_COST = 0.15f;
    public float RECOVER = 0.075f;
    public boolean WATER_ANIMAL = false;
    public boolean RECOVER_WHEN_WALK = true;

    public void executeDefaultAttackType(){}

    public void executeSpecialAttackType(){}

    public void executeJudgementAttackType(){}

    public void executeTurnAttackType(){}

    public void tickStamina(boolean waterAnimal,boolean recoverWhenWalk){
        if(this.getControllingPassenger() instanceof Player){
            if(this.isSprinting()){
                staminaCount = 0;
                if(isInWater()){
                    this.setStamina(this.getStamina() - SWIM_COST);
                }else{
                    this.setStamina(this.getStamina() - SPRINT_COST);
                }
            }
            else{
                if(!waterAnimal && isInWater())
                    return;
                if(recoverWhenWalk && isMoving())
                    return;

                staminaCount++;
                if(staminaCount > 40)
                    this.setStamina(this.getStamina() + RECOVER);
            }
        }
        else{
            staminaCount++;
            if(staminaCount > 40)
                this.setStamina(this.getStamina() + RECOVER);
        }

        if(getStamina() <= 0){
            setStamina(0);
        }

    }

    public void startAttack(AttackType type) {
        if(type.getStaminaCost() > this.getStamina())
            return;

        if(type.getStaminaCost() != 0)
            this.setStamina(getStamina() - type.getStaminaCost());

        this.setAttackState(new MobAttack(type,this));
        triggerAnim("attack", type.getAnimName());
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);
        pCompound.putInt("Variant", getVariantId());
        pCompound.putFloat("Stamina", getStamina());
        pCompound.putBoolean("saddled", this.isSaddled());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setVariantId(pCompound.getInt("Variant"));
        this.setStamina(pCompound.getFloat("Stamina"));
        this.setSaddled(pCompound.getBoolean("saddled"));
    }

    public int getVariantId() {
        return this.entityData.get(DATA_VARIANT);
    }

    public void setVariantId(int i) {
        this.entityData.set(DATA_VARIANT, i);
    }

    /**
     * Trigger when mount move
     */
    public abstract void updateMount();

    public void updateLeader(){
        double followDistance = this.getAttributeValue(Attributes.FOLLOW_RANGE);
        AABB followArea = AABB.unitCubeFromLowerCorner(this.position())
                .inflate(followDistance, 10.0, followDistance);

        List<? extends Mob> nearbyMobs = this.level().getEntitiesOfClass(
                this.getClass(), followArea, EntitySelector.NO_SPECTATORS);

        ErsTamableVehicle<?> currentLeader = null;
        float largestScale = -1.0f;

        for (Mob candidate : nearbyMobs) {
            if (candidate instanceof ErsTamableVehicle<?> tamable && tamable.getType() == this.getType()) {
                if(!tamable.isAlive() || tamable.isBaby())
                    continue;
                if(this.isTame() && !tamable.isTame())
                    continue;
                float scale = tamable.getScale();
                if (scale > largestScale) {
                    largestScale = scale;
                    currentLeader = tamable;
                }
            }
        }

        if(currentLeader != null){
            this.leader = currentLeader;
        }
    }

    @Override
    public boolean isSprinting() {
        return (super.isSprinting() || (this.getControllingPassenger() instanceof Player player && player.isSprinting())) && getStamina() > 0;
    }

    public int getInventorySize(){
        return 7;
    }

    protected void createInventory()
    {
        this.inventory = new SimpleContainer(this.getInventorySize());
        this.inventory.addListener(this);
    }

    public @Nullable SimpleContainer getInventory() {
        return inventory;
    }

    public boolean isSaddled()
    {
        return entityData.get(DATA_SADDLED);
    }
    public void setSaddled(boolean saddled)
    {
        entityData.set(DATA_SADDLED, saddled);
    }

    @Override
    protected void addPassenger(@NotNull Entity passenger)
    {
        super.addPassenger(passenger);
        if (passenger instanceof Player player && player.isLocalPlayer())
        {
            MountCameraManager.onDragonMount();
        }
    }

    @Override
    protected void removePassenger(@NotNull Entity passenger)
    {
        super.removePassenger(passenger);

        if(!this.level().isClientSide)
            TickHelper.tickLater(this.level(),2,()-> this.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false)));

        this.setSprinting(false);
        if (passenger instanceof Player player && player.isLocalPlayer())
        {
            MountCameraManager.onDragonDismount();
        }
    }

    @Nullable
    @Override
    public LivingEntity getControllingPassenger() {
        Entity passenger = getFirstPassenger();
        return passenger instanceof LivingEntity ? (LivingEntity) passenger : null;
    }

    @Override
    protected void positionRider(@NotNull Entity pPassenger, @NotNull MoveFunction pCallback) {
        if(level().isClientSide)
        {
            Vector3d vector3d = this.getAnimData(ErsDataTickets.SADDLE_POS);
            if(vector3d != null)
            {
                pCallback.accept(pPassenger,
                        this.getX() + vector3d.x,
                        this.getY() + vector3d.y - 0.5f,
                        this.getZ() + vector3d.z);
            }
        } else {
            super.positionRider(pPassenger, pCallback);
        }
    }

    private int calculateDownY() {
        if (this.getNavigation().getPath() != null) {
            Path path = this.getNavigation().getPath();
            Vec3 p = path.getEntityPosAtNode(this, Math.min(path.getNodeCount() - 1, path.getNextNodeIndex() + 1));
            if (p.y < this.getY() - 1) {
                return -1;
            }
        }
        return 1;
    }

    protected void breakBlock() {
        if (ForgeEventFactory.getMobGriefingEvent(this.level(), this)) {
            final int bounds = 1;
            final int yMinus = calculateDownY();
            BlockPos.betweenClosedStream(
                    (int) Math.floor(this.getBoundingBox().minX) - bounds,
                    (int) Math.floor(this.getBoundingBox().minY) + yMinus,
                    (int) Math.floor(this.getBoundingBox().minZ) - bounds,
                    (int) Math.floor(this.getBoundingBox().maxX) + bounds,
                    (int) Math.floor(this.getBoundingBox().maxY) + bounds,
                    (int) Math.floor(this.getBoundingBox().maxZ) + bounds
            ).forEach(pos -> {
                final BlockState state = level().getBlockState(pos);
                boolean prehistoricCompat = EcologicalReplenishmentStation.PrehistoricFaunaLoaded && state.is(PFBlocks.NELUMBO_STEM.get());
                if (state.is(Blocks.LILY_PAD) || state.is(BlockTags.LEAVES) || state.is(Blocks.MANGROVE_ROOTS) || state.is(Blocks.COBWEB) || prehistoricCompat) {
                    this.setDeltaMovement(this.getDeltaMovement().multiply(0.6F, 1, 0.6F));
                    if (!level().isClientSide) {
                        level().destroyBlock(pos, false);
                    }
                }
            });
        }
    }

    protected void stompEffect(float hurtSize, float forwards, float damage) {
        Vec3 center = this.position().add(
                new Vec3(0, 0, forwards)
                        .yRot(-this.yBodyRot * ((float) Math.PI / 180F))
        );

        AABB aabb = new AABB(center.subtract(hurtSize, 1, hurtSize),
                center.add(hurtSize, 1, hurtSize));

        for (LivingEntity living : level().getEntitiesOfClass(LivingEntity.class, aabb,
                EntitySelector.NO_CREATIVE_OR_SPECTATOR)) {

            if (!living.is(this) &&
                    !this.isAlliedTo(living) &&
                    living.getType() != this.getType() &&
                    living.distanceToSqr(center) <= hurtSize * hurtSize) {

                stompDamage(damage, living);
            }
        }
    }

    protected void stompDamage(float damage, LivingEntity living) {
        if (living.hurt(this.damageSources().mobAttack(this), damage)) {

            Vec3 vec31 = (new Vec3(this.getX() - living.getX(), 0.0D, this.getZ() - living.getZ())).normalize().scale(0.5F);

            living.setDeltaMovement(
                    living.getDeltaMovement().x / 2.0D - vec31.x,
                    living.onGround() ? Math.min(0.4D, living.getDeltaMovement().y / 2.0D + (double) 0.5F) : living.getDeltaMovement().y,
                    living.getDeltaMovement().z / 2.0D - vec31.z
            );
        }
    }


    @Override
    public boolean causeFallDamage(float pFallDistance, float pMultiplier, @NotNull DamageSource pSource) {
        float[] ret = ForgeHooks.onLivingFall(this, pFallDistance, pMultiplier);
        if (ret == null) {
            return false;
        } else {
            pFallDistance = ret[0];
            pMultiplier = ret[1];
            int i = this.calculateFallDamage(pFallDistance, pMultiplier);
            if (i > 0) {
                this.playSound(this.getFallSounds().big(), 1.0F, 1.0F);
                this.playBlockFallSound();
                this.hurt(pSource, (float)i);
                return true;
            } else {
                return false;
            }
        }
    }

    protected void waterAiStep(float waterHeight){
        if(isInWater()){
            this.setNoGravity(true);
            if(this.level().getBlockState(this.getOnPos()).isSolidRender(this.level(), this.getOnPos())){
                this.setNoGravity(false);
            }

            if(!onGround() && getSwimState() == 0){
                if(wasEyeInWater)
                    setSwimState(2);
                else
                    setSwimState(1);
            }

            if(wasEyeInWater && getSwimState() == 1 && !(this.level().getBlockState(this.getOnPos()).isSolidRender(this.level(),this.getOnPos()))
                    && this.getDeltaMovement().y >= 0){
                this.setDeltaMovement(this.getDeltaMovement().add(0,0.01,0));
            }
            else if(getFluidTypeHeight(ForgeMod.WATER_TYPE.get()) < waterHeight && getSwimState() != 3){
                this.setDeltaMovement(this.getDeltaMovement().add(0,-0.03,0));
            }
        }else {
            this.setNoGravity(false);
            if(getSwimState() != 3){
                this.setSwimState(0);
            }
            else{
                if (this.level().isClientSide)
                    return;
                TickHelper.tickLater(this.level(),40,()-> setSwimState(0));
            }
        }
    }
}
