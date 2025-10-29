package cn.aurorian.ers.event;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.joml.Math;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class RiderShootHandler {
    @SubscribeEvent
    public static void onRiderShoot(EntityJoinLevelEvent event) {
        if(event.getEntity() instanceof Projectile projectile){
            if(projectile.getOwner() instanceof Player player){
                if(player.isPassenger() && player.getVehicle() instanceof DentisaurusLongirostrisEntity sotek){
                    float forward = sotek.getRenderSize(); // 向前偏移的距离
                    forward = (projectile instanceof AbstractArrow) ? forward * 2.7f : forward * 1.6f;
                    // 将实体的旋转角度转换为弧度
                    float yRot = Math.toRadians(sotek.getYRot());
                    // 计算X和Z方向的偏移
                    float offsetX = Math.sin(-yRot) * forward;
                    float offsetZ = Math.cos(yRot) * forward;
                    projectile.setPos(projectile.getX() + offsetX, projectile.getY(), projectile.getZ() + offsetZ);
                }
            }
        }
    }
}
