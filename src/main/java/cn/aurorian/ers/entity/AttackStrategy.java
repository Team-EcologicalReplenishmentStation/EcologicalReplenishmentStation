package cn.aurorian.ers.entity;

import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisAttackExecutor;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.SuchomimusAttackExecutor;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.SaevusAttackExecutor;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.init.ErsItems;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusAttackExecutor;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.item.ItemStack;

public class AttackStrategy {
    public static final AttackExecutor EMPTY_STRATEGY = (attack, mount) -> {};
    public static final AttackExecutor SWAMP_DRAGON_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_JUDGEMENT_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonJudgement(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_SPECIAL_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonSpecialAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_TURNING_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonTurningAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_JUMP_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonJumpAttack(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_AFTER_JUMP_ATTACK_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonAfterJumpLand(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_CATCH_FISH_SMALL_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonCatchFishSmall(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor SWAMP_DRAGON_CATCH_FISH_MIDDLE_STRATEGY = (attack, mount) -> DentisaurusLongirostrisAttackExecutor.getInstance().executeSwampDragonCatchFishMiddle(attack, (DentisaurusLongirostrisEntity) mount);
    public static final AttackExecutor TUBUNASUS_ATTACK_STRATEGY = (attack, mount) -> TubunasusAttackExecutor.getInstance().executeTubunasusAttack(attack, (ErsTamableVehicle<?>) mount);
    public static final AttackExecutor TUBUNASUS_TURNING_ATTACK_STRATEGY = (attack, mount) -> TubunasusAttackExecutor.getInstance().executeTubunasusTurningAttack(attack, (ErsTamableVehicle<?>) mount);
    public static final AttackExecutor SAEVUS_ATTACK_STRATEGY = (attack, mount) -> SaevusAttackExecutor.getInstance().executeSaevusAttack(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SAEVUS_STRIKE_STRATEGY = (attack, mount) -> SaevusAttackExecutor.getInstance().executeSaevusStrike(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SAEVUS_ROAR_STRATEGY = (attack, mount) -> SaevusAttackExecutor.getInstance().executeSaevusRoar(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SAEVUS_ATTACK_TURN_STRATEGY = (attack, mount) -> SaevusAttackExecutor.getInstance().executeSaevusTurningAttack(attack, (TerridensaurusSaevusEntity) mount);
    public static final AttackExecutor SUCHOMIMUS_HOLD_STRATEGY = ((attack, entity) -> SuchomimusAttackExecutor.getInstance().executeSuchomimusHold(attack, (LatimeriaSuchomimusEntity) entity));
    public static final AttackExecutor FALL_DOWN_STRATEGY = (attack, mount) -> {
        if(attack.animatorTick != AttackType.KNOCK_DOWN_LEFT.getAnimationLength())
            return;

        if(mount instanceof DentisaurusLongirostrisEntity || mount instanceof TerridensaurusSaevusEntity){
           mount.getAttackState().animatorTick = 60;
        }

        mount.setSprinting(false);

        if(mount instanceof ErsTamableVehicle<?> vehicle){
            SimpleContainer inventory = vehicle.getInventory();
            mount.getNavigation().stop();

            if(inventory != null){
                for(int i = 1; i <= 3; i++){
                    ItemStack itemStack = inventory.getItem(i);
                    if(!itemStack.isEmpty()){
                        if(itemStack.is(ErsItems.RIDING_GUIDE.get())){
                            return;
                        }
                    }
                }
            }
            mount.ejectPassengers();
        }
    };
}
