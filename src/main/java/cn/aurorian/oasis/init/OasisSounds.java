package cn.aurorian.oasis.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.oasis.Oasis;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Oasis.MODID);

    public static final RegistryObject<SoundEvent> DUROVELA_FOOTSTEP = register("durovela_footstep");
    public static final RegistryObject<SoundEvent> DUROVELA_HURT = register("durovela_hurt");
    public static final RegistryObject<SoundEvent> DUROVELA_CALL = register("durovela_call");
    public static final RegistryObject<SoundEvent> ANNULATUM_WALK = register("annulatum_walk");
    public static final RegistryObject<SoundEvent> ANNULATUM_LOOK_AROUND = register("annulatum_look_around");

    private static RegistryObject<SoundEvent> register(String sound) {
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(EcologicalReplenishmentStation.prefix(sound)));
    }
}
