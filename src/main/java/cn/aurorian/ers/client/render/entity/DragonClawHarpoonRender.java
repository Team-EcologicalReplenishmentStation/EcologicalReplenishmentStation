package cn.aurorian.ers.client.render.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import cn.aurorian.ers.client.model.entity.DragonClawHarpoonModel;
import cn.aurorian.ers.client.model.layer.ErsLayers;
import cn.aurorian.ers.entity.projectile.DragonClawHarpoonEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.projectile.ThrownTrident;
import org.jetbrains.annotations.NotNull;

public class DragonClawHarpoonRender extends EntityRenderer<ThrownTrident> {
    public static final ResourceLocation LOCATION = EcologicalReplenishmentStation.prefix("textures/entity/dragon_claw_harpoon.png");
    private final DragonClawHarpoonModel<DragonClawHarpoonEntity> model;
    public DragonClawHarpoonRender(EntityRendererProvider.Context pContext) {
        super(pContext);
        this.model = new DragonClawHarpoonModel<>(pContext.bakeLayer(ErsLayers.DRAGON_CLAW_HARPOON));
    }

    public void render(ThrownTrident pEntity, float pEntityYaw, float pPartialTicks, PoseStack pPoseStack, @NotNull MultiBufferSource pBuffer, int pPackedLight) {
        pPoseStack.pushPose();
        pPoseStack.mulPose(Axis.YP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.yRotO, pEntity.getYRot()) - 90.0F));
        pPoseStack.mulPose(Axis.ZP.rotationDegrees(Mth.lerp(pPartialTicks, pEntity.xRotO, pEntity.getXRot()) + 90.0F));
        VertexConsumer $$6 = ItemRenderer.getFoilBufferDirect(pBuffer, this.model.renderType(this.getTextureLocation(pEntity)), false, pEntity.isFoil());
        this.model.renderToBuffer(pPoseStack, $$6, pPackedLight, OverlayTexture.NO_OVERLAY, 1.0F, 1.0F, 1.0F, 1.0F);
        pPoseStack.popPose();
        super.render(pEntity, pEntityYaw, pPartialTicks, pPoseStack, pBuffer, pPackedLight);
    }

    public @NotNull ResourceLocation getTextureLocation(@NotNull ThrownTrident pEntity) {
        return LOCATION;
    }
}
