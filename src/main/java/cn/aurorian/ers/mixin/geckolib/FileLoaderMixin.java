package cn.aurorian.ers.mixin.geckolib;

import cn.aurorian.ers.util.GeoResourceCrypto;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import software.bernie.geckolib.loading.FileLoader;

@Mixin(FileLoader.class)
public class FileLoaderMixin {

    @Inject(
            method =
                    "getFileContents(Lnet/minecraft/resources/ResourceLocation;Lnet/minecraft/server/packs/resources/ResourceManager;)Ljava/lang/String;",
            at = @At("RETURN"),
            cancellable = true,
            remap = false)
    private static void ers$decryptGeoResource(
            ResourceLocation location, ResourceManager manager, CallbackInfoReturnable<String> cir) {
        String path = location.getPath();
        String ns = location.getNamespace();

        if (("ers".equals(ns) || "oasis".equals(ns)) && (path.startsWith("geo/") || path.startsWith("animations/"))) {
            String decrypted = GeoResourceCrypto.decryptIfNeeded(cir.getReturnValue());
            cir.setReturnValue(decrypted);
        }
    }
}
