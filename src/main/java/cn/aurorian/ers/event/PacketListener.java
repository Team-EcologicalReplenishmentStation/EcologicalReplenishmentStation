package cn.aurorian.ers.event;

import com.mojang.logging.LogUtils;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.NetworkEvent;
import org.slf4j.Logger;
@Mod.EventBusSubscriber(value = Dist.CLIENT)
public class PacketListener {
    private static final Logger LOGGER = LogUtils.getLogger();

//    @SubscribeEvent
    public static void onClientPacket(NetworkEvent.ClientCustomPayloadEvent event) {
        FriendlyByteBuf buf = event.getPayload();
        NetworkEvent.Context context = event.getSource().get();
        
        try {
            // 获取数据包的标识符
            String packetInfo = context.getNetworkManager().getRemoteAddress().toString();
            LOGGER.info("接收到数据包，来自: {}", packetInfo);
            
            // 如果你想查看数据包的内容（注意：这会消耗数据包的数据）
            if (buf != null && buf.readableBytes() > 0) {
                // 创建一个副本以便不影响原始数据包的处理
                FriendlyByteBuf copy = new FriendlyByteBuf(buf.copy());
                
                // 读取并记录数据（根据你的需求调整）
                byte[] data = new byte[copy.readableBytes()];
                copy.readBytes(data);
                LOGGER.info("数据包内容: {}", bytesToHex(data));
                
                // 释放副本
                copy.release();
            }
        } catch (Exception e) {
            LOGGER.error("处理数据包时发生错误", e);
        }
    }
    
    // 辅助方法：将字节数组转换为十六进制字符串
    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            sb.append(String.format("%02X ", b));
        }
        return sb.toString();
    }
}
