package cn.aurorian.ers.entity.creatures.dentisauruslongirostris;

import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.AttackType;
import cn.aurorian.ers.entity.MobAttack;
import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsParticleType;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Pose;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.joml.Vector3f;

import java.util.List;
import java.util.Objects;

public class DentisaurusLongirostrisAttackExecutor {
    private static final DentisaurusLongirostrisAttackExecutor INSTANCE = new DentisaurusLongirostrisAttackExecutor();
    public static DentisaurusLongirostrisAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeSwampDragonAttack(MobAttack attack, DentisaurusLongirostrisEntity mount) {
        if(!mount.isVehicle())
            return;

        if(attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK.getAnimationLength() - 9)
        {
            float scale = ErsUtils.calculateRenderSize(mount.getAgeInDays());
            double range = 2.5 * scale;
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);
            damage = checkEquipment(mount, ErsItems.BULLY_STICK.get()) ? damage * 1.4 : damage;
            // 获取FOOD骨骼的位置
            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y;
            double z = mount.getZ() + foodPos.z;

            List<Entity> entities = mount.level().getEntities(mount,
                    new AABB(
                            x - range, y - range, z - range,
                            x + range * 0.7, y + range, z + range * 0.7
                    ),
                    entity -> entity instanceof LivingEntity && entity != mount && entity != mount.getControllingPassenger()
            );

            for (Entity entity : entities) {
                if (entity instanceof LivingEntity living) {
                    living.hurt(mount.level().damageSources().mobAttack(mount), (float)damage);
                    ErsBleedingEffect.giveBleedingEffect(living,4);
                    mount.getEntityData().set(DentisaurusLongirostrisEntity.BLOODY, true);
                }

                if (mount.level() instanceof ServerLevel serverLevel) {
                    serverLevel.sendParticles(
                            ErsParticleType.BLOOD.get(),
                            x,
                            y,
                            z,
                            20,
                            0.3,
                            0.3,
                            0.3,
                            0.5
                    );
                }
            }
        }
    }

    public void executeSwampDragonJudgement(MobAttack attack, DentisaurusLongirostrisEntity mount) {
        if(attack.judgementTarget == null && !attack.triggered)
        {
            attack.triggered = true;
            float scale = ErsUtils.calculateRenderSize(mount.getAgeInDays());
            double range = 6.2 * scale;

            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y;
            double z = mount.getZ() + foodPos.z;

            List<Entity> entities = mount.level().getEntities(mount,
                    new AABB(
                            x - range, y - range, z - range,
                            x + range, y + range, z + range
                    ),
                    entity -> entity instanceof LivingEntity && entity != mount && entity != mount.getControllingPassenger()
            );

            entities.removeIf(entity1 -> entity1.getDimensions(Pose.STANDING).width >= 2
                    || entity1.getDimensions(Pose.STANDING).height >= 3);

            if(!mount.isElite()){
                entities.removeIf(entity1 -> entity1.getDimensions(Pose.STANDING).width >= 1);
            }

            boolean swim = mount.isInWater() && !mount.onGround();

            if (!entities.isEmpty()) {

                LivingEntity closestEntity = null;
                double closestDistance = Double.MAX_VALUE;

                for (Entity entity : entities) {
                    if (entity instanceof LivingEntity) {
                        double distance = mount.distanceToSqr(entity);
                        if (distance < closestDistance) {
                            closestDistance = distance;
                            closestEntity = (LivingEntity) entity;
                        }
                    }
                }

                if (closestEntity != null) {
                    attack.judgementTarget = closestEntity;
                } else {
                    attack.judgementTarget = (LivingEntity) entities.get(mount.level().random.nextInt(entities.size()));
                }

                if(swim){
                    mount.triggerAnim("attack","swim_attack");
                    attack.triggerTime = 40;
                }else if(attack.judgementTarget.getDimensions(Pose.STANDING).height * attack.judgementTarget.getDimensions(Pose.STANDING).width * attack.judgementTarget.getDimensions(Pose.STANDING).width <= 0.648){
                    mount.triggerAnim("attack","attack2-small");
                    attack.triggerTime = 56;
                }else {
                    mount.triggerAnim("attack","attack2-middle");
                    attack.triggerTime = 40;
                }
            }else {
                if(swim){
                    mount.triggerAnim("attack","swim_attack-miss");
                }else {
                    mount.triggerAnim("attack","attack2-miss");
                    attack.triggerTime = 38;
                }
            }

        }

        if(attack.judgementTarget == null){
            if(attack.animatorTick == AttackType.SWAMP_DRAGON_JUDGEMENT.getAnimationLength() - 40)
                attack.animatorTick = 0;
        }



        if(attack.animatorTick >= 0 && attack.judgementTarget != null)
        {
            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y + 0.1;
            double z = mount.getZ() + foodPos.z;
            attack.judgementTarget.setDeltaMovement(0,0,0);
            // 设置实体旋转 - 让实体平躺
            attack.judgementTarget.setXRot(90.0F);  // 90度使实体面朝上平躺
            attack.judgementTarget.setYRot(90.0F);   // 可以根据需要调整水平方向

            attack.judgementTarget.setPos(x,y,z);
            if(attack.animatorTick == AttackType.SWAMP_DRAGON_JUDGEMENT.getAnimationLength() - attack.triggerTime)
            {
                if(attack.judgementTarget instanceof Player player){
                    player.setHealth(0.1f);
                    player.setAbsorptionAmount(0f);
                    player.hurt(mount.level().damageSources().mobAttack(mount), player.getMaxHealth());
                }
                attack.judgementTarget.hurt(mount.level().damageSources().mobAttack(mount), Float.MAX_VALUE);
                mount.getEntityData().set(DentisaurusLongirostrisEntity.BLOODY, true);
                if(mount.level() instanceof ServerLevel serverLevel)
                {
                    mount.heal(10);
                    serverLevel.sendParticles(ErsParticleType.BLOOD.get(), x, y, z, 100, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }
    }

    public void executeSwampDragonSpecialAttack(MobAttack attack, DentisaurusLongirostrisEntity mount) {
        if (attack.animatorTick == AttackType.SWAMP_DRAGON_SPECIAL_ATTACK.getAnimationLength() - 13 || attack.animatorTick == AttackType.SWAMP_DRAGON_SPECIAL_ATTACK.getAnimationLength() - 29 || attack.animatorTick == AttackType.SWAMP_DRAGON_SPECIAL_ATTACK.getAnimationLength() - 49) {
            Vec3 lookVec = mount.getLookAngle();
            Vec3 pos = mount.position();

            float scale = ErsUtils.calculateRenderSize(mount.getAgeInDays());
            double length =  4 * scale;

            double range = 4 * scale;

            // 从实体前方4格开始的位置
            double startX = pos.x + lookVec.x * length;
            double startY = pos.y + lookVec.y;
            double startZ = pos.z + lookVec.z * length;

            AABB attackBox = new AABB(
                    startX - range, startY, startZ - range,
                    startX + range,
                    startY + range,
                    startZ + range
            );

            List<Entity> entities = mount.level().getEntities(mount, attackBox,
                    entity -> entity instanceof LivingEntity);

            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE);
            damage = checkEquipment(mount, ErsItems.SCRATCHING_BOARD.get()) ? damage * 1.4 : damage;
            for (Entity target : entities) {
                if(target.equals(mount.getControllingPassenger()))
                    continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float)damage);
                    ErsBleedingEffect.giveBleedingEffect(livingEntity,4);
                    mount.getEntityData().set(DentisaurusLongirostrisEntity.BLOODY, true);
                }
            }

            if (mount.level() instanceof ServerLevel serverLevel) {
                for (int i = 0; i < 10; i++) {
                    double particleX = attackBox.minX + mount.getRandom().nextDouble() * (attackBox.maxX - attackBox.minX);
                    double particleY = attackBox.minY + mount.getRandom().nextDouble() * (attackBox.maxY - attackBox.minY);
                    double particleZ = attackBox.minZ + mount.getRandom().nextDouble() * (attackBox.maxZ - attackBox.minZ);
                    if(mount.isInWater()){
                        serverLevel.sendParticles(ParticleTypes.BUBBLE, particleX, particleY, particleZ, 6, 0.3, 0.3, 0.3, 0.1);
                    }else {
                        serverLevel.sendParticles(ParticleTypes.FIREWORK, particleX, particleY, particleZ, 6, 0.3, 0.3, 0.3, 0.1);
                    }
                }
            }
        }
    }

    public void executeSwampDragonTurningAttack(MobAttack attack, DentisaurusLongirostrisEntity mount){
        if(attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength() - 20 || attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength() - 10) {
            Player player = (Player) mount.getControllingPassenger();
            if(player == null)
                return;
            Vec3 lookVec = player.getLookAngle();
            Vec3 pos = mount.position();
            double range = 3.2;
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
            double damage = mount.getAttributeValue(Attributes.ATTACK_DAMAGE) + 5;
            damage = checkEquipment(mount ,ErsItems.BULLY_STICK.get()) ? damage * 1.4 : damage;
            for (Entity target : entities) {
                if (target.equals(mount.getControllingPassenger()))
                    continue;
                if (target instanceof LivingEntity livingEntity) {
                    livingEntity.hurt(mount.level().damageSources().mobAttack(mount), (float) damage);
                    ErsBleedingEffect.giveBleedingEffect(livingEntity,4);
                    mount.getEntityData().set(DentisaurusLongirostrisEntity.BLOODY, true);
                }
            }
        }

        if(attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength() - 1){
            Player player = (Player) mount.getControllingPassenger();
            attack.turningAttackDirection = player.getLookAngle().normalize();
        }

        if(attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength() - 16 || attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength() - 18 || attack.animatorTick == AttackType.SWAMP_DRAGON_ATTACK_TURN.getAnimationLength() - 20){
            mount.setDeltaMovement( attack.turningAttackDirection.x * 1.7,0,  attack.turningAttackDirection.z * 1.7);
        }
    }

    public void executeSwampDragonCatchFishSmall(MobAttack attack, DentisaurusLongirostrisEntity mount){
        if(attack.getType() != AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL)
            return;

        if(attack.animatorTick == AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL.getAnimationLength() - 470 && attack.judgementTarget == null && !attack.triggered){
            if(mount.level() instanceof ServerLevel serverLevel){
                System.out.println(attack);
                attack.judgementTarget = Objects.requireNonNull(ErsEntities.LATIMERIA_PERCOIDES.get().create(serverLevel));
                serverLevel.addFreshEntity(attack.judgementTarget);
                attack.triggered = true;
            }

        }

        if(attack.animatorTick >= 0 && attack.judgementTarget != null)
        {
            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y + 0.1;
            double z = mount.getZ() + foodPos.z;
            attack.judgementTarget.setDeltaMovement(0,0,0);
            attack.judgementTarget.setXRot(90.0F);
            attack.judgementTarget.setYRot(90.0F);

            attack.judgementTarget.setPos(x,y,z);
            if(attack.animatorTick == AttackType.SWAMP_DRAGON_CATCH_FISH_SMALL.getAnimationLength() - 558)
            {
                attack.judgementTarget.hurt(mount.level().damageSources().mobAttack(mount), Float.MAX_VALUE);
                mount.getEntityData().set(DentisaurusLongirostrisEntity.BLOODY, true);
                if(mount.level() instanceof ServerLevel serverLevel)
                {
                    mount.heal(10);
                    mount.feed(2);
                    serverLevel.sendParticles(ErsParticleType.BLOOD.get(), x, y, z, 100, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }

        if(attack.animatorTick == 0){
           mount.getAnimatableInstanceCache().getManagerForId(mount.getId()).stopTriggeredAnimation("catch_fish-small");
        }
    }

    public void executeSwampDragonCatchFishMiddle(MobAttack attack, DentisaurusLongirostrisEntity mount){
        if(attack.getType() != AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE)
            return;

        if(attack.animatorTick == AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE.getAnimationLength() - 470 && attack.judgementTarget == null && !attack.triggered){
            if(mount.level() instanceof ServerLevel serverLevel){
                System.out.println(attack);
                attack.judgementTarget = Objects.requireNonNull(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get().create(serverLevel));
                serverLevel.addFreshEntity(attack.judgementTarget);
                attack.triggered = true;
            }

        }

        if(attack.animatorTick >= 0 && attack.judgementTarget != null)
        {
            Vector3f foodPos = mount.getFoodPosition();
            double x = mount.getX() + foodPos.x;
            double y = mount.getY() + foodPos.y + 0.1;
            double z = mount.getZ() + foodPos.z;
            attack.judgementTarget.setDeltaMovement(0,0,0);
            attack.judgementTarget.setXRot(90.0F);
            attack.judgementTarget.setYRot(90.0F);

            attack.judgementTarget.setPos(x,y,z);
            if(attack.animatorTick == AttackType.SWAMP_DRAGON_CATCH_FISH_MIDDLE.getAnimationLength() - 540)
            {
                attack.judgementTarget.hurt(mount.level().damageSources().mobAttack(mount), Float.MAX_VALUE);
                mount.getEntityData().set(DentisaurusLongirostrisEntity.BLOODY, true);
                if(mount.level() instanceof ServerLevel serverLevel)
                {
                    mount.heal(12);
                    mount.feed(3);
                    serverLevel.sendParticles(ErsParticleType.BLOOD.get(), x, y, z, 100, 0.3, 0.3, 0.3, 0.1);
                }
            }
        }

        if(attack.animatorTick == 0){
            mount.getAnimatableInstanceCache().getManagerForId(mount.getId()).stopTriggeredAnimation("catch_fish-middle");
        }
    }

    public boolean checkEquipment(DentisaurusLongirostrisEntity mount, Item equipment){
        for (int i = 5; i <= 7; i++) {
            ItemStack itemStack = mount.getInventory().getItem(i);
            if(!itemStack.isEmpty()){
                if(itemStack.is(equipment)){
                    return true;
                }
            }
        }
        return false;
    }
}
