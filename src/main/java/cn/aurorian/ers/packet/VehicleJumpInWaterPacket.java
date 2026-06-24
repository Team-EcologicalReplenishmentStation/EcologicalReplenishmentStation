package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class VehicleJumpInWaterPacket {
    private final int entityId;

    public VehicleJumpInWaterPacket(int entityId) {
        this.entityId = entityId;
    }

    public VehicleJumpInWaterPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static VehicleJumpInWaterPacket decode(FriendlyByteBuf buf) {
        return new VehicleJumpInWaterPacket(buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    Entity entity = player.level().getEntity(entityId);
                    if (entity instanceof ErsTamableVehicle<?> tamable) {
                        tamable.setSwimState(3);
                    }
                }
                ctx.get().setPacketHandled(true);
            }
        });
    }
}
