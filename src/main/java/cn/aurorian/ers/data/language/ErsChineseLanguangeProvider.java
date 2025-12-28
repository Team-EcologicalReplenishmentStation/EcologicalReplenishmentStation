package cn.aurorian.ers.data.language;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.init.*;
import cn.aurorian.oasis.init.OasisEntities;
import cn.aurorian.oasis.init.OasisItems;
import cn.aurorian.oasis.init.OasisMobEffects;
import net.minecraft.data.PackOutput;
import net.minecraftforge.common.data.LanguageProvider;

public class ErsChineseLanguangeProvider extends LanguageProvider {
    public ErsChineseLanguangeProvider(PackOutput output) {
        super(output, EcologicalReplenishmentStation.MODID, "zh_cn");
    }
    @Override
    protected void addTranslations() {
        add(ErsItems.SWAMP_DRAGON_SADDLE.get(), "泽龙鞍");
        add(ErsItems.SAEVUS_SADDLE.get(),"恐齿龙鞍");
        add(ErsEntities.DENTISAURUS_LONGIROSTRIS.get(), "裳尾泽龙");
        add(ErsEntities.LATIMERIA_PERCOIDES.get(), "肉鳍鳜");
        add(ErsEntities.LATIMERIA_SUCHOMIMUS.get(), "肉鳍鳄䲢");
        add(ErsEntities.MAGNIDISCUMYZON_SARCOPTERUS.get(),"大攀鳅");
        add(ErsEntities.ARGENTUMNISCUS_ACICULAULAR.get(),"银光鱵鳕");
        add(ErsEntities.MANDGEMARE_LABIUM.get(),"迷宫宝石鲷");
        add(ErsEntities.TACHYPLEUS_GLADIUS.get(), "匕首鲎");
        add(ErsEntities.ACANTHODES_CHLAMYDOSELACHOIDES.get(), "绞口棘鱼");
        add(ErsEntities.TERRIDENSAURUS_SAEVUS.get(),"恐齿龙");
        add(ErsEntities.DRAGON_CLAW_HARPOON.get(), "龙爪鱼叉");
        add(ErsItems.DENTISAURUS_LONGIROSTRIS_SPAWN_EGG.get(), "泽龙生成蛋");
        add(ErsItems.LATIMERIA_PERCOIDES_SPAWN_EGG.get(),"肉鳍鳜生成蛋");
        add(ErsItems.TACHYPLEUS_GLADIUS_SPAWN_EGG.get(), "匕首鲎生成蛋");
        add(ErsItems.CHLAMYDOSELACHOIDES_SPAWN_EGG.get(), "绞口棘鱼生成蛋");
        add(ErsItems.LATIMERIA_SUCHOMIMUS_SPAWN_EGG.get(), "肉鳍鳄䲢生成蛋");
        add(ErsItems.TERRIDENSAURUS_SAEVUS_SPAWN_EGG.get(),"恐齿龙生成蛋");
        add(ErsItems.MAGNIDISCUMYZON_SARCOPTERUS_SPAWN_EGG.get(),"大攀鳅生成蛋");
        add(ErsItems.ARGENTUMNISCUS_ACICULAULAR_SPAWN_EGG.get(),"银光鱵鳕生成蛋");
        add(ErsItems.MANDGEMARE_LABIUM_SPAWN_EGG.get(),"迷宫宝石鲷生成蛋");
        add(ErsCreativeTab.ERS_TAB.get().getDisplayName().getString(), "生态补给站");
        add(ErsKeyBindings.DIVE_KEY.getName(), "坐骑 下潜");
        add(ErsKeyBindings.ATTACK_KEY.getName(), "攻击技能");
        add(ErsKeyBindings.ATTACK2_KEY.getName(), "处决技能");
        add(ErsKeyBindings.ATTACK3_KEY.getName(), "特殊攻击技能");
        add(ErsKeyBindings.ATTACK4_KEY.getName(), "转向攻击技能");
        add(ErsKeyBindings.CATEGORY, "生态补给站");
        add(ErsItems.DENTISARUS_LONGIROSTRIS_LARGE_BUCKET.get(), "泽龙大桶");
        add(ErsItems.CHLAMYDOSELACHOIDES_LARGE_BUCKET.get(), "绞口棘鱼大桶");
        add(ErsItems.SUCHOMIMUS_LARGE_BUCKET.get(), "肉鳍鳄䲢大桶");
        add(ErsItems.SARCOPTERUS_BUCKET.get(),"大攀鳅桶");
        add(ErsItems.PERCH_BUCKET.get(), "肉鳍鳜桶");
        add(ErsItems.TACHYPLEUS_GLADIUS_BUCKET.get(), "匕首鲎桶");
        add(ErsItems.ACICULABULAR_BUCKET.get(), "银光鱵鳕桶");
        add(ErsItems.LABIUM_BUCKET.get(),"迷宫宝石鲷桶");
        add(ErsItems.LARGE_BUCKET.get(), "大桶");
        add(ErsItems.LARGE_WATER_BUCKET.get(), "大水桶");
        add(ErsItems.GILDED_HORN.get(), "镶金号角");
        add(ErsItems.FILLED_GILDED_HORN.get(), "填充的镶金号角");
        add(ErsItems.FECES.get(), "粪便");
        add(ErsItems.BONE_FECES.get(), "带骨粪便");
        add(ErsItems.SMALL_FECES.get(), "小型粪便");
        add(ErsItems.LARGE_FECES.get(), "大型粪便");
        add(ErsItems.GLASSES_FECES.get(), "带眼镜粪便");
        add(ErsItems.TEL_FECES.get(), "带电话粪便");
        add(ErsItems.SOUL_CUBE.get(), "灵魂魔方");
        add(ErsBlocks.SWAMP_DRAGON_NEST.get(), "泽龙巢穴");
        add(ErsBlocks.SAEVUS_NEST.get(),"恐齿龙巢穴");
        add(ErsBlocks.ARTIFICIAL_NEST.get(), "人工巢穴");
        add(ErsBlocks.EQUISETUM.get(),"木贼");
        add(ErsItems.SWAMP_DRAGON_EGG.get(), "泽龙蛋");
        add(ErsItems.SAEVUS_EGG.get(),"恐齿龙蛋");
        add(ErsItems.SOUL_CUBE_GIFT.get(), "灵魂魔方赐物");
        add(ErsItems.CLOVER.get(),"幸运草");
        add(ErsItems.BAIT_BOX.get(), "鱼饵箱");
        add(ErsItems.BULLY_STICK.get(), "磨牙棒");
        add(ErsItems.SCRATCHING_BOARD.get(), "抓挠板");
        add(ErsItems.DRIED_FISH.get(), "小鱼干");
        add(ErsItems.RIDING_GUIDE.get(), "骑乘指南");
        add(ErsItems.FISH_FILLET.get(), "鱼段");
        add(ErsItems.COOKED_FISH_FILLET.get(), "熟鱼段");
        add(ErsItems.CARNIVORE_FEED.get(), "肉食动物饲料");
        add(ErsItems.PISCIVORES_FEED.get(), "食鱼动物饲料");
        add(ErsItems.PERCH.get(), "肉鳍鳜");
        add(ErsItems.SUCHOMIMUS.get(), "肉鳍鳄䲢");
        add(ErsItems.COOKED_SUCHOMIMUS.get(), "熟肉鳍鳄䲢");
        add(ErsItems.COOKED_PERCH.get(), "熟肉鳍鳜");
        add(ErsItems.HORSESHOE_CRAB_STINGER.get(), "鲎刺");
        add(ErsItems.SWAMP_DRAGON_CLAW.get(), "泽龙尖爪");
        add(ErsItems.FLUORESCENCE_LENS.get(), "荧光晶状体");
        add(ErsItems.SWAMP_DRAGON_MEAT.get(), "泽龙肉");
        add(ErsItems.COOKED_SWAMP_DRAGON_MEAT.get(), "熟泽龙肉");
        add(ErsItems.HORSESHOE_CRAB_MEAT.get(), "鲎肉");
        add(ErsItems.COOKED_HORSESHOE_CRAB_MEAT.get(), "熟鲎肉");
        add(ErsItems.CHLAMYDOSELACHOIDES.get(), "绞口棘鱼");
        add(ErsItems.COOKED_CHLAMYDOSELACHOIDES.get(), "熟绞口棘鱼");
        add(ErsItems.CHLAMYDOSELACHOIDES_TOOTH.get(), "棘鱼牙齿");
        add(ErsItems.SARCOPETERUS.get(),"大攀鳅");
        add(ErsItems.COOKED_SARCOPETERUS.get(),"熟大攀鳅");
        add(ErsItems.SAEVUS_MEAT.get(),"恐齿龙肉");
        add(ErsItems.COOKED_SAEVUS_MEAT.get(),"熟恐齿龙肉");
        add(ErsItems.DRAGON_BONE.get(),"龙骨");
        add(ErsItems.DRAGON_BONE_FLUTE.get(),"龙骨笛");
        add(ErsItems.SOUL_FLUTE.get(),"灵魂笛");
        add(ErsItems.ACICULABULAR.get(),"银光鱵鳕");
        add(ErsItems.COOKED_ACICULABULAR.get(),"熟银光鱵鳕");
        add(ErsItems.LABIUM.get(),"迷宫宝石鲷");
        add(ErsItems.COOKED_LABIUM.get(),"熟迷宫宝石鲷");
        add(ErsItems.CHLAMYDOSELACHOIDES_TOOTH_SWORD.get(), "棘齿剑");
        add(ErsItems.DRAGON_CLAW_HARPOON.get(), "龙爪鱼叉");
        add(ErsItems.TOURNIQUET.get(), "止血带");
        add("config.jade.plugin_ers.age", "显示驯服生物年龄");
        add("config.jade.plugin_ers.food", "显示驯服生物饥饿值");
        add("config.jade.plugin_ers.hatch", "显示孵化进度");
        add("config.jade.plugin_oasis.gender", "显示生物性别");
        add("tooltip.ers.age", "年龄: %d 天");
        add("tooltip.ers.food", "食物值: %f");
        add("tooltip.ers.hatch", "孵化进度: %d%%");
        add("tooltip.oasis.gender.male", "§b雄性");
        add("tooltip.oasis.gender.female", "§d雌性");
        add("ers.command.sit","坐下");
        add("ers.command.stand","站立");
        add("ers.command.follow","跟随");
        add("commands.ers.age.success", "设置 %s 年龄至 %d 天");
        add("commands.ers.age.failed", "非法对象 %s ,无法设置年龄.");
        add("commands.ers.elite.success", "设置 %s 为精英个体.");
        add("commands.ers.elite.failed", "非法对象 %s ,无法设置为精英个体.");
        add("death.attack.bleeding", "%s流血而死");
        add("death.attack.bleeding.player", "%1$s在与%2$s战斗时流血而死");
        add(ErsMobEffects.BLEEDING.get(), "流血");
        add(ErsMobEffects.FRACTURE.get(), "骨折");

        add("ers.tamable.missing", "§c你的宠物似乎消失或者卸载了,最后追踪位置: %s");

        add("item.ers.book.name","生态补给站手册");
        add("item.ers.book.landing_text","在进化分异之后，我们需要重新认识这些生物体。");

        addAdvancement("enter_world", "生态补给站", "欢迎来到生态补给站的世界!");
        addAdvancement("swamp_dragon_egg", "获得泽龙蛋", "将其放入人工巢穴中孵化.");
        addAdvancement("tame_swamp_dragon", "驯服泽龙", "使用鱼肉或食鱼动物饲料喂养它.");
        addAdvancement("saevus_egg", "获得恐齿龙蛋", "将其放入人工巢穴中孵化.");
        addAdvancement("tame_saevus", "驯服恐齿龙", "使用肉类或肉食动物饲料喂养它.");

        add("itemGroup.oasis_group", "绿洲");
        add("item.minecraft.potion.effect.embryo_healing", "胚胎治疗");
        add("item.minecraft.splash_potion.effect.embryo_healing", "溅射型胚胎治疗");
        add("item.minecraft.lingering_potion.effect.embryo_healing", "滞留型胚胎治疗");
        add(OasisEntities.TUBUNASUS_DUROVELA.get(),"杜拉帆管兽");
        add(OasisEntities.TUBUNASUS_CLYDEROTUNDA.get(),"圆盾管鼻兽");
        add(OasisEntities.PYGOPODUS_ANNULATUM.get(),"环颈合足兽");
        add(OasisItems.TUBUNASUS_DUROVELA_SPAWN_EGG.get(), "杜拉帆管兽生成蛋");
        add(OasisItems.TUBUNASUS_CLYDEROTUNDA_SPAWN_EGG.get(), "圆盾管鼻兽生成蛋");
        add(OasisItems.PYGOPODUS_ANNULATUM_SPAWN_EGG.get(), "环颈合足兽生成蛋");
        add(OasisItems.TUBUNASUS_DUROVELA_LARGE_BUCKET.get(), "杜拉帆管兽大桶");
        add(OasisItems.TUBUNASUS_SADDLE.get(), "杜拉帆管兽鞍");
        add(OasisItems.HORSESHOE.get(),"马蹄铁");
        add(OasisItems.BONE.get(), "???兽骨");
        add(OasisItems.HEART.get(), "???心脏");
        add(OasisItems.INTESTINES.get(), "???肠子");
        add(OasisItems.LEATHER.get(), "???皮革");
        add(OasisItems.LIVER.get(), "???肝脏");
        add(OasisItems.LUNG.get(), "???肺");
        add(OasisItems.EMBRYO.get(), "???胚胎");
        add(OasisItems.COOKED_LUNG.get(), "烤???肺");
        add(OasisItems.KIDNEY.get(), "???肾脏");
        add(OasisItems.COOKED_KIDNEY.get(), "烤???肾脏");
        add(OasisItems.ANNULATUM.get(),"合足兽肉");
        add(OasisItems.COOKED_ANNULATUM.get(),"烤合足兽肉");
        add(OasisItems.TEASELGOURD.get(),"黄刺瓜");

        add(OasisItems.DUROVELA_SPECIMEN.get(), "杜拉帆管兽标本");
        add(OasisItems.CLYDEROTUNDA_SPECIMEN.get(), "圆盾管鼻兽标本");
        add(OasisMobEffects.APHRODISIAC.get(), "壮阳");
        add(OasisMobEffects.BREATH_HOLD.get(), "屏息");
    }

    private void addAdvancement(String name, String title, String desc){
        add("advancements." + EcologicalReplenishmentStation.MODID  + "." + name + ".title", title);
        add("advancements." + EcologicalReplenishmentStation.MODID  + "." + name + ".desc", desc);
    }
}
