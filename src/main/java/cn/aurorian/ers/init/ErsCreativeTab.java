package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ErsCreativeTab {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, EcologicalReplenishmentStation.MODID);
    public static RegistryObject<CreativeModeTab> ERS_TAB = TABS.register("main", () -> CreativeModeTab.builder()
            .title(Component.translatable("itemGroup.ers_group"))
            .icon(() -> ErsItems.SWAMP_DRAGON_SADDLE.get().getDefaultInstance())
            .displayItems((par, output) -> {
                output.accept(ErsItems.DENTISAURUS_LONGIROSTRIS_SPAWN_EGG.get());
                output.accept(ErsItems.LATIMERIA_PERCOIDES_SPAWN_EGG.get());
                output.accept(ErsItems.TACHYPLEUS_GLADIUS_SPAWN_EGG.get());
                output.accept(ErsItems.CHLAMYDOSELACHOIDES_SPAWN_EGG.get());
                output.accept(ErsItems.LATIMERIA_SUCHOMIMUS_SPAWN_EGG.get());
                output.accept(ErsItems.TERRIDENSAURUS_SAEVUS_SPAWN_EGG.get());
                output.accept(ErsItems.MAGNIDISCUMYZON_SARCOPTERUS_SPAWN_EGG.get());
                output.accept(ErsItems.ARGENTUMNISCUS_ACICULAULAR_SPAWN_EGG.get());
                output.accept(ErsItems.MANDGEMARE_LABIUM_SPAWN_EGG.get());
                output.accept(ErsItems.DENTISARUS_LONGIROSTRIS_LARGE_BUCKET.get());
                output.accept(ErsItems.CHLAMYDOSELACHOIDES_LARGE_BUCKET.get());
                output.accept(ErsItems.SUCHOMIMUS_LARGE_BUCKET.get());
                output.accept(ErsItems.SARCOPTERUS_BUCKET.get());
                output.accept(ErsItems.PERCH_BUCKET.get());
                output.accept(ErsItems.TACHYPLEUS_GLADIUS_BUCKET.get());
                output.accept(ErsItems.ACICULABULAR_BUCKET.get());
                output.accept(ErsItems.LABIUM_BUCKET.get());
                output.accept(ErsItems.LARGE_BUCKET.get());
                output.accept(ErsItems.LARGE_WATER_BUCKET.get());
                output.accept(ErsItems.GILDED_HORN.get());
                output.accept(ErsItems.FILLED_GILDED_HORN.get());
                output.accept(ErsItems.SWAMP_DRAGON_EGG.get());
                output.accept(ErsItems.SAEVUS_EGG.get());
                output.accept(ErsItems.SWAMP_DRAGON_SADDLE.get());
                output.accept(ErsItems.SAEVUS_SADDLE.get());
                output.accept(ErsItems.FECES.get());
                output.accept(ErsItems.CLOVER.get());
                output.accept(ErsItems.BAIT_BOX.get());
                output.accept(ErsItems.BULLY_STICK.get());
                output.accept(ErsItems.SCRATCHING_BOARD.get());
                output.accept(ErsItems.DRIED_FISH.get());
                output.accept(ErsItems.RIDING_GUIDE.get());
                output.accept(ErsItems.SOUL_CUBE_GIFT.get());
                output.accept(ErsItems.FISH_FILLET.get());
                output.accept(ErsItems.COOKED_FISH_FILLET.get());
                output.accept(ErsItems.CARNIVORE_FEED.get());
                output.accept(ErsItems.PISCIVORES_FEED.get());
                output.accept(ErsItems.PERCH.get());
                output.accept(ErsItems.COOKED_PERCH.get());
                output.accept(ErsItems.SUCHOMIMUS.get());
                output.accept(ErsItems.COOKED_SUCHOMIMUS.get());
                output.accept(ErsItems.HORSESHOE_CRAB_STINGER.get());
                output.accept(ErsItems.SWAMP_DRAGON_CLAW.get());
                output.accept(ErsItems.FLUORESCENCE_LENS.get());
                output.accept(ErsItems.SWAMP_DRAGON_MEAT.get());
                output.accept(ErsItems.COOKED_SWAMP_DRAGON_MEAT.get());
                output.accept(ErsItems.HORSESHOE_CRAB_MEAT.get());
                output.accept(ErsItems.COOKED_HORSESHOE_CRAB_MEAT.get());
                output.accept(ErsItems.CHLAMYDOSELACHOIDES.get());
                output.accept(ErsItems.COOKED_CHLAMYDOSELACHOIDES.get());
                output.accept(ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get());
                output.accept(ErsItems.SARCOPETERUS.get());
                output.accept(ErsItems.COOKED_SARCOPETERUS.get());
                output.accept(ErsItems.SAEVUS_MEAT.get());
                output.accept(ErsItems.COOKED_SAEVUS_MEAT.get());
                output.accept(ErsItems.ACICULABULAR.get());
                output.accept(ErsItems.COOKED_ACICULABULAR.get());
                output.accept(ErsItems.LABIUM.get());
                output.accept(ErsItems.COOKED_LABIUM.get());
                output.accept(ErsItems.DRAGON_BONE.get());
                //
                output.accept(ErsItems.CHLAMYDOSELACHOIDES_TOOTH_SWORD.get());
                output.accept(ErsItems.DRAGON_CLAW_HARPOON.get());
                output.accept(ErsItems.DRAGON_BONE_FLUTE.get());
                output.accept(ErsItems.SOUL_FLUTE.get());
                //
                output.accept(ErsItems.TOURNIQUET.get());
                //Block
                output.accept(ErsItems.SOUL_CUBE.get());
                output.accept(ErsItems.SWAMP_DRAGON_NEST.get());
                output.accept(ErsItems.SAEVUS_NEST.get());
                output.accept(ErsItems.ARTIFICIAL_NEST.get());
                output.accept(ErsItems.EQUISETUM.get());
            }).build());
    public static void register(IEventBus eventBus) {
        TABS.register(eventBus);
    }
}
