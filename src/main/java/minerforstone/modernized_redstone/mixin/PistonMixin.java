package minerforstone.modernized_redstone.mixin;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.piston.BlockPistonBase;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(BlockPistonBase.class)
public abstract class PistonMixin {
	/**
	 * @author
	 * MinerForStone
	 * @reason
	 * Modern Quasi Connectivity
	 */
	@Overwrite(remap = false)
	private boolean getNeighborSignal(World world, int x, int y, int z, int direction) {
		if (direction != 0 && world.isBlockIndirectlyProvidingPowerTo(x, y - 1, z, 0)) {
			return true;
		} else if (direction != 1 && world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z, 1)) {
			return true;
		} else if (direction != 2 && world.isBlockIndirectlyProvidingPowerTo(x, y, z - 1, 2)) {
			return true;
		} else if (direction != 3 && world.isBlockIndirectlyProvidingPowerTo(x, y, z + 1, 3)) {
			return true;
		} else if (direction != 5 && world.isBlockIndirectlyProvidingPowerTo(x + 1, y, z, 5)) {
			return true;
		} else if (direction != 4 && world.isBlockIndirectlyProvidingPowerTo(x - 1, y, z, 4)) {
			return true;
		} else if (world.isBlockIndirectlyProvidingPowerTo(x, y, z, 0)) {
			return true;
		} else if (world.getBlock(x, y + 1, z) == null) {
			return false;
		} else if (world.getBlock(x, y + 1, z).id == Block.pistonBase.id || world.getBlock(x, y + 1, z).id == Block.pistonBaseSticky.id) { // Only use quasi if block above is piston
			if (world.isBlockIndirectlyProvidingPowerTo(x, y + 2, z, 1)) {
				return true;
			} else if (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z - 1, 2)) {
				return true;
			} else if (world.isBlockIndirectlyProvidingPowerTo(x, y + 1, z + 1, 3)) {
				return true;
			} else {
				return world.isBlockIndirectlyProvidingPowerTo(x - 1, y + 1, z, 4) || world.isBlockIndirectlyProvidingPowerTo(x + 1, y + 1, z, 5);
			}
		} else return false;
	}
}
