package cn.aurorian.ers.entity;

public interface AttackExecutor {
    void execute(MobAttack attack, ErsTamableVehicle<?> entity);
}
