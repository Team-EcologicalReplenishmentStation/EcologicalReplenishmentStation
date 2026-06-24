package cn.aurorian.oasis.init;

import cn.aurorian.oasis.Oasis;
import cn.aurorian.oasis.effect.AphrodisiacEffect;
import cn.aurorian.oasis.effect.BreathHoldEffect;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisMobEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, Oasis.MODID);
    public static final RegistryObject<MobEffect> APHRODISIAC =
            MOB_EFFECTS.register("aphrodisiac", () -> new AphrodisiacEffect(MobEffectCategory.NEUTRAL, 0xDC143C)
                    .addAttributeModifier(
                            Attributes.MOVEMENT_SPEED,
                            "A1AEAA56-376B-4498-935B-2F7F68070635",
                            0.20000000298023224,
                            AttributeModifier.Operation.MULTIPLY_TOTAL)
                    .addAttributeModifier(
                            Attributes.ATTACK_SPEED,
                            "BF8B6E3F-3328-4C0A-AA36-5BA2BB9DBEF3",
                            0.10000000149011612,
                            AttributeModifier.Operation.MULTIPLY_TOTAL));

    public static final RegistryObject<MobEffect> BREATH_HOLD =
            MOB_EFFECTS.register("breath_hold", () -> new BreathHoldEffect(MobEffectCategory.BENEFICIAL, 0x1E90FF));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
