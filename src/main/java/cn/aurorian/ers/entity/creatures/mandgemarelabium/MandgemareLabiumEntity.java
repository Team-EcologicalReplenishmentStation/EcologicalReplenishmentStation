package cn.aurorian.ers.entity.creatures.mandgemarelabium;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.animator.MandgemareLabiumAnimator;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.GeneralBodyControl;
import cn.aurorian.ers.entity.HasGender;
import cn.aurorian.ers.entity.SpawnVariant;
import cn.aurorian.ers.entity.creatures.ErsWaterAnimal;
import cn.aurorian.ers.entity.creatures.mandgemarelabium.ai.LabiumFightGoal;
import cn.aurorian.ers.init.ErsItems;
import com.mojang.serialization.Codec;
import java.util.UUID;
import java.util.function.IntFunction;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.util.ByIdMap;
import net.minecraft.util.RandomSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.control.BodyRotationControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingLookControl;
import net.minecraft.world.entity.ai.control.SmoothSwimmingMoveControl;
import net.minecraft.world.entity.ai.goal.AvoidEntityGoal;
import net.minecraft.world.entity.ai.util.DefaultRandomPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.ServerLevelAccessor;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class MandgemareLabiumEntity extends ErsWaterAnimal
        implements GeoEntity,
                ErsEntity<MandgemareLabiumEntity>,
                HasGender,
                VariantHolder<MandgemareLabiumEntity.Variant> {

    public MandgemareLabiumEntity(EntityType<? extends ErsWaterAnimal> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.moveControl = new SmoothSwimmingMoveControl(this, 85, 10, 0.02F, 0.1F, true);
        this.lookControl = new SmoothSwimmingLookControl(this, 10);
        animator = new MandgemareLabiumAnimator(this);
    }

    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final EntityDataAccessor<Boolean> GENDER =
            SynchedEntityData.defineId(MandgemareLabiumEntity.class, EntityDataSerializers.BOOLEAN);

    private static final EntityDataAccessor<Integer> DATA_VARIANT =
            SynchedEntityData.defineId(MandgemareLabiumEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_MARKING =
            SynchedEntityData.defineId(MandgemareLabiumEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_TAIL =
            SynchedEntityData.defineId(MandgemareLabiumEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_FIGURE =
            SynchedEntityData.defineId(MandgemareLabiumEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> FORCE =
            SynchedEntityData.defineId(MandgemareLabiumEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> COMPLETE =
            SynchedEntityData.defineId(MandgemareLabiumEntity.class, EntityDataSerializers.INT);

    private final GeneralAnimator<MandgemareLabiumEntity> animator;

    public void setForce(int force) {
        this.entityData.set(FORCE, force);
    }

    public int getForce() {
        return this.entityData.get(FORCE);
    }

    public void setComplete(int complete) {
        this.entityData.set(COMPLETE, complete);
    }

    public int getComplete() {
        return this.entityData.get(COMPLETE);
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(GENDER, false);
        this.entityData.define(DATA_VARIANT, 0);
        this.entityData.define(DATA_MARKING, 0);
        this.entityData.define(DATA_TAIL, 0);
        this.entityData.define(DATA_FIGURE, 0);
        this.entityData.define(FORCE, 0);
        this.entityData.define(COMPLETE, 0);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.addAdditionalSaveData(compoundTag);
        compoundTag.putBoolean("Gender", this.getGender());
        compoundTag.putInt("Variant", this.getVariant().getId());
        compoundTag.putInt("Marking", this.getMarking().getId());
        compoundTag.putInt("Tail", this.getTail().getId());
        compoundTag.putInt("Force", this.getForce());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compoundTag) {
        super.readAdditionalSaveData(compoundTag);
        this.setGender(compoundTag.getBoolean("Gender"));
        this.setVariant(Variant.byId(compoundTag.getInt("Variant")));
        this.setMarking(Markings.byId(compoundTag.getInt("Marking")));
        this.setTail(Tails.byId(compoundTag.getInt("Tail")));
        this.setForce(compoundTag.getInt("Force"));
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
        return new ItemStack(ErsItems.LABIUM_BUCKET.get());
    }

    @Override
    public int getMaxSpawnClusterSize() {
        return 4;
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        AnimationController<MandgemareLabiumEntity> main = new AnimationController<>(this, "main", 10, state -> {
            RawAnimation builder = RawAnimation.begin();
            if (isInWater()) {
                if (state.isMoving()) {
                    if (isSprinting()) {
                        builder.thenLoop("animation.quickly_swim");
                    } else {
                        builder.thenLoop("animation.swim");
                    }
                } else {
                    builder.thenLoop("animation.idle");
                }
            } else {
                builder.thenLoop("animation.flop");
            }
            return state.setAndContinue(builder);
        });
        AnimationController<MandgemareLabiumEntity> attack = new AnimationController<>(
                        this, "attack", 0, state -> PlayState.STOP)
                .triggerableAnim("show", RawAnimation.begin().thenPlay("animation.show"))
                .triggerableAnim("close", RawAnimation.begin().thenPlay("animation.close"));

        controllerRegistrar.add(main, attack);
    }

    public static AttributeSupplier.@NotNull Builder createAttributes() {
        return Mob.createMobAttributes()
                .add(Attributes.MAX_HEALTH, 6.0)
                .add(Attributes.MOVEMENT_SPEED, 0.4)
                .add(ForgeMod.SWIM_SPEED.get(), 0.5);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public GeneralAnimator<MandgemareLabiumEntity> getAnimator() {
        return animator;
    }

    @Override
    public void tick() {
        super.tick();
        if (level().isClientSide()) {
            animator.tick();
        } else {
            if (tickCount % 20 == 0) {
                if (getComplete() > 0) {
                    setComplete(getComplete() - 20);
                } else {
                    setComplete(0);
                }
            }
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
        this.goalSelector.addGoal(1, new AvoidEntityGoal<>(this, Player.class, 16.0F, 1f, 2f, var::test) {
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
            }

            @Override
            public void stop() {
                super.stop();
                this.mob.setSprinting(false);
            }
        });
        this.goalSelector.addGoal(2, new LabiumFightGoal(this, 2f));
    }

    private static final UUID SPEED_MODIFIER_SPRINTING_UUID = UUID.fromString("662A6B8D-DA3E-4C1C-8813-96EA6097278D");
    private static final AttributeModifier SPEED_MODIFIER_SPRINTING = new AttributeModifier(
            SPEED_MODIFIER_SPRINTING_UUID, "Sprinting speed boost", 2.0D, AttributeModifier.Operation.MULTIPLY_TOTAL);

    @Override
    public void setSprinting(boolean pSprinting) {
        this.setSharedFlag(3, pSprinting);
        AttributeInstance attributeinstance = this.getAttribute(Attributes.MOVEMENT_SPEED);
        if (attributeinstance.getModifier(SPEED_MODIFIER_SPRINTING_UUID) != null) {
            attributeinstance.removeModifier(SPEED_MODIFIER_SPRINTING);
        }

        if (pSprinting) {
            attributeinstance.addTransientModifier(SPEED_MODIFIER_SPRINTING);
        }
    }

    @Override
    public void setGender(boolean gender) {
        this.entityData.set(GENDER, gender);
    }

    @Override
    public boolean getGender() {
        return this.entityData.get(GENDER);
    }

    @Override
    public void setVariant(@NotNull Variant variant) {
        this.entityData.set(DATA_VARIANT, variant.id);
    }

    @Override
    public @NotNull Variant getVariant() {
        return Variant.byId(this.entityData.get(DATA_VARIANT));
    }

    public void setMarking(Markings markings) {
        this.entityData.set(DATA_MARKING, markings.getId());
    }

    public Markings getMarking() {
        return Markings.byId(this.entityData.get(DATA_MARKING));
    }

    public void setTail(Tails tail) {
        this.entityData.set(DATA_TAIL, tail.getId());
    }

    public Tails getTail() {
        return Tails.byId(this.entityData.get(DATA_TAIL));
    }

    public void setFigure(Figures figure) {
        this.entityData.set(DATA_FIGURE, figure.getId());
    }

    public Figures getFigure() {
        return Figures.byId(this.entityData.get(DATA_FIGURE));
    }

    @Override
    public @Nullable SpawnGroupData finalizeSpawn(
            @NotNull ServerLevelAccessor pLevel,
            @NotNull DifficultyInstance pDifficulty,
            @NotNull MobSpawnType pReason,
            @Nullable SpawnGroupData pSpawnData,
            @Nullable CompoundTag pDataTag) {
        if (pReason == MobSpawnType.BUCKET) {
            return pSpawnData;
        } else {
            RandomSource random = pLevel.getRandom();
            this.setVariant(SpawnVariant.getCommonSpawnVariant(Variant.values(), random));
            this.setMarking(Markings.byId(random.nextInt(Markings.values().length)));
            this.setTail(Tails.byId(random.nextInt(Tails.values().length)));
            this.setFigure(Figures.byId(random.nextInt(Figures.values().length)));
            this.setGender(random.nextBoolean());
            if (getGender()) {
                if (getForce() == 0) {
                    this.setForce(random.nextInt(10));
                }
            }
        }

        return super.finalizeSpawn(pLevel, pDifficulty, pReason, pSpawnData, pDataTag);
    }

    public enum Figures {
        SPOT(0),
        STRIPE(1);

        private static final IntFunction<Figures> BY_ID =
                ByIdMap.continuous(Figures::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        private final int id;

        Figures(int pId) {
            this.id = pId;
        }

        public int getId() {
            return this.id;
        }

        public static Figures byId(int pId) {
            return BY_ID.apply(pId);
        }
    }

    public enum Tails {
        BASIC(0),
        LION(1),
        ROUND(2);

        private static final IntFunction<Tails> BY_ID =
                ByIdMap.continuous(Tails::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        private final int id;

        Tails(int pId) {
            this.id = pId;
        }

        public int getId() {
            return this.id;
        }

        public static Tails byId(int pId) {
            return BY_ID.apply(pId);
        }
    }

    public enum Markings {
        WHITE(0),
        BLACK(1),
        YELLOW(2),
        BLUE(3),
        RED(4),
        CYAN(5),
        GREEN(6);

        private static final IntFunction<Markings> BY_ID =
                ByIdMap.continuous(Markings::getId, values(), ByIdMap.OutOfBoundsStrategy.WRAP);
        private final int id;

        Markings(int pId) {
            this.id = pId;
        }

        public int getId() {
            return this.id;
        }

        public static Markings byId(int pId) {
            return BY_ID.apply(pId);
        }
    }

    public enum Variant implements StringRepresentable, SpawnVariant {
        WHITE(0, "white", true),
        BLACK(1, "black", true),
        YELLOW(2, "yellow", true),
        BLUE(3, "blue", true),
        RED(4, "red", true),
        CYAN(5, "cyan", true),
        GREEN(6, "green", true);

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
