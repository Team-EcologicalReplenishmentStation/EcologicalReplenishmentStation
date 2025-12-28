package cn.aurorian.ers.item;

import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.gameevent.GameEvent;
import org.jetbrains.annotations.NotNull;

import javax.annotation.Nullable;
import java.util.UUID;

public class SoulFlute extends Item {
    public SoulFlute(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level pLevel, @NotNull Player player, @NotNull InteractionHand pUsedHand) {
        if(!pLevel.isClientSide()){
            ItemStack itemStack = player.getItemInHand(pUsedHand);

            //TODO: 下个版本删除
            if(itemStack.getOrCreateTag().contains("UUID")){
                itemStack.getOrCreateTag().putString("StringUUID", itemStack.getOrCreateTag().getString("UUID"));
                itemStack.getOrCreateTag().remove("UUID");
            }

            if(itemStack.getOrCreateTag().contains("StringUUID")){
                player.getCooldowns().addCooldown(ErsItems.SOUL_FLUTE.get(), 1200);

                String uuidStr = itemStack.getOrCreateTag().getString("StringUUID");
                Entity tamable = ((ServerLevel)pLevel).getEntity(UUID.fromString(uuidStr));
                if(tamable != null){
                    tamable.moveTo(player.getX(), player.getY(), player.getZ(), tamable.getYRot(), tamable.getXRot());
                    player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
                }else {
                    this.checkExtraContent(player, pLevel, itemStack, player.getOnPos());
                    player.awardStat(Stats.ITEM_USED.get(this));
                }
                return InteractionResultHolder.success(itemStack);
            }

        }
        return super.use(pLevel, player, pUsedHand);
    }

    @Override
    public @NotNull InteractionResult interactLivingEntity(@NotNull ItemStack itemStack, @NotNull Player player, @NotNull LivingEntity pInteractionTarget, @NotNull InteractionHand pUsedHand) {
        if(!itemStack.getOrCreateTag().contains("StringUUID") && pInteractionTarget instanceof ErsTamable<?> tamable && tamable.isOwnedBy(player)){
            ItemStack newStack = new ItemStack(ErsItems.SOUL_FLUTE.get());
            newStack.getOrCreateTag().putString("StringUUID", tamable.getStringUUID());
            flutePickup(newStack, pInteractionTarget);
            player.setItemInHand(pUsedHand,newStack);
            player.playSound(SoundEvents.ENCHANTMENT_TABLE_USE);
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static <T extends LivingEntity> void flutePickup(ItemStack itemstack, T pEntity) {
        if (pEntity.isAlive()) {
            pEntity.addAdditionalSaveData(itemstack.getOrCreateTag());
            itemstack.getOrCreateTag().putString("EntityType", BuiltInRegistries.ENTITY_TYPE.getKey(pEntity.getType()).toString());
            pEntity.discard();
        }
    }

    public void checkExtraContent(@Nullable Player pPlayer, Level pLevel, ItemStack pContainerStack, BlockPos pPos) {
        if (pLevel instanceof ServerLevel) {
            this.spawn((ServerLevel)pLevel, pContainerStack, pPos);
            pLevel.gameEvent(pPlayer, GameEvent.ENTITY_PLACE, pPos);
        }
    }

    private void spawn(ServerLevel pServerLevel, ItemStack stack, BlockPos pPos) {
        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(ResourceLocation.parse(
                stack.getTag().getString("EntityType")));
        Entity entity = type.spawn(pServerLevel, stack, null, pPos, MobSpawnType.BUCKET, true, false);
        if (entity instanceof ErsTamable<?> tamable) {
            tamable.readAdditionalSaveData(stack.getOrCreateTag());
            tamable.setSoul(true);
        }

    }
}
