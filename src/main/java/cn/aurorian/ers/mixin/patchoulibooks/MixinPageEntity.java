package cn.aurorian.ers.mixin.patchoulibooks;

import cn.aurorian.ers.entity.ErsEntity;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import vazkii.patchouli.client.book.page.PageEntity;

@Mixin(value = PageEntity.class, remap = false)
public class MixinPageEntity {
    @Shadow
    transient Entity entity;

    @Inject(
            method = "render",
            at =
                    @At(
                            value = "INVOKE",
                            target =
                                    "Lvazkii/patchouli/client/book/page/PageEntity;renderEntity(Lnet/minecraft/client/gui/GuiGraphics;Lnet/minecraft/world/entity/Entity;FFFFF)V"))
    private void beforeRenderEntity(GuiGraphics graphics, int mouseX, int mouseY, float pticks, CallbackInfo ci) {
        if (entity instanceof ErsEntity<?> ersEntity) {
            ersEntity.getAnimator().isInScreen = true;
        }
    }
}
