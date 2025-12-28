package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.init.ErsNetwork;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

import java.util.function.Supplier;

public class MobTurnPacket {
    private final MobRotDirection direction;
    private final int entityId;

    public MobTurnPacket(int entityId, MobRotDirection diving) {
        this.entityId = entityId;
        this.direction = diving;
    }

    public MobTurnPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.direction = MobRotDirection.of(
                MobRotDirection.RotDirection.values()[buf.readInt()],
                buf.readBoolean()
        );
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(direction.getDirection().ordinal());
        buf.writeBoolean(direction.isLargeTurn());
    }

    public static MobTurnPacket decode(FriendlyByteBuf buf) {
        return new MobTurnPacket(buf.readInt(),MobRotDirection.of(
                MobRotDirection.RotDirection.values()[buf.readInt()],
                buf.readBoolean()
        ));
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if(ctx.get().getDirection().getReceptionSide().isClient()){
                Entity entity = Minecraft.getInstance().level.getEntity(entityId);
                if (entity instanceof ErsTamableVehicle<?> rideable) {
                    rideable.setRotDirection(direction);
                }
                ctx.get().setPacketHandled(true);
            }else {
                Entity entity = ctx.get().getSender().level().getEntity(entityId);
                if(entity instanceof ErsTamableVehicle<?> rideable){
                    rideable.setRotDirection(direction);
                }
                ErsNetwork.INSTANCE.send(PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity), new MobTurnPacket(entityId,direction));
            }
        });

    }
}
