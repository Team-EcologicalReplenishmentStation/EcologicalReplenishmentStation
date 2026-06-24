package cn.aurorian.ers.data.language;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.*;
import cn.aurorian.oasis.init.OasisEntities;
import cn.aurorian.oasis.init.OasisItems;
import cn.aurorian.oasis.init.OasisMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.minecraftforge.common.data.LanguageProvider;
import net.minecraftforge.registries.ForgeRegistries;

public class ErsEnglishLanguageProvider extends LanguageProvider {

    public ErsEnglishLanguageProvider(PackOutput output) {
        super(output, EcologicalReplenishmentStation.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ErsItems.SWAMP_DRAGON_SADDLE.get(), "SwampDragon Saddle");
        add(ErsItems.DINOSAURIFORMIS_SADDLE.get(), "Aquicornis Saddle");
        add(ErsItems.SAEVUS_SADDLE.get(), "Terridensaurus Saevus Saddle");
        add(ErsItems.ANTIQUUS_SADDLE.get(), "Eosuchosaurus Antiquus Saddle");
        add(ErsItems.PTEROCHIRUS_DUX_SADDLE.get(), "Pterochirus Dux Saddle");
        add(ErsItems.CRISTATODROMEUS_BRACHYPTERUS_SADDLE.get(), "Cristatodromeus Brachypterus Saddle");
        add(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), "Dentisaurus longirostris");
        add(ErsEntities.AQUICORNIS_DINOSAURIFORMIS.get(), "Aquicornis dinosauriformis");
        add(ErsEntities.LATIMERIA_PERCOIDES.get(), "Latimeria percoides");
        add(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), "Latimeria suchomimus");
        add(ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(), "Magnidiscumyzon sarcopterus");
        add(ErsEntities.ARGENTUMNISCUS_ACICULAULAR.get(), "Argentirhynchus gadiformis");
        add(ErsEntities.MANDGEMARE_LABIUM.get(), "Mandgemare labium");
        add(ErsEntities.BENTHOSUCHUS_PLANIDENS.get(), "Benthosuchus planidens");
        add(ErsEntities.TACHYPLEUS_GLADIUS.get(), "Tachypleus gladius");
        add(ErsEntities.TACHYCARIS_GUSTATUS.get(), "Tachycaris gustatus");
        add(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), "Acanthodes chlamydoselachoides");
        add(ErsEntities.TERRIDENSAURUS_SAEVUS.get(), "Terridensaurus saevus");
        add(ErsEntities.EOSUCHOSAURUS_ANTIQUUS.get(), "Eosuchosaurus Antiquus");
        add(ErsEntities.ECHINOMORPHUS_CONVERGENS.get(), "Echinomorphus convergens");
        add(ErsEntities.PTEROCHIRUS_DUX.get(), "Pterochirus dux");
        add(ErsEntities.REMIPES_SICARIUS.get(), "Remipes sicarius");
        add(ErsEntities.PLESIOCHELYS_LONGICOLLIS.get(), "Plesiochelys longicollis");
        add(ErsEntities.CRISTATODROMEUS_BRACHYPTERUS.get(), "Cristatodromeus brachypterus");
        add(ErsEntities.DRAGON_CLAW_HARPOON.get(), "Dragon Claw Harpoon");
        add(ErsItems.DENTISAURUS_LONGIROSTRIS_SPAWN_EGG.get(), "Longirostris Spawn Egg");
        add(ErsItems.AQUICORNIS_DINOSAURIFORMIS_SPAWN_EGG.get(), "Aquicornis Spawn Egg");
        add(ErsItems.LATIMERIA_PERCOIDES_SPAWN_EGG.get(), "Percoides Spawn Egg");
        add(ErsItems.TACHYPLEUS_GLADIUS_SPAWN_EGG.get(), "Gladius Spawn Egg");
        add(ErsItems.TACHYCARIS_GUSTATUS_SPAWN_EGG.get(), "Tachycaris Gustatus Spawn Egg");
        add(ErsItems.CHLAMYDOSELACHOIDES_SPAWN_EGG.get(), "Chlamydoselachoides Spawn Egg");
        add(ErsItems.LATIMERIA_SUCHOMIMUS_SPAWN_EGG.get(), "Suchomimus Spawn Egg");
        add(ErsItems.TERRIDENSAURUS_SAEVUS_SPAWN_EGG.get(), "Saevus Spawn Egg");
        add(ErsItems.MAGNIDISCUMYZON_SARCOPTERUS_SPAWN_EGG.get(), "Sarcopterus Spawn Egg");
        add(ErsItems.ARGENTUMNISCUS_ACICULAULAR_SPAWN_EGG.get(), "Aciculular Spawn Egg");
        add(ErsItems.MANDGEMARE_LABIUM_SPAWN_EGG.get(), "Labium Spawn Egg");
        add(ErsItems.BENTHOSUCHUS_PLANIDENS_SPAWN_EGG.get(), "Benthosuchus planidens Spawn Egg");
        add(ErsItems.EOSUCHOSAURUS_ANTIQUUS_SPAWN_EGG.get(), "Eosuchosaurus Antiquus Spawn Egg");
        add(ErsItems.ECHINOMORPHUS_CONVERGENS_SPAWN_EGG.get(), "Echinomorphus Convergens Spawn Egg");
        add(ErsItems.PTEROCHIRUS_DUX_SPAWN_EGG.get(), "Pterochirus Dux Spawn Egg");
        add(ErsItems.REMIPES_SICARIUS_SPAWN_EGG.get(), "Remipes Sicarius Spawn Egg");
        add(ErsItems.PLESIOCHELYS_LONGICOLLIS_SPAWN_EGG.get(), "Plesiochelys longicollis Spawn Egg");
        add(ErsItems.CRISTATODROMEUS_BRACHYPTERUS_SPAWN_EGG.get(), "Cristatodromeus Brachypterus Spawn Egg");
        add(ErsItems.ECHINOMORPHUS_CONVERGENS_BUCKET.get(), "Echinomorphus Convergens Bucket");
        add(ErsItems.REMIPES_SICARIUS_BUCKET.get(), "Bucket of Remipes Sicarius");
        add(ErsItems.ECHINOMORPHUS_CONVERGENS_SHELL.get(), "Echinomorphus Convergens Shell");
        add(ErsCreativeTab.ERS_TAB.get().getDisplayName().getString(), "Ecological Replenishment Station");
        add(ErsKeyBindings.DIVE_KEY.getName(), "Mount Down");
        add(ErsKeyBindings.ATTACK_KEY.getName(), "Attack Skill");
        add(ErsKeyBindings.ATTACK2_KEY.getName(), "Judgement Skill");
        add(ErsKeyBindings.ATTACK3_KEY.getName(), "Special Skill");
        add(ErsKeyBindings.ATTACK4_KEY.getName(), "Turning Attack Skill");
        add(ErsKeyBindings.CATEGORY, "Ecological Replenishment Station");
        add(ErsItems.DENTISARUS_LONGIROSTRIS_LARGE_BUCKET.get(), "SwampDragon Large Bucket");
        add(ErsItems.AQUICORNIS_DINOSAURIFORMIS_LARGE_BUCKET.get(), "Aquicornis Large Bucket");
        add(ErsItems.CHLAMYDOSELACHOIDES_LARGE_BUCKET.get(), "Acanthodii Large Bucket");
        add(ErsItems.SUCHOMIMUS_LARGE_BUCKET.get(), "Suchomimus Large Bucket");
        add(ErsItems.PERCH_BUCKET.get(), "Sarcopterygii Perch Bucket");
        add(ErsItems.SARCOPTERUS_BUCKET.get(), "Sarcopterus Bucket");
        add(ErsItems.TACHYPLEUS_GLADIUS_BUCKET.get(), "Knife Horseshoe Crab Bucket");
        add(ErsItems.TACHYCARIS_GUSTATUS_BUCKET.get(), "Tachycaris Gustatus Bucket");
        add(ErsItems.ACICULABULAR_BUCKET.get(), "Argentirhynchus Bucket");
        add(ErsItems.LABIUM_BUCKET.get(), "Labium Bucket");
        add(ErsItems.BENTHOSUCHUS_PLANIDENS_LARGE_BUCKET.get(), "Benthosuchus planidens Large Bucket");
        add(ErsItems.PLESIOCHELYS_LONGICOLLIS_LARGE_BUCKET.get(), "Plesiochelys longicollis Large Bucket");
        add(ErsItems.LARGE_BUCKET.get(), "Large Bucket");
        add(ErsItems.LARGE_WATER_BUCKET.get(), "Water Large Bucket");
        add(ErsItems.GILDED_HORN.get(), "Gilded Horn");
        add(ErsItems.FILLED_GILDED_HORN.get(), "Filled Gilded Horn");
        add(ErsItems.FECES.get(), "Feces");
        add(ErsItems.BONE_FECES.get(), "Bone Feces");
        add(ErsItems.SMALL_FECES.get(), "Small Feces");
        add(ErsItems.LARGE_FECES.get(), "Large Feces");
        add(ErsItems.GLASSES_FECES.get(), "Glasses Feces");
        add(ErsItems.TEL_FECES.get(), "Tel Feces");
        add(ErsItems.SOUL_CUBE.get(), "Soul Cube");
        add(ErsBlocks.SWAMP_DRAGON_NEST.get(), "SwampDragon Nest");
        add(ErsBlocks.SAEVUS_NEST.get(), "Saevus Nest");
        add(ErsBlocks.DINOSAURIFORMIS_NEST.get(), "Dinosauriformis Nest");
        add(ErsBlocks.ANTIQUUS_NEST.get(), "Antiquus Nest");
        add(ErsBlocks.ARTIFICIAL_NEST.get(), "Artificial Nest");
        add(ErsBlocks.EQUISETUM.get(), "Equisetum");
        add(ErsItems.SWAMP_DRAGON_EGG.get(), "SwampDragon Egg");
        add(ErsItems.DINOSAURIFORMIS_EGG.get(), "Dinosauriformis Egg");
        add(ErsItems.SAEVUS_EGG.get(), "Saevus Egg");
        add(ErsItems.ANTIQUUS_EGG.get(), "Antiquus Egg");
        add(ErsItems.SOUL_CUBE_GIFT.get(), "Soul Cube Gift");
        add(ErsItems.CLOVER.get(), "Clover");
        add(ErsItems.BAIT_BOX.get(), "Bait Box");
        add(ErsItems.BULLY_STICK.get(), "Bully Stick");
        add(ErsItems.SCRATCHING_BOARD.get(), "Scratching Board");
        add(ErsItems.DRIED_FISH.get(), "Dried Fish");
        add(ErsItems.RIDING_GUIDE.get(), "Riding Guide");
        add(ErsItems.FISH_FILLET.get(), "Fish Fillet");
        add(ErsItems.COOKED_FISH_FILLET.get(), "Cooked Fish Fillet");
        add(ErsItems.MEAT_FEED.get(), "Meat Feed");
        add(ErsItems.FISH_FEED.get(), "Fish Feed");
        add(ErsItems.FRUIT_FEED.get(), "Fruit Feed");
        add(ErsItems.HAY_FEED.get(), "Hay Feed");
        add(ErsItems.VEGETABLE_FEED.get(), "Vegetable Feed");
        add(ErsItems.WORM_FEED.get(), "Worm Feed");
        add(ErsItems.PERCH.get(), "Perch");
        add(ErsItems.SUCHOMIMUS.get(), "Suchomimus");
        add(ErsItems.COOKED_SUCHOMIMUS.get(), "Cooked Suchomimus");
        add(ErsItems.COOKED_PERCH.get(), "Cooked Perch");
        add(ErsItems.HORSESHOE_CRAB_STINGER.get(), "Horseshoe Crab Stinger");
        add(ErsItems.SWAMP_DRAGON_CLAW.get(), "SwampDragon Claw");
        add(ErsItems.FLUORESCENCE_LENS.get(), "Fluorescence Lens");
        add(ErsItems.SWAMP_DRAGON_MEAT.get(), "SwampDragon Meat");
        add(ErsItems.COOKED_SWAMP_DRAGON_MEAT.get(), "Cooked SwampDragon Meat");
        add(ErsItems.HORSESHOE_CRAB_MEAT.get(), "Horseshoe Crab Meat");
        add(ErsItems.COOKED_HORSESHOE_CRAB_MEAT.get(), "Cooked Horseshoe Crab Meat");
        add(ErsItems.TACHYCARIS_GUSTATUS_MEAT.get(), "Tachycaris Gustatus Meat");
        add(ErsItems.REMIPES_SICARIUS_MEAT.get(), "Raw Remipes Sicarius Meat");
        add(ErsItems.COOKED_REMIPES_SICARIUS_MEAT.get(), "Cooked Remipes Sicarius Meat");
        add(ErsItems.CHLAMYDOSELACHOIDES.get(), "Acanthodii");
        add(ErsItems.COOKED_CHLAMYDOSELACHOIDES.get(), "Cooked Acanthodii");
        add(ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get(), "Acanthodii Tooth");
        add(ErsItems.SARCOPETERUS.get(), "Sarcopterus");
        add(ErsItems.COOKED_SARCOPETERUS.get(), "Cooked Sarcopterus");
        add(ErsItems.SAEVUS_MEAT.get(), "Saevus Meat");
        add(ErsItems.COOKED_SAEVUS_MEAT.get(), "Cooked Saevus Meat");
        add(ErsItems.CHLAMYDOSELACHOIDES_TOOTH_SWORD.get(), "Acanthodii Tooth Sword");
        add(ErsItems.DRAGON_BONE.get(), "Dragon Bone");
        add(ErsItems.DRAGON_BONE_FLUTE.get(), "Dragon Bone Flute");
        add(ErsItems.SOUL_FLUTE.get(), "Soul Flute");
        add(ErsItems.ACICULABULAR.get(), "Argentirhynchus");
        add(ErsItems.COOKED_ACICULABULAR.get(), "Cooked Argentirhynchus");
        add(ErsItems.LABIUM.get(), "Labium");
        add(ErsItems.COOKED_LABIUM.get(), "Cooked Labium");
        add(ErsItems.BENTHOSUCHUS_PLANIDENS.get(), "Benthosuchus planidens");
        add(ErsItems.COOKED_BENTHOSUCHUS_PLANIDENS.get(), "Cooked Benthosuchus planidens");
        add(ErsItems.PLESIOCHELYS_LONGICOLLIS.get(), "Plesiochelys longicollis Meat");
        add(ErsItems.COOKED_PLESIOCHELYS_LONGICOLLIS.get(), "Cooked Plesiochelys longicollis Meat");
        add(ErsItems.DINOSAURIFORMIS_MEAT.get(), "Dinosauriformis Meat");
        add(ErsItems.COOKED_DINOSAURIFORMIS_MEAT.get(), "Cooked Dinosauriformis Meat");
        add(ErsItems.ANTIQUUS_MEAT.get(), "Antiquus Meat");
        add(ErsItems.COOKED_ANTIQUE_MEAT.get(), "Cooked Antiquus Meat");
        add(ErsItems.DRAGON_CLAW_HARPOON.get(), "Dragon Claw Harpoon");
        add(ErsItems.TOURNIQUET.get(), "Tourniquet");
        add("config.jade.plugin_ers.age", "Show Tamable Age");
        add("config.jade.plugin_ers.food", "Show Tamable Hunger");
        add("config.jade.plugin_ers.hatch", "Show Egg Hatching Progress");
        add("config.jade.plugin_oasis.gender", "Show Creature Gender");
        add("tooltip.ers.age", "Age: %d days");
        add("tooltip.ers.food", "Hunger: %f");
        add("tooltip.ers.hatch", "Hatch Progress: %d%%");
        add("tooltip.oasis.gender.male", "§bMale");
        add("tooltip.oasis.gender.female", "§dFemale");
        add("ers.command.sit", "Siting now");
        add("ers.command.stand", "Standing now");
        add("ers.command.follow", "Following now");
        add("ers.taming.dinosauriformis.wait", "Dinosauriformis doesn't want to eat right now");
        add("ers.taming.dinosauriformis.tip", "Dinosauriformis wants to eat %s");
        add("commands.ers.age.success", "Set %s's age to %d days");
        add("commands.ers.age.failed", "%s is not a valid target, cannot set age.");
        add("commands.ers.elite.success", "Set %s to elite.");
        add("commands.ers.elite.failed", "%s is not a valid target, cannot set elite.");
        add("death.attack.bleeding", "%s bled to death");
        add("death.attack.bleeding.player", "%1$s died from bleeding while fighting %2$s");
        add(ErsMobEffects.BLEEDING.get(), "Bleeding");
        add(ErsMobEffects.COMFORT.get(), "Comfort");
        add(ErsMobEffects.FRACTURE.get(), "Fracture");

        addAdvancement("enter_world", "Enter the World", "Welcome to the world of Ecological Replenishment Station.");
        addAdvancement("swamp_dragon_egg", "Obtain a SwampDragon Egg", "Put it in an artificial nest to hatch it.");
        addAdvancement("tame_swamp_dragon", "Tame a SwampDragon", "feed it fish to raise it.");
        addAdvancement("saevus_egg", "Obtain a Terridensaurus Egg", "Put it in an artificial nest to hatch it.");
        addAdvancement("tame_saevus", "Tame a Terridensaurus Saevus", "feed it meat to raise it.");
        addTooltip(ErsItems.CLOVER.get(), "A symbol of good fortune. Grants the rider the Luck effect while mounted.");
        addTooltip(ErsItems.BULLY_STICK.get(), "A sturdy chew toy. Enhances the mount's bite attack when equipped.");
        addTooltip(
                ErsItems.SCRATCHING_BOARD.get(),
                "A rough scratching surface. Enhances the mount's claw attack when equipped.");
        addTooltip(
                ErsItems.RIDING_GUIDE.get(),
                "A comprehensive manual on proper riding technique. Grants immunity to most direct attacks while mounted.");
        addTooltip(ErsItems.RIDING_GUIDE.get(), "patchouli", "Install Patchouli to unlock more features");
        addTooltip(
                ErsItems.DRIED_FISH.get(),
                "§ePiscivores Only§r Salted fish jerky. Consumes one fish every 10s to automatically restore hunger and health.");
        addTooltip(
                ErsItems.BAIT_BOX.get(),
                "§ePiscivores Only§r A container full of fragrant bait. Increases fishing success rate when mounted.");

        add("ers.tamable.missing", "§cSeems Like your pet is not loading or missing, Last Tracking Location: %s");

        add("item.ers.book.name", "Ecological Replenishment Station Manual");
        add(
                "item.ers.book.landing_text",
                "After the divergence of evolution, we need to re-understand these organisms in our world.");
        //
        add("itemGroup.oasis_group", "Oasis");
        add(OasisEntities.TUBUNASUS_DUROVELA.get(), "Tubunasus durovela");
        add(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(), "Tubunasus clyderotunda");
        add(OasisEntities.PYGOPODUS_ANNULATUM.get(), "Pygopodus annulatum");
        add(OasisEntities.IMPERIOVENATOR_REGIUS.get(), "Imperiovenator regius");
        add("item.minecraft.potion.effect.embryo_healing", "Embryo Healing");
        add("item.minecraft.splash_potion.effect.embryo_healing", "Splash Embryo Healing");
        add("item.minecraft.lingering_potion.effect.embryo_healing", "Lingering Embryo Healing");
        add(OasisItems.TUBUNASUS_DUROVELA_SPAWN_EGG.get(), "Tubunasus durovela Spawn Egg");
        add(OasisItems.TUBUNASUS_CLYDEROTUNDA_SPAWN_EGG.get(), "Tubunasus clyderotunda Spawn Egg");
        add(OasisItems.PYGOPODUS_ANNULATUM_SPAWN_EGG.get(), "Pygopodus annulatum Spawn Egg");
        add(OasisItems.IMPERIOVENATOR_REGIUS_SPAWN_EGG.get(), "Imperiovenator regius Spawn Egg");
        add(OasisItems.TUBUNASUS_DUROVELA_LARGE_BUCKET.get(), "Durovela Large Bucket");
        add(OasisItems.TUBUNASUS_SADDLE.get(), "Tubunasus Saddle");
        add(OasisItems.HORSESHOE.get(), "Horseshoe");
        add(OasisItems.BONE.get(), "??? Bone");
        add(OasisItems.HEART.get(), "??? Heart");
        add(OasisItems.INTESTINES.get(), "??? Intestines");
        add(OasisItems.LEATHER.get(), "??? Leather");
        add(OasisItems.LIVER.get(), "??? Liver");
        add(OasisItems.LUNG.get(), "??? Lung");
        add(OasisItems.EMBRYO.get(), "??? Embryo");
        add(OasisItems.COOKED_LUNG.get(), "Cooked ??? Lung");
        add(OasisItems.KIDNEY.get(), "??? Kidney");
        add(OasisItems.COOKED_KIDNEY.get(), "Cooked ??? Kidney");
        add(OasisItems.ANNULATUM.get(), "Annulatum Meat");
        add(OasisItems.TUBUNASUS_MEAT.get(), "Tubunasus Meat");
        add(OasisItems.COOKED_ANNULATUM.get(), "Cooked Annulatum Meat");
        add(OasisItems.TEASELGOURD.get(), "Teaselgourd");
        add(OasisItems.CUDMILK.get(), "Cudmilk");
        add(OasisItems.EMPTY_MILK_BOTTLE.get(), "Empty Milk Bottle");
        add(OasisItems.DUROVELA_SPECIMEN.get(), "Durovela Specimen");
        add(OasisItems.CLYDEROTUNDA_SPECIMEN.get(), "Clyderotunda Specimen");
        add(OasisMobEffects.APHRODISIAC.get(), "Aphrodisiac");
        add(OasisMobEffects.BREATH_HOLD.get(), "BreathHold");
    }

    private void addAdvancement(String name, String title, String desc) {
        add("advancements." + EcologicalReplenishmentStation.MODID + "." + name + ".title", title);
        add("advancements." + EcologicalReplenishmentStation.MODID + "." + name + ".desc", desc);
    }

    private void addTooltip(Item item, String translation) {
        var key = ForgeRegistries.ITEMS.getKey(item);
        if (key != null) add("tooltip." + key.getNamespace() + "." + key.getPath(), translation);
    }

    private void addTooltip(Item item, String suffix, String translation) {
        var key = ForgeRegistries.ITEMS.getKey(item);
        if (key != null) add("tooltip." + key.getNamespace() + "." + key.getPath() + "." + suffix, translation);
    }
}
