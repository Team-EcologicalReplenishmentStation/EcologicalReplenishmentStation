package cn.aurorian.ers.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.jetbrains.annotations.NotNull;

public class MobAttackSerializer implements EntityDataSerializer<MobAttack> {
    @Override
    public void write(FriendlyByteBuf buffer, MobAttack attack) {
        ErsAttackType type = attack.getType();
        if (type instanceof AttackType attackType) {
            buffer.writeBoolean(true);
            buffer.writeInt(attackType.ordinal());
        } else {
            buffer.writeBoolean(false);
            buffer.writeUtf(type.getId());
            buffer.writeUtf(type.getAnimName());
            buffer.writeInt(type.getAnimationLength());
            buffer.writeBoolean(type.canMove());
            buffer.writeInt(type.getStaminaCost());
            buffer.writeBoolean(type.isTurning());
            buffer.writeBoolean(type.isCancelable());
            buffer.writeBoolean(type.isForcePlay());
        }
        buffer.writeInt(attack.animatorTick);
        buffer.writeInt(attack.getEntity() != null ? attack.getEntity().getId() : -1);
        buffer.writeBoolean(attack.triggered);
    }

    @Override
    public @NotNull MobAttack read(FriendlyByteBuf buffer) {
        ErsAttackType type;
        if (buffer.readBoolean()) {
            type = AttackType.values()[buffer.readInt()];
        } else {
            type = SimpleAttackType.synced(
                    buffer.readUtf(),
                    buffer.readUtf(),
                    buffer.readInt(),
                    buffer.readBoolean(),
                    buffer.readInt(),
                    buffer.readBoolean(),
                    buffer.readBoolean(),
                    buffer.readBoolean());
        }
        int animatorTick = buffer.readInt();
        int entityId = buffer.readInt();
        boolean triggered = buffer.readBoolean();

        ErsTamable<?> entity = null;
        if (entityId != -1 && FMLEnvironment.dist == Dist.CLIENT) {
            entity = resolveEntityClient(entityId);
        }

        MobAttack attack = new MobAttack(type, entity);

        attack.animatorTick = animatorTick;
        attack.triggered = triggered;

        return attack;
    }

    /**
     * Isolated client-only method to resolve entity from client level. Must not be inlined — keeps
     * Minecraft class reference out of read().
     */
    private static ErsTamable<?> resolveEntityClient(int entityId) {
        if (Minecraft.getInstance().level != null) {
            var e = Minecraft.getInstance().level.getEntity(entityId);
            return e instanceof ErsTamable<?> t ? t : null;
        }
        return null;
    }

    @Override
    public @NotNull MobAttack copy(MobAttack attack) {
        MobAttack newAttack = new MobAttack(attack.getType(), attack.getEntity());
        newAttack.animatorTick = attack.animatorTick;
        newAttack.triggered = attack.triggered;
        newAttack.isSyncInstance = true;
        return newAttack;
    }
}
