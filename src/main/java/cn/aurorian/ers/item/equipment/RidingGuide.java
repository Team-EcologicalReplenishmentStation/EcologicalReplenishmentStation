package cn.aurorian.ers.item.equipment;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class RidingGuide extends MountEquipmentItem {
    public RidingGuide(Properties pProperties) {
        super(pProperties.stacksTo(1));
    }

    @Override
    public void tickEquip(ItemStack stack, ErsTamableVehicle<?> entity) {}

    @Override
    public void appendHoverText(
            @NotNull ItemStack pStack,
            @Nullable Level pLevel,
            @NotNull List<Component> pTooltipComponents,
            @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        pTooltipComponents.add(
                Component.translatable("tooltip.ers.riding_guide.patchouli").withStyle(ChatFormatting.DARK_PURPLE));
    }
}
