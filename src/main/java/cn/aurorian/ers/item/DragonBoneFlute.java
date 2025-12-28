package cn.aurorian.ers.item;

import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class DragonBoneFlute extends Item {
    public DragonBoneFlute(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player player, @NotNull InteractionHand pUsedHand) {
        if(!pLevel.isClientSide()){
            ItemStack itemStack = player.getItemInHand(pUsedHand);
            if(itemStack.getOrCreateTag().contains("uuid")){
                player.getCooldowns().addCooldown(ErsItems.DRAGON_BONE_FLUTE.get(), 200);

                String uuidStr = itemStack.getOrCreateTag().getString("uuid");
                Entity tamable = ((ServerLevel)pLevel).getEntity(UUID.fromString(uuidStr));
                if(tamable != null){
                    tamable.moveTo(player.getX(), player.getY(), player.getZ(), tamable.getYRot(), tamable.getXRot());
                    player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
                }else {
                    String trackPos = itemStack.getOrCreateTag().getString("TrackPosition");
                    player.displayClientMessage(Component.translatable("ers.tamable.missing",trackPos),true);
                }
                return InteractionResultHolder.success(itemStack);
            }
        }
        return super.use(pLevel, player, pUsedHand);
    }

    @Override
    public @NotNull UseAnim getUseAnimation(@NotNull ItemStack pStack) {
        return UseAnim.BOW;
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if(pInteractionTarget instanceof ErsTamable<?> tamable && tamable.isOwnedBy(player)){
            if(tamable.isSoul())
                return InteractionResult.FAIL;
            ItemStack newStack = new ItemStack(ErsItems.DRAGON_BONE_FLUTE.get());
            newStack.getOrCreateTag().putString("uuid", tamable.getStringUUID());
            player.setItemInHand(pUsedHand,newStack);
            player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    @Override
    public void inventoryTick(@NotNull ItemStack itemStack, @NotNull Level pLevel, @NotNull Entity pEntity, int pSlotId, boolean pIsSelected) {
       if(!pIsSelected && !pLevel.isClientSide && pEntity instanceof Player){
           if(itemStack.getOrCreateTag().contains("uuid")){
               String uuidStr = itemStack.getOrCreateTag().getString("uuid");
               Entity tamable = ((ServerLevel)pLevel).getEntity(UUID.fromString(uuidStr));
               if(tamable != null){
                   itemStack.getOrCreateTag().putString("TrackPosition", String.valueOf(tamable.getOnPos()));
               }
           }
       }

    }
}
