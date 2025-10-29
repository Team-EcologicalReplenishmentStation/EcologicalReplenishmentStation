package cn.aurorian.ers.client;

import net.minecraft.network.FriendlyByteBuf;
import org.joml.Vector3d;
import software.bernie.geckolib.network.SerializableDataTicket;
import software.bernie.geckolib.util.GeckoLibUtil;

public class ErsDataTickets {
    public static final SerializableDataTicket<Vector3d> SADDLE_POS = GeckoLibUtil.addDataTicket(
            new SerializableDataTicket<>("ers:saddle_pos", Vector3d.class) {
                @Override
                public void encode(Vector3d data, FriendlyByteBuf buffer) {
                    buffer.writeDouble(data.x());
                    buffer.writeDouble(data.y());
                    buffer.writeDouble(data.z());
                }

                @Override
                public Vector3d decode(FriendlyByteBuf buffer) {
                    return new Vector3d(
                            buffer.readDouble(),
                            buffer.readDouble(),
                            buffer.readDouble()
                    );
                }
            }
    );
    public static void register() {}
}