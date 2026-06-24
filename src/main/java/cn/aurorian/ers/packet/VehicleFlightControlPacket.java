package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsFlyableVehicle;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class VehicleFlightControlPacket {
    private final int entityId;
    private final int verticalInput;

    public VehicleFlightControlPacket(int entityId, int verticalInput) {
        this.entityId = entityId;
        this.verticalInput = Mth.clamp(verticalInput, -1, 1);
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(verticalInput);
    }

    public static VehicleFlightControlPacket decode(FriendlyByteBuf buf) {
        return new VehicleFlightControlPacket(buf.readInt(), buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    Entity entity = player.level().getEntity(entityId);
                    if (entity instanceof ErsFlyableVehicle<?> vehicle) {
                        vehicle.onFlightKeyUpdate(verticalInput);
                    }
                }
            }
            ctx.get().setPacketHandled(true);
        });
    }
}
