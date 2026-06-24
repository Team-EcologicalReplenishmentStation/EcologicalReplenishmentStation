package cn.aurorian.ers.entity.creatures.argentirhynchusgadiformis;

import cn.aurorian.ers.client.animator.ArgentumniscusAciculabularAnimator;
import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.ai.goal.ErsFollowFlockLeaderGoal;
import cn.aurorian.ers.entity.ai.movecontrol.ErsWaterAnimalMoveControl;
import cn.aurorian.ers.entity.creatures.ErsWaterAnimal;
import cn.aurorian.ers.init.ErsItems;
import java.util.function.Predicate;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySelector;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.animal.Bucketable;
import net.minecraft.world.entity.animal.WaterAnimal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;

public class ArgentumniscusAciculabularEntity extends ErsWaterAnimal
        implements ErsEntity<ArgentumniscusAciculabularEntity>, Bucketable {
    public ArgentumniscusAciculabularEntity(EntityType<? extends WaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new ErsWaterAnimalMoveControl(this);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        animator = new ArgentumniscusAciculabularAnimator(this);
    }

    private final GeneralAnimator<ArgentumniscusAciculabularEntity> animator;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<ArgentumniscusAciculabularEntity> main =
                new AnimationController<>(this, "main", 4, state -> {
                    RawAnimation builder = RawAnimation.begin();
                    if (isInWater()) {
                        if (isSprinting()) {
                            builder.thenLoop("animation.swim");
                        } else {
                            builder.thenLoop("animation.slow_swim");
                        }
                    } else {
                        builder.thenLoop("animation.flop");
                    }

                    return state.setAndContinue(builder);
                });

        controllerRegistrar.add(main);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 8.0)
                .add(Attributes.MOVEMENT_SPEED, 1)
                .add(ForgeMod.SWIM_SPEED.get(), 0.7);
    }

    public GeneralAnimator<ArgentumniscusAciculabularEntity> getAnimator() {
        return animator;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            animator.tick();
        }
    }

    @Override
    protected @NotNull BodyRotationControl createBodyControl() {
        return new GeneralBodyControl(this, 26);
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        Predicate<Entity> var = EntitySelector.NO_SPECTATORS;
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 16.0F, 3f, 3f, var::test) {
            @Override
            public boolean canUse() {
                super.canUse();
                if (isFollower()) return false;

                if (this.toAvoid == null) {
                    return false;
                } else {
                    Vec3 $$0 = DefaultRandomPos.getPosAway(this.mob, 32, 7, this.toAvoid.position());
                    if ($$0 == null) {
                        return false;
                    } else if (this.toAvoid.distanceToSqr($$0.x, $$0.y, $$0.z) < this.toAvoid.distanceToSqr(this.mob)) {
                        return false;
                    } else {
                        this.path = this.pathNav.createPath($$0.x, $$0.y, $$0.z, 0);
                        return this.path != null;
                    }
                }
            }

            @Override
            public void start() {
                super.start();
                this.mob.setSprinting(true);
                this.mob.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(3.0);
            }

            @Override
            public void stop() {
                super.stop();
                this.mob.setSprinting(false);
                this.mob.getAttribute(ForgeMod.SWIM_SPEED.get()).setBaseValue(0.7);
            }
        });
        this.goalSelector.addGoal(5, new ErsFollowFlockLeaderGoal(this));
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public @NotNull ItemStack getBucketItemStack() {
        return new ItemStack(ErsItems.ACICULABULAR_BUCKET.get());
    }
}
