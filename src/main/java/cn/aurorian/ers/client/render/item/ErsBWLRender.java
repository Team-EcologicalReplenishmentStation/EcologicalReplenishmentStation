package cn.aurorian.ers.client.render.item;

import cn.aurorian.ers.client.model.entity.DragonClawHarpoonModel;
import cn.aurorian.ers.client.model.layer.ErsLayers;
import cn.aurorian.ers.client.render.entity.DragonClawHarpoonRender;
import cn.aurorian.ers.entity.projectile.DragonClawHarpoonEntity;
import cn.aurorian.ers.init.ErsItems;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderDispatcher;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.nbt.ListTag;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

public class ErsBWLRender extends BlockEntityWithoutLevelRenderer {
    private DragonClawHarpoonModel<DragonClawHarpoonEntity> model;
    private final EntityModelSet entityModelSet;

    public ErsBWLRender(BlockEntityRenderDispatcher p_172550_, EntityModelSet entityModelSet) {
        super(p_172550_, entityModelSet);
        this.entityModelSet = entityModelSet;
        this.model = new DragonClawHarpoonModel<>(this.entityModelSet.bakeLayer(ErsLayers.DRAGON_CLAW_HARPOON));
    }

    @Override
    public void onResourceManagerReload(@NotNull ResourceManager pResourceManager) {
        super.onResourceManagerReload(pResourceManager);
        this.model = new DragonClawHarpoonModel<>(this.entityModelSet.bakeLayer(ErsLayers.DRAGON_CLAW_HARPOON));
    }

    @Override
    public void renderByItem(
            @NotNull ItemStack stack,
            @NotNull ItemDisplayContext type,
            PoseStack stackIn,
            @NotNull MultiBufferSource bufferIn,
            int combinedLightIn,
            int combinedOverlayIn) {
        stackIn.translate(0.5F, 0.5f, 0.5f);
        if (type == ItemDisplayContext.GUI
                || type == ItemDisplayContext.FIXED
                || type == ItemDisplayContext.NONE
                || type == ItemDisplayContext.GROUND) {
            ItemStack tridentInventory = new ItemStack(ErsItems.DRAGON_CLAW_HARPOON_INVENTORY.get());
            if (stack.isEnchanted()) {
                ListTag enchantments = stack.getTag().getList("Enchantments", 10);
                tridentInventory.addTagElement("Enchantments", enchantments);
            }
            Minecraft.getInstance()
                    .getItemRenderer()
                    .renderStatic(
                            tridentInventory,
                            type,
                            type == ItemDisplayContext.GROUND ? combinedLightIn : 240,
                            combinedOverlayIn,
                            stackIn,
                            bufferIn,
                            Minecraft.getInstance().level,
                            0);
        } else {
            stackIn.pushPose();
            stackIn.mulPose(Axis.XP.rotationDegrees(180.0F));
            VertexConsumer glintVertexBuilder = ItemRenderer.getFoilBufferDirect(
                    bufferIn, RenderType.entityCutoutNoCull(DragonClawHarpoonRender.LOCATION), false, stack.hasFoil());
            model.renderToBuffer(
                    stackIn, glintVertexBuilder, combinedLightIn, combinedOverlayIn, 1.0F, 1.0F, 1.0F, 1.0F);
            stackIn.popPose();
        }
    }
}
