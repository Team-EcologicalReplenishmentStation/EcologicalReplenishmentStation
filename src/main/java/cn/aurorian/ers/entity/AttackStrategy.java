package cn.aurorian.ers.entity;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisAttackExecutor;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.SaevusAttackExecutor;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaAttackExecutor;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;

public class AttackStrategy {
    public static final AttackExecutor EMPTY_STRATEGY = (attack, mount) -> {};
    public static final AttackExecutor SWAMP_DRAGON_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_JUDGEMENT_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonJudgement(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_SPECIAL_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonSpecialAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_TURNING_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonTurningAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_CATCH_FISH_SMALL_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonCatchFishSmall(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_CATCH_FISH_MIDDLE_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonCatchFishMiddle(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor TUBUNASUS_ATTACK_STRATEGY = (attack, mount) -> TubunasusDurovelaAttackExecutor.getInstance().executeTubunasusAttack(attack, (TubunasusDurovelaEntity) mount);
    public static final AttackExecutor TUBUNASUS_TURNING_ATTACK_STRATEGY = (attack, mount) -> TubunasusDurovelaAttackExecutor.getInstance().executeTubunasusTurningAttack(attack, (TubunasusDurovelaEntity) mount);
    public static final AttackExecutor SAEVUS_ATTACK_STRATEGY = (attack, mount) -> SaevusAttackExecutor.getInstance().executeSaevusAttack(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SAEVUS_ATTACK_TURN_STRATEGY = (attack, mount) -> SaevusAttackExecutor.getInstance().executeSaevusTurningAttack(attack, (TerridensaurusSaevusEntity) mount);
}
