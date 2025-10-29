package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.data.PackOutput;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class ErsBlockStateProvider extends BlockStateProvider{
    
    public ErsBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, EcologicalReplenishmentStation.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        
    }
    
}
