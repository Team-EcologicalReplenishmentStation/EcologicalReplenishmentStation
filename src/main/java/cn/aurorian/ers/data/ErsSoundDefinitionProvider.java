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

        this.add(ErsSounds.SAEVUS_ATTACK, definition().with(
                sound(EcologicalReplenishmentStation.prefix("saevus/attack"))
        ));
        this.add(ErsSounds.SAEVUS_SWIM, definition().with(
                sound(EcologicalReplenishmentStation.prefix("saevus/swim"))
        ));
        this.add(ErsSounds.SAEVUS_FOOTSTEP, definition().with(
                sound(EcologicalReplenishmentStation.prefix("saevus/footstep"))
        ));
        this.add(ErsSounds.SAEVUS_STRIKE, definition().with(
                sound(EcologicalReplenishmentStation.prefix("saevus/strike"))
        ));
        this.add(ErsSounds.SAEVUS_ROAR, definition().with(
                sound(EcologicalReplenishmentStation.prefix("saevus/roar"))
        ));
        this.add(ErsSounds.SAEVUS_ATTACK_TURN, definition().with(
                sound(EcologicalReplenishmentStation.prefix("saevus/attack_turn"))
        ));
        this.add(ErsSounds.SAEVUS_KNOCKDOWN, definition().with(
                sound(EcologicalReplenishmentStation.prefix("saevus/knockdown"))
        ));

        this.add(OasisSounds.DUROVELA_FOOTSTEP, definition().with(
                sound(Oasis.prefix("durovela/footstep"))
        ));
        this.add(OasisSounds.DUROVELA_HURT, definition().with(
                sound(Oasis.prefix("durovela/hurt"))
        ));
        this.add(OasisSounds.DUROVELA_CALL, definition().with(
                sound(Oasis.prefix("durovela/call"))
        ));

        this.add(OasisSounds.ANNULATUM_WALK, definition().with(
                sound(Oasis.prefix("annulatum/walk"))
        ));

        this.add(OasisSounds.ANNULATUM_LOOK_AROUND, definition().with(
                sound(Oasis.prefix("annulatum/look_around"))
        ));
    }

    public void register(RegistryObject<SoundEvent> soundEvent) {
        this.add(soundEvent, definition().with(sound(soundEvent.getId())));
    }

}
