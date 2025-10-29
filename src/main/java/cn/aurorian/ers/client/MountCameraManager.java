package cn.aurorian.ers.client;

import cn.aurorian.ers.entity.ErsPlayerRideable;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import net.minecraft.client.Camera;
import net.minecraft.client.CameraType;
import net.minecraft.client.Minecraft;

public class MountCameraManager
{
    private static CameraType previousPerspective = CameraType.FIRST_PERSON;

    public static void onDragonMount()
    {
        
        previousPerspective = Minecraft.getInstance().options.getCameraType();
        Minecraft.getInstance().options.setCameraType(CameraType.THIRD_PERSON_BACK);
        
    }
    public static void onDragonDismount()
    {
        
        Minecraft.getInstance().options.setCameraType(previousPerspective);
    }
    public static void setMountCameraAngles(Camera camera)
    {
        if (Minecraft.getInstance().player.getVehicle() instanceof ErsPlayerRideable rideable)
        {
            if(!Minecraft.getInstance().options.getCameraType().isFirstPerson()){
                var offsets = getFixedCameraOffsets(Minecraft.getInstance().options.getCameraType() == CameraType.THIRD_PERSON_BACK);

                if(rideable instanceof TubunasusDurovelaEntity entity){
                    if(entity.getRushTimer() > 75){
                        offsets[0] = Math.min(offsets[0] + (double) (entity.getRushTimer() - 75) / 40, 8);
                        offsets[1] = Math.min(offsets[1] + (double) (entity.getRushTimer() - 75) / 40, 5);
                    }
                    if(entity.getRushTimer() > 167){
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
     * 提供固定的相机偏移值，不依赖配置文件
     * @param isBackView 是否是第三人称背后视角
     * @return 包含三个偏移值的数组：[距离, 垂直偏移, 水平偏移]
     */
    private static double[] getFixedCameraOffsets(boolean isBackView)
    {
        if (isBackView) {
            // 第三人称背后视角的固定值
            return new double[] {
                7.0,  // 距离 - 稍微拉远一点以获得更好的视野
                4.0,  // 垂直偏移 - 稍微提高以便看到更多前方区域
            };
        } else {
            // 第三人称前方视角的固定值
            return new double[] {
                7.0,   // 距离
                3.5,   // 垂直偏移
            };
        }
    }
}
