package cn.aurorian.ers.util;

import java.util.*;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * 游戏刻辅助工具类。
 *
 * <p>该类提供延迟执行任务的工具方法，允许在指定的游戏刻后运行代码。 这对于需要延迟触发的游戏逻辑非常有用，如攻击动画同步、特效延迟等。
 *
 * <p>主要功能：
 *
 * <ul>
 *   <li>{@link #addTask(int, Runnable)} - 在指定游戏刻执行任务
 *   <li>{@link #nextTick(Level, Runnable)} - 在下一游戏刻执行任务
 *   <li>{@link #tickLater(Level, int, Runnable)} - 在若干游戏刻后执行任务
 * </ul>
 *
 * <p>注意：此工具仅在服务器端有效，客户端调用会被忽略。
 *
 * @author mlus
 * @version 1.2.0-alpha
 */
@Mod.EventBusSubscriber
public class TickHelper {
    /** 存储待执行任务的映射表，键为游戏刻编号，值为任务列表 */
    private static final Map<Integer, List<Runnable>> tickTasks = new HashMap<>();

    /** 任务执行间隔计数器，用于避免在同一帧执行过多任务 */
    private static int tickTimerFsr = 0;

    /**
     * 添加一个延迟执行的任务。
     *
     * @param tick 目标游戏刻编号
     * @param task 要执行的任务
     */
    public static void addTask(int tick, Runnable task) {
        if (!tickTasks.containsKey(tick)) tickTasks.put(tick, new ArrayList<>());

        tickTasks.get(tick).add(task);
    }

    /**
     * 在下一游戏刻执行任务。
     *
     * @param level 游戏世界实例
     * @param task 要执行的任务
     */
    public static void nextTick(Level level, Runnable task) {
        addTask(Objects.requireNonNull(level.getServer()).getTickCount() + 1, task);
    }

    /**
     * 在指定数量的游戏刻后执行任务。
     *
     * @param level 游戏世界实例
     * @param tickNumber 延迟的游戏刻数量
     * @param task 要执行的任务
     */
    public static void tickLater(Level level, int tickNumber, Runnable task) {
        addTask(Objects.requireNonNull(level.getServer()).getTickCount() + tickNumber, task);
    }

    /**
     * 每个游戏刻执行一次，用于检查和运行待执行的任务。
     *
     * @param event 等级游戏刻事件
     */
    @SubscribeEvent
    static void runTasks(TickEvent.LevelTickEvent event) {
        if (event.level instanceof ServerLevel
                && tickTimerFsr == 0
                && tickTasks.containsKey(event.level.getServer().getTickCount())) {
            tickTasks.get(event.level.getServer().getTickCount()).forEach(Runnable::run);
            tickTasks.remove(event.level.getServer().getTickCount());

            tickTimerFsr += 3;
        } else if (tickTimerFsr > 0) tickTimerFsr--;
    }
}
