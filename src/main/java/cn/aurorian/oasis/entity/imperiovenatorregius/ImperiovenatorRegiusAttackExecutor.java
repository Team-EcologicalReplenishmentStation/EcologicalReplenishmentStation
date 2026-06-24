package cn.aurorian.oasis.entity.imperiovenatorregius;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.ErsUtils;
import java.util.List;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

public class ImperiovenatorRegiusAttackExecutor {
    private static final ImperiovenatorRegiusAttackExecutor INSTANCE = new ImperiovenatorRegiusAttackExecutor();

    private ImperiovenatorRegiusAttackExecutor() {}

    public static ImperiovenatorRegiusAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeAttack(MobAttack attack, ErsTamableVehicle<?> mount) {
        if (!mount.isVehicle()) return;

        if (attack.animatorTick == AttackType.TUBUNASUS_ATTACK.getAnimationLength() - 9) {
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);
            damage = mount.checkEquipment(ErsItems.BULLY_STICK.get()) ? damage * 1.4f : damage;

            damage = ((ImperiovenatorRegiusEntity) mount).isSailUp() ? damage : damage * 1.1;

            if (mount.isBaby()) damage *= 0.5;

            Player player = (Player) mount.getControllingPassenger();
            if (player == null) return;

            double range = 2.4;

            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y;
            double z = mount.getZ() + foodPos.z;

            List<Entity> entities = mount.level()
                    .getEntities(
                            mount,
                            new AABB(x - range, y - range, z - range, x + range, y + range, z + range),
                            entity -> entity instanceof LivingEntity && entity != mount && entity != player);

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                    mount.setBloody(true);
                }
            }
        }
    }

    public void executeTurningAttack(MobAttack attack, ErsTamableVehicle<?> mount) {
        if (attack.animatorTick == AttackType.REGIUS_ATTACK_TURN.getAnimationLength() - 10) {
            Player player = (Player) mount.getControllingPassenger();
            Vec3 lookVec;
            if (player == null) lookVec = mount.getLookAngle();
            else lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 2.6;
            double startX = pos.x + lookVec.x * range;
            double startY = pos.y + lookVec.y;
            double startZ = pos.z + lookVec.z * range;

            AABB attackBox =
                    new AABB(startX - range, startY, startZ - range, startX + range, startY + range, startZ + range);

            List<Entity> entities =
                    mount.level().getEntities(mount, attackBox, entity -> entity instanceof LivingEntity);

            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) * 2.5;
            damage = mount.checkEquipment(ErsItems.SCRATCHING_BOARD.get()) ? damage * 1.4f : damage;

            damage = ((ImperiovenatorRegiusEntity) mount).isSailUp() ? damage : damage * 1.1;

            for (Entity target : entities) {
                if (mount.getPassengers().contains(target)) continue;
                if (target instanceof ImperiovenatorRegiusEntity && mount.isAlliedTo(target)) continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                }
            }
        }
    }

    public void executeRoar(MobAttack attack, ErsTamableVehicle<?> mount) {
        ((ImperiovenatorRegiusEntity) mount).setRoarTime(130);
    }

    public void executeJumpAttack(MobAttack attack, ErsTamableVehicle<?> mount) {
        if (attack.animatorTick == AttackType.REGIUS_JUMP_ATTACK.getAnimationLength() - 5) {
            Vec3 pos = mount.position();
            double range = 4.2;
            double startX = pos.x;
            double startY = pos.y;
            double startZ = pos.z;

            AABB attackBox = new AABB(
                    startX - range, startY - range, startZ - range, startX + range, startY + range, startZ + range);

            List<Entity> entities = mount.level()
                    .getEntities(
                            mount,
                            attackBox,
                            entity -> entity instanceof LivingEntity
                                    && !mount.isAlliedTo(entity)
                                    && mount.getOwner() != entity
                                    && !(entity.getVehicle() instanceof ErsTamableVehicle<?> vehicle
                                            && vehicle.checkEquipment(ErsItems.RIDING_GUIDE.get())));

            if (entities.isEmpty()) {
                attack.animatorTick = 1;
                return;
            }

            attack.judgementTarget = (LivingEntity) entities.get(0);
            double volume = attack.judgementTarget.getBoundingBox().getXsize()
                    * attack.judgementTarget.getBoundingBox().getYsize()
                    * attack.judgementTarget.getBoundingBox().getZsize();

            if (volume > 18) {
                if (attack.judgementTarget.getPassengers().stream()
                        .anyMatch(entity -> entity instanceof ImperiovenatorRegiusEntity)) return;
                mount.startRiding(attack.judgementTarget, true);
                ((ImperiovenatorRegiusEntity) mount).setHanging(false);
            } else {
                attack.judgementTarget.startRiding(mount, true);
                ((ImperiovenatorRegiusEntity) mount).setHanging(volume > 4);
                attack.judgementTarget.setDeltaMovement(0, 0, 0);
                attack.judgementTarget.setPose(Pose.DYING);
                attack.judgementTarget.setXRot(90);
                attack.judgementTarget.setYRot(90);
            }
        } else {
            if (attack.judgementTarget != null && attack.judgementTarget.isAlive()) {
                if (mount.isInWater()) {
                    mount.stopRiding();
                    attack.judgementTarget.stopRiding();
                    ((ImperiovenatorRegiusEntity) mount).setHanging(false);
                    attack.animatorTick = 1;
                    return;
                }

                if (attack.judgementTarget instanceof Player player) {
                    player.startRiding(mount);
                }
                if (mount.tickCount % 20 == 0) {
                    attack.judgementTarget.hurt(mount.damageSources().mobAttack(mount), 10);
                    mount.setBloody(true);
                    ErsBleedingEffect.giveBleedingEffect(attack.judgementTarget, 2);
                    mount.setStamina(mount.getStamina() - 4f);
                    if (mount.tickCount % 40 == 0 && attack.judgementTarget instanceof ErsTamable<?> tamable) {
                        tamable.triggerAnim("extra", "struggle");
                    }
                }

                if (mount.getStamina() > 0) {
                    attack.animatorTick++;
                } else {
                    attack.animatorTick = 1;
                    double volume = attack.judgementTarget.getBbHeight()
                            * attack.judgementTarget.getBbWidth()
                            * attack.judgementTarget.getBbWidth();
                    if (volume > 18) {
                        mount.stopRiding();
                        ((ImperiovenatorRegiusEntity) mount).setHanging(false);
                        if (ErsUtils.calculateFallDirection(mount, attack.judgementTarget))
                            mount.startAttack(AttackType.KNOCK_DOWN_LEFT);
                        else mount.startAttack(AttackType.KNOCK_DOWN_RIGHT);
                    } else {
                        attack.judgementTarget.stopRiding();
                        ((ImperiovenatorRegiusEntity) mount).setHanging(false);
                    }
                    attack.judgementTarget = null;
                }

            } else {
                ((ImperiovenatorRegiusEntity) mount).setHanging(false);
            }
        }
    }
}
