package cn.aurorian.ers.entity;

public interface ErsAttackType {
    String getId();

    String getAnimName();

    int getAnimationLength();

    boolean canMove();

    int getStaminaCost();

    boolean isTurning();

    boolean isCancelable();

    boolean isForcePlay();

    void execute(MobAttack attack, ErsTamable<?> mount);
}
