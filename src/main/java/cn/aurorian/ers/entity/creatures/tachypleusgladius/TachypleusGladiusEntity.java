package cn.aurorian.ers.entity.creatures.tachypleusgladius;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.KnifeHorseshoeCrabAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.creatures.tachypleusgladius.ai.CrabAmbushGoal;
import cn.aurorian.ers.entity.creatures.tachypleusgladius.ai.CrabAttackGoal;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.goal.PanicGoal;
import net.minecraft.world.entity.ai.goal.RandomSwimmingGoal;
import net.minecraft.world.entity.ai.goal.TryFindWaterGoal;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;

public class TachypleusGladiusEntity extends WaterAnimal implements GeoEntity, Bucketable, ErsEntity<TachypleusGladiusEntity> {
    public TachypleusGladiusEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        animator = new KnifeHorseshoeCrabAnimator(this);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private final KnifeHorseshoeCrabAnimator animator;
    private static final EntityDataAccessor<Boolean> IS_WAITING = SynchedEntityData.defineId(TachypleusGladiusEntity.class, EntityDataSerializers.BOOLEAN);
    private static final EntityDataAccessor<Integer> COOLDOWN =  SynchedEntityData.defineId(TachypleusGladiusEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<Boolean> FROM_BUCKET = SynchedEntityData.defineId(TachypleusGladiusEntity.class, EntityDataSerializers.BOOLEAN);

    public boolean isWaiting() {
        return entityData.get(IS_WAITING);
    }

    public void setWaiting(boolean waiting) {
        entityData.set(IS_WAITING, waiting);
    }

    public int getCooldown() {
        return entityData.get(COOLDOWN);
    }

    public void setCooldown(int cooldown) {
        entityData.set(COOLDOWN, cooldown);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        entityData.define(IS_WAITING, false);
        entityData.define(COOLDOWN, 0);
        entityData.define(FROM_BUCKET, false);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<TachypleusGladiusEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            if (ErsUtils.isMoving(this)) {
                builder.thenLoop("animation.walk");
            }
            else if(state.animationTick >= 60 && isWaiting()) {
                builder.thenLoop("animation.wait");
            }else {
                builder.thenLoop("animation.idle");
            }
            return state.setAndContinue(builder);
        });

        AnimationController<TachypleusGladiusEntity> extra = new AnimationController<>(this, "extra", 4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"))
                .triggerableAnim("ambush", RawAnimation.begin().thenPlay("animation.ambush"))
                .triggerableAnim("dodge", RawAnimation.begin().thenPlay("animation.dodge"));

        controllerRegistrar.add(main,extra);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 20.0)
                .add(Attributes.ATTACK_DAMAGE, 20.0)
                .add(Attributes.MOVEMENT_SPEED,0.35)
                .add(Attributes.ARMOR,6)
                .add(Attributes.FOLLOW_RANGE, 6);
    }

    @Override
    public boolean isPushable() {
        return !isWaiting();
    }

    @Override
    public void knockback(double pStrength, double pX, double pZ) {
        if(isWaiting())
            return;
        super.knockback(pStrength, pX, pZ);
    }

    @Override
    protected void registerGoals() {
        this.goalSelector.addGoal(1, new CrabAttackGoal(this));
        this.goalSelector.addGoal(1, new TryFindWaterGoal(this));
        this.goalSelector.addGoal(2, new PanicGoal(this, 1.0){
            @Override
            public boolean canUse() {
                return super.canUse() && !isWaiting();
            }
        });
        this.goalSelector.addGoal(4, new RandomSwimmingGoal(this,1.0,40){
            @Override
            public boolean canUse() {
                return super.canUse() && !isWaiting();
            }
        });
        this.goalSelector.addGoal(5, new CrabAmbushGoal(this));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this,Mob.class,true,entity -> !(entity instanceof TachypleusGladiusEntity)){
            @Override
            public boolean canUse() {
                return super.canUse() && isWaiting();
            }
        });
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, Player.class,true){
            @Override
            public boolean canUse() {
                return super.canUse() && isWaiting();
            }
        });
    }

    @Override
    public void tick() {
        super.tick();
        animator.tick();
        if(getCooldown() > 0) {
            setCooldown(getCooldown() - 1);
        }
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this,20);
    }

    public void crabAttack(){
        setCooldown(200);
        Vec3 targetTo = new Vec3(
                this.getTarget().getX() - this.getX(),
                this.getTarget().getY() - this.getY(),
                this.getTarget().getZ() - this.getZ()
        ).normalize();
        float f = (float) (Mth.atan2(targetTo.z, targetTo.x) * 57.2957763671875D) - 90.0F;

        this.setYRot(ErsUtils.rotlerp(this.getYRot(), f, 360));
        this.yBodyRot = this.getYRot();

        this.setDeltaMovement(targetTo.x * 1.6,targetTo.y * 1.6,targetTo.z * 1.6);

        Vec3 pos = this.position();
        double range = 3;
        double startX = pos.x + targetTo.x * range;
        double startY = pos.y + targetTo.y * range;
        double startZ = pos.z + targetTo.z * range;

        this.triggerAnim("extra", "attack");

        AABB attackBox = new AABB(
                startX - range, startY - range, startZ - range,
                startX + range,
                startY + range,
                startZ + range
        );
        List<Entity> entities = this.level().getEntities(this, attackBox,
                entity -> entity instanceof LivingEntity);
        double damage = this.getAttributeValue(Attributes.ATTACK_DAMAGE);
            
        boolean shouldEscape = false;
        for (Entity target : entities) {
            target.setAirSupply(target.getAirSupply() - 150);
            target.hurt(this.level().damageSources().mobAttack(this), (float) damage);
            if(target.isAlive()){
                shouldEscape = true;
            }else {
                setCooldown(1200);
            }
        }

        if(shouldEscape){
            crabEscape(this.getTarget());
        }
    }

    public void crabEscape(Entity target) {
        setCooldown(400);
        setWaiting(false);
        this.triggerAnim("extra", "dodge");
        Vec3 targetTo = new Vec3(
                target.getX() - this.getX(),
                target.getY() - this.getY(),
                target.getZ() - this.getZ()
        ).normalize();
        float f = (float) (Mth.atan2(targetTo.z, targetTo.x) * 57.2957763671875D) - 90.0F;

        this.setYRot(ErsUtils.rotlerp(this.getYRot(), f, 360));
        this.yBodyRot = this.getYRot();
        this.setDeltaMovement(
                -targetTo.x * 2,
                0,
                -targetTo.z * 2
        );
    }

    @Override
    public boolean hurt(@NotNull DamageSource pSource, float pAmount) {
        if (pSource.is(DamageTypes.MOB_ATTACK) || pSource.is(DamageTypes.PLAYER_ATTACK)) {
            if(!isWaiting()){
                if(getLastHurtByMob() != null){
                    crabEscape(getLastHurtByMob());
                }else if(pSource.is(DamageTypes.PLAYER_ATTACK)){
                    crabEscape(pSource.getEntity());
                }

            }
            if(pSource.getEntity() instanceof LivingEntity living){
                living.hurt(level().damageSources().thorns(this), 4);
            }
        }
        return super.hurt(pSource, pAmount);
    }

    @Override
    public @NotNull MobType getMobType() {
        return MobType.ARTHROPOD;
    }

    public GeneralAnimator<TachypleusGladiusEntity> getAnimator() {
        return animator;
    }

    public boolean fromBucket() {
        return this.entityData.get(FROM_BUCKET);
    }

    public void setFromBucket(boolean pFromBucket) {
        this.entityData.set(FROM_BUCKET, pFromBucket);
    }

    protected @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return Bucketable.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }

    public void saveToBucketTag(@NotNull ItemStack pStack) {
        Bucketable.saveDefaultDataToBucketTag(this, pStack);
    }

    public void loadFromBucketTag(@NotNull CompoundTag pTag) {
        Bucketable.loadDefaultDataFromBucketTag(this, pTag);
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.TACHYPLEUS_GLADIUS_BUCKET.get());
    }

    public @NotNull SoundEvent getPickupSound() {
        return SoundEvents.BUCKET_FILL_FISH;
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }
}
