package cn.aurorian.ers.entity;

import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

public class MobAttackSerializer implements EntityDataSerializer<MobAttack> {
    @Override
    public void write(FriendlyByteBuf buffer, MobAttack attack) {
        buffer.writeInt(attack.getType().ordinal());
        buffer.writeInt(attack.animatorTick);
        buffer.writeInt(attack.getEntity() != null ? attack.getEntity().getId() : -1);
        buffer.writeBoolean(attack.triggered);
    }

    @Override
    public @NotNull MobAttack read(FriendlyByteBuf buffer) {
        AttackType type = AttackType.values()[buffer.readInt()];
        int animatorTick = buffer.readInt();
        int entityId = buffer.readInt();
        boolean triggered = buffer.readBoolean();
        ErsTamable<?> entity = entityId != -1 ?
            (ErsTamable<?>) Minecraft.getInstance().level.getEntity(entityId) : null;

        MobAttack attack = new MobAttack(type, entity);

        attack.animatorTick = animatorTick;
        attack.triggered = triggered;
        
        return attack;
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