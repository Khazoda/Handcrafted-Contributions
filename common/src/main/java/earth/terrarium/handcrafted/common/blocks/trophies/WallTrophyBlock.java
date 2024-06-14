package earth.terrarium.handcrafted.common.blocks.trophies;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import com.mojang.serialization.MapCodec;
import earth.terrarium.handcrafted.common.blocks.base.SimpleBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.HorizontalDirectionalBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;

public class WallTrophyBlock extends SimpleBlock {
    public static final MapCodec<WallTrophyBlock> CODEC = simpleCodec(WallTrophyBlock::new);
    private static final EnumMap<Direction, VoxelShape> AABBS = Maps.newEnumMap(
        ImmutableMap.of(
            Direction.NORTH, Block.box(0.0, 4.5, 14.0, 16.0, 12.5, 16.0),
            Direction.SOUTH, Block.box(0.0, 4.5, 0.0, 16.0, 12.5, 2.0),
            Direction.EAST, Block.box(0.0, 4.5, 0.0, 2.0, 12.5, 16.0),
            Direction.WEST, Block.box(14.0, 4.5, 0.0, 16.0, 12.5, 16.0)));

    public WallTrophyBlock(Properties properties) {
        super(properties);
    }

    @Override
    protected MapCodec<? extends HorizontalDirectionalBlock> codec() {
        return CODEC;
    }

    public @NotNull VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext context) {
        return AABBS.get(state.getValue(FACING));
    }
}
