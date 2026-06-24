package cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.init.ErsItems;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.phys.AABB;
import org.joml.Vector3f;

public class AquicornisDinosauriformisAttackExecutor {
    private static final AquicornisDinosauriformisAttackExecutor INSTANCE =
            new AquicornisDinosauriformisAttackExecutor();

    public static AquicornisDinosauriformisAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeAttack(MobAttack attack, AquicornisDinosauriformisEntity mount) {
        if (!mount.isVehicle()) return;

        if (attack.animatorTick == AttackType.DINOSAURIFORMIS_ATTACK.getAnimationLength() - 9) {
            double range = 1.3 * mount.getRenderSize();
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
                }
            }
        }
    }
}
