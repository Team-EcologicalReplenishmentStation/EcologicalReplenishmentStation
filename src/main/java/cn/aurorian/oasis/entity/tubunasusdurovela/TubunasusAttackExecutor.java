package cn.aurorian.oasis.entity.tubunasusdurovela;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.oasis.entity.tubunasusclyderotunda.TubunasusClyderotundaEntity;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class TubunasusAttackExecutor {
    private static final TubunasusAttackExecutor INSTANCE = new TubunasusAttackExecutor();

    public static TubunasusAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeAttack(MobAttack attack, ErsTamableVehicle<?> mount) {
        if (!mount.isVehicle()) return;

        if (attack.animatorTick == AttackType.TUBUNASUS_ATTACK.getAnimationLength() - 9) {
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);

            if (mount.isBaby()) damage *= 0.5;

            Player player = (Player) mount.getControllingPassenger();
            if (player == null) return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.2;
            double x = pos.x + lookVec.x * 2;
            double y = pos.y + lookVec.y;
            double z = pos.z + lookVec.z * 2;

            List<Entity> entities = mount.level()
                    .getEntities(
                            mount,
                            new AABB(x - range, y - range, z - range, x + range * 0.7, y + range, z + range * 0.7),
                            entity -> entity instanceof LivingEntity && entity != mount && entity != player);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    if (mount instanceof TubunasusClyderotundaEntity) ErsBleedingEffect.giveBleedingEffect(living, 1);
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                }
            }
        }
    }

    public void executeTurningAttack(MobAttack attack, ErsTamableVehicle<?> mount) {
        if (attack.animatorTick == AttackType.TUBUNASUS_ATTACK_TURN.getAnimationLength() - 10
                || attack.animatorTick == AttackType.TUBUNASUS_ATTACK_TURN.getAnimationLength() - 20) {
            Player player = (Player) mount.getControllingPassenger();
            if (player == null) return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.6;
            double startX = pos.x + lookVec.x * range;
            double startY = pos.y + lookVec.y;
            double startZ = pos.z + lookVec.z * range;

            AABB attackBox =
                    new AABB(startX - range, startY, startZ - range, startX + range, startY + range, startZ + range);

            List<Entity> entities =
                    mount.level().getEntities(mount, attackBox, entity -> entity instanceof LivingEntity);

            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) * 4.375;

            if (mount instanceof TubunasusClyderotundaEntity) {
                damage *= 0.5;
            }

            for (Entity target : entities) {
                if (mount.getPassengers().contains(target)) continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                }
            }

            mount.breakWood();
        }
    }

    public void executeStruggle(MobAttack attack, ErsTamableVehicle<?> mount) {
        if (attack.animatorTick == AttackType.TUBUNASUS_STRUGGLE.getAnimationLength() - 20) {
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);

            if (mount.isBaby()) damage *= 0.5;

            Player player = (Player) mount.getControllingPassenger();
            if (player == null) return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.2;
            double x = pos.x - lookVec.x * 3;
            double y = pos.y - lookVec.y;
            double z = pos.z - lookVec.z * 3;

            List<Entity> entities = mount.level()
                    .getEntities(
                            mount,
                            new AABB(x - range, y - range, z - range, x + range * 0.7, y + range, z + range * 0.7),
                            entity -> entity instanceof LivingEntity && entity != mount && entity != player);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    if (living.getVehicle() != mount)
                        living.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                    if (living instanceof ErsTamableVehicle<?> tamableVehicle) {
                        tamableVehicle.setStamina(tamableVehicle.getStamina() - 20f);
                    }
                }
            }
        }
    }
}
