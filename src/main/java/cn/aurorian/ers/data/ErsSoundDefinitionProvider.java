package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsSounds;
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
        this.register(OasisSounds.DUROVELA_TUBUNASUS_HURT);
        this.register(OasisSounds.DUROVELA_TUBUNASUS_CALL);
    }

    public void register(RegistryObject<SoundEvent> soundEvent) {
        this.add(soundEvent, definition().with(sound(soundEvent.getId())));
    }

}
