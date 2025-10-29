package cn.aurorian.ers.entity.creatures.terridensaurussaevus;

import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.init.ErsMobEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import java.util.List;
import java.util.Objects;

public class SaevusAttackExecutor {
    private static final SaevusAttackExecutor INSTANCE = new SaevusAttackExecutor();
    public static SaevusAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeSaevusAttack(MobAttack attack, TerridensaurusSaevusEntity mount) {
        if(!mount.isVehicle())
            return;

        if(attack.animatorTick == AttackType.SAEVUS_ATTACK.getAnimationLength() - 14)
        {
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);

            Player player = (Player) mount.getControllingPassenger();
            if(player == null)
                return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.7;
            double x = pos.x + lookVec.x * range;
            double y = pos.y + lookVec.y;
            double z = pos.z + lookVec.z * range;

            List<Entity> entities = mount.level().getEntities(mount,
                    new AABB(
                            x - range, y - range, z - range,
                            x + range, y + range, z + range
                    ),
                    entity -> entity instanceof LivingEntity && entity != mount && entity != player
            );

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    if(mount.getRandom().nextFloat() < 0.33f)
                        living.addEffect(new MobEffectInstance(ErsMobEffects.FRACTURE.get(),30));
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float)damage);
                }
            }
        }
    }

    public void executeSaevusTurningAttack(MobAttack attack, TerridensaurusSaevusEntity mount) {
        if(attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength() - 14) {
            Player player = (Player) mount.getControllingPassenger();
            if(player == null)
                return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.4;
            double startX = pos.x + lookVec.x * range;
            double startY = pos.y + lookVec.y;
            double startZ = pos.z + lookVec.z * range;

            AABB attackBox = new AABB(
                    startX - range, startY - range, startZ - range,
                    startX + range,
                    startY + range,
                    startZ + range
            );

            List<Entity> entities = mount.level().getEntities(mount, attackBox,
                    entity -> entity instanceof LivingEntity);
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) - 20;
            for (Entity target : entities) {
                if (target.equals(mount.getControllingPassenger()))
                    continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                }
            }
        }

        if(attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength()){
            Player player = (Player) mount.getControllingPassenger();
            attack.turningAttackDirection = Objects.requireNonNullElse(player, mount).getLookAngle().normalize();

            if(Objects.requireNonNullElse(player, mount).getYRot() > mount.yBodyRot){
                mount.triggerAnim("attack","right_attack_turn");
            }else {
                mount.triggerAnim("attack","left_attack_turn");
            }
        }
    }
}
