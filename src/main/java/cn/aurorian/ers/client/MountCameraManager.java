package cn.aurorian.ers.client;

import cn.aurorian.ers.config.ErsClientConfig;
import cn.aurorian.ers.entity.ErsTamableVehicle;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public class MountCameraManager {
    private static CameraType previousPerspective = CameraType.FIRST_PERSON;
    private static boolean changedPerspective = false;

    public static void onDragonMount() {
        if (!ErsClientConfig.ENABLE_MOUNT_CAMERA_CORRECTION.get()) {
            return;
        }

        previousPerspective = Minecraft.getInstance().options.getCameraType();
        Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
        changedPerspective = true;
    }

    public static void onDragonDismount() {
        if (!changedPerspective) {
            return;
        }

        Minecraft.getInstance().options.setCameraType(previousPerspective);
        changedPerspective = false;
    }

    public static void setMountCameraAngles(Camera camera) {
        if (!ErsClientConfig.ENABLE_MOUNT_CAMERA_CORRECTION.get()) {
            return;
        }

        if (Minecraft.getInstance().player.getVehicle() instanceof ErsTamableVehicle<?> rideable) {
            if (!Minecraft.getInstance().options.getCameraType().isFirstPerson()) {
                var offsets = getConfiguredCameraOffsets(
                        Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK);

                if (rideable instanceof TubunasusDurovelaEntity entity) {
                    if (entity.getRushTimer() > 75) {
                        offsets[0] = Math.min(offsets[0] + (double) (entity.getRushTimer() - 75) / 40, 8);
                        offsets[1] = Math.min(offsets[1] + (double) (entity.getRushTimer() - 75) / 40, 5);
                    }
                    if (entity.getRushTimer() > 167) {
                        offsets[0] = Math.min(offsets[0] + (double) (entity.getRushTimer() - 167) / 40, 9);
                        offsets[1] = Math.min(offsets[1] + (double) (entity.getRushTimer() - 167) / 40, 6);
                    }
                }

                camera.move(0, offsets[1], 0);
                camera.move(-camera.getMaxZoom(offsets[0]), 0, 0);
                // do distance calcs AFTER our new position is set
            }
        }
    }

    /**
     * 提供配置中的相机偏移值
     *
     * @param isBackView 是否是第三人称背后视角
     * @return 包含三个偏移值的数组：[距离, 垂直偏移, 水平偏移]
     */
    private static double[] getConfiguredCameraOffsets(boolean isBackView) {
        if (isBackView) {
            return new double[] {
                ErsClientConfig.MOUNT_CAMERA_BACK_DISTANCE.get(),
                ErsClientConfig.MOUNT_CAMERA_BACK_VERTICAL_OFFSET.get(),
            };
        } else {
            return new double[] {
                ErsClientConfig.MOUNT_CAMERA_FRONT_DISTANCE.get(),
                ErsClientConfig.MOUNT_CAMERA_FRONT_VERTICAL_OFFSET.get(),
            };
        }
    }
}
