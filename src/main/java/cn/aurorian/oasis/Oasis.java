package cn.aurorian.oasis;

import java.util.Locale;
import net.minecraft.resources.ResourceLocation;

public class Oasis {
    public static final String MODID = "oasis";

    public static ResourceLocation prefix(String name) {
        return ResourceLocation.fromNamespaceAndPath(MODID, name.toLowerCase(Locale.ROOT));
    }
}
