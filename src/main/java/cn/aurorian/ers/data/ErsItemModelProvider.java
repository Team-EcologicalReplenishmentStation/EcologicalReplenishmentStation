package cn.aurorian.ers.data;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.ErsItems;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
public class ErsItemModelProvider extends ItemModelProvider {
    public ErsItemModelProvider(PackOutput output, ExistingFileHelper existingFileHelper){
        super(output, EcologicalReplenishmentStation.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        this.simpleItem(ErsItems.SWAMP_DRAGON_SADDLE.get());
        this.simpleItem(ErsItems.DENTISAURUS_LONGIROSTRIS_SPAWN_EGG.get());
        this.simpleItem(ErsItems.LATIMERIA_PERCOIDES_SPAWN_EGG.get());
        this.simpleItem(ErsItems.TACHYPLEUS_GLADIUS_SPAWN_EGG.get());
        this.simpleItem(ErsItems.CHLAMYDOSELACHOIDES_SPAWN_EGG.get());
        this.simpleItem(ErsItems.LATIMERIA_SUCHOMIMUS_SPAWN_EGG.get());
        this.simpleItem(ErsItems.PERCH_BUCKET.get());
        this.simpleItem(ErsItems.TACHYPLEUS_GLADIUS_BUCKET.get());
        this.simpleItem(ErsItems.SWAMP_DRAGON_LARGE_BUCKET.get());
        this.simpleItem(ErsItems.CHLAMYDOSELACHOIDES_LARGE_BUCKET.get());
        this.simpleItem(ErsItems.SUCHOMIMUS_LARGE_BUCKET.get());

        this.simpleItem(ErsItems.SWAMP_DRAGON_NEST.get());
        this.simpleItem(ErsItems.SWAMP_DRAGON_ARTIFICIAL_NEST.get());
        this.simpleItem(ErsItems.SWAMP_DRAGON_EGG.get());
        this.simpleItem(ErsItems.SAEVUS_EGG.get());

        this.simpleItem(ErsItems.EQUISETUM.get());

        this.simpleItem(ErsItems.LARGE_BUCKET.get());
        this.simpleItem(ErsItems.LARGE_WATER_BUCKET.get());
        this.simpleItem(ErsItems.BAIT_BOX.get());
        this.simpleItem(ErsItems.BULLY_STICK.get());
        this.simpleItem(ErsItems.CLOVER.get());
        this.simpleItem(ErsItems.COOKED_FISH_FILLET.get());
        this.simpleItem(ErsItems.COOKED_PERCH.get());
        this.simpleItem(ErsItems.DRIED_FISH.get());
        this.simpleItem(ErsItems.RIDING_GUIDE.get());
        this.simpleItem(ErsItems.SOUL_CUBE_GIFT.get());
        this.simpleItem(ErsItems.FISH_FILLET.get());
        this.simpleItem(ErsItems.HORSESHOE_CRAB_STINGER.get());
        this.simpleItem(ErsItems.SWAMP_DRAGON_CLAW.get());
        this.simpleItem(ErsItems.FLUORESCENCE_LENS.get());
        this.simpleItem(ErsItems.SWAMP_DRAGON_MEAT.get());
        this.simpleItem(ErsItems.COOKED_SWAMP_DRAGON_MEAT.get());
        this.simpleItem(ErsItems.HORSESHOE_CRAB_MEAT.get());
        this.simpleItem(ErsItems.COOKED_HORSESHOE_CRAB_MEAT.get());
        this.simpleItem(ErsItems.PERCH.get());
        this.simpleItem(ErsItems.SUCHOMIMUS.get());
        this.simpleItem(ErsItems.COOKED_SUCHOMIMUS.get());
        this.simpleItem(ErsItems.PISCIVORES_FEED.get());
        this.simpleItem(ErsItems.CHLAMYDOSELACHOIDES.get());
        this.simpleItem(ErsItems.COOKED_CHLAMYDOSELACHOIDES.get());
        this.simpleItem(ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get());
        this.simpleItem(ErsItems.DRAGON_CLAW_HARPOON_INVENTORY.get());
        this.simpleItem(ErsItems.TOURNIQUET.get());
        this.simpleItem(ErsItems.SCRATCHING_BOARD.get());
    }

    private void simpleItem(Item item) {
        String path = BuiltInRegistries.ITEM.getKey(item).getPath();
        this.withExistingParent(path, this.mcLoc("item/generated"))
                .texture("layer0", this.modLoc("item/" + path));
    }

    public void simpleBlockItem(Block block) {
        this.withExistingParent(this.blockName(block), this.modLoc("block/" + this.blockName(block)));
    }

    private String blockName(Block block) {
        return BuiltInRegistries.BLOCK.getKey(block).getPath();
    }
}
