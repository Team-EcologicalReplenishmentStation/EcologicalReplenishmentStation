package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.client.animator.GeneralAnimator;
import cn.aurorian.ers.client.model.entity.SwampDragonModel;
import cn.aurorian.ers.client.render.entity.layer.SwampDragonEyeLayer;
import cn.aurorian.ers.client.render.entity.layer.SwampDragonHiddenLayer;
import cn.aurorian.ers.entity.creatures.dentisauruslongirostris.DentisaurusLongirostrisEntity;
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

public class SwampDragonRender extends GeoEntityRenderer<DentisaurusLongirostrisEntity> {

    public SwampDragonRender(EntityRendererProvider.Context ctx) {
        super(ctx, new SwampDragonModel());
        addRenderLayer(new SwampDragonHiddenLayer(this));
        addRenderLayer(new SwampDragonEyeLayer(this));
        this.shadowRadius = 0.5F;
    }

    @Override
    protected void applyRotations(DentisaurusLongirostrisEntity animatable, PoseStack poseStack, float ageInTicks, float rotationYaw,
                                  float partialTick) {
        super.applyRotations(animatable, poseStack, ageInTicks, rotationYaw, partialTick);
        GeneralAnimator<DentisaurusLongirostrisEntity> animator = animatable.getAnimator();
        float scale = animatable.getRenderSize();
    
        poseStack.scale(scale, scale, scale);

        if(!animatable.getAnimator().isInScreen){
            poseStack.translate(0, 1.5, 0.5);
            poseStack.mulPose(Axis.XP.rotationDegrees(animator.getModelPitch(partialTick)));
            poseStack.translate(0, -1.5, -0.5);
        }

    }
    @Override
    public void renderFinal(PoseStack poseStack, DentisaurusLongirostrisEntity animatable, BakedGeoModel model,
                            MultiBufferSource bufferSource, VertexConsumer buffer, float partialTick, int packedLight,
                            int packedOverlay, float red, float green, float blue, float alpha) {
        super.renderFinal(poseStack, animatable, model, bufferSource, buffer, partialTick, packedLight, packedOverlay, red,
                green, blue, alpha);
                model.getBone("Food").ifPresent(bone ->{
                    if (!animatable.getAttackState().isEmpty()) {
                        //向服务器发包
                        ErsNetwork.INSTANCE.sendToServer(new ErsTamableTrackFoodPacket(animatable.getId(), new Vector3f((float)bone.getLocalPosition().x, (float)bone.getLocalPosition().y, (float)bone.getLocalPosition().z)));
                    }
                });
    }
}

