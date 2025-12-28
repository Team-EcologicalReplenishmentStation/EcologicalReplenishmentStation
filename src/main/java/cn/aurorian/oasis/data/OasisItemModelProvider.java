package cn.aurorian.oasis.data;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.init.OasisItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;

public class OasisItemModelProvider extends ItemModelProvider {

    public OasisItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, Oasis.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        //Oasis
        this.simpleItem(OasisItems.TUBUNASUS_DUROVELA_SPAWN_EGG.get());
        this.simpleItem(OasisItems.TUBUNASUS_CLYDEROTUNDA_SPAWN_EGG.get());
        this.simpleItem(OasisItems.PYGOPODUS_ANNULATUM_SPAWN_EGG.get());
        this.simpleItem(OasisItems.TUBUNASUS_DUROVELA_LARGE_BUCKET.get());
        this.simpleItem(OasisItems.TUBUNASUS_SADDLE.get());
        this.simpleItem(OasisItems.HORSESHOE.get());
        this.simpleItem(OasisItems.BONE.get());
        this.simpleItem(OasisItems.HEART.get());
        this.simpleItem(OasisItems.INTESTINES.get());
        this.simpleItem(OasisItems.LEATHER.get());
        this.simpleItem(OasisItems.LIVER.get());
        this.simpleItem(OasisItems.LUNG.get());
        this.simpleItem(OasisItems.EMBRYO.get());
        this.simpleItem(OasisItems.COOKED_LUNG.get());
        this.simpleItem(OasisItems.KIDNEY.get());
        this.simpleItem(OasisItems.COOKED_KIDNEY.get());
        this.simpleItem(OasisItems.ANNULATUM.get());
        this.simpleItem(OasisItems.COOKED_ANNULATUM.get());
        this.simpleItem(OasisItems.TEASELGOURD.get());

        this.simpleItem(OasisItems.DUROVELA_SPECIMEN.get());
//        this.simpleItem(OasisItems.CLYDEROTUNDA_SPECIMEN.get());
    }

    private void simpleItem(Item item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        this.withExistingParent(path, this.mcLoc("item/generated"))
                .texture("layer0", this.modLoc("item/" + path));
    }
}
