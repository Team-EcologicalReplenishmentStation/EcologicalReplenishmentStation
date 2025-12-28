package cn.aurorian.ers.mixin.alexsmobs;

import cn.aurorian.ers.entity.ErsTamableVehicle;
import com.github.alexthe666.alexsmobs.entity.EntityTiger;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(targets = "com.github.alexthe666.alexsmobs.entity.EntityTiger$AIMelee")
public abstract class MixinEntityTiger{
    @Redirect(method = "tick", at = @At(value = "INVOKE", target = "Lcom/github/alexthe666/alexsmobs/entity/EntityTiger;setHolding(Z)V"))
    private void onTigerHold(EntityTiger instance, boolean running){
        instance.setHolding(!(instance.getTarget() instanceof ErsTamableVehicle<?>));
    }
}