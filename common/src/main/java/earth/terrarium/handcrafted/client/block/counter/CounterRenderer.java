package earth.terrarium.handcrafted.client.block.counter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.handcrafted.Handcrafted;
import earth.terrarium.handcrafted.common.block.chair.couch.ExpandableCouchBlock;
import earth.terrarium.handcrafted.common.block.counter.CounterBlockEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.geom.EntityModelSet;
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

@Environment(EnvType.CLIENT)
public class CounterRenderer implements BlockEntityRenderer<CounterBlockEntity> {
    public CounterRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    private static void render(ResourceLocation overlay, ResourceLocation texture, CounterModel model, Direction direction, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
        try (var ignored = new CloseablePoseStack(poseStack)) {
            poseStack.translate(0.5, 1.5, 0.5);
            poseStack.mulPose(Axis.YN.rotationDegrees(direction.toYRot()));
            poseStack.mulPose(Axis.XP.rotationDegrees(180));

            model.getMain().getChild("top").visible = false;
            model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(new ResourceLocation(texture.getNamespace(), "textures/block/counter/counter/" + texture.getPath() + ".png"))), packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
            model.getMain().getChild("top").visible = true;
            model.getMain().getChild("base").visible = false;
            if (!overlay.toString().equals("minecraft:air")) {
                model.renderToBuffer(poseStack, buffer.getBuffer(RenderType.entityCutout(new ResourceLocation(texture.getNamespace(), "textures/block/counter/counter/overlay/" + overlay.getPath() + ".png"))), packedLight, packedOverlay, 1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    @Override
    public void render(CounterBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        EntityModelSet modelSet = Minecraft.getInstance().getEntityModels();
        CounterModel model = new CounterModel(modelSet.bakeLayer(CounterModel.LAYER_LOCATION));

        render(BuiltInRegistries.ITEM.getKey(entity.getStack().getItem()), BuiltInRegistries.BLOCK.getKey(entity.getBlockState().getBlock()), model, entity.getBlockState().getValue(ExpandableCouchBlock.FACING), poseStack, bufferSource, packedLight, packedOverlay);
    }

    public static class ItemRenderer extends BlockEntityWithoutLevelRenderer {
        public ItemRenderer() {
            super(Minecraft.getInstance().getBlockEntityRenderDispatcher(), Minecraft.getInstance().getEntityModels());
        }

        @Override
        public void renderByItem(ItemStack stack, ItemDisplayContext itemDisplayContext, PoseStack poseStack, MultiBufferSource buffer, int packedLight, int packedOverlay) {
            render(new ResourceLocation("calcite"), new ResourceLocation(Handcrafted.MOD_ID, BuiltInRegistries.ITEM.getKey(stack.getItem()).getPath() + "_1"), new CounterModel(Minecraft.getInstance().getEntityModels().bakeLayer(CounterModel.LAYER_LOCATION)), Direction.NORTH, poseStack, buffer, packedLight, packedOverlay);
        }
    }
}
