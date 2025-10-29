package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<SoundEvent> TEL_RING = register("tel_ring");

    private static RegistryObject<SoundEvent> register(String sound) {
        return SOUND_EVENTS.register(sound, () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, sound)));
    }
}
