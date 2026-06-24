package cn.aurorian.ers.entity;

public class SimpleAttackType implements ErsAttackType {
    private final String id;
    private final String animName;
    private final int animationLength;
    private final AttackExecutor executor;
    private final boolean canMove;
    private final int stamina;
    private final boolean turning;
    private final boolean cancelable;
    private final boolean forcePlay;

    public SimpleAttackType(String id, String animName, int animationLength, AttackExecutor executor, boolean canMove) {
        this(id, animName, animationLength, executor, canMove, 0, false, false, false);
    }

    public SimpleAttackType(
            String id,
            String animName,
            int animationLength,
            AttackExecutor executor,
            boolean canMove,
            int stamina,
            boolean turning,
            boolean cancelable,
            boolean forcePlay) {
        this.id = id;
        this.animName = animName;
        this.animationLength = animationLength;
        this.executor = executor;
        this.canMove = canMove;
        this.stamina = stamina;
        this.turning = turning;
        this.cancelable = cancelable;
        this.forcePlay = forcePlay;
    }

    public static SimpleAttackType synced(
            String id,
            String animName,
            int animationLength,
            boolean canMove,
            int stamina,
            boolean turning,
            boolean cancelable,
            boolean forcePlay) {
        return new SimpleAttackType(
                id,
                animName,
                animationLength,
                AttackStrategy.EMPTY_STRATEGY,
                canMove,
                stamina,
                turning,
                cancelable,
                forcePlay);
    }

    @Override
    public String getId() {
        return id;
    }

    @Override
    public String getAnimName() {
        return animName;
    }

    @Override
    public int getAnimationLength() {
        return animationLength;
    }

    @Override
    public boolean canMove() {
        return canMove;
    }

    @Override
    public int getStaminaCost() {
        return stamina;
    }

    @Override
    public boolean isTurning() {
        return turning;
    }

    @Override
    public boolean isCancelable() {
        return cancelable;
    }

    @Override
    public boolean isForcePlay() {
        return forcePlay;
    }

    @Override
    public void execute(MobAttack attack, ErsTamable<?> mount) {
        executor.execute(attack, mount);
    }
}
