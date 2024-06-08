package minerforstone.modernized_redstone.mixin;

import minerforstone.modernized_redstone.block.BlockWire;
import net.minecraft.core.block.Block;
import net.minecraft.core.block.tag.BlockTags;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(value = Block.class, remap = false)
public abstract class BlockMixin {
	@Shadow
	@Final
	@Mutable
	public static Block wireRedstone;

	@Shadow
	@Final
	public static Block[] blocksList;

	@Inject(method = "<clinit>", at = @At("TAIL"))
	private static void swapToNewClasses(CallbackInfo ci) {
		blocksList[450] = null;
		wireRedstone = (new BlockWire("wire.redstone", 450)).withTexCoords(4, 10).withHardness(0.0F).withDisabledStats().withDisabledNeighborNotifyOnMetadataChange().withTags(BlockTags.BROKEN_BY_FLUIDS, BlockTags.NOT_IN_CREATIVE_MENU, BlockTags.PREVENT_MOB_SPAWNS);
	}
}
