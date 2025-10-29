package cn.aurorian.oasis.event;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.oasis.entity.pygopodusannulatum.PygopodusAnnulatumEntity;
import cn.aurorian.oasis.entity.tubunasusdurovela.TubunasusDurovelaEntity;
import cn.aurorian.oasis.init.OasisEntities;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = EcologicalReplenishmentStation.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class OasisEntityAttributeHandler{
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(OasisEntities.TUBUNASUS_DUROVELA.get(), TubunasusDurovelaEntity.createAttributes().build());
        event.put(OasisEntities.PYGOPODUS_ANNULATUM.get(), PygopodusAnnulatumEntity.createAttributes().build());
    }

}
