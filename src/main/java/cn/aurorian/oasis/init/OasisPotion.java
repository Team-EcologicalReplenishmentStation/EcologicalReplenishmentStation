package cn.aurorian.oasis.init;

import cn.aurorian.oasis.Oasis;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class OasisPotion {
    public static final DeferredRegister<Potion> POTION = DeferredRegister.create(ForgeRegistries.POTIONS,
            Oasis.MODID);

    public static final RegistryObject<Potion> EMBRYO_HEALING = POTION.register("embryo_healing",
            () -> new Potion(new MobEffectInstance(MobEffects.REGENERATION, 72000)));

    public static void register(IEventBus eventBus) {
        POTION.register(eventBus);
    }
}
