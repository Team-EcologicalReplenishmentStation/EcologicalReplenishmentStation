package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MobSyncDimPacket {
    private final int entityId;
    public MobSyncDimPacket(int entityId) {
        this.entityId = entityId;
    }
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
    }
    public static MobSyncDimPacket decode(FriendlyByteBuf buf) {
        return new MobSyncDimPacket(buf.readInt());
    }
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                LivingEntity entity = (DentisaurusLongirostrisEntity) Minecraft.getInstance().level.getEntity(entityId);
                if (entity != null) {
                    // 刷新实体维度
                    entity.refreshDimensions();
                   
                }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
