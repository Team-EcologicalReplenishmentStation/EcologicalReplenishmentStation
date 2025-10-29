package cn.aurorian.ers.entity;

public class MobRotDirection {
    private final RotDirection direction;
    private final boolean isLargeTurn;
    
    private MobRotDirection(RotDirection direction, boolean isLarge) {
        this.direction = direction;
        this.isLargeTurn = isLarge;
    }
    
    public static MobRotDirection of(RotDirection direction, boolean isLarge) {
        return new MobRotDirection(direction, isLarge);
    }
    
    public RotDirection getDirection() {
        return direction;
    }
    
    public boolean isLargeTurn() {
        return isLargeTurn;
    }
    public boolean isRight() {
        return direction == RotDirection.RIGHT;
    }
    public boolean isLeft() {
        return direction == RotDirection.LEFT;
    }
    public boolean isNone() {
        return direction == RotDirection.NONE;
    }
    
    public enum RotDirection {
        LEFT,
        RIGHT,
        NONE;

        RotDirection() {}
    }
}