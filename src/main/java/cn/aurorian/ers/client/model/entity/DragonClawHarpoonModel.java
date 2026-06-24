package cn.aurorian.ers.client.model.entity;

import cn.aurorian.ers.EcologicalReplenishmentStation;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.world.entity.Entity;
import org.jetbrains.annotations.NotNull;

public class DragonClawHarpoonModel<T extends Entity> extends EntityModel<T> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
            EcologicalReplenishmentStation.prefix("textures/entity/dragon_claw_harpoon"), "main");
    private final ModelPart all;

    public DragonClawHarpoonModel(ModelPart root) {
        this.all = root.getChild("all");
    }

    public static LayerDefinition createBodyLayer() {
        MeshDefinition meshdefinition = new MeshDefinition();
        PartDefinition partdefinition = meshdefinition.getRoot();

        PartDefinition all = partdefinition.addOrReplaceChild(
                "all",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(-0.75F, -29.0F, -0.75F, 1.5F, 29.0F, 1.5F, new CubeDeformation(0.0F))
                        .texOffs(12, 4)
                        .addBox(-2.5F, -31.0F, -0.5F, 5.0F, 2.0F, 1.0F, new CubeDeformation(-0.02F))
                        .texOffs(16, 24)
                        .addBox(2.5353F, -37.8637F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(16, 24)
                        .mirror()
                        .addBox(-3.5353F, -37.8637F, -0.5F, 1.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
                        .mirror(false)
                        .texOffs(8, 0)
                        .addBox(-0.5F, -42.9796F, -0.5F, 1.0F, 12.0F, 1.0F, new CubeDeformation(0.0F))
                        .texOffs(14, 12)
                        .addBox(-1.25F, -0.5F, -1.25F, 2.5F, 1.0F, 2.5F, new CubeDeformation(0.0F))
                        .texOffs(8, 16)
                        .addBox(-1.5F, -26.05F, -1.0F, 3.0F, 1.3F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offset(0.0F, 21.5F, 0.5F));

        PartDefinition all_r1 = all.addOrReplaceChild(
                "all_r1",
                CubeListBuilder.create()
                        .texOffs(8, 24)
                        .addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -4.4F, 0.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition all_r2 = all.addOrReplaceChild(
                "all_r2",
                CubeListBuilder.create()
                        .texOffs(22, 7)
                        .addBox(-1.0F, -1.0F, -1.5F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.02F)),
                PartPose.offsetAndRotation(0.0F, -3.15F, 0.5F, 0.0F, 0.0F, 0.7854F));
        PartDefinition all_r3 = all.addOrReplaceChild(
                "all_r3",
                CubeListBuilder.create()
                        .texOffs(16, 20)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, 1.0F, 0.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition all_r4 = all.addOrReplaceChild(
                "all_r4",
                CubeListBuilder.create()
                        .texOffs(8, 20)
                        .addBox(-1.0F, -1.0F, -1.0F, 2.0F, 2.0F, 2.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(0.0F, -21.75F, 0.0F, 0.0F, 0.0F, 0.7854F));
        PartDefinition all_r5 = all.addOrReplaceChild(
                "all_r5",
                CubeListBuilder.create()
                        .texOffs(18, 16)
                        .addBox(-1.0F, -1.0F, -1.25F, 2.0F, 2.0F, 2.0F, new CubeDeformation(-0.1F)),
                PartPose.offsetAndRotation(0.0F, -24.75F, 0.25F, 0.0F, 0.0F, 0.7854F));
        PartDefinition all_r6 = all.addOrReplaceChild(
                "all_r6",
                CubeListBuilder.create()
                        .texOffs(24, 3)
                        .mirror()
                        .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.02F))
                        .mirror(false),
                PartPose.offsetAndRotation(-2.5353F, -37.8637F, 0.0F, 0.0F, 0.0F, -1.2217F));
        PartDefinition all_r7 = all.addOrReplaceChild(
                "all_r7",
                CubeListBuilder.create()
                        .texOffs(12, 0)
                        .mirror()
                        .addBox(-2.5F, -1.0F, -1.0F, 4.0F, 2.0F, 1.8F, new CubeDeformation(0.0F))
                        .mirror(false),
                PartPose.offsetAndRotation(-1.5F, -31.2F, 0.1F, 0.0F, 0.0F, 0.3054F));
        PartDefinition all_r8 = all.addOrReplaceChild(
                "all_r8",
                CubeListBuilder.create()
                        .texOffs(24, 11)
                        .mirror()
                        .addBox(-1.0F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F))
                        .mirror(false),
                PartPose.offsetAndRotation(-2.1811F, -31.6736F, 0.0F, 0.0F, 0.0F, -0.2618F));
        PartDefinition all_r9 = all.addOrReplaceChild(
                "all_r9",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .mirror()
                        .addBox(-0.5F, -1.0F, 0.1F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.02F))
                        .mirror(false),
                PartPose.offsetAndRotation(-2.5F, -28.45F, -0.5F, 0.0F, 0.0F, -0.6545F));
        PartDefinition all_r10 = all.addOrReplaceChild(
                "all_r10",
                CubeListBuilder.create()
                        .texOffs(24, 3)
                        .addBox(-1.0F, -1.0F, -0.5F, 2.0F, 1.0F, 1.0F, new CubeDeformation(0.02F)),
                PartPose.offsetAndRotation(2.5353F, -37.8637F, 0.0F, 0.0F, 0.0F, 1.2217F));
        PartDefinition all_r11 = all.addOrReplaceChild(
                "all_r11",
                CubeListBuilder.create()
                        .texOffs(24, 11)
                        .addBox(0.0F, -1.5F, -0.5F, 1.0F, 4.0F, 1.0F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(2.1811F, -31.6736F, 0.0F, 0.0F, 0.0F, 0.2618F));
        PartDefinition all_r12 = all.addOrReplaceChild(
                "all_r12",
                CubeListBuilder.create()
                        .texOffs(24, 0)
                        .addBox(-2.5F, -1.0F, 0.1F, 3.0F, 2.0F, 1.0F, new CubeDeformation(0.02F)),
                PartPose.offsetAndRotation(2.5F, -28.45F, -0.5F, 0.0F, 0.0F, 0.6545F));
        PartDefinition all_r13 = all.addOrReplaceChild(
                "all_r13",
                CubeListBuilder.create()
                        .texOffs(12, 0)
                        .addBox(-1.5F, -1.0F, -1.0F, 4.0F, 2.0F, 1.8F, new CubeDeformation(0.0F)),
                PartPose.offsetAndRotation(1.5F, -31.2F, 0.1F, 0.0F, 0.0F, -0.3054F));
        PartDefinition all_r14 = all.addOrReplaceChild(
                "all_r14",
                CubeListBuilder.create()
                        .texOffs(12, 7)
                        .addBox(-2.0F, -2.0F, -1.0F, 3.0F, 3.0F, 2.0F, new CubeDeformation(0.1F)),
                PartPose.offsetAndRotation(0.0F, -29.25F, 0.0F, 0.0F, 0.0F, 0.7854F));

        return LayerDefinition.create(meshdefinition, 32, 32);
    }

    @Override
    public void renderToBuffer(
            @NotNull PoseStack poseStack,
            @NotNull VertexConsumer vertexConsumer,
            int packedLight,
            int packedOverlay,
            float red,
            float green,
            float blue,
            float alpha) {
        all.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
    }

    @Override
    public void setupAnim(@NotNull T t, float v, float v1, float v2, float v3, float v4) {}
}
