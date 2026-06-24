package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.ai;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import java.util.EnumSet;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;

public class AquicornisDinosauriformisForageGoal extends Goal {
    private final AquicornisDinosauriformisEntity dinosaur;
    private final int searchRange;
    private final int digCooldownTicks;

    private BlockPos targetPos;
    private int digProgress;
    private static final int DIG_DURATION = 40;

    private static final ResourceLocation LOOT_TABLE = EcologicalReplenishmentStation.prefix("dinosauriformis/swamp");

    private static final ResourceLocation LOOT_TABLE_SAVANA =
            EcologicalReplenishmentStation.prefix("dinosauriformis/savana");

    public AquicornisDinosauriformisForageGoal(AquicornisDinosauriformisEntity dinosaur) {
        this.dinosaur = dinosaur;
        this.searchRange = 16;
        this.digCooldownTicks = 2400; // 120 seconds = 2 minutes
        this.setFlags(EnumSet.of(Flag.MOVE, Flag.LOOK));
    }

    @Override
    public boolean canUse() {
        if (!dinosaur.isTame() || dinosaur.getControllingPassenger() != null) {
            return false;
        }

        if (!isInCorrectBiome()) {
            return false;
        }

        if (!dinosaur.isInLove()) {
            return false;
        }

        // Check if cooldown has passed
        int lastDigTime = dinosaur.getLastDigTime();
        if (lastDigTime > 0 && dinosaur.tickCount - lastDigTime < digCooldownTicks) {
            return false;
        }

        // Search for grass blocks
        targetPos = findNearbyGrassBlock();
        return targetPos != null;
    }

    @Override
    public boolean canContinueToUse() {
        return targetPos != null && dinosaur.distanceToSqr(Vec3.atCenterOf(targetPos)) < searchRange * searchRange;
    }

    @Override
    public void start() {
        digProgress = 0;
    }

    @Override
    public void tick() {
        if (targetPos == null) return;

        Level level = dinosaur.level();
        double distanceSqr = dinosaur.distanceToSqr(Vec3.atCenterOf(targetPos));

        if (distanceSqr > 4.0) {
            // Move towards target
            dinosaur.getNavigation().moveTo(targetPos.getX() + 0.5, targetPos.getY(), targetPos.getZ() + 0.5, 1.0);
        } else {
            // At target, start digging
            dinosaur.getLookControl().setLookAt(targetPos.getX() + 0.5, targetPos.getY() + 1, targetPos.getZ() + 0.5);
            digProgress++;

            if (digProgress == 1) {
                // Trigger dig animation
                dinosaur.triggerAnim("attack", "dig");
            }

            if (digProgress >= DIG_DURATION) {
                // Complete the dig
                performDig(level, targetPos);
                dinosaur.setLastDigTime(dinosaur.tickCount);
                this.stop();
            }
        }
    }

    @Override
    public void stop() {
        targetPos = null;
        digProgress = 0;
        dinosaur.getNavigation().stop();
    }

    private boolean isInCorrectBiome() {
        BlockPos pos = dinosaur.blockPosition();
        var biomeHolder = dinosaur.level().getBiome(pos);

        return biomeHolder.is(Biomes.SWAMP) || biomeHolder.is(Biomes.MANGROVE_SWAMP) || biomeHolder.is(Biomes.SAVANNA);
    }

    private BlockPos findNearbyGrassBlock() {
        BlockPos centerPos = dinosaur.blockPosition();
        Level level = dinosaur.level();

        for (int x = centerPos.getX() - searchRange; x <= centerPos.getX() + searchRange; x++) {
            for (int z = centerPos.getZ() - searchRange; z <= centerPos.getZ() + searchRange; z++) {
                for (int y = centerPos.getY() - 2; y <= centerPos.getY() + 2; y++) {
                    BlockPos pos = new BlockPos(x, y, z);
                    BlockState state = level.getBlockState(pos);

                    if (isGrassBlock(state)) {
                        return pos;
                    }
                }
            }
        }

        return null;
    }

    private boolean isGrassBlock(BlockState state) {
        return state.is(Blocks.GRASS_BLOCK);
    }

    private void performDig(Level level, BlockPos pos) {
        if (level.isClientSide) return;

        // Determine which loot table to use based on biome
        ResourceLocation lootTableId = getLootTableId();

        // Get loot table and drop items
        ServerLevel serverLevel = (ServerLevel) level;
        LootTable lootTable = serverLevel.getServer().getLootData().getLootTable(lootTableId);

        // Create loot params for the block
        LootParams lootParams = new LootParams.Builder(serverLevel)
                .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                .withParameter(LootContextParams.THIS_ENTITY, this.dinosaur)
                .create(LootContextParamSets.GIFT);

        lootTable.getRandomItems(
                lootParams,
                itemStack -> level.addFreshEntity(
                        new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, itemStack)));
    }

    private ResourceLocation getLootTableId() {
        BlockPos pos = dinosaur.blockPosition();
        var biomeHolder = dinosaur.level().getBiome(pos);

        if (biomeHolder.is(Biomes.SWAMP) || biomeHolder.is(Biomes.MANGROVE_SWAMP)) {
            return LOOT_TABLE;
        } else if (biomeHolder.is(Biomes.SAVANNA)) {
            return LOOT_TABLE_SAVANA;
        }

        return LOOT_TABLE;
    }
}
