package cn.aurorian.ers.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

public class ErsClientConfig {
    public static final ForgeConfigSpec CLIENT_CONFIG;
    public static final ForgeConfigSpec.BooleanValue ENABLE_MOUNT_CAMERA_CORRECTION;
    public static final ForgeConfigSpec.DoubleValue MOUNT_CAMERA_BACK_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue MOUNT_CAMERA_BACK_VERTICAL_OFFSET;
    public static final ForgeConfigSpec.DoubleValue MOUNT_CAMERA_FRONT_DISTANCE;
    public static final ForgeConfigSpec.DoubleValue MOUNT_CAMERA_FRONT_VERTICAL_OFFSET;

    static {
        ForgeConfigSpec.Builder CLIENT_BUILDER = new ForgeConfigSpec.Builder();
        CLIENT_BUILDER.comment("Client settings").push("client");

        ENABLE_MOUNT_CAMERA_CORRECTION = CLIENT_BUILDER
                .comment("Enable custom camera correction while riding ERS mounts.")
                .define("EnableMountCameraCorrection", true);

        CLIENT_BUILDER.comment("Mount camera settings").push("mountCamera");

        MOUNT_CAMERA_BACK_DISTANCE = CLIENT_BUILDER
                .comment("Third person back camera distance while riding ERS mounts.")
                .defineInRange("BackDistance", 7.0, 0.0, 32.0);
        MOUNT_CAMERA_BACK_VERTICAL_OFFSET = CLIENT_BUILDER
                .comment("Third person back vertical camera offset while riding ERS mounts.")
                .defineInRange("BackVerticalOffset", 4.0, -16.0, 16.0);
        MOUNT_CAMERA_FRONT_DISTANCE = CLIENT_BUILDER
                .comment("Third person front camera distance while riding ERS mounts.")
                .defineInRange("FrontDistance", 7.0, 0.0, 32.0);
        MOUNT_CAMERA_FRONT_VERTICAL_OFFSET = CLIENT_BUILDER
                .comment("Third person front vertical camera offset while riding ERS mounts.")
                .defineInRange("FrontVerticalOffset", 3.5, -16.0, 16.0);

        CLIENT_BUILDER.pop();
        CLIENT_BUILDER.pop();
        CLIENT_CONFIG = CLIENT_BUILDER.build();
    }

    public static void register(FMLJavaModLoadingContext context) {
        context.registerConfig(ModConfig.Type.CLIENT, CLIENT_CONFIG);
    }
}
