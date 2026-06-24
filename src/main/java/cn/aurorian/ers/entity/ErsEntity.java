package cn.aurorian.ers.entity;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import net.minecraft.world.entity.Mob;
import software.bernie.geckolib.core.animatable.GeoAnimatable;

/**
 * 生态补给站模组实体的基础接口。
 *
 * <p>所有模组中的生物实体都需要实现此接口，以提供：
 *
 * <ul>
 *   <li>动画控制器
 *   <li>音量设置
 *   <li>声音范围设置
 * </ul>
 *
 * <p>该接口结合了Minecraft的{@link Mob}和GeckoLib的{@link GeoAnimatable}，
 * 使得生物既拥有Minecraft的实体行为，又拥有GeckoLib的3D模型动画能力。
 *
 * @param <T> 实体类型，必须同时继承Mob和实现GeoAnimatable
 * @author mlus
 * @version 1.2.0-alpha
 * @see Mob
 * @see GeoAnimatable
 */
public interface ErsEntity<T extends Mob & GeoAnimatable> {

    /**
     * 获取该实体的动画控制器。
     *
     * @return 通用动画控制器实例
     */
    GeneralAnimator<T> getAnimator();

    /**
     * 获取该实体播放声音的音量。
     *
     * @return 音量大小，默认为1.0f
     */
    default float getVolume() {
        return 1f;
    }

    /**
     * 获取该实体播放声音的有效范围。
     *
     * @return 声音传播范围（方块数），默认为25f
     */
    default float getSoundRange() {
        return 25f;
    }
}
