package cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides;

import cn.aurorian.ers.client.animator.ChlamydoselachusAcanthodiiAnimator;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.ai.movecontrol.ErsWaterAnimalMoveControl;
import cn.aurorian.ers.entity.creatures.ErsWaterAnimal;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.ai.AcanthodesMeleeAttackGoal;
import cn.aurorian.ers.entity.creatures.acanthodeschlamydoselachoides.navigation.AcanthodesNavigation;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsMobEffects;
import cn.aurorian.ers.item.ErsMobLargeBucket;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.target.HurtByTargetGoal;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.navigation.PathNavigation;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.Squid;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

public class AcanthodesChlamydoselachoidesEntity extends ErsWaterAnimal implements ErsEntity<AcanthodesChlamydoselachoidesEntity>, Bucketable {
    public AcanthodesChlamydoselachoidesEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new ErsWaterAnimalMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        animator = new ChlamydoselachusAcanthodiiAnimator(this);
    }
    private final GeneralAnimator<AcanthodesChlamydoselachoidesEntity> animator;

    @Override
    protected @NotNull PathNavigation createNavigation(@NotNull Level pLevel) {
        return new AcanthodesNavigation(this,pLevel);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<AcanthodesChlamydoselachoidesEntity> main = new AnimationController<>(this, "main", 4, state -> {
            RawAnimation builder = RawAnimation.begin();
            if(isInWater()){
                if (isAggressive()) {
                    builder.thenLoop("animation.swim");
                } else {
                    builder.thenLoop("animation.slow_swim");
                }
            }else {
                if(ErsUtils.isMoving(this)){
                    builder.thenLoop("animation.walk");
                }else {
                    builder.thenLoop("animation.idle");
                }
            }

            return state.setAndContinue(builder);
        });

        AnimationController<AcanthodesChlamydoselachoidesEntity> extra = new AnimationController<>(this, "extra", 4, state -> PlayState.STOP)
                .triggerableAnim("attack", RawAnimation.begin().thenPlay("animation.attack"));

        controllerRegistrar.add(main,extra);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 40.0)
                .add(Attributes.ATTACK_DAMAGE, 6)
                .add(Attributes.MOVEMENT_SPEED,1)
                .add(Attributes.FOLLOW_RANGE,64)
                .add(ForgeMod.SWIM_SPEED.get(),0.7);
    }

    public GeneralAnimator<AcanthodesChlamydoselachoidesEntity> getAnimator() {
        return animator;
    }

    @Override
    public void tick() {
        super.tick();
        if(level().isClientSide()){
            animator.tick();
        }
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(1, new AcanthodesMeleeAttackGoal(this,2,false));

        this.targetSelector.addGoal(1, new HurtByTargetGoal(this));
        this.targetSelector.addGoal(2, new NearestAttackableTargetGoal<>(this, Squid.class, true));
        this.targetSelector.addGoal(3, new NearestAttackableTargetGoal<>(this, LivingEntity.class,true,
                entity -> entity.hasEffect(ErsMobEffects.BLEEDING.get()) && entity.getType() != this.getType()));
    }

    @Override
    public boolean removeWhenFarAway(double pDistanceToClosestPlayer) {
        return false;
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 1;
    }

    protected @NotNull InteractionResult mobInteract(@NotNull Player pPlayer, @NotNull InteractionHand pHand) {
        return ErsMobLargeBucket.bucketMobPickup(pPlayer, pHand, this).orElse(super.mobInteract(pPlayer, pHand));
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.CHLAMYDOSELACHOIDES_LARGE_BUCKET.get());
    }
}
