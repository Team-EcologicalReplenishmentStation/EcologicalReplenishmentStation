package cn.aurorian.ers.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ErsServerConfig {
    public static final ForgeConfigSpec SERVER_CONFIG;
    public static final ForgeConfigSpec.IntValue HATCHING_RATE;
    public static final ForgeConfigSpec.IntValue MATURE_RATE;


    static {
        ForgeConfigSpec.Builder COMMON_BUILDER = new ForgeConfigSpec.Builder();
        COMMON_BUILDER.comment("General settings").push("general");
        HATCHING_RATE = COMMON_BUILDER.comment("The rate at which eggs hatch. Higher is faster. Default is 48000 ticks (40 minutes).")
                .defineInRange("EggHatchingSpeedMultiplier", 1, 1, Integer.MAX_VALUE);
        MATURE_RATE = COMMON_BUILDER.comment("The rate at which creatures grow up. Higher is faster. Default is 24000 ticks (20 minutes).")
                .defineInRange("BabyMatureSpeedMultiplier", 1, 1, Integer.MAX_VALUE);

        COMMON_BUILDER.pop();
        SERVER_CONFIG = COMMON_BUILDER.build();
    }

    public static void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.SERVER, SERVER_CONFIG);
    }
}
