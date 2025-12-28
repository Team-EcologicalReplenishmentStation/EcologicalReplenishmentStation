package cn.aurorian.ers.packet;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class MobAttackPacket {
    private final int entityId;
    private final int attackId;

    public MobAttackPacket(int entityId, int attackId) {
        this.entityId = entityId;
        this.attackId = attackId;
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeInt(entityId);
        buf.writeInt(attackId);
    }
    public static MobAttackPacket decode(FriendlyByteBuf buf) {
        return new MobAttackPacket(buf.readInt(),buf.readInt());
    }

    public void handle(Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            if (ctx.get().getDirection().getReceptionSide().isServer()) {
                ErsTamableVehicle<?> tamable = (ErsTamableVehicle<?>) ctx.get().getSender().level().getEntity(entityId);

                if(tamable == null)
                    return;

                switch (this.attackId) {
                    case 1:
                        tamable.executeDefaultAttackType();
                        break;
                    case 2:
                        tamable.executeSpecialAttackType();
                        break;
                    case 3:
                        tamable.executeJudgementAttackType();
                        break;
                    case 4:
                        tamable.executeTurnAttackType();
                        break;
                    case 5:
                        tamable.executeJumpAttackType();
                        break;
                     default:
                         break;
                    }
            }
        });
        ctx.get().setPacketHandled(true);
    }
}
