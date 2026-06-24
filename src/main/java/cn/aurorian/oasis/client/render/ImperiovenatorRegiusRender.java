package cn.aurorian.oasis.client.render;

import cn.aurorian.ers.client.render.entity.ErsRenderer;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.packet.ErsTamableTrackFoodPacket;
import cn.aurorian.oasis.client.model.RegiusModel;
import cn.aurorian.oasis.client.render.layer.RegiusHiddenLayer;
import cn.aurorian.oasis.entity.imperiovenatorregius.ImperiovenatorRegiusEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;

public class ImperiovenatorRegiusRender extends ErsRenderer<ImperiovenatorRegiusEntity> {
    public ImperiovenatorRegiusRender(EntityRendererProvider.Context context) {
        super(context, new RegiusModel(), 6);
        addRenderLayer(new RegiusHiddenLayer(this));
    }

    @Override
    protected void applyRotations(
            ImperiovenatorRegiusEntity animatable,
            PoseStack poseStack,
            float ageInTicks,
            float rotationYaw,
            float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        float scale = animatable.getScale();
        poseStack.scale(scale, scale, scale);
    }

    @Override
    public void renderFinal(
            PoseStack poseStack,
            ImperiovenatorRegiusEntity animatable,
            BakedGeoModel model,
            MultiBufferSource bufferSource,
            VertexConsumer buffer,
            float partialTick,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha) {
        super.renderFinal(
                poseStack,
                animatable,
                model,
                bufferSource,
                buffer,
                partialTick,
                packedLight,
                packedOverlay,
                red,
                green,
                blue,
                alpha);
        model.getBone("food").ifPresent(bone -> {
            if (!animatable.getAttackState().isEmpty()) {
                ErsNetwork.INSTANCE.sendToServer(new ErsTamableTrackFoodPacket(
                        animatable.getId(),
                        new Vector3f((float) bone.getLocalPosition().x, (float) bone.getLocalPosition().y, (float)
                                bone.getLocalPosition().z)));
            }
        });
    }
}
