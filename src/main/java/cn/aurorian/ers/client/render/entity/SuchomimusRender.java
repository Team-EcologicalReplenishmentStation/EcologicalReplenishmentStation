package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.entity.creatures.latimeriasuchomimus.LatimeriaSuchomimusEntity;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.packet.ErsTamableTrackFoodPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class SuchomimusRender extends ErsRenderer<LatimeriaSuchomimusEntity> {
    public SuchomimusRender(EntityRendererProvider.Context renderManager) {
        super(renderManager,20);
    }

    @Override
    public void renderFinal(PoseStack poseStack, LatimeriaSuchomimusEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        model.getBone("food").ifPresent(bone ->{
            if (!animatable.getAttackState().isEmpty()) {
                ErsNetwork.INSTANCE.sendToServer(new ErsTamableTrackFoodPacket(animatable.getId(), new Vector3f((float)bone.getLocalPosition().x, (float)bone.getLocalPosition().y, (float)bone.getLocalPosition().z)));
            }
        });
    }
}
