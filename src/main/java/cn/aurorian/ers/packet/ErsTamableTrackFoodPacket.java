package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamable;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;
import org.joml.Vector3f;

import java.util.function.Supplier;

public class ErsTamableTrackFoodPacket {
    private final int entityId;
    private final Vector3f foodPosition;
    public ErsTamableTrackFoodPacket(int entityId, Vector3f foodPosition) {
        this.entityId = entityId;
        this.foodPosition = foodPosition;
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeVector3f(foodPosition);
    }

    public static ErsTamableTrackFoodPacket decode(FriendlyByteBuf buf) {
        return new ErsTamableTrackFoodPacket(buf.readInt(),buf.readVector3f());
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ErsTamable<?> entity = (ErsTamable<?>) ctx.get().getSender().level().getEntity(entityId);
                entity.setFoodPosition(foodPosition);
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
