package cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
import java.util.List;
import java.util.Objects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class EosuchosaurusAntiquusAttackExecutor {
    private static final EosuchosaurusAntiquusAttackExecutor INSTANCE = new EosuchosaurusAntiquusAttackExecutor();

    public static EosuchosaurusAntiquusAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeAttack(MobAttack attack, EosuchosaurusAntiquusEntity mount) {
        if (!mount.isVehicle()) return;

        if (attack.animatorTick == AttackType.ANTIQUUS_ATTACK.getAnimationLength() - 9) {
            double range = 1.4 * mount.getRenderSize();
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);
            damage = mount.checkEquipment(ErsItems.BULLY_STICK.get()) ? damage * 1.4 : damage;
            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y;
            double z = mount.getZ() + foodPos.z;

            List<Entity> entities = mount.level()
                    .getEntities(
                            mount,
                            new AABB(x - range, y - range, z - range, x + range * 0.7, y + range, z + range * 0.7),
                            entity -> entity instanceof LivingEntity
                                    && entity != mount
                                    && entity != mount.getControllingPassenger()
                                    && entity != mount.getOwner());

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                    ErsBleedingEffect.giveBleedingEffect(living, 2);
                }
            }
        }
    }

    public void executeTurningAttack(MobAttack attack, EosuchosaurusAntiquusEntity mount) {
        if (attack.animatorTick == AttackType.ANTIQUUS_ATTACK_TURN.getAnimationLength() - 8) {
            Vec3 pos = mount.position();
            double range = 2 * mount.getScale();
            double startX = pos.x;
            double startY = pos.y;
            double startZ = pos.z;

            AABB attackBox = new AABB(
                    startX - range, startY - range, startZ - range, startX + range, startY + range, startZ + range);

            List<Entity> entities =
                    mount.level().getEntities(mount, attackBox, entity -> entity instanceof LivingEntity);
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.2;
            for (Entity target : entities) {
                if (target.equals(mount.getControllingPassenger())) continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                    ErsBleedingEffect.giveBleedingEffect(livingEntity, 2);
                }
            }
        }

        if (attack.animatorTick == AttackType.ANTIQUUS_ATTACK_TURN.getAnimationLength()) {
            Player player = (Player) mount.getControllingPassenger();
            attack.turningAttackDirection =
                    Objects.requireNonNullElse(player, mount).getLookAngle().normalize();

            if (Objects.requireNonNullElse(player, mount).getYHeadRot() > mount.yBodyRot) {
                mount.triggerAnim("attack", "right_attack");
            } else {
                mount.triggerAnim("attack", "left_attack");
            }
        }

        if (attack.animatorTick == 1) {
            mount.setYBodyRot(ErsUtils.yRotationFromDirection(attack.turningAttackDirection));
        }
    }
}
