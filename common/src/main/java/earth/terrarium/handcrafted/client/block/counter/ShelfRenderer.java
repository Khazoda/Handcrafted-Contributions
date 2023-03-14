package earth.terrarium.handcrafted.client.block.counter;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import com.teamresourceful.resourcefullib.client.CloseablePoseStack;
import earth.terrarium.handcrafted.Handcrafted;
import earth.terrarium.handcrafted.common.block.counter.ShelfBlock;
import earth.terrarium.handcrafted.common.block.counter.ShelfBlockEntity;
import earth.terrarium.handcrafted.common.block.crockery.CrockeryBlock;
import earth.terrarium.handcrafted.common.block.pot.PotBlock;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.WebBlock;

public class ShelfRenderer implements BlockEntityRenderer<ShelfBlockEntity> {
    public static final ResourceLocation BOOKS = new ResourceLocation(Handcrafted.MOD_ID, "textures/block/counter/shelf/overlay/books.png");
    public static final ResourceLocation COBWEBS = new ResourceLocation(Handcrafted.MOD_ID, "textures/block/counter/shelf/overlay/cobwebs.png");
    public static final ResourceLocation POTIONS = new ResourceLocation(Handcrafted.MOD_ID, "textures/block/counter/shelf/overlay/potions.png");
    public static final ResourceLocation POTS = new ResourceLocation(Handcrafted.MOD_ID, "textures/block/counter/shelf/overlay/pots.png");

    public ShelfRenderer(BlockEntityRendererProvider.Context ctx) {
    }

    @Override
    public void render(ShelfBlockEntity entity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (entity.getStack().isEmpty()) return;
        if (entity.getLevel() == null) return;

        Item item = entity.getStack().getItem();
        Block block = BuiltInRegistries.BLOCK.get(BuiltInRegistries.ITEM.getKey(item));
        Direction dir = entity.getBlockState().getValue(ShelfBlock.FACING);
        ResourceLocation id;
        try (var ignored = new CloseablePoseStack(poseStack)) {
            poseStack.mulPose(Axis.YN.rotationDegrees(dir.toYRot()));
            poseStack.translate(0, 1.0, 0);
            switch (dir) {
                case NORTH -> poseStack.translate(-1, 0, 0.001);
                case SOUTH -> poseStack.translate(0, 0, 1.001);
                case EAST -> poseStack.translate(-1, 0, 1.001);
                case WEST -> poseStack.translate(0, 0, 0.001);
            }
            poseStack.mulPose(Axis.XP.rotationDegrees(180));

            if (item instanceof BookItem || item instanceof EnchantedBookItem || item instanceof WritableBookItem || item instanceof WrittenBookItem) {
                id = BOOKS;
            } else if (block instanceof WebBlock) {
                id = COBWEBS;
            } else if (item instanceof PotionItem || item instanceof BottleItem) {
                id = POTIONS;
            } else if (block instanceof PotBlock || block instanceof CrockeryBlock) {
                id = POTS;
            } else {
                return;
            }

            VertexConsumer vertexConsumer = bufferSource.getBuffer(RenderType.entityCutout(new ResourceLocation(id.getNamespace(), id.getPath().replace(".png", "_" + entity.getBlockState().getValue(ShelfBlock.SHELF_SHAPE).toString().toLowerCase() + ".png"))));
            int light = LevelRenderer.getLightColor(entity.getLevel(), entity.getBlockPos().relative(entity.getBlockState().getValue(ShelfBlock.FACING)));

            renderShelfOverlay(poseStack, vertexConsumer, dir, light, packedOverlay);
        }
    }

    private static void renderShelfOverlay(PoseStack poseStack, VertexConsumer consumer, Direction dir, int light, int overlay) {
        Vec3i normal = dir.getNormal();
        consumer.vertex(poseStack.last().pose(), 0, 0, 0).color(255, 255, 255, 255).uv(0, 0).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), normal.getX(), normal.getY(), normal.getZ()).endVertex();
        consumer.vertex(poseStack.last().pose(), 0, 1, 0).color(255, 255, 255, 255).uv(0, 1).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), normal.getX(), normal.getY(), normal.getZ()).endVertex();
        consumer.vertex(poseStack.last().pose(), 1, 1, 0).color(255, 255, 255, 255).uv(1, 1).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), normal.getX(), normal.getY(), normal.getZ()).endVertex();
        consumer.vertex(poseStack.last().pose(), 1, 0, 0).color(255, 255, 255, 255).uv(1, 0).overlayCoords(overlay).uv2(light).normal(poseStack.last().normal(), normal.getX(), normal.getY(), normal.getZ()).endVertex();
    }
}
