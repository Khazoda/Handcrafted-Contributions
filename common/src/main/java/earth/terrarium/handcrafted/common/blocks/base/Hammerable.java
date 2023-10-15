package earth.terrarium.handcrafted.common.blocks.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;

public interface Hammerable {
    void onHammer(Level level, BlockPos pos, BlockState state, Direction side, Player user, Vec3 hitPos);
}
