package cn.aurorian.ers.block.be;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.init.ErsBlockEntities;
import cn.aurorian.ers.init.ErsEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoBlockEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.util.GeckoLibUtil;

public class SwampDragonArtificialNestBlockEntity extends BlockEntity implements GeoBlockEntity {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public SwampDragonArtificialNestBlockEntity(BlockPos pPos, BlockState pBlockState) {
        super(ErsBlockEntities.SWAMP_DRAGON_ARTIFICIAL_NEST_BLOCK_ENTITY.get(), pPos, pBlockState);
    }

    boolean hasEgg;
    int hatchingTime;
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {}

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }

    public static void tick(Level level, BlockPos pos, BlockState blockState, SwampDragonArtificialNestBlockEntity entity) {
        if(level.isClientSide)
            return;

        if(entity.hasEgg) {
            entity.hatchingTime++;
            entity.setChanged();
            if(entity.hatchingTime % 20 == 0)
                entity.markUpdated();
            if(entity.hatchingTime >= 48000) {
                entity.hatchingTime = 0;
                entity.hasEgg = false;

                entity.markUpdated();
                DentisaurusLongirostrisEntity entity1 = ErsEntities.DENTISAURUS_LONGIROSTRIS.get().create(level);
                if(entity1 != null){
                    if(level.random.nextFloat() < 0.05f){
                        entity1.setCanBeElite(true);
                    }
                    if(level.random.nextFloat() < 0.1f) {
                        entity1.setVariant(DentisaurusLongirostrisEntity.Variant.getRareSpawnVariant(level.random));
                    }else {
                        entity1.setVariant(DentisaurusLongirostrisEntity.Variant.getCommonSpawnVariant(level.random));
                    }
                    entity1.setAgeInTicks(0);
                    entity1.updateFromAgeServer();
                    entity1.moveTo((double)pos.getX() + 0.3, pos.getY() + 1, (double)pos.getZ() + 0.3, 0.0F, 0.0F);
                    level.playSound(entity1,pos, SoundEvents.TURTLE_EGG_CRACK, entity1.getSoundSource(), 0.7F, 0.9F + level.random.nextFloat() * 0.2F);
                    level.addFreshEntity(entity1);
                }
            }
        }else{
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
        this.hasEgg = pTag.getBoolean("HasEgg");
        this.hatchingTime = pTag.getInt("HatchingTime");
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag pTag) {
        super.saveAdditional(pTag);
        pTag.putBoolean("HasEgg", this.hasEgg);
        pTag.putInt("HatchingTime", this.hatchingTime);
    }

    public boolean hasEgg() {
        return hasEgg;
    }

    public void setHasEgg(boolean hasEgg) {
        this.hasEgg = hasEgg;
        setChanged();
    }

    public int getHatchingTime() {
        return hatchingTime;
    }
}
