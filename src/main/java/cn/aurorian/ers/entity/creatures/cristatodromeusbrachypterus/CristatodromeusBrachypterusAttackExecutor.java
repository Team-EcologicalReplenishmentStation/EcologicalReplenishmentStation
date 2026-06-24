package cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus;

import cn.aurorian.ers.entity.MobAttack;

public class CristatodromeusBrachypterusAttackExecutor {
    private static final CristatodromeusBrachypterusAttackExecutor INSTANCE =
            new CristatodromeusBrachypterusAttackExecutor();

    private CristatodromeusBrachypterusAttackExecutor() {}

    public static CristatodromeusBrachypterusAttackExecutor getInstance() {
        return INSTANCE;
    }

    public void executeKick(MobAttack attack, CristatodromeusBrachypterusEntity entity) {
        if (attack.triggered || attack.animatorTick != 15) {
            return;
        }

        attack.triggered = true;
        entity.performKickDamage();
    }
}
