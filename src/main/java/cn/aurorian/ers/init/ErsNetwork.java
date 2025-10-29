package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.packet.*;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

import java.util.Optional;

public class ErsNetwork {
    private static final String PROTOCOL_VERSION = "1";
    
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, "network"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );
    private static int packetId = 0;
    private static int nextId() {
        return packetId++;
    }
    public static void register() {
        INSTANCE.messageBuilder(MobAttackPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
            .encoder(MobAttackPacket::encode)
            .decoder(MobAttackPacket::decode)
            .consumerMainThread(MobAttackPacket::handle)
            .add();
        INSTANCE.messageBuilder(SwampDragonTrackFoodPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
            .encoder(SwampDragonTrackFoodPacket::encode)
            .decoder(SwampDragonTrackFoodPacket::decode)
            .consumerMainThread(SwampDragonTrackFoodPacket::handle)
            .add();
        INSTANCE.messageBuilder(MobSyncDimPacket.class, nextId(), NetworkDirection.PLAY_TO_CLIENT)
            .encoder(MobSyncDimPacket::encode)
            .decoder(MobSyncDimPacket::decode)
            .consumerMainThread(MobSyncDimPacket::handle)
            .add();
        INSTANCE.messageBuilder(VehicleDivePacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(VehicleDivePacket::encode)
                .decoder(VehicleDivePacket::decode)
                .consumerMainThread(VehicleDivePacket::handle)
                .add();
        INSTANCE.messageBuilder(VehicleSprintPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(VehicleSprintPacket::encode)
                .decoder(VehicleSprintPacket::decode)
                .consumerMainThread(VehicleSprintPacket::handle)
                .add();
        INSTANCE.messageBuilder(VehicleJumpPacket.class, nextId(), NetworkDirection.PLAY_TO_SERVER)
                .encoder(VehicleJumpPacket::encode)
                .decoder(VehicleJumpPacket::decode)
                .consumerMainThread(VehicleJumpPacket::handle)
                .add();

        INSTANCE.registerMessage(nextId(),
                MobTurnPacket.class,
                MobTurnPacket::encode,
                MobTurnPacket::decode,
                MobTurnPacket::handle, Optional.empty());
    }
    
}
