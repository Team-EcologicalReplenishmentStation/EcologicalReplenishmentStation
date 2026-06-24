package cn.aurorian.ers.util;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.ErsEntity;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.LivingEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.keyframe.event.SoundKeyframeEvent;

public class SimpleAutoPlayingSoundKeyFrameHandler<A extends GeoAnimatable>
        implements AnimationController.SoundKeyframeHandler<A> {
    private final String namespace;

    public SimpleAutoPlayingSoundKeyFrameHandler() {
        this.namespace = EcologicalReplenishmentStation.MODID;
    }

    public SimpleAutoPlayingSoundKeyFrameHandler(String namespace) {
        this.namespace = namespace;
    }

    @Override
    public void handle(SoundKeyframeEvent<A> soundKeyframeEvent) {
        var soundName = soundKeyframeEvent.getKeyframeData().getSound();
        var sound = BuiltInRegistries.SOUND_EVENT.get(ResourceLocation.fromNamespaceAndPath(namespace, soundName));

        if (sound == null || !(soundKeyframeEvent.getAnimatable() instanceof LivingEntity entity)) return;

        if (!(soundKeyframeEvent.getAnimatable() instanceof ErsEntity<?> ers)) return;

        var playerPos = Minecraft.getInstance().player.getEyePosition();

        var entityPos = entity.position().add(0, 1, 0);
        var distance = Math.sqrt(playerPos.distanceToSqr(entityPos));

        if (distance > ers.getSoundRange()) return;

        var volume = ers.getVolume() * Math.min(1f, 1f / (distance / ers.getSoundRange()));

        if (Minecraft.getInstance().player.getVehicle() instanceof ErsTamableVehicle<?>
                && Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK) {
            volume *= 2f;
        }

        entity.level()
                .playLocalSound(
                        entity.getX(),
                        entity.getY(),
                        entity.getZ(),
                        sound,
                        entity.getSoundSource(),
                        (float) volume,
                        1.0f,
                        false);
    }
}
