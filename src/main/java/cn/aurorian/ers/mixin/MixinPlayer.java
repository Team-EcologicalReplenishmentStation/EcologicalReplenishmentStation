package cn.aurorian.ers.mixin;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.joml.Math;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(Player.class)
public class MixinPlayer {
    @Redirect(method="drop(Lnet/minecraft/world/item/ItemStack;ZZ)Lnet/minecraft/world/entity/item/ItemEntity;",at = @At(value = "NEW", target = "(Lnet/minecraft/world/level/Level;DDDLnet/minecraft/world/item/ItemStack;)Lnet/minecraft/world/entity/item/ItemEntity;"))
    private ItemEntity drop(Level pLevel, double pPosX, double pPosY, double pPosZ, ItemStack pItemStack){
        Player player = (Player) (Object) this;
        if(player.isPassenger() && player.getVehicle() instanceof DentisaurusLongirostrisEntity sotek){
            float forward = sotek.getRenderSize() * 2.7f; // 向前偏移的距离
            // 将实体的旋转角度转换为弧度
            float yRot = Math.toRadians(sotek.getYRot());
            // 计算X和Z方向的偏移
            float offsetX = Math.sin(-yRot) * forward;
            float offsetZ = Math.cos(yRot) * forward;
            return new ItemEntity(pLevel,
                        pPosX + offsetX,
                        pPosY + 0.5,
                        pPosZ + offsetZ, pItemStack);
        }
        return new ItemEntity(pLevel, pPosX, pPosY, pPosZ, pItemStack);
    }
}
