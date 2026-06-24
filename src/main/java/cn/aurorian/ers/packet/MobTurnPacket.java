package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.entity.MobRotDirection;
import cn.aurorian.ers.init.ErsNetwork;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.network.NetworkEvent;
import net.minecraftforge.network.PacketDistributor;

/**
 * 生物转向网络数据包。
 *
 * <p>用于同步坐骑生物的转向状态。当玩家控制坐骑转向时， 此数据包会在客户端和服务器之间传递，确保所有玩家看到一致的转向动画。
 *
 * <p>数据包包含：
 *
 * <ul>
 *   <li>实体ID
 *   <li>转向方向（向左/向右/无）
 *   <li>是否为大幅转向
 * </ul>
 *
 * @author mlus
 * @version 1.2.0-alpha
 * @see MobRotDirection
 * @see ErsTamableVehicle
 */
public class MobTurnPacket {

    /** 转向方向 */
    private final MobRotDirection direction;

    /** 实体ID */
    private final int entityId;

    /**
     * 构造转向数据包。
     *
     * @param entityId 实体ID
     * @param diving 转向方向
     */
    public MobTurnPacket(int entityId, MobRotDirection diving) {
        this.entityId = entityId;
        this.direction = diving;
    }

    /**
     * 从字节缓冲读取数据包。
     *
     * @param buf 字节缓冲区
     */
    public MobTurnPacket(FriendlyByteBuf buf) {
        this.entityId = buf.readInt();
        this.direction = MobRotDirection.of(MobRotDirection.RotDirection.values()[buf.readInt()], buf.readBoolean());
    }

    /**
     * 将数据包编码到字节缓冲。
     *
     * @param buf 字节缓冲区
     */
    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(direction.getDirection().ordinal());
        buf.writeBoolean(direction.isLargeTurn());
    }

    /**
     * 从字节缓冲解码数据包。
     *
     * @param buf 字节缓冲区
     * @return 解码后的数据包
     */
    public static MobTurnPacket decode(FriendlyByteBuf buf) {
        return new MobTurnPacket(
                buf.readInt(),
                MobRotDirection.of(MobRotDirection.RotDirection.values()[buf.readInt()], buf.readBoolean()));
    }

    /**
     * 处理接收到的数据包。
     *
     * @param ctx 网络事件上下文提供者
     */
    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isClient()) {
                Entity entity = Minecraft.getInstance().level.getEntity(entityId);
                if (entity instanceof ErsTamableVehicle<?> rideable) {
                    rideable.setRotDirection(direction);
                }
                ctx.get().setPacketHandled(true);
            } else {
                Entity entity = ctx.get().getSender().level().getEntity(entityId);
                if (entity instanceof ErsTamableVehicle<?> rideable) {
                    rideable.setRotDirection(direction);
                }
                ErsNetwork.INSTANCE.send(
                        PacketDistributor.TRACKING_ENTITY_AND_SELF.with(() -> entity),
                        new MobTurnPacket(entityId, direction));
            }
        });
    }
}
