package cn.aurorian.oasis;

import net.minecraft.resources.ResourceLocation;

import java.util.Locale;

public class Oasis {
    public static final String MODID = "oasis";

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));
    }
}
