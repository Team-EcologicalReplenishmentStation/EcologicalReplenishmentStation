package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<SoundEvent> TEL_RING = register("tel_ring");

    public static final RegistryObject<SoundEvent> SAEVUS_ATTACK = register("saevus_attack");
    public static final RegistryObject<SoundEvent> SAEVUS_SWIM = register("saevus_swim");
    public static final RegistryObject<SoundEvent> SAEVUS_FOOTSTEP = register("saevus_footstep");
    public static final RegistryObject<SoundEvent> SAEVUS_STRIKE = register("saevus_strike");
    public static final RegistryObject<SoundEvent> SAEVUS_ROAR = register("saevus_roar");
    public static final RegistryObject<SoundEvent> SAEVUS_ATTACK_TURN = register("saevus_attack_turn");
    public static final RegistryObject<SoundEvent> SAEVUS_KNOCKDOWN = register("saevus_knockdown");

    private static RegistryObject<SoundEvent> register(String sound) {
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(EcologicalReplenishmentStation.prefix(sound)));
    }
}
