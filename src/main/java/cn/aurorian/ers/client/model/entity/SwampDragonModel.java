package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import cn.aurorian.ers.molang.ErsMolang;
import java.util.HashMap;
import java.util.Map;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.core.molang.MolangParser;

public class SwampDragonModel extends ErsModel<DentisaurusLongirostrisEntity> {

    @Override
    public ResourceLocation getModelResource(DentisaurusLongirostrisEntity entity) {
        if (!entity.isMature()) {
            return EcologicalReplenishmentStation.prefix("geo/entity/dentisaurus_longirostris_baby.geo.json");
        } else if (entity.isElite()) {
            return EcologicalReplenishmentStation.prefix("geo/entity/dentisaurus_longirostris_elite.geo.json");
        }
        return super.getModelResource(entity);
    }

    @Override
    public ResourceLocation getTextureResource(DentisaurusLongirostrisEntity animatable) {
        String base = "textures/entity/dentisaurus_longirostris/";

        if (animatable.isElite()) {
            base = base + "elite";
            if (animatable.hasCustomName()) {
                String suffix =
                        switch (animatable.getCustomName().getString()) {
                            case "Ladon" -> "_ladon";
                            case "Forsaken" -> "_forsaken";
                            case "Acheron_Pollux" -> "_acheron_pollux";
                            case "Nakishimo" -> "_nakishimo";
                            case "Carpodacus dubius" -> "_carpodacusdubius";
                            case "krsn_crow" -> "_krsn_crow";
                            case "sunfyre" -> "_sunfyre";
                            case "U3UUU" -> "_u3uuu";
                            case "Kai_Sylph" -> "_kai_sylph";
                            case "Skadi" -> "_skadi";
                            case "Bearer of the flowing red train" -> "_bearer_of_the_flowing_red_train";
                            default -> null;
                        };
                if (suffix != null) return EcologicalReplenishmentStation.prefix(base + suffix + ".png");
            }

        } else if (!animatable.isMature()) {
            base = base + "baby";
            if (animatable.hasCustomName()) {
                String suffix =
                        switch (animatable.getCustomName().getString()) {
                            case "krsn_crow" -> "_krsn_crow";
                            case "Kai_Sylph" -> "_kai_sylph";
                            case "Carpodacus dubius" -> "_carpodacusdubius";
                            default -> null;
                        };
                if (suffix != null) return EcologicalReplenishmentStation.prefix(base + suffix + ".png");
            }
        } else {
            base = base + "base";
            if (animatable.hasCustomName()) {
                String suffix =
                        switch (animatable.getCustomName().getString()) {
                            case "profound" -> "_profound";
                            case "krsn_crow" -> "_krsn_crow";
                            case "Nekorizu" -> "_nekorizu";
                            default -> null;
                        };
                if (suffix != null) return EcologicalReplenishmentStation.prefix(base + suffix + ".png");
            }
        }

        switch (animatable.getVariant().getId()) {
            case 3, 4, 5 -> base = base + "_lackyellow";
            case 6, 7, 8 -> base = base + "_black";
            case 9, 10, 11 -> base = base + "_white";
            case 0, 1, 2 -> {}
        }

        if (animatable.isMature()) {
            switch (animatable.getVariant().getId()) {
                case 0, 3, 6, 9 -> base = base + ".png";
                case 1, 4, 7, 10 -> base = base + "_light.png";
                case 2, 5, 8, 11 -> base = base + "_deep.png";
            }
        } else {
            base = base + ".png";
        }

        return EcologicalReplenishmentStation.prefix(base);
    }

    @Override
    public ResourceLocation getAnimationResource(DentisaurusLongirostrisEntity entity) {
        if (!entity.isMature())
            return EcologicalReplenishmentStation.prefix(
                    "animations/entity/dentisaurus_longirostris_baby.animation.json");
        else return super.getAnimationResource(entity);
    }

    private static final Map<Integer, Integer> angleCache = new HashMap<>();

    @Override
    public void applyMolangQueries(DentisaurusLongirostrisEntity animatable, double animTime) {
        super.applyMolangQueries(animatable, animTime);
        MolangParser parser = MolangParser.INSTANCE;
        angleCache.putIfAbsent(animatable.getId(), 0);
        if (animatable.isVehicle())
            parser.setMemoizedValue(ErsMolang.MOVE_TURN, () -> angleCache.get(animatable.getId()));
        Integer angle = angleCache.get(animatable.getId());

        if (animatable.getRotDirection().isNone()) {
            parser.setMemoizedValue(ErsMolang.V_TURN, () -> 0);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> 0);
            if (angle > 0) angle--;
            else if (angle < 0) angle++;
        } else if (animatable.getRotDirection().isRight()) {
            if (animatable.isMature()) parser.setMemoizedValue(ErsMolang.V_TURN, () -> -55);
            else parser.setMemoizedValue(ErsMolang.V_TURN, () -> 55);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> -100);

            angle = Math.max(-30, --angle);

        } else if (animatable.getRotDirection().isLeft()) {
            if (animatable.isMature()) parser.setMemoizedValue(ErsMolang.V_TURN, () -> 55);
            else parser.setMemoizedValue(ErsMolang.V_TURN, () -> -55);
            parser.setMemoizedValue(ErsMolang.W_TURN, () -> 100);
            angle = Math.min(30, ++angle);
        }

        angleCache.put(animatable.getId(), angle);
    }
}
