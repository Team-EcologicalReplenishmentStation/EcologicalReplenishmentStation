package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsSounds;
import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.init.OasisSounds;
import net.minecraft.data.PackOutput;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.common.data.SoundDefinitionsProvider;
import net.minecraftforge.registries.RegistryObject;

public class ErsSoundDefinitionProvider extends SoundDefinitionsProvider {

    public ErsSoundDefinitionProvider(PackOutput output, ExistingFileHelper helper) {
        super(output, EcologicalReplenishmentStation.MODID, helper);
    }

    @Override
    public void registerSounds() {
        this.register(ErsSounds.TEL_RING);

        this.add(
                ErsSounds.LONGIROSTRIS_WALK,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/walk"))));
        this.add(
                ErsSounds.LONGIROSTRIS_RUN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/run"))));
        this.add(
                ErsSounds.LONGIROSTRIS_TURN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/turn"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK_SWIM_TURN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack_swim_turn"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK_TURN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack_turn"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK_IDLE,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack-idle"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK_IDLE2,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack-idle2"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK2_SMALL,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack2-small"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK2_MIDDLE,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack2-middle"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK2_MISS,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack2-miss"))));
        this.add(
                ErsSounds.LONGIROSTRIS_ATTACK3,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/attack3"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_ATTACK2,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_attack2"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_ATTACK,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_attack"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_ATTACK_MISS,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_attack-miss"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_ATTACKMISS,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_attack-miss"))));
        this.add(
                ErsSounds.LONGIROSTRIS_KNOCKDOWN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/knockdown"))));
        this.add(
                ErsSounds.LONGIROSTRIS_JUMP_ATTACK,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/jump_attack"))));
        this.add(
                ErsSounds.LONGIROSTRIS_CATCH_FISH_SMALL,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/catch_fish-small"))));
        this.add(
                ErsSounds.LONGIROSTRIS_CATCH_FISH_MIDDLE,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/catch_fish-middle"))));
        this.add(
                ErsSounds.LONGIROSTRIS_QUICKLY_SWIMMING,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/quickly_swimming"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_UP,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_up"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_DOWN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_down"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_MID,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_mid"))));
        this.add(
                ErsSounds.LONGIROSTRIS_SWIM_SHALLOW,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/swim_shallow"))));
        this.add(
                ErsSounds.LONGIROSTRIS_HURT,
                definition()
                        .with(
                                sound(EcologicalReplenishmentStation.prefix("longirostris/hurt1")),
                                sound(EcologicalReplenishmentStation.prefix("longirostris/hurt2"))));
        this.add(
                ErsSounds.LONGIROSTRIS_IDLE2,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/idle2"))));
        this.add(
                ErsSounds.LONGIROSTRIS_IDLE3,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/idle3"))));
        this.add(
                ErsSounds.LONGIROSTRIS_IDLE_SIT3,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/idle_sit3"))));
        this.add(
                ErsSounds.LONGIROSTRIS_IDLE_SIT4,
                definition().with(sound(EcologicalReplenishmentStation.prefix("longirostris/idle_sit4"))));

        this.add(
                ErsSounds.SAEVUS_ATTACK,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/attack"))));
        this.add(ErsSounds.SAEVUS_SWIM, definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/swim"))));

        this.add(
                ErsSounds.SAEVUS_FOOTSTEP,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/footstep"))));
        this.add(ErsSounds.SAEVUS_TURN, definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/turn"))));
        this.add(
                ErsSounds.SAEVUS_IDLE2,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/idle2"))));
        this.add(
                ErsSounds.SAEVUS_IDLE3,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/idle3"))));
        this.add(
                ErsSounds.SAEVUS_IDLE_SIT2,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/idle_sit2"))));
        this.add(
                ErsSounds.SAEVUS_IDLE_SIT3,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/idle_sit3"))));
        this.add(
                ErsSounds.SAEVUS_STRIKE,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/strike"))));
        this.add(ErsSounds.SAEVUS_ROAR, definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/roar"))));
        this.add(
                ErsSounds.SAEVUS_ATTACK_TURN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/attack_turn"))));
        this.add(
                ErsSounds.SAEVUS_KNOCKDOWN,
                definition().with(sound(EcologicalReplenishmentStation.prefix("saevus/knockdown"))));

        this.add(OasisSounds.TUBUNASUS_WALK, definition().with(sound(Oasis.prefix("tubunasus/walk"))));
        this.add(OasisSounds.DUROVELA_HURT, definition().with(sound(Oasis.prefix("tubunasus/durovela_hurt"))));
        this.add(OasisSounds.DUROVELA_CALL, definition().with(sound(Oasis.prefix("tubunasus/durovela_call"))));
        this.add(OasisSounds.CLYDEROTUNDA_HURT, definition().with(sound(Oasis.prefix("tubunasus/clyderotunda_hurt"))));
        this.add(OasisSounds.CLYDEROTUNDA_CALL, definition().with(sound(Oasis.prefix("tubunasus/clyderotunda_call"))));

        this.add(OasisSounds.ANNULATUM_WALK, definition().with(sound(Oasis.prefix("annulatum/walk"))));

        this.add(OasisSounds.ANNULATUM_LOOK_AROUND, definition().with(sound(Oasis.prefix("annulatum/look_around"))));

        this.add(OasisSounds.REGIUS_ROAR, definition().with(sound(Oasis.prefix("regius/roar"))));

        this.add(OasisSounds.REGIUS_ATTACK, definition().with(sound(Oasis.prefix("regius/attack"))));

        this.add(OasisSounds.REGIUS_ATTACK_TURN, definition().with(sound(Oasis.prefix("regius/attack_turn"))));

        this.add(OasisSounds.REGIUS_HOLD_ATTACK_AIR, definition().with(sound(Oasis.prefix("regius/hold_attack_air"))));

        this.add(
                OasisSounds.REGIUS_HOLD_ATTACK_GROUND,
                definition().with(sound(Oasis.prefix("regius/hold_attack_ground"))));

        this.add(OasisSounds.REGIUS_KNOCKDOWN_LEFT, definition().with(sound(Oasis.prefix("regius/knockdown_left"))));

        this.add(OasisSounds.REGIUS_KNOCKDOWN_RIGHT, definition().with(sound(Oasis.prefix("regius/knockdown_right"))));

        this.add(
                OasisSounds.REGIUS_HURT,
                definition().with(sound(Oasis.prefix("regius/hurt1")), sound(Oasis.prefix("regius/hurt2"))));
    }

    public void register(RegistryObject<SoundEvent> soundEvent) {
        this.add(soundEvent, definition().with(sound(soundEvent.getId())));
    }
}
