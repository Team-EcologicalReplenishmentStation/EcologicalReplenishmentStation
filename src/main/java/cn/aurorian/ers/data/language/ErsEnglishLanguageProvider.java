package cn.aurorian.ers.data.language;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.*;
import cn.aurorian.oasis.init.OasisEntities;
import cn.aurorian.oasis.init.OasisItems;
import cn.aurorian.oasis.init.OasisMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ErsEnglishLanguageProvider extends LanguageProvider {

    public ErsEnglishLanguageProvider(PackOutput output) {
        super(output, EcologicalReplenishmentStation.MODID, "en_us");
    }

    @Override
    protected void addTranslations() {
        add(ErsItems.SWAMP_DRAGON_SADDLE.get(), "SwampDragon Saddle");
        add(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), "Dentisaurus longirostris");
        add(ErsEntities.LATIMERIA_PERCOIDES.get(), "Latimeria percoides");
        add(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), "Latimeria suchomimus");
        add(ErsEntities.TACHYPLEUS_GLADIUS.get(), "Tachypleus gladius");
        add(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), "Acanthodes chlamydoselachoides");
        add(ErsEntities.TERRIDENSAURUS_SAEVUS.get(),"Terridensaurus saevus");
        add(ErsEntities.DRAGON_CLAW_HARPOON.get(), "Dragon Claw Harpoon");
        add(ErsItems.DENTISAURUS_LONGIROSTRIS_SPAWN_EGG.get(), "Longirostris Spawn Egg");
        add(ErsItems.LATIMERIA_PERCOIDES_SPAWN_EGG.get(),"Percoides Spawn Egg");
        add(ErsItems.TACHYPLEUS_GLADIUS_SPAWN_EGG.get(), "Gladius Spawn Egg");
        add(ErsItems.CHLAMYDOSELACHOIDES_SPAWN_EGG.get(), "Chlamydoselachoides Spawn Egg");
        add(ErsItems.LATIMERIA_SUCHOMIMUS_SPAWN_EGG.get(), "Suchomimus Spawn Egg");
        add(ErsCreativeTab.ERS_TAB.get().getDisplayName().getString(), "Ecological Replenishment Station");
        add(ErsKeyBindings.DIVE_KEY.getName(), "Mount Down");
        add(ErsKeyBindings.ATTACK_KEY.getName(), "Attack Skill");
        add(ErsKeyBindings.ATTACK2_KEY.getName(), "Judgement Skill");
        add(ErsKeyBindings.ATTACK3_KEY.getName(), "Special Skill");
        add(ErsKeyBindings.ATTACK4_KEY.getName(), "Turning Attack Skill");
        add(ErsKeyBindings.CATEGORY, "Ecological Replenishment Station");
        add(ErsItems.SWAMP_DRAGON_LARGE_BUCKET.get(), "SwampDragon Large Bucket");
        add(ErsItems.CHLAMYDOSELACHOIDES_LARGE_BUCKET.get(), "Acanthodii Large Bucket");
        add(ErsItems.SUCHOMIMUS_LARGE_BUCKET.get(), "Suchomimus Large Bucket");
        add(ErsItems.PERCH_BUCKET.get(), "Sarcopterygii Perch Bucket");
        add(ErsItems.TACHYPLEUS_GLADIUS_BUCKET.get(), "Knife Horseshoe Crab Bucket");
        add(ErsItems.LARGE_BUCKET.get(), "Large Bucket");
        add(ErsItems.LARGE_WATER_BUCKET.get(), "Water Large Bucket");
        add(ErsItems.SWAMP_DRAGON_FECES.get(), "SwampDragon Feces");
        add(ErsItems.SWAMP_DRAGON_BONE_FECES.get(), "SwampDragon Bone Feces");
        add(ErsItems.SWAMP_DRAGON_SMALL_FECES.get(), "SwampDragon Small Feces");
        add(ErsItems.SWAMP_DRAGON_LARGE_FECES.get(), "SwampDragon Large Feces");
        add(ErsItems.SWAMP_DRAGON_GLASSES_FECES.get(), "SwampDragon Glasses Feces");
        add(ErsItems.SWAMP_DRAGON_TEL_FECES.get(), "SwampDragon Tel Feces");
        add(ErsItems.SOUL_CUBE.get(), "Soul Cube");
        add(ErsBlocks.SWAMP_DRAGON_NEST.get(), "SwampDragon Nest");
        add(ErsBlocks.SWAMP_DRAGON_ARTIFICIAL_NEST.get(), "SwampDragon Artificial Nest");
        add(ErsBlocks.EQUISETUM.get(),"Equisetum");
        add(ErsItems.SWAMP_DRAGON_EGG.get(), "SwampDragon Egg");
        add(ErsItems.SAEVUS_EGG.get(), "Terridensaurus Egg");
        add(ErsItems.SOUL_CUBE_GIFT.get(), "Soul Cube Gift");
        add(ErsItems.CLOVER.get(),"Clover");
        add(ErsItems.BAIT_BOX.get(), "Bait Box");
        add(ErsItems.BULLY_STICK.get(), "Bully Stick");
        add(ErsItems.SCRATCHING_BOARD.get(), "Scratching Board");
        add(ErsItems.DRIED_FISH.get(), "Dried Fish");
        add(ErsItems.RIDING_GUIDE.get(), "Riding Guide");
        add(ErsItems.FISH_FILLET.get(), "Fish Fillet");
        add(ErsItems.COOKED_FISH_FILLET.get(), "Cooked Fish Fillet");
        add(ErsItems.PISCIVORES_FEED.get(), "Piscivores Feed");
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
        add(ErsItems.CHLAMYDOSELACHOIDES.get(), "Acanthodii");
        add(ErsItems.COOKED_CHLAMYDOSELACHOIDES.get(), "Cooked Acanthodii");
        add(ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get(), "Acanthodii Tooth");
        add(ErsItems.CHLAMYDOSELACHOIDES_TOOTH_SWORD.get(), "Acanthodii Tooth Sword");
        add(ErsItems.DRAGON_CLAW_HARPOON.get(), "Dragon Claw Harpoon");
        add(ErsItems.TOURNIQUET.get(), "Tourniquet");
        add("config.jade.plugin_ers.age", "Show Tamable Age");
        add("config.jade.plugin_ers.food", "Show Tamable Hunger");
        add("config.jade.plugin_ers.hatch", "Show Egg Hatching Progress");
        add("tooltip.ers.age", "Age: %d days");
        add("tooltip.ers.food", "Hunger: %f");
        add("tooltip.ers.hatch", "Hatch Progress: %d%%");
        add("ers.command.sit","Siting now");
        add("ers.command.stand","Standing now");
        add("ers.command.follow","Following now");
        add("commands.ers.age.success", "Set %s's age to %d days");
        add("commands.ers.age.failed", "%s is not a valid target, cannot set age.");
        add("commands.ers.elite.success", "Set %s to elite.");
        add("commands.ers.elite.failed", "%s is not a valid target, cannot set elite.");
        add("death.attack.bleeding", "%s bled to death");
        add("death.attack.bleeding.player", "%1$s died from bleeding while fighting %2$s");
        add(ErsMobEffects.BLEEDING.get(), "Bleeding");
        add(ErsMobEffects.FRACTURE.get(), "Fracture");

        addAdvancement("enter_world", "Enter the World", "Welcome to the world of Ecological Replenishment Station.");
        addAdvancement("swamp_dragon_egg", "Obtain a SwampDragon Egg", "Put it in an artificial nest to hatch it.");
        addAdvancement("tame_swamp_dragon", "Tame a SwampDragon", "feed it fish to raise it.");
       //
        add("itemGroup.oasis_group", "Oasis");
        add(OasisEntities.TUBUNASUS_DUROVELA.get(), "Tubunasus durovela");
        add(OasisEntities.PYGOPODUS_ANNULATUM.get(), "Pygopodus annulatum");
        add("item.minecraft.potion.effect.embryo_healing", "Embryo Healing");
        add("item.minecraft.splash_potion.effect.embryo_healing", "Splash Embryo Healing");
        add("item.minecraft.lingering_potion.effect.embryo_healing", "Lingering Embryo Healing");
        add(OasisItems.TUBUNASUS_DUROVELA_SPAWN_EGG.get(), "Tubunasus durovela Spawn Egg");
        add(OasisItems.PYGOPODUS_ANNULATUM_SPAWN_EGG.get(), "Pygopodus annulatum Spawn Egg");
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
        add(OasisItems.ANNULATUM.get(),"Annulatum Meat");
        add(OasisItems.COOKED_ANNULATUM.get(),"Cooked Annulatum Meat");
        add(OasisItems.TEASELGOURD.get(),"Teaselgourd");
        add(OasisMobEffects.APHRODISIAC.get(), "Aphrodisiac");
        add(OasisMobEffects.BREATH_HOLD.get(), "BreathHold");
    }

    private void addAdvancement(String name, String title, String desc){
        add("advancements." + EcologicalReplenishmentStation.MODID  + "." + name + ".title", title);
        add("advancements." + EcologicalReplenishmentStation.MODID  + "." + name + ".desc", desc);
    }
    
}
