package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.effect.ErsBleedingEffect;
import cn.aurorian.ers.effect.FractureEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, EcologicalReplenishmentStation.MODID);
    public static final RegistryObject<MobEffect> BLEEDING = MOB_EFFECTS.register("bleeding", () -> new ErsBleedingEffect(MobEffectCategory.HARMFUL, 0xDC143C));
    public static final RegistryObject<MobEffect> FRACTURE = MOB_EFFECTS.register("fracture", () -> new FractureEffect(MobEffectCategory.HARMFUL, 0x808080)
            .addAttributeModifier(Attributes.ATTACK_DAMAGE, "EE126E3C-3428-4CDA-BA34-3BA2BBADBEC5", -0.3333333, AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
