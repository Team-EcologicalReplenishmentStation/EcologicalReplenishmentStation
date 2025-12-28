package cn.aurorian.ers.entity.creatures.terridensaurussaevus;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsMobEffects;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

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

        if(attack.animatorTick == AttackType.SAEVUS_ATTACK.getAnimationLength() - 18)
        {
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);
            damage = mount.checkEquipment(ErsItems.BULLY_STICK.get()) ? damage * 1.4f : damage;

            Player player = (Player) mount.getControllingPassenger();
            if(player == null)
                return;
            double range = 3 * mount.getRenderSize();

            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y;
            double z = mount.getZ() + foodPos.z;

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
                        living.addEffect(new MobEffectInstance(ErsMobEffects.FRACTURE.get(),600));
                    ErsBleedingEffect.giveBleedingEffect(living,2);
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float)damage);
                    mount.setBloody(true);
                }
            }
        }
    }

    public void executeSaevusRoar(MobAttack attack, TerridensaurusSaevusEntity mount) {
        if(attack.animatorTick == AttackType.SAEVUS_ROAR.getAnimationLength() - 1) {
           mount.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 600, 0));
        }
    }

    public void executeSaevusStrike(MobAttack attack, TerridensaurusSaevusEntity mount){
        if(attack.animatorTick == AttackType.SAEVUS_STRIKE.getAnimationLength() - 29) {
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) * 1.48f;
            damage = mount.checkEquipment(ErsItems.BULLY_STICK.get()) ? damage * 1.4f : damage;

            Player player = (Player) mount.getControllingPassenger();
            if(player == null)
                return;
            double range = 5 * mount.getRenderSize();

            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y;
            double z = mount.getZ() + foodPos.z;

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
                        living.addEffect(new MobEffectInstance(ErsMobEffects.FRACTURE.get(),600));
                    ErsBleedingEffect.giveBleedingEffect(living,2);
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float)damage);
                    mount.setBloody(true);
                }
            }
        }

        if(attack.animatorTick == AttackType.SAEVUS_STRIKE.getAnimationLength()) {
            attack.turningAttackDirection = mount.getLookAngle().normalize();
            if(mount.isElite()){
                mount.setStamina(mount.getStamina() - 15f);
            }
        }

        if(attack.animatorTick < AttackType.SAEVUS_STRIKE.getAnimationLength() - 6 && attack.animatorTick > AttackType.SAEVUS_STRIKE.getAnimationLength() - 30) {
            mount.setRiddenSpeed((float) (mount.getAttributeValue(Attributes.MOVEMENT_SPEED) * 3f));
            mount.setSprinting(true);
        }else {
            mount.setSprinting(false);
        }
    }

    public void executeSaevusTurningAttack(MobAttack attack, TerridensaurusSaevusEntity mount) {
        if(attack.animatorTick == AttackType.SAEVUS_ATTACK_TURN.getAnimationLength() - 14) {
            Player player = (Player) mount.getControllingPassenger();
            Vec3 lookVec;
            lookVec = Objects.requireNonNullElse(player, mount).getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.6 * mount.getScale();
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
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) * 0.37;
            for (Entity target : entities) {
                if (target.equals(mount.getControllingPassenger()))
                    continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);

                    livingEntity.addDeltaMovement(
                            livingEntity.getDeltaMovement().add(
                                    lookVec.x * 2,
                                    0.5,
                                    lookVec.z * 2
                            )
                    );

                    if(livingEntity instanceof ErsTamableVehicle<?> tamable){
                        if(!tamable.isAlive())
                            return;
                        if(tamable.isBaby() || (tamable.doAgeTick() && tamable.getAgeInDays() < 20)){
                            livingEntity.addDeltaMovement(
                                    livingEntity.getDeltaMovement().add(
                                            lookVec.x * 2,
                                            1,
                                            lookVec.z * 2
                                    )
                            );
                            return;
                        }

                        double volume = mount.getBoundingBox().getXsize() * mount.getBoundingBox().getYsize() *
                                mount.getBoundingBox().getZsize();
                        double targetVolume = tamable.getBoundingBox().getXsize() * tamable.getBoundingBox().getYsize() *
                                tamable.getBoundingBox().getZsize();
                        if(targetVolume < volume * 1.3){
                            if(ErsUtils.calculateFallDirection(mount, tamable))
                                tamable.startAttack(AttackType.KNOCK_DOWN_RIGHT);
                            else
                                tamable.startAttack(AttackType.KNOCK_DOWN_LEFT);
                        }
                    }
                }
            }
        }

        if(attack.animatorTick == AttackType.SAEVUS_ATTACK_TURN.getAnimationLength()){
            Player player = (Player) mount.getControllingPassenger();
            attack.turningAttackDirection = Objects.requireNonNullElse(player, mount).getLookAngle().normalize();

            if(Objects.requireNonNullElse(player, mount).getYHeadRot() > mount.yBodyRot){
                mount.triggerAnim("attack","right_attack_turn");
            }else {
                mount.triggerAnim("attack","left_attack_turn");
            }
            if(mount.isElite()){
                mount.setStamina(mount.getStamina() - 15f);
            }
        }

        if(attack.animatorTick == 1){
            mount.setYBodyRot(ErsUtils.yRotationFromDirection(attack.turningAttackDirection));
        }
    }
}
