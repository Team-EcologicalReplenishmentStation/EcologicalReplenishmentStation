package cn.aurorian.ers.block.be;

import cn.aurorian.ers.config.ErsServerConfig;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.init.ErsBlockEntities;
import cn.aurorian.ers.item.egg.ErsEgg;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ArtificialNestBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    private ItemStack egg;

    public ArtificialNestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ErsBlockEntities.ARTIFICIAL_NEST_BLOCK_ENTITY.get(), pPos, pBlockState);
        egg = ItemStack.EMPTY;
    }

    int hatchingTime;

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, ArtificialNestBlockEntity entity) {
        if (level.isClientSide) return;

        if (!entity.getEgg().isEmpty()) {
            int hatchingTime = entity.egg.getOrCreateTag().getInt("HatchTime");
            if (hatchingTime > entity.hatchingTime) {
                entity.hatchingTime = hatchingTime;
            } else {
                entity.egg.getOrCreateTag().putInt("HatchTime", entity.hatchingTime);
            }

            entity.hatchingTime = entity.hatchingTime + ErsServerConfig.HATCHING_RATE.get();

            entity.setChanged();
            if (entity.hatchingTime % 20 == 0) {
                entity.markUpdated();
            }

            ErsEgg hatchingEgg = (ErsEgg) entity.egg.getItem();

            if (entity.hatchingTime >= hatchingEgg.getHatchTime()) {
                entity.hatchingTime = 0;
                entity.egg = ItemStack.EMPTY;
                entity.markUpdated();

                ErsTamable<?> entity1 = hatchingEgg.born(level);
                if (entity1 != null) {
                    entity1.setAgeInTicks(0);
                    entity1.updateAgeFromServer();
                    entity1.moveTo((double) pos.getX() + 0.3, pos.getY() + 1, (double) pos.getZ() + 0.3, 0.0F, 0.0F);
                    level.playSound(
                            entity1,
                            pos,
                            SoundEvents.TURTLE_EGG_CRACK,
                            entity1.getSoundSource(),
                            0.7F,
                            0.9F + level.random.nextFloat() * 0.2F);
                    level.addFreshEntity(entity1);
                }
            }
        } else {
            entity.hatchingTime = 0;
        }
    }

    private void markUpdated() {
        this.setChanged();
        this.getLevel().sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Nullable
    @Override
    public Packet<ClientGamePacketListener> getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        return saveWithoutMetadata();
    }

    @Override
    public void load(@NotNull CompoundTag pTag) {
        super.load(pTag);
        this.egg = ItemStack.of(pTag.getCompound("EggItem"));
        this.hatchingTime = pTag.getInt("HatchingTime");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.put("EggItem", this.egg.save(new CompoundTag()));
        pTag.putInt("HatchingTime", this.hatchingTime);
    }

    public void setEgg(ItemStack stack) {
        this.egg = stack;
        setChanged();
    }

    public ItemStack getEgg() {
        return egg;
    }

    public int getHatchingTime() {
        return hatchingTime;
    }
}
