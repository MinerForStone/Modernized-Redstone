package minerforstone.modernized_redstone.mixin;

import net.minecraft.core.block.BlockRedstoneTorch;
import net.minecraft.core.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(value = BlockRedstoneTorch.class, remap = false)
public abstract class RedstoneTorch {
	@Redirect(method = "updateTick", at = @At(value = "INVOKE", target = "Lnet/minecraft/core/block/BlockRedstoneTorch;checkForBurnout(Lnet/minecraft/core/world/World;IIIZ)Z"))
	private boolean disableBurnout(BlockRedstoneTorch torch, World world, int i, int j, int k, boolean flag) {
		return false;
	}
}
