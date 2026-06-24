package cn.aurorian.ers.entity;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.syncher.EntityDataSerializer;
import org.jetbrains.annotations.NotNull;

public class MobRotationSerializer implements EntityDataSerializer<MobRotDirection> {
    @Override
    public void write(@NotNull FriendlyByteBuf friendlyByteBuf, @NotNull MobRotDirection mobRotDirection) {
        friendlyByteBuf.writeInt(mobRotDirection.getDirection().ordinal());
        friendlyByteBuf.writeBoolean(mobRotDirection.isLargeTurn());
    }

    @Override
    public @NotNull MobRotDirection read(@NotNull FriendlyByteBuf friendlyByteBuf) {
        return MobRotDirection.of(
                MobRotDirection.RotDirection.values()[friendlyByteBuf.readInt()], friendlyByteBuf.readBoolean());
    }

    @Override
    public @NotNull MobRotDirection copy(@NotNull MobRotDirection mobRotDirection) {
        return MobRotDirection.of(mobRotDirection.getDirection(), mobRotDirection.isLargeTurn());
    }
}
