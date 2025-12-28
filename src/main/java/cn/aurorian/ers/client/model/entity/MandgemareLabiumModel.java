package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.mandgemarelabium.MandgemareLabiumEntity;
import net.minecraft.resources.ResourceLocation;

public class MandgemareLabiumModel extends ErsModel<MandgemareLabiumEntity> {
    @Override
    public ResourceLocation getModelResource(MandgemareLabiumEntity entity) {
        if(entity.getGender()){
            return EcologicalReplenishmentStation.prefix("geo/entity/mandgemare_labium_male.geo.json");
        }else{
            return EcologicalReplenishmentStation.prefix("geo/entity/mandgemare_labium_female.geo.json");
        }
    }

    @Override
    public ResourceLocation getTextureResource(MandgemareLabiumEntity entity) {
        if(entity.getGender()){
            String base = "textures/entity/mandgemare_labium/";

            switch (entity.getTail().getId()) {
                case 0 -> base = base + "basic";
                case 1 -> base = base + "lion";
                case 2 -> base = base + "round";
            }

            switch (entity.getVariant().getId()) {
                case 0 -> base = base + "_white.png";
                case 1 -> base = base + "_black.png";
                case 2 -> base = base + "_yellow.png";
                case 3 -> base = base + "_blue.png";
                case 4 -> base = base + "_red.png";
                case 5 -> base = base + "_cyan.png";
                case 6 -> base = base + "_green.png";
            }
            return EcologicalReplenishmentStation.prefix(base);
        }else {
            return EcologicalReplenishmentStation.prefix("textures/entity/mandgemare_labium/female.png");
        }
    }
}
