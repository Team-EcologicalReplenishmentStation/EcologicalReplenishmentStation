package cn.aurorian.ers.entity.projectile;

import cn.aurorian.ers.init.ErsEntities;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.ers.util.TickHelper;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.projectile.ThrownTrident;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;

public class DragonClawHarpoonEntity extends ThrownTrident {
    public DragonClawHarpoonEntity(EntityType<? extends ThrownTrident> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.tridentItem = new ItemStack(ErsItems.DRAGON_CLAW_HARPOON.get());
        this.entityData.set(ID_LOYALTY, (byte) 2);
    }

    public DragonClawHarpoonEntity(Level pLevel, LivingEntity thrower, ItemStack pStack) {
        this(ErsEntities.DRAGON_CLAW_HARPOON.get(), pLevel);
        this.setPos(thrower.getX(), thrower.getEyeY() - 0.1F, thrower.getZ());
        this.setOwner(thrower);
        this.tridentItem = pStack.copy();
        this.entityData.set(ID_LOYALTY, (byte) 2);
        this.entityData.set(ID_FOIL, pStack.hasFoil());
        int piercingLevel = EnchantmentHelper.getTagEnchantmentLevel(Enchantments.PIERCING, pStack);
        this.setPierceLevel((byte) piercingLevel);
    }

    List<Entity> lootedEntities;

    @Override
    public void tick() {
        super.tick();
        if (!this.level().isClientSide) {
            if (lootedEntities != null && !lootedEntities.isEmpty()) {
                lootedEntities.forEach(entity -> {
                    ItemEntity itemEntity = (ItemEntity) entity;
                    itemEntity.teleportTo(this.position().x, this.position().y, this.position().z);
                });
            }
        }
    }

    @Override
    protected void onHitEntity(EntityHitResult result) {
        Entity entity = result.getEntity();
        float f = 12.0F;
        if (entity instanceof LivingEntity livingentity) {
            f += EnchantmentHelper.getDamageBonus(this.tridentItem, livingentity.getMobType());
        }

        Entity entity1 = this.getOwner();
        DamageSource $$5 = this.damageSources().trident(this, entity1 == null ? this : entity1);
        this.dealtDamage = true;
        SoundEvent $$6 = SoundEvents.TRIDENT_HIT;
        if (entity.hurt($$5, f)) {
            if (entity.getType() == EntityType.ENDERMAN) {
                return;
            }

            if (entity instanceof LivingEntity livingentity1) {
                if (entity1 instanceof LivingEntity) {
                    EnchantmentHelper.doPostHurtEffects(livingentity1, entity1);
                    EnchantmentHelper.doPostDamageEffects((LivingEntity) entity1, livingentity1);
                }

                this.doPostHurtEffects(livingentity1);
            }
        }

        this.setDeltaMovement(this.getDeltaMovement().multiply(-0.01, -0.1, -0.01));
        float $$8 = 1.0F;
        if (this.level() instanceof ServerLevel && this.level().isThundering() && this.isChanneling()) {
            BlockPos $$9 = entity.blockPosition();
            if (this.level().canSeeSky($$9)) {
                LightningBolt $$10 = EntityType.LIGHTNING_BOLT.create(this.level());
                if ($$10 != null) {
                    $$10.moveTo(Vec3.atBottomCenterOf($$9));
                    $$10.setCause(entity1 instanceof ServerPlayer ? (ServerPlayer) entity1 : null);
                    this.level().addFreshEntity($$10);
                    $$6 = SoundEvents.TRIDENT_THUNDER;
                    $$8 = 5.0F;
                }
            }
        }

        this.playSound($$6, $$8, 1.0F);

        if (level() instanceof ServerLevel serverLevel) {
            double startX = position().x;
            double startY = position().y;
            double startZ = position().z;
            AABB attackBox =
                    new AABB(startX - 2.5, startY - 2.5, startZ - 2.5, startX + 2.5, startY + 2.5, startZ + 2.5);
            TickHelper.tickLater(
                    serverLevel,
                    2,
                    () -> lootedEntities = this.level()
                            .getEntities(
                                    this,
                                    attackBox,
                                    item -> item instanceof ItemEntity
                                            && ((ItemEntity) item).getItem().is(ItemTags.FISHES)));
        }
    }
}
