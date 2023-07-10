package earth.terrarium.handcrafted.client.block.chair.chair;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.handcrafted.common.block.chair.chair.ChairBlockEntity;
import earth.terrarium.handcrafted.common.block.chair.couch.ExpandableCouchBlock;
import earth.terrarium.handcrafted.common.block.property.CouchShape;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;

public class ChairRenderer implements BlockEntityRenderer<ChairBlockEntity> {
    public ChairRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    private static void render(ResourceLocation cushion, ResourceLocation texture, Direction direction, CouchShape shape, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        ChairModel model = new ChairModel(Minecraft.getInstance().getEntityModels().bakeLayer(ChairModel.LAYER_LOCATION));
        try (var ignored = new CloseablePoseStack(poseStack)) {
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(direction.getOpposite().toYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180));
            model.getMain().getChild("with_cushion").visible = false;
            model.getMain().getChild("seat").visible = false;
            VertexConsumer vertex = buffer.getBuffer(RenderType.entityCutout(new ResourceLocation(texture.getNamespace(), "textures/block/chair/chair/" + texture.getPath() + ".png")));
            model.renderToBuffer(poseStack, vertex, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);

            model.getMain().getChild("base").visible = false;
            model.getMain().getChild("seat").visible = true;
            model.getMain().getChild("chair").visible = false;
            poseStack.scale(0.999f, 1, 1);
            model.renderToBuffer(poseStack, vertex, packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
            poseStack.scale(1.001f, 1, 1);


            model.getMain().getChild("with_cushion").visible = true;
            if (!cushion.toString().equals("minecraft:air")) {
                model.getMain().getChild("base").visible = false;
                model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(new ResourceLocation(texture.getNamespace(), "textures/block/chair/chair/cushion/" + cushion.getPath() + ".png"))), packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
            } else {
                model.getMain().getChild("with_cushion").visible = false;
            }
        }
    }

    @Override
    public void render(ChairBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        CouchShape shape = entity.getBlockState().getValue(ExpandableCouchBlock.COUCH_SHAPE);
        render(BuiltInRegistries.ITEM.getKey(entity.getStack().getItem()), BuiltInRegistries.BLOCK.getKey(entity.getBlockState().getBlock()), entity.getBlockState().getValue(ExpandableCouchBlock.FACING), shape, poseStack, bufferSource, packedLight, packedOverlay);
    }

    public static class ItemRenderer extends BlockEntityWithoutLevelRenderer {
        public ItemRenderer() {
            super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }

        @Override
        public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
            try (var ignored = new CloseablePoseStack(poseStack)) {
                poseStack.scale(0.75f, 0.75f, 0.75f);
                render(new ResourceLocation("air"), BuiltInRegistries.ITEM.getKey(stack.getItem()), Direction.SOUTH, CouchShape.SINGLE, poseStack, buffer, packedLight, packedOverlay);
            }
        }
    }
}
