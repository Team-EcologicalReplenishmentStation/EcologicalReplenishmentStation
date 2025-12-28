package cn.aurorian.ers.entity.creatures.magnidiscumyzonsarcopterus.ai;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.magnidiscumyzonsarcopterus.MagnidiscumyzonSarcopterusEntity;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;

public class SarcopterusEatGoal extends Goal {
    private final MagnidiscumyzonSarcopterusEntity entity;
    public SarcopterusEatGoal(MagnidiscumyzonSarcopterusEntity entity) {
        this.entity = entity;
    }

    private static final ResourceLocation LOOT_TABLE = EcologicalReplenishmentStation.prefix("entities/magnidiscumyzon_sarcopterus_reward");

    @Override
    public void start() {
        this.entity.setEating(true);
        tickCount = this.entity.getRandom().nextIntBetweenInclusive(100,200);
    }

    private int tickCount = 0;

    @Override
    public boolean canUse() {
        if (this.entity.isVehicle() || !this.entity.isInWater() || !this.entity.onGround()) {
            return false;
        } else {
            if (this.entity.getNoActionTime() >= 100) {
                return false;
            }

            return this.entity.getRandom().nextInt(reducedTickDelay(100)) == 0;
        }
    }

    @Override
    public void tick() {
        tickCount--;
        BlockParticleOption particleOptions = new BlockParticleOption(ParticleTypes.BLOCK, this.entity.getBlockStateOn());
        if (this.entity.getRandom().nextFloat() < 0.3f && this.entity.level() instanceof ServerLevel serverLevel) {
            serverLevel.sendParticles(
                    particleOptions,
                    this.entity.getX(),
                    this.entity.getY(),
                    this.entity.getZ(),
                    20,
                    0.3,
                    0.3,
                    0.3,
                    0.5
            );
        }
    }

    @Override
    public boolean canContinueToUse() {
        return tickCount > 0;
    }

    @Override
    public void stop() {
        if(this.entity.getRandom().nextFloat() < 0.1f){
            LootParams params = new LootParams.Builder((ServerLevel)this.entity.level())
                    .withParameter(LootContextParams.ORIGIN, this.entity.position())
                    .withParameter(LootContextParams.THIS_ENTITY, this.entity)
                    .create(LootContextParamSets.GIFT);
            LootTable lootTable = this.entity.level().getServer().getLootData().getLootTable(LOOT_TABLE);

            for (ItemStack stack : lootTable.getRandomItems(params)) {
                this.entity.level().addFreshEntity(new ItemEntity(this.entity.level(),entity.getX(),entity.getY(),entity.getZ(), stack));
            }
        }
      this.entity.setEating(false);
    }
}
