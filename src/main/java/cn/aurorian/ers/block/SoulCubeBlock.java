package cn.aurorian.ers.block;

import cn.aurorian.ers.init.ErsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.phys.BlockHitResult;
import org.jetbrains.annotations.NotNull;

public class SoulCubeBlock extends Block {
    public SoulCubeBlock(Properties pProperties) {
        super(pProperties);
        this.registerDefaultState(
                this.stateDefinition.any().setValue(FACING, Direction.NORTH).setValue(ACTIVE, false));
    }

    public static final DirectionProperty FACING = HorizontalDirectionalBlock.FACING;
    public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

    @Override
    public @NotNull RenderShape getRenderShape(@NotNull BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(FACING, ACTIVE);
    }

    @Override
    public @NotNull InteractionResult use(
            @NotNull BlockState state,
            @NotNull Level level,
            @NotNull BlockPos pos,
            @NotNull Player player,
            @NotNull InteractionHand pHand,
            @NotNull BlockHitResult pHit) {
        if (player.getMainHandItem().is(Items.TOTEM_OF_UNDYING)) {
            if (!state.getValue(SoulCubeBlock.ACTIVE)) {
                player.getMainHandItem().shrink(1);
                BlockState newState = state.setValue(SoulCubeBlock.ACTIVE, true);
                level.playSound(player, pos, SoundEvents.RESPAWN_ANCHOR_CHARGE, SoundSource.BLOCKS);
                level.setBlockAndUpdate(pos, newState);

                ItemStack gift = new ItemStack(ErsItems.SOUL_CUBE_GIFT.get());
                gift.getOrCreateTag().putInt("X", pos.getX());
                gift.getOrCreateTag().putInt("Y", pos.getY());
                gift.getOrCreateTag().putInt("Z", pos.getZ());

                level.addFreshEntity(new ItemEntity(level, pos.getX(), pos.getY(), pos.getZ(), gift));
                return InteractionResult.SUCCESS;
            }
        }
        return InteractionResult.PASS;
    }
}
