package cn.aurorian.ers.client;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.ErsFlyableVehicle;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.ers.init.ErsKeyBindings;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.packet.MobAttackPacket;
import cn.aurorian.ers.packet.VehicleDivePacket;
import cn.aurorian.ers.packet.VehicleFlightControlPacket;
import cn.aurorian.ers.packet.VehicleSprintPacket;
import cn.aurorian.ers.util.ErsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(
        modid = EcologicalReplenishmentStation.MODID,
        bus = Mod.EventBusSubscriber.Bus.FORGE,
        value = Dist.CLIENT)
public class ClientForgeListener {
    @SubscribeEvent
    public static void onCameraSetup(ViewportEvent.ComputeCameraAngles event) {
        MountCameraManager.setMountCameraAngles(event.getCamera());
    }

    @SubscribeEvent
    public static void onKeyInput(InputEvent.Key event) {
        // Only trigger when pressed
        if (event.getAction() != 1) {
            return;
        }

        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null
                && player.getVehicle() instanceof ErsTamableVehicle<?> tamable
                && (tamable.getAttackState().isFinished()
                        || tamable.getAttackState().getType().isCancelable())) {
            if (!tamable.isTame() || tamable.getOwner() != player) return;

            int key = event.getKey();
            MobAttackPacket packet1 = null;

            if (key == ErsKeyBindings.ATTACK_KEY.getKey().getValue()) {
                if (!ErsUtils.isMoving(tamable) && ErsKeyBindings.ATTACK4_KEY.isDown()) {
                    packet1 = new MobAttackPacket(player.getVehicle().getId(), 4);
                } else packet1 = new MobAttackPacket(player.getVehicle().getId(), 1);
            } else if (key == ErsKeyBindings.ATTACK2_KEY.getKey().getValue()) {
                packet1 = new MobAttackPacket(player.getVehicle().getId(), 2);
            } else if (key == ErsKeyBindings.ATTACK3_KEY.getKey().getValue()) {
                packet1 = new MobAttackPacket(player.getVehicle().getId(), 3);
            } else if (key == Minecraft.getInstance().options.keyJump.getKey().getValue()) {
                packet1 = new MobAttackPacket(player.getVehicle().getId(), 5);
            }
            MobAttackPacket packet = packet1;

            if (packet != null) {
                tamable.updateMount();
                ErsNetwork.INSTANCE.sendToServer(packet);
            }
        }
    }

    private static boolean lastDiveState = false;

    @SubscribeEvent
    public static void onDive(TickEvent.ClientTickEvent event) {
        Player player = Minecraft.getInstance().player;
        if (player != null && player.getVehicle() instanceof ErsTamableVehicle<?>) {
            boolean currentState = ErsKeyBindings.DIVE_KEY.isDown();
            if (currentState != lastDiveState) {
                ErsNetwork.INSTANCE.sendToServer(
                        new VehicleDivePacket(player.getVehicle().getId(), currentState));
                lastDiveState = currentState;
            }
        }
    }

    private static boolean lastDiveState2 = false;
    private static int triggerTime = 0;
    private static int lastFlightVerticalInput = 0;

    @SubscribeEvent
    public static void onSprint(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.getVehicle() instanceof ErsTamableVehicle<?> tamable) {
            if (!tamable.isTame()) return;

            if (triggerTime > 0
                    && !Minecraft.getInstance().options.keySprint.isDown()
                    && !Minecraft.getInstance().options.keyUp.isDown()) {
                triggerTime--;
            } else if (triggerTime <= 0
                    && (Minecraft.getInstance().options.keySprint.isDown()
                            || Minecraft.getInstance().options.keyUp.isDown())) {
                triggerTime = 6;
            }
            boolean currentState = Minecraft.getInstance().options.keySprint.isDown()
                    || (triggerTime > 0 && triggerTime != 6)
                            && Minecraft.getInstance().options.keyUp.isDown();
            if (currentState != lastDiveState2) {
                ErsNetwork.INSTANCE.sendToServer(
                        new VehicleSprintPacket(player.getVehicle().getId(), currentState));
                lastDiveState2 = currentState;
            }
        }
    }

    @SubscribeEvent
    public static void onFlightControl(TickEvent.ClientTickEvent event) {
        LocalPlayer player = Minecraft.getInstance().player;
        if (player != null && player.getVehicle() instanceof ErsFlyableVehicle<?> vehicle) {
            int verticalInput = 0;
            boolean jump = Minecraft.getInstance().options.keyJump.isDown();
            boolean dive = ErsKeyBindings.DIVE_KEY.isDown();

            if (jump != dive) {
                verticalInput = jump ? 1 : -1;
            }

            if (verticalInput != lastFlightVerticalInput) {
                ErsNetwork.INSTANCE.sendToServer(new VehicleFlightControlPacket(vehicle.getId(), verticalInput));
                lastFlightVerticalInput = verticalInput;
            }
        } else if (lastFlightVerticalInput != 0) {
            lastFlightVerticalInput = 0;
        }
    }
}
