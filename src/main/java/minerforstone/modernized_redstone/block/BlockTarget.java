package minerforstone.modernized_redstone.block;

import net.minecraft.core.block.Block;
import net.minecraft.core.block.material.Material;

public class BlockTarget extends Block {
	public BlockTarget(String key, int id) {
		super(key, id, Material.dirt);
	}

	@Override
	public boolean canProvidePower() {
		return true;
	}
}
