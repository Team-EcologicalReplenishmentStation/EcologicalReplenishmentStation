package cn.aurorian.ers.util;


import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ErsUtils {
    /**
     * 计算基于年龄的缩放值
     * 使用对数函数来实现随时间增长但增长速度逐渐减缓的效果
     * 
     * @param ageInDays 年龄（天数）
     * @return 缩放值，在达到DAYS_TO_FULL_SIZE天时为1.0
     */
    public static float calculateRenderSize(int ageInDays) {
        if (ageInDays < 20) {
            return 0.8f + (0.2f * ageInDays / 20f);
        } else {
            return (float) (Math.log(ageInDays + 350) / Math.log(300));
        }
    }

    public static float calculateBabyRenderSize(int ageInDays) {
        return 0.7f + (0.4f * ageInDays / 20f);
    }

    /**
     * Returns the directional vector from start to end
     */
    public static Vec3 directionVecTo(Entity start, Entity end) {
        return end.position().subtract(start.position());
    }

    public static boolean isMoving(Mob mob) {
        return mob.getX() != mob.xOld || mob.getZ() != mob.zOld;
    }

    public static float rotlerp(float pSourceAngle, float pTargetAngle, float pMaximumChange) {
        float f = Mth.wrapDegrees(pTargetAngle - pSourceAngle);
        if (f > pMaximumChange) {
            f = pMaximumChange;
        }

        if (f < -pMaximumChange) {
            f = -pMaximumChange;
        }

        float f1 = pSourceAngle + f;
        if (f1 < 0.0F) {
            f1 += 360.0F;
        } else if (f1 > 360.0F) {
            f1 -= 360.0F;
        }

        return f1;
    }

    public static float getDirectionAngle(float x, float z) {
        // 从正前方为0度，顺时针旋转计算角度
        double angle = (90 - Math.toDegrees(Math.atan2(z, x))) % 360;
        // 确保角度在0-360度范围内
        if (angle < 0) angle += 360;
        return (float)angle;
    }

    /***
     * Returns an integer value from 0 (brightest) to 11 (darkest).
     * @return darkness
     */

    public static int updateSkyBrightness(Level level) {
        double d0 = 1.0 - (double)(level.getRainLevel(1.0F) * 5.0F) / 16.0;
        double d1 = 1.0 - (double)(level.getThunderLevel(1.0F) * 5.0F) / 16.0;
        double d2 = 0.5 + 2.0 * Mth.clamp(Mth.cos(level.getTimeOfDay(1.0F) * 6.2831855F), -0.25, 0.25);
        return (int)((1.0 - d2 * d0 * d1) * 11.0);
    }
}