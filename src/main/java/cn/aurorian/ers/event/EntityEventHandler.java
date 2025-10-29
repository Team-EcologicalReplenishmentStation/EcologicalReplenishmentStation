package cn.aurorian.ers.event;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.entity.ErsTamable;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.tachypleusgladius.TachypleusGladiusEntity;
import cn.aurorian.ers.init.ErsBlocks;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.init.ErsMobEffects;
import cn.aurorian.ers.init.ErsParticleType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraftforge.event.entity.ProjectileImpactEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID)
public class EntityEventHandler {
    @SubscribeEvent
    public static void onProjectileImpact(ProjectileImpactEvent event) {
        if (!(event.getProjectile() instanceof AbstractArrow arrow)) {
            return;
        }

        if (event.getRayTraceResult() instanceof EntityHitResult entityHit
                && entityHit.getEntity() instanceof DentisaurusLongirostrisEntity sotek) {
            if (!sotek.level().isClientSide) {
                // 取消原始碰撞并防止伤害
                event.setImpactResult(ProjectileImpactEvent.ImpactResult.SKIP_ENTITY);

                // 直接反转原箭矢的运动方向
                arrow.setDeltaMovement(arrow.getDeltaMovement().multiply(-0.2 + Mth.nextDouble(arrow.level().random,-0.2 ,0.2), -0.3, -0.2 + Mth.nextDouble(arrow.level().random,-0.2 ,0.2)));
                ((ServerLevel)sotek.level()).sendParticles(
                        ParticleTypes.CRIT,
                        entityHit.getLocation().x,
                        entityHit.getLocation().y,
                        entityHit.getLocation().z,
                        10,
                        0.2, 0.2, 0.2,
                        0.1
                );

                sotek.level().playSound(
                        null,
                        sotek.getX(),
                        sotek.getY(),
                        sotek.getZ(),
                        SoundEvents.SHIELD_BLOCK,
                        SoundSource.NEUTRAL,
                        1.0F,
                        1.0F
                );
            }
        }
    }

    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity() instanceof TachypleusGladiusEntity crab) {
            if (event.getSource().is(DamageTypes.MOB_ATTACK) || event.getSource().is(DamageTypes.PLAYER_ATTACK)) {
                if(!crab.isWaiting()){
                    if(crab.getLastHurtByMob() != null){
                        crab.crabEscape(crab.getLastHurtByMob());
                    }else if(event.getSource().is(DamageTypes.PLAYER_ATTACK)){
                        crab.crabEscape(event.getSource().getEntity());
                    }

                }
                if(event.getSource().getEntity() instanceof LivingEntity living){
                    living.hurt(crab.level().damageSources().thorns(crab), 4);
                }

            }
        }

        if (event.getEntity().getHealth() - event.getAmount() > 0.0F) {
            return;
        }

        if (event.getEntity() instanceof DentisaurusLongirostrisEntity sotek) {
            for (int i = 5; i <= 7; i++) {
                ItemStack itemStack = sotek.getInventory().getItem(i);
                if (!itemStack.isEmpty()) {
                    if (itemStack.is(Items.TOTEM_OF_UNDYING)) {
                        itemStack.shrink(1);
                        sotek.setHealth(sotek.getMaxHealth());
                        sotek.level().playSound(sotek, sotek.getOnPos(),
                                SoundEvents.TOTEM_USE, SoundSource.NEUTRAL,
                                1.0F, 1.0F);
                        return;
                    }
                }
            }
        }

        if(event.getEntity() instanceof ErsTamable<?> pet && pet.isEnable()){
            BlockPos pos = pet.getRespawnPos();
            ServerLevel respawnlevel = null;
            for (ServerLevel level : pet.getServer().getAllLevels()) {
                if(level.dimension().toString().equals(pet.getDimension())){
                    respawnlevel = level;
                    break;
                }
            }

            if(respawnlevel == null)
                return;

             if(respawnlevel.getBlockState(pos).is(ErsBlocks.SOUL_CUBE.get())){
                 pet.level().playSound(pet, pet.getOnPos(),
                         SoundEvents.TOTEM_USE, SoundSource.NEUTRAL,
                         0.8F, 1.0F);
                 pet.ejectPassengers();

                 pet.clearFire();
                 pet.teleportTo(respawnlevel,pos.getX(),pos.getY() + 1, pos.getZ(),
                         null,pet.getYRot(),pet.getXRot());
                 pet.setHealth(1);
                 pet.removeAllEffects();

                 if(pet instanceof DentisaurusLongirostrisEntity swampDragon){
                     swampDragon.setCommand(1);
                     swampDragon.setRotDirection(MobRotDirection.of(MobRotDirection.RotDirection.NONE, false));
                     swampDragon.setAgeInTicks(swampDragon.getAgeInTicks() / 2);
                     if(swampDragon.getAgeInDays() < 38){
                         swampDragon.setElite(false);
                         if(swampDragon.getAgeInDays() < 20){
                             swampDragon.setMature(false);
                         }
                     }
                     swampDragon.updateFromAgeServer();
                 }
                 event.setCanceled(true);
             }
        }

        if(event.getSource().is(DamageTypes.MOB_ATTACK) && event.getEntity() instanceof ServerPlayer player){
            if(player.getVehicle() instanceof ErsTamableVehicle<?> tamable){
                SimpleContainer inventory = tamable.getInventory();
                if(inventory != null){
                    for(int i = 4; i < inventory.getContainerSize(); i++){
                        ItemStack itemStack = inventory.getItem(i);
                        if(!itemStack.isEmpty()){
                            if(itemStack.is(ErsItems.RIDING_GUIDE.get())){
                                event.setCanceled(true);
                                return;
                            }
                        }
                    }
                }
            }

        }
    }

    @SubscribeEvent
    public static void onFishDie(LivingDeathEvent event){
        if(event.getEntity() instanceof AbstractFish && event.getSource().getEntity() instanceof DentisaurusLongirostrisEntity swampDragon) {
            if(!swampDragon.isTame())
                swampDragon.feed(8);
            swampDragon.feed(2);
        }
    }

    @SubscribeEvent
    public static void onPlayerUse(PlayerInteractEvent.EntityInteract event){
        ItemStack itemStack = event.getItemStack();
       if(itemStack.is(ErsItems.SOUL_CUBE_GIFT.get()) && event.getTarget() instanceof ErsTamable<?> pet) {
           if(!pet.isTame())
               return;
           if(!pet.isOwnedBy(event.getEntity()))
               return;

           pet.setEnable(true);

           CompoundTag tag = itemStack.getOrCreateTag();
           pet.setDimension(event.getLevel().dimension().toString());
           pet.setRespawnPos(new BlockPos(tag.getInt("X"),
                   tag.getInt("Y"),
                   tag.getInt("Z")));
           itemStack.shrink(1);
           event.getLevel().playSound(event.getEntity(),
                   event.getTarget().getOnPos(),
                   SoundEvents.RESPAWN_ANCHOR_SET_SPAWN, SoundSource.NEUTRAL,
                   1.0F, 1.0F);
       }

       if(itemStack.is(ErsItems.TOURNIQUET.get()) && event.getTarget() instanceof LivingEntity living) {
            if (living.hasEffect(ErsMobEffects.BLEEDING.get())) {
                ErsBleedingEffect.removeBleedingEffect(living);
                itemStack.shrink(1);
            }

       }
    }

    @SubscribeEvent
    public static void onLivingEntityTick(LivingEvent.LivingTickEvent event){
        if(event.getEntity().hasEffect(ErsMobEffects.BLEEDING.get())){
            if (event.getEntity().level() instanceof ServerLevel serverLevel) {
                SimpleParticleType type = event.getEntity().isInWater()? ErsParticleType.BLOOD.get(): ErsParticleType.DRIPPING_BLOOD.get();
                serverLevel.sendParticles(
                        type,
                        event.getEntity().getX(),
                        event.getEntity().getY(),
                        event.getEntity().getZ(),
                        1,
                        0.8,
                        0.8,
                        0.8,
                        0
                );
            }
        }
    }
}
