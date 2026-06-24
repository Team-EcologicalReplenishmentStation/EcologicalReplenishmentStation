package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.init.ErsMobEffects;
import java.util.function.Supplier;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;

public class VehicleSprintPacket {
    private final boolean sprint;
    private final int entityId;

    public VehicleSprintPacket(int entityId, boolean diving) {
        this.entityId = entityId;
        this.sprint = diving;
    }

    public VehicleSprintPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.sprint = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeBoolean(sprint);
    }

    public static VehicleSprintPacket decode(FriendlyByteBuf buf) {
        return new VehicleSprintPacket(buf.readInt(), buf.readBoolean());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ServerPlayer player = ctx.get().getSender();
                if (player != null) {
                    Entity entity = player.level().getEntity(entityId);
                    if (entity instanceof ErsTamableVehicle<?> vehicle) {
                        if (vehicle.hasEffect(ErsMobEffects.FRACTURE.get())) return;

                        if (vehicle.isInWater()) {
                            entity.setSprinting(sprint);
                            player.setSprinting(false);
                        } else {
                            entity.setSprinting(false);
                            player.setSprinting(sprint);
                        }
                    }
                }
                ctx.get().setPacketHandled(true);
            }
        });
    }
}
