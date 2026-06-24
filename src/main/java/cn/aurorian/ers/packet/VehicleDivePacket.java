package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class VehicleDivePacket {
    private final boolean diving;
    private final int entityId;

    public VehicleDivePacket(int entityId, boolean diving) {
        this.entityId = entityId;
        this.diving = diving;
    }

    public VehicleDivePacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.diving = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(diving);
    }

    public static VehicleDivePacket decode(FriendlyByteBuf buf) {
        return new VehicleDivePacket(buf.readInt(), buf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    Entity entity = player.level().getEntity(entityId);
                    if (entity instanceof ErsTamableVehicle<?> tamable) {
                        tamable.onDiveKeyUpdate(diving);
                        if (diving) tamable.setSwimState(2);
                    }
                }
                ctx.get().setPacketHandled(true);
            }
        });
    }
}
