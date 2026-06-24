package cn.aurorian.ers.entity;

/**
 * 生物转向方向类。
 *
 * <p>用于表示可驯服生物的转向状态，主要用于坐骑的转向攻击动画。 该类封装了转向方向（向左/向右/无）和转向幅度（普通/大幅）。
 *
 * <p>使用示例：
 *
 * <pre>
 * // 创建一个大幅左转
 * MobRotDirection leftTurn = MobRotDirection.of(MobRotDirection.RotDirection.LEFT, true);
 *
 * // 创建一个普通右转
 * MobRotDirection rightTurn = MobRotDirection.of(MobRotDirection.RotDirection.RIGHT, false);
 *
 * // 创建一个无转向
 * MobRotDirection noneTurn = MobRotDirection.of(MobRotDirection.RotDirection.NONE, false);
 * </pre>
 *
 * @author mlus
 * @version 1.2.0-alpha
 */
public class MobRotDirection {

    /** 转向方向 */
    private final RotDirection direction;

    /** 是否为大幅转向 */
    private final boolean isLargeTurn;

    /**
     * 私有构造函数。
     *
     * @param direction 转向方向
     * @param isLarge 是否为大幅转向
     */
    private MobRotDirection(RotDirection direction, boolean isLarge) {
        this.direction = direction;
        this.isLargeTurn = isLarge;
    }

    /**
     * 工厂方法，创建转向方向实例。
     *
     * @param direction 转向方向
     * @param isLarge 是否为大幅转向
     * @return 转向方向实例
     */
    public static MobRotDirection of(RotDirection direction, boolean isLarge) {
        return new MobRotDirection(direction, isLarge);
    }

    /**
     * 获取转向方向。
     *
     * @return 转向方向枚举值
     */
    public RotDirection getDirection() {
        return direction;
    }

    /**
     * 判断是否为大幅转向。
     *
     * @return true表示大幅转向
     */
    public boolean isLargeTurn() {
        return isLargeTurn;
    }

    /**
     * 判断是否为向右转向。
     *
     * @return true表示向右
     */
    public boolean isRight() {
        return direction == RotDirection.RIGHT;
    }

    /**
     * 判断是否为向左转向。
     *
     * @return true表示向左
     */
    public boolean isLeft() {
        return direction == RotDirection.LEFT;
    }

    /**
     * 判断是否为无转向。
     *
     * @return true表示无转向
     */
    public boolean isNone() {
        return direction == RotDirection.NONE;
    }

    /** 转向方向枚举。 */
    public enum RotDirection {
        /** 向左 */
        LEFT,
        /** 向右 */
        RIGHT,
        /** 无转向 */
        NONE;

        RotDirection() {}
    }
}
