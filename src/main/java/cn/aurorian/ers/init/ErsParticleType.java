package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.particle.FallingBloodParticle;
import net.minecraft.client.particle.FlameParticle;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(
        modid = EcologicalReplenishmentStation.MODID,
        bus = Mod.EventBusSubscriber.Bus.MOD,
        value = Dist.CLIENT)
public class ErsParticleType {
    public static final DeferredRegister<ParticleType<?>> PARTICLE_TYPES =
            DeferredRegister.create(ForgeRegistries.PARTICLE_TYPES, EcologicalReplenishmentStation.MODID);
    public static final RegistryObject<SimpleParticleType> BLOOD =
            PARTICLE_TYPES.register("blood", () -> new SimpleParticleType(Boolean.FALSE));
    public static final RegistryObject<SimpleParticleType> DRIPPING_BLOOD =
            PARTICLE_TYPES.register("dripping_blood", () -> new SimpleParticleType(Boolean.FALSE));

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void registerProviders(RegisterParticleProvidersEvent event) {
        event.registerSpriteSet(BLOOD.get(), FlameParticle.Provider::new);
        event.registerSpriteSet(DRIPPING_BLOOD.get(), FallingBloodParticle.Provider::new);
    }
}
