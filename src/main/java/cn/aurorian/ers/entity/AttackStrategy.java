package cn.aurorian.ers.entity;

import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisAttackExecutor;
import cn.aurorian.ers.entity.creatures.aquicornisdinosauriformis.AquicornisDinosauriformisEntity;
import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.BenthosuchusPlanidensPlanidensEntity;
import cn.aurorian.ers.entity.creatures.benthosuchusplanidens.PlanidensAttackExecutor;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusAttackExecutor;
import cn.aurorian.ers.entity.creatures.cristatodromeusbrachypterus.CristatodromeusBrachypterusEntity;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisAttackExecutor;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusAttackExecutor;
import cn.aurorian.ers.entity.creatures.eosuchosaurusantiquus.EosuchosaurusAntiquusEntity;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.SuchomimusAttackExecutor;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.SaevusAttackExecutor;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusAttackExecutor;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusAttackExecutor;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class AttackStrategy {
    public static final AttackExecutor EMPTY_STRATEGY = (attack, mount) -> {};

    public static final AttackExecutor SWAMP_DRAGON_ATTACK_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_JUDGEMENT_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeJudgement(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_SPECIAL_ATTACK_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeSpecialAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_TURNING_ATTACK_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeTurningAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_JUMP_ATTACK_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeJumpAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_AFTER_JUMP_ATTACK_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeAfterJumpLand(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_CATCH_FISH_SMALL_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeCatchFishSmall(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_CATCH_FISH_MIDDLE_STRATEGY =
            (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance()
                    .executeCatchFishMiddle(attack, (DentisaurusLongirostrisEntity) mount);

    public static final AttackExecutor TUBUNASUS_ATTACK_STRATEGY = (attack, mount) ->
            TubunasusAttackExecutor.getInstance().executeAttack(attack, (ErsTamableVehicle<?>) mount);
    public static final AttackExecutor TUBUNASUS_TURNING_ATTACK_STRATEGY = (attack, mount) ->
            TubunasusAttackExecutor.getInstance().executeTurningAttack(attack, (ErsTamableVehicle<?>) mount);
    public static final AttackExecutor TUBUNASUS_STRUGGLE_STRATEGY = ((attack, entity) ->
            TubunasusAttackExecutor.getInstance().executeStruggle(attack, (ErsTamableVehicle<?>) entity));

    public static final AttackExecutor SAEVUS_ATTACK_STRATEGY = (attack, mount) ->
            SaevusAttackExecutor.getInstance().executeAttack(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SAEVUS_STRIKE_STRATEGY = (attack, mount) ->
            SaevusAttackExecutor.getInstance().executeStrike(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SAEVUS_ROAR_STRATEGY = (attack, mount) ->
            SaevusAttackExecutor.getInstance().executeRoar(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SAEVUS_ATTACK_TURN_STRATEGY = (attack, mount) ->
            SaevusAttackExecutor.getInstance().executeTurningAttack(attack, (TerridensaurusSaevusEntity) mount);

    public static final AttackExecutor SUCHOMIMUS_HOLD_STRATEGY = ((attack, entity) ->
            SuchomimusAttackExecutor.getInstance().executeHold(attack, (LatimeriaSuchomimusEntity) entity));

    public static final AttackExecutor PLANIDENS_HOLD_STRATEGY = ((attack, entity) ->
            PlanidensAttackExecutor.getInstance().executeHold(attack, (BenthosuchusPlanidensPlanidensEntity) entity));

    public static final AttackExecutor DINOSAURIFORMIS_ATTACK_STRATEGY =
            (attack, mount) -> AquicornisDinosauriformisAttackExecutor.getInstance()
                    .executeAttack(attack, (AquicornisDinosauriformisEntity) mount);

    public static final AttackExecutor ANTIQUUS_ATTACK_STRATEGY =
            (attack, entity) -> EosuchosaurusAntiquusAttackExecutor.getInstance()
                    .executeAttack(attack, (EosuchosaurusAntiquusEntity) entity);
    public static final AttackExecutor ANTIQUUS_ATTACK_TURN_STRATEGY =
            (attack, mount) -> EosuchosaurusAntiquusAttackExecutor.getInstance()
                    .executeTurningAttack(attack, (EosuchosaurusAntiquusEntity) mount);

    public static final AttackExecutor CRISTATODROMEUS_KICK_STRATEGY =
            (attack, entity) -> CristatodromeusBrachypterusAttackExecutor.getInstance()
                    .executeKick(attack, (CristatodromeusBrachypterusEntity) entity);

    public static final AttackExecutor REGUIS_ATTACK_STRATEGY = (attack, mount) ->
            ImperiovenatorRegiusAttackExecutor.getInstance().executeAttack(attack, (ErsTamableVehicle<?>) mount);
    public static final AttackExecutor REGUIS_ATTACK_TURN_STRATEGY = (attack, mount) ->
            ImperiovenatorRegiusAttackExecutor.getInstance().executeTurningAttack(attack, (ErsTamableVehicle<?>) mount);
    public static final AttackExecutor REGUIS_ROAR_STRATEGY = (attack, mount) ->
            ImperiovenatorRegiusAttackExecutor.getInstance().executeRoar(attack, (ErsTamableVehicle<?>) mount);
    public static final AttackExecutor REGUIS_JUMP_ATTACK_STRATEGY = (attack, mount) ->
            ImperiovenatorRegiusAttackExecutor.getInstance().executeJumpAttack(attack, (ErsTamableVehicle<?>) mount);

    public static final AttackExecutor FALL_DOWN_STRATEGY = (attack, mount) -> {
        if (attack.animatorTick != AttackType.KNOCK_DOWN_LEFT.getAnimationLength()) return;

        if (mount instanceof DentisaurusLongirostrisEntity || mount instanceof TerridensaurusSaevusEntity) {
            mount.getAttackState().animatorTick = 60;
        }

        if (mount instanceof EosuchosaurusAntiquusEntity) {
            mount.getAttackState().animatorTick = 35;
        }

        mount.setSprinting(false);

        if (mount instanceof ErsTamableVehicle<?> vehicle) {
            SimpleContainer inventory = vehicle.getInventory();
            mount.getNavigation().stop();

            if (inventory != null) {
                for (int i = 1; i <= 3; i++) {
                    ItemStack itemStack = inventory.getItem(i);
                    if (!itemStack.isEmpty()) {
                        if (itemStack.is(ErsItems.RIDING_GUIDE.get())) {
                            return;
                        }
                    }
                }
            }
            mount.ejectPassengers();
        }
    };
}
