package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.model.entity.TerridensaurusSaevusModel;
import cn.aurorian.ers.client.render.entity.layer.SaevusHiddenLayer;
import cn.aurorian.ers.entity.creatures.terridensaurussaevus.TerridensaurusSaevusEntity;
import cn.aurorian.ers.init.ErsNetwork;
import cn.aurorian.ers.packet.ErsTamableTrackFoodPacket;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import org.joml.Vector3f;
import software.bernie.geckolib.cache.object.BakedGeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class TerridensaurusSaevusRender extends GeoEntityRenderer<TerridensaurusSaevusEntity> {
    public TerridensaurusSaevusRender(EntityRendererProvider.Context renderManager) {
        super(renderManager, new TerridensaurusSaevusModel());
        addRenderLayer(new SaevusHiddenLayer(this));
    }

    @Override
    protected void applyRotations(TerridensaurusSaevusEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw,
                                  float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        GeneralAnimator<TerridensaurusSaevusEntity> animator = animatable.getAnimator();
        float scale = animatable.getRenderSize();
        poseStack.scale(scale, scale, scale);

        if(!animatable.getAnimator().isInScreen){
            poseStack.mulPose(Axis.XP.rotationDegrees(animator.getModelPitch(partialTick,7)));
        }
    }

    @Override
    public void renderFinal(PoseStack poseStack, TerridensaurusSaevusEntity animatable, BakedGeoModel model, MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red, green, blue, alpha);
        model.getBone("Food").ifPresent(bone ->{
            if (!animatable.getAttackState().isEmpty()) {
                ErsNetwork.INSTANCE.sendToServer(new ErsTamableTrackFoodPacket(animatable.getId(), new Vector3f((float)bone.getLocalPosition().x, (float)bone.getLocalPosition().y, (float)bone.getLocalPosition().z)));
            }
        });
    }
}
