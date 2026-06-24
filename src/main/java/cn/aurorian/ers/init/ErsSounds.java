package cn.aurorian.ers.init;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import net.minecraft.sounds.SoundEvent;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ErsSounds {
    public static final DeferredRegister<SoundEvent> SOUND_EVENTS =
            DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, EcologicalReplenishmentStation.MODID);

    public static final RegistryObject<SoundEvent> TEL_RING = register("tel_ring");

    public static final RegistryObject<SoundEvent> LONGIROSTRIS_WALK = register("dentisaurus_longirostris_walk");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_RUN = register("dentisaurus_longirostris_run");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_TURN = register("dentisaurus_longirostris_turn");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK_SWIM_TURN =
            register("dentisaurus_longirostris_attack_swim_turn");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK_TURN =
            register("dentisaurus_longirostris_attack_turn");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK_IDLE =
            register("dentisaurus_longirostris_attackidle");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK_IDLE2 =
            register("dentisaurus_longirostris_attackidle2");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK2_SMALL =
            register("dentisaurus_longirostris_attack2small");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK2_MIDDLE =
            register("dentisaurus_longirostris_attack2middle");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK2_MISS =
            register("dentisaurus_longirostris_attack2miss");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_ATTACK3 = register("dentisaurus_longirostris_attack3");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_ATTACK2 =
            register("dentisaurus_longirostris_swim_attack2");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_ATTACK =
            register("dentisaurus_longirostris_swim_attack");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_ATTACK_MISS =
            register("dentisaurus_longirostris_swim_attack_miss");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_KNOCKDOWN =
            register("dentisaurus_longirostris_knockdown");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_JUMP_ATTACK =
            register("dentisaurus_longirostris_jump_attack");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_CATCH_FISH_SMALL =
            register("dentisaurus_longirostris_catch_fish_small");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_CATCH_FISH_MIDDLE =
            register("dentisaurus_longirostris_catch_fish_middle");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_QUICKLY_SWIMMING =
            register("dentisaurus_longirostris_quickly_swimming");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_UP = register("dentisaurus_longirostris_swim_up");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_DOWN =
            register("dentisaurus_longirostris_swim_down");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_MID =
            register("dentisaurus_longirostris_swim_mid");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_SHALLOW =
            register("dentisaurus_longirostris_swim_shallow");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_SWIM_ATTACKMISS =
            register("dentisaurus_longirostris_swim_attackmiss");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_HURT = register("dentisaurus_longirostris_hurt");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_IDLE2 = register("dentisaurus_longirostris_idle2");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_IDLE3 = register("dentisaurus_longirostris_idle3");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_IDLE_SIT3 =
            register("dentisaurus_longirostris_idle_sit3");
    public static final RegistryObject<SoundEvent> LONGIROSTRIS_IDLE_SIT4 =
            register("dentisaurus_longirostris_idle_sit4");

    public static final RegistryObject<SoundEvent> SAEVUS_ATTACK = register("saevus_attack");
    public static final RegistryObject<SoundEvent> SAEVUS_SWIM = register("saevus_swim");
    public static final RegistryObject<SoundEvent> SAEVUS_FOOTSTEP = register("saevus_footstep");
    public static final RegistryObject<SoundEvent> SAEVUS_TURN = register("saevus_turn");
    public static final RegistryObject<SoundEvent> SAEVUS_IDLE2 = register("saevus_idle2");
    public static final RegistryObject<SoundEvent> SAEVUS_IDLE3 = register("saevus_idle3");
    public static final RegistryObject<SoundEvent> SAEVUS_IDLE_SIT2 = register("saevus_idle_sit2");
    public static final RegistryObject<SoundEvent> SAEVUS_IDLE_SIT3 = register("saevus_idle_sit3");
    public static final RegistryObject<SoundEvent> SAEVUS_STRIKE = register("saevus_strike");
    public static final RegistryObject<SoundEvent> SAEVUS_ROAR = register("saevus_roar");
    public static final RegistryObject<SoundEvent> SAEVUS_ATTACK_TURN = register("saevus_attack_turn");
    public static final RegistryObject<SoundEvent> SAEVUS_KNOCKDOWN = register("saevus_knockdown");

    private static RegistryObject<SoundEvent> register(String sound) {
        return SOUND_EVENTS.register(
                sound, () -> SoundEvent.createVariableRangeEvent(EcologicalReplenishmentStation.prefix(sound)));
    }
}
