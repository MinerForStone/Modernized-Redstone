package minerforstone.modernized_redstone.mixin;

import net.minecraft.core.block.BlockRedstoneWire;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(value = BlockRedstoneWire.class, remap = false)
public abstract class RedstoneWire {
}
