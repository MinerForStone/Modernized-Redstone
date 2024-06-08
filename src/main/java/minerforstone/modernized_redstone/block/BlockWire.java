package minerforstone.modernized_redstone.block;

import net.minecraft.client.util.helper.Colors;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.entity.TileEntity;
import net.minecraft.core.block.logic.BedDirections;
import net.minecraft.core.block.material.Material;
import net.minecraft.core.enums.EnumDropCause;
import net.minecraft.core.item.Item;
import net.minecraft.core.item.ItemStack;
import net.minecraft.core.util.helper.Color;
import net.minecraft.core.util.phys.AABB;
import net.minecraft.core.world.World;
import net.minecraft.core.world.WorldSource;
import net.minecraft.core.world.chunk.ChunkPosition;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Random;
import java.util.Set;

public class BlockWire extends Block {
	private final int maxRange = 15;
	private boolean wiresProvidePower = true;
	private final Set field_21031_b = new HashSet();

	public BlockWire(String key, int id) {
		super(key, id, Material.decoration);
		this.setBlockBounds(0.0F, 0.0F, 0.0F, 1.0F, 0.0625F, 1.0F);
	}

	public AABB getCollisionBoundingBoxFromPool(World world, int x, int y, int z) {
		return null;
	}

	public boolean isSolidRender() {
		return false;
	}

	public boolean renderAsNormalBlock() {
		return false;
	}

	public boolean canPlaceBlockAt(World world, int x, int y, int z) {
		return world.canPlaceOnSurfaceOfBlock(x, y - 1, z);
	}

	private void updateAndPropagateCurrentStrength(World world, int i, int j, int k) {
		this.func_21030_a(world, i, j, k, i, j, k);
		ArrayList arraylist = new ArrayList(this.field_21031_b);
		this.field_21031_b.clear();

		for(int l = 0; l < arraylist.size(); ++l) {
			ChunkPosition chunkposition = (ChunkPosition)arraylist.get(l);
			world.notifyBlocksOfNeighborChange(chunkposition.x, chunkposition.y, chunkposition.z, this.id);
		}

	}

	private void func_21030_a(World world, int i, int j, int k, int l, int i1, int j1) {
		int k1 = world.getBlockMetadata(i, j, k);
		int l1 = 0;
		this.wiresProvidePower = false;
		boolean flag = world.isBlockIndirectlyGettingPowered(i, j, k);
		this.wiresProvidePower = true;
		int j2;
		int l2;
		int j3;
		if (flag) {
			l1 = maxRange;
		} else {
			for(j2 = 0; j2 < 4; ++j2) {
				l2 = i;
				j3 = k;
				if (j2 == 0) {
					l2 = i - 1;
				}

				if (j2 == 1) {
					++l2;
				}

				if (j2 == 2) {
					j3 = k - 1;
				}

				if (j2 == 3) {
					++j3;
				}

				if (l2 != l || j != i1 || j3 != j1) {
					l1 = this.getMaxCurrentStrength(world, l2, j, j3, l1);
				}

				if (world.isBlockNormalCube(l2, j, j3) && !world.isBlockNormalCube(i, j + 1, k)) {
					if (l2 != l || j + 1 != i1 || j3 != j1) {
						l1 = this.getMaxCurrentStrength(world, l2, j + 1, j3, l1);
					}
				} else if (!world.isBlockNormalCube(l2, j, j3) && (l2 != l || j - 1 != i1 || j3 != j1)) {
					l1 = this.getMaxCurrentStrength(world, l2, j - 1, j3, l1);
				}
			}

			if (l1 > 0) {
				--l1;
			} else {
				l1 = 0;
			}
		}

		if (k1 != l1) {
			world.editingBlocks = true;
			world.setBlockMetadataWithNotify(i, j, k, l1);
			world.markBlocksDirty(i, j, k, i, j, k);
			world.editingBlocks = false;

			for(j2 = 0; j2 < 4; ++j2) {
				l2 = i;
				j3 = k;
				int k3 = j - 1;
				if (j2 == 0) {
					l2 = i - 1;
				}

				if (j2 == 1) {
					++l2;
				}

				if (j2 == 2) {
					j3 = k - 1;
				}

				if (j2 == 3) {
					++j3;
				}

				if (world.isBlockNormalCube(l2, j, j3)) {
					k3 += 2;
				}

				int l3 = this.getMaxCurrentStrength(world, l2, j, j3, -1);
				l1 = world.getBlockMetadata(i, j, k);
				if (l1 > 0) {
					--l1;
				}

				if (l3 >= 0 && l3 != l1) {
					this.func_21030_a(world, l2, j, j3, i, j, k);
				}

				l3 = this.getMaxCurrentStrength(world, l2, k3, j3, -1);
				l1 = world.getBlockMetadata(i, j, k);
				if (l1 > 0) {
					--l1;
				}

				if (l3 >= 0 && l3 != l1) {
					this.func_21030_a(world, l2, k3, j3, i, j, k);
				}
			}

			if (k1 == 0 || l1 == 0) {
				this.field_21031_b.add(new ChunkPosition(i, j, k));
				this.field_21031_b.add(new ChunkPosition(i - 1, j, k));
				this.field_21031_b.add(new ChunkPosition(i + 1, j, k));
				this.field_21031_b.add(new ChunkPosition(i, j - 1, k));
				this.field_21031_b.add(new ChunkPosition(i, j + 1, k));
				this.field_21031_b.add(new ChunkPosition(i, j, k - 1));
				this.field_21031_b.add(new ChunkPosition(i, j, k + 1));
			}
		}

	}

	private void notifyWireNeighborsOfNeighborChange(World world, int i, int j, int k) {
		if (world.getBlockId(i, j, k) == this.id) {
			world.notifyBlocksOfNeighborChange(i, j, k, this.id);
			world.notifyBlocksOfNeighborChange(i - 1, j, k, this.id);
			world.notifyBlocksOfNeighborChange(i + 1, j, k, this.id);
			world.notifyBlocksOfNeighborChange(i, j, k - 1, this.id);
			world.notifyBlocksOfNeighborChange(i, j, k + 1, this.id);
			world.notifyBlocksOfNeighborChange(i, j - 1, k, this.id);
			world.notifyBlocksOfNeighborChange(i, j + 1, k, this.id);
		}
	}

	public void onBlockAdded(World world, int x, int y, int z) {
		super.onBlockAdded(world, x, y, z);
		if (!world.isClientSide) {
			this.updateAndPropagateCurrentStrength(world, x, y, z);
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.id);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
			this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
			this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
			if (world.isBlockNormalCube(x - 1, y, z)) {
				this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
			}

			if (world.isBlockNormalCube(x + 1, y, z)) {
				this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
			}

			if (world.isBlockNormalCube(x, y, z - 1)) {
				this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
			}

			if (world.isBlockNormalCube(x, y, z + 1)) {
				this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
			}

		}
	}

	public void onBlockRemoved(World world, int x, int y, int z, int data) {
		super.onBlockRemoved(world, x, y, z, data);
		if (!world.isClientSide) {
			world.notifyBlocksOfNeighborChange(x, y + 1, z, this.id);
			world.notifyBlocksOfNeighborChange(x, y - 1, z, this.id);
			this.updateAndPropagateCurrentStrength(world, x, y, z);
			this.notifyWireNeighborsOfNeighborChange(world, x - 1, y, z);
			this.notifyWireNeighborsOfNeighborChange(world, x + 1, y, z);
			this.notifyWireNeighborsOfNeighborChange(world, x, y, z - 1);
			this.notifyWireNeighborsOfNeighborChange(world, x, y, z + 1);
			if (world.isBlockNormalCube(x - 1, y, z)) {
				this.notifyWireNeighborsOfNeighborChange(world, x - 1, y + 1, z);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x - 1, y - 1, z);
			}

			if (world.isBlockNormalCube(x + 1, y, z)) {
				this.notifyWireNeighborsOfNeighborChange(world, x + 1, y + 1, z);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x + 1, y - 1, z);
			}

			if (world.isBlockNormalCube(x, y, z - 1)) {
				this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z - 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z - 1);
			}

			if (world.isBlockNormalCube(x, y, z + 1)) {
				this.notifyWireNeighborsOfNeighborChange(world, x, y + 1, z + 1);
			} else {
				this.notifyWireNeighborsOfNeighborChange(world, x, y - 1, z + 1);
			}

		}
	}

	private int getMaxCurrentStrength(World world, int i, int j, int k, int l) {
		if (world.getBlockId(i, j, k) != this.id) {
			return l;
		} else {
			int i1 = world.getBlockMetadata(i, j, k);
			return i1 > l ? i1 : l;
		}
	}

	public void onNeighborBlockChange(World world, int x, int y, int z, int blockId) {
		if (!world.isClientSide) {
			int i1 = world.getBlockMetadata(x, y, z);
			boolean flag = this.canPlaceBlockAt(world, x, y, z);
			if (!flag) {
				this.dropBlockWithCause(world, EnumDropCause.WORLD, x, y, z, i1, (TileEntity)null);
				world.setBlockWithNotify(x, y, z, 0);
			} else {
				this.updateAndPropagateCurrentStrength(world, x, y, z);
			}

			super.onNeighborBlockChange(world, x, y, z, blockId);
		}
	}

	public ItemStack[] getBreakResult(World world, EnumDropCause dropCause, int x, int y, int z, int meta, TileEntity tileEntity) {
		return new ItemStack[]{new ItemStack(Item.dustRedstone)};
	}

	public boolean isIndirectlyPoweringTo(World world, int x, int y, int z, int side) {
		return !this.wiresProvidePower ? false : this.isPoweringTo(world, x, y, z, side);
	}

	public boolean isPoweringTo(WorldSource blockAccess, int x, int y, int z, int side) {
		if (!this.wiresProvidePower) {
			return false;
		} else if (blockAccess.getBlockMetadata(x, y, z) == 0) {
			return false;
		} else if (side == 1) {
			return true;
		} else {
			boolean flag = isPowerProviderOrWire(blockAccess, x - 1, y, z, 1) || !blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y - 1, z, -1);
			boolean flag1 = isPowerProviderOrWire(blockAccess, x + 1, y, z, 3) || !blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y - 1, z, -1);
			boolean flag2 = isPowerProviderOrWire(blockAccess, x, y, z - 1, 2) || !blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z - 1, -1);
			boolean flag3 = isPowerProviderOrWire(blockAccess, x, y, z + 1, 0) || !blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y - 1, z + 1, -1);
			if (!blockAccess.isBlockNormalCube(x, y + 1, z)) {
				if (blockAccess.isBlockNormalCube(x - 1, y, z) && isPowerProviderOrWire(blockAccess, x - 1, y + 1, z, -1)) {
					flag = true;
				}

				if (blockAccess.isBlockNormalCube(x + 1, y, z) && isPowerProviderOrWire(blockAccess, x + 1, y + 1, z, -1)) {
					flag1 = true;
				}

				if (blockAccess.isBlockNormalCube(x, y, z - 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z - 1, -1)) {
					flag2 = true;
				}

				if (blockAccess.isBlockNormalCube(x, y, z + 1) && isPowerProviderOrWire(blockAccess, x, y + 1, z + 1, -1)) {
					flag3 = true;
				}
			}

			if (!flag2 && !flag1 && !flag && !flag3 && side >= 2 && side <= 5) {
				return true;
			} else if (side == 2 && flag2 && !flag && !flag1) {
				return true;
			} else if (side == 3 && flag3 && !flag && !flag1) {
				return true;
			} else if (side == 4 && flag && !flag2 && !flag3) {
				return true;
			} else {
				return side == 5 && flag1 && !flag2 && !flag3;
			}
		}
	}

	public boolean canProvidePower() {
		return this.wiresProvidePower;
	}

	public void randomDisplayTick(World world, int x, int y, int z, Random rand) {
		int meta = world.getBlockMetadata(x, y, z);
		if (meta != 0) {
			double px = (double)x + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
			double py = (double)((float)y + 0.0625F);
			double pz = (double)z + 0.5 + ((double)rand.nextFloat() - 0.5) * 0.2;
			Color color = Colors.allRedstoneColors[meta];
			if (color != null) {
				float red = (float)color.getRed() / 255.0F;
				float green = (float)color.getGreen() / 255.0F;
				float blue = (float)color.getBlue() / 255.0F;
				world.spawnParticle("reddust", px, py, pz, (double)red, (double)green, (double)blue);
			}
		}
	}

	public static boolean isPowerProviderOrWire(WorldSource iblockaccess, int i, int j, int k, int l) {
		int i1 = iblockaccess.getBlockId(i, j, k);
		if (i1 == Block.wireRedstone.id) {
			return true;
		} else if (i1 == 0) {
			return false;
		} else if (Block.blocksList[i1].canProvidePower()) {
			return true;
		} else if (i1 != Block.repeaterIdle.id && i1 != Block.repeaterActive.id) {
			return false;
		} else {
			int j1 = iblockaccess.getBlockMetadata(i, j, k);
			return l == BedDirections.field_22279_b[j1 & 3];
		}
	}
}
