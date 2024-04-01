package minerforstone.modernized_redstone.block;

import minerforstone.modernized_redstone.ModernizedRedstone;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.BlockChest;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;

import java.util.Random;

public class BlockStockMeter extends Block {
	private boolean shouldBeOn;

	public BlockStockMeter(String key, int id) {
		super(key, id, Material.decoration);
	}

	@Override
	public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
		shouldBeOn = getContainerStatus((World) blockAccess, x, y, z);
		return shouldBeOn;
	}

	@Override
	public int tickRate() {
		return 2;
	}

	@Override
	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		updatePowerStatus(world, x, y, z);
	}

	@Override
	public void onBlockAdded(World world, int x, int y, int z) {
		updatePowerStatus(world, x, y, z);
	}

	private void updatePowerStatus(World world, int x, int y, int z) {
		shouldBeOn = getContainerStatus(world, x, y, z);
		if (shouldBeOn != (this.id == ModernizedRedstone.stockMeterActive.id))
			world.scheduleBlockUpdate(x, y, z, this.id, this.tickRate());
	}

	private boolean getContainerStatus(World world, int x, int y, int z) {
		return BlockChest.isChest(world, x + 1, y, z) || BlockChest.isChest(world, x - 1, y, z)
				|| BlockChest.isChest(world, x, y, z + 1) || BlockChest.isChest(world, x, y, z - 1);
	}

	@Override
	public void updateTick(World world, int x, int y, int z, Random rand) {
		if (shouldBeOn) world.setBlockAndMetadataWithNotify(x, y, z, ModernizedRedstone.stockMeterActive.id, 0);
		if (!shouldBeOn) world.setBlockAndMetadataWithNotify(x, y, z, ModernizedRedstone.stockMeterIdle.id, 0);
	}

	@Override
	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(ModernizedRedstone.stockMeterIdle)};
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}
}
