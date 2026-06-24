package cn.aurorian.oasis.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.oasis.Oasis;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Oasis.MODID);

    public static final RegistryObject<SoundEvent> TUBUNASUS_WALK = register("tubunasus_walk");
    public static final RegistryObject<SoundEvent> DUROVELA_HURT = register("tubunasus_hurt");
    public static final RegistryObject<SoundEvent> CLYDEROTUNDA_HURT = register("clyderotunda_hurt");
    public static final RegistryObject<SoundEvent> DUROVELA_CALL = register("durovela_call");
    public static final RegistryObject<SoundEvent> CLYDEROTUNDA_CALL = register("clyderotunda_call");
    public static final RegistryObject<SoundEvent> ANNULATUM_WALK = register("annulatum_walk");
    public static final RegistryObject<SoundEvent> ANNULATUM_LOOK_AROUND = register("annulatum_look_around");
    public static final RegistryObject<SoundEvent> REGIUS_ROAR = register("regius_roar");
    public static final RegistryObject<SoundEvent> REGIUS_ATTACK = register("regius_attack");
    public static final RegistryObject<SoundEvent> REGIUS_ATTACK_TURN = register("regius_attack_turn");
    public static final RegistryObject<SoundEvent> REGIUS_HOLD_ATTACK_AIR = register("regius_hold_attack_air");
    public static final RegistryObject<SoundEvent> REGIUS_HOLD_ATTACK_GROUND = register("regius_hold_attack_ground");
    public static final RegistryObject<SoundEvent> REGIUS_KNOCKDOWN_LEFT = register("regius_knockdown_left");
    public static final RegistryObject<SoundEvent> REGIUS_KNOCKDOWN_RIGHT = register("regius_knockdown_right");
    public static final RegistryObject<SoundEvent> REGIUS_HURT = register("regius_hurt");

    private static RegistryObject<SoundEvent> register(String sound) {
        return SOUND_EVENTS.register(
                sound, () -> SoundEvent.createVariableRangeEvent(EcologicalReplenishmentStation.prefix(sound)));
    }
}
