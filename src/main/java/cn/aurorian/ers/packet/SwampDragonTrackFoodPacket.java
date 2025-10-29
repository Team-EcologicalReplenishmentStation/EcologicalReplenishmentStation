package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class SwampDragonTrackFoodPacket {
    private final int entityId;
    private final Vector3f foodPosition;
    public SwampDragonTrackFoodPacket(int entityId, Vector3f foodPosition) {
        this.entityId = entityId;
        this.foodPosition = foodPosition;
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeVector3f(foodPosition);
    }

    public static SwampDragonTrackFoodPacket decode(FriendlyByteBuf buf) {
        return new SwampDragonTrackFoodPacket(buf.readInt(),buf.readVector3f());
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                DentisaurusLongirostrisEntity sotek = (DentisaurusLongirostrisEntity) ctx.get().getSender().level().getEntity(entityId);
                sotek.setFoodPosition(foodPosition);
            }
        });
        ctx.get().setPacketHandled(true);
    }
    
}
