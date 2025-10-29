package cn.aurorian.ers.client.render.entity.layer;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import software.bernie.geckolib.renderer.GeoRenderer;
import software.bernie.geckolib.renderer.layer.AutoGlowingGeoLayer;

@OnlyIn(Dist.CLIENT)
public class SwampDragonEyeLayer<T extends DentisaurusLongirostrisEntity> extends AutoGlowingGeoLayer<T> {
    public SwampDragonEyeLayer(GeoRenderer<T> renderer) {
        super(renderer);
    }

    @Override
    protected ResourceLocation getTextureResource(T animatable) {
        String base = "textures/entity/dentisaurus_longirostris";

        if(animatable.isElite()){
            base = base + "_elite";
        }else if(!animatable.isMature()) {
            base = base + "_baby";
        }

        switch (animatable.getVariant().getId()) {
            case 3,4,5 -> base = base + "_lackyellow.png";
            case 0,1,2,6,7,8 -> base = base + ".png";
            case 9,10,11 -> base = base + "_white.png";
        }

        return ResourceLocation.fromNamespaceAndPath(EcologicalReplenishmentStation.MODID, base);
    }
}