package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import java.util.function.Supplier;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

public class MobPositionRiderPacket {
    private final int entityId;
    private final Vector3f riderPosition;

    public MobPositionRiderPacket(int entityId, Vector3f riderPosition) {
        this.entityId = entityId;
        this.riderPosition = riderPosition;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeVector3f(riderPosition);
    }

    public static MobPositionRiderPacket decode(FriendlyByteBuf buf) {
        return new MobPositionRiderPacket(buf.readInt(), buf.readVector3f());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ErsTamableVehicle<?> entity =
                        (ErsTamableVehicle<?>) ctx.get().getSender().level().getEntity(entityId);
                entity.setRiderPos(new BlockPos((int) riderPosition.x, (int) riderPosition.y, (int) riderPosition.z));
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
