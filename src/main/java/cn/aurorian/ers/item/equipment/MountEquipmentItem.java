package cn.aurorian.ers.item.equipment;

import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class MountEquipmentItem extends Item implements MountEquipment {

    public MountEquipmentItem(Properties properties) {
        super(properties);
    }

    @Override
    public void appendHoverText(
            @NotNull ItemStack pStack,
            @Nullable Level pLevel,
            @NotNull List<Component> pTooltipComponents,
            @NotNull TooltipFlag pIsAdvanced) {
        super.appendHoverText(pStack, pLevel, pTooltipComponents, pIsAdvanced);
        var key = ForgeRegistries.ITEMS.getKey(this);
        if (key != null) {
            pTooltipComponents.add(Component.translatable("tooltip." + key.getNamespace() + "." + key.getPath())
                    .withStyle(ChatFormatting.GRAY));
        }
    }
}
