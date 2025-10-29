package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class VehicleJumpPacket {
    private final int entityId;

    public VehicleJumpPacket(int entityId) {
        this.entityId = entityId;
    }

    public VehicleJumpPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }

    public static VehicleJumpPacket decode(FriendlyByteBuf buf) {
        return new VehicleJumpPacket(buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide().isServer()){
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
