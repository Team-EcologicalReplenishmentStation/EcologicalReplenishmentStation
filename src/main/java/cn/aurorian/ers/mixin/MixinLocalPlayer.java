package cn.aurorian.ers.mixin;

import cn.aurorian.ers.entity.ErsFlyableVehicle;
import net.minecraft.client.player.LocalPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

/**
 * 修改 LocalPlayer 的蓄力条行为：骑乘 PterochirusDuxEntity 时，
 * 蓄力条只增不减，蓄满后保持满值直到松开空格。
 */
@Mixin(LocalPlayer.class)
public class MixinLocalPlayer {
    @Shadow
    private float jumpRidingScale;

    @Unique
    private float ers$prevJumpRidingScale = 0.0f;

    @Inject(method = "rideTick", at = @At("HEAD"))
    private void onRideTickHead(CallbackInfo ci) {
        this.ers$prevJumpRidingScale = this.jumpRidingScale;
    }

    @Inject(method = "rideTick", at = @At("TAIL"))
    private void onRideTickTail(CallbackInfo ci) {
        LocalPlayer player = (LocalPlayer) (Object) this;
        if (player.getVehicle() instanceof ErsFlyableVehicle<?> && player.input.jumping) {
            // 如果蓄力条被原版减少了，恢复为 tick 开始前的值
            if (this.jumpRidingScale < this.ers$prevJumpRidingScale) {
                this.jumpRidingScale = this.ers$prevJumpRidingScale;
            }
            // 限制上限为 1.0
            if (this.jumpRidingScale > 1.0f) {
                this.jumpRidingScale = 1.0f;
            }
        }
    }
}
