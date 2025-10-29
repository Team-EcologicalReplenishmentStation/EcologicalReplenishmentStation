package cn.aurorian.oasis.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.oasis.Oasis;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, Oasis.MODID);

    public static final RegistryObject<SoundEvent> DUROVELA_TUBUNASUS_HURT = register("durovela_tubunasus_hurt");
    public static final RegistryObject<SoundEvent> DUROVELA_TUBUNASUS_CALL = register("durovela_tubunasus_call");

    private static RegistryObject<SoundEvent> register(String sound) {
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, sound)));
    }
}
