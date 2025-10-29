package cn.aurorian.oasis.entity.tubunasusdurovela;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobAttack;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;

public class TubunasusDurovelaAttackExecutor {
    private static final TubunasusDurovelaAttackExecutor INSTANCE = new TubunasusDurovelaAttackExecutor();

    public static TubunasusDurovelaAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeTubunasusAttack(MobAttack attack, TubunasusDurovelaEntity mount) {
        if(!mount.isVehicle())
            return;

        if(attack.animatorTick == AttackType.TUBUNASUS_ATTACK.getAnimationLength() - 9)
        {
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);

            if(mount.isBaby())
                damage *= 0.5;

            Player player = (Player) mount.getControllingPassenger();
            if(player == null)
                return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.2;
            double x = pos.x + lookVec.x * range;
            double y = pos.y + lookVec.y;
            double z = pos.z + lookVec.z * range;

            List<Entity> entities = mount.level().getEntities(mount,
                    new AABB(
                            x - range, y - range, z - range,
                            x + range * 0.7, y + range, z + range * 0.7
                    ),
                    entity -> entity instanceof LivingEntity && entity != mount && entity != player
            );

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float)damage);
                }
            }
        }
    }

    public void executeTubunasusTurningAttack(MobAttack attack, TubunasusDurovelaEntity mount) {
        if(attack.animatorTick == AttackType.TUBUNASUS_ATTACK_TURN.getAnimationLength() - 10 || attack.animatorTick == AttackType.TUBUNASUS_ATTACK_TURN.getAnimationLength() - 20) {
            Player player = (Player) mount.getControllingPassenger();
            if(player == null)
                return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.6;
            double startX = pos.x + lookVec.x * range;
            double startY = pos.y + lookVec.y;
            double startZ = pos.z + lookVec.z * range;

            AABB attackBox = new AABB(
                    startX - range, startY, startZ - range,
                    startX + range,
                    startY + range,
                    startZ + range
            );

            List<Entity> entities = mount.level().getEntities(mount, attackBox,
                    entity -> entity instanceof LivingEntity);

            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) + 27;

            for (Entity target : entities) {
                if (target.getVehicle() != null && target.getVehicle().equals(mount))
                    continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                }
            }
        }
    }
}
