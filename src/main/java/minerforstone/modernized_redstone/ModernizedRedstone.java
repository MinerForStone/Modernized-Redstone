package minerforstone.modernized_redstone;

import minerforstone.modernized_redstone.block.BlockStockMeter;
import minerforstone.modernized_redstone.block.BlockTarget;
import net.fabricmc.api.ModInitializer;
import net.minecraft.core.block.tag.BlockTags;
import net.minecraft.core.sound.BlockSounds;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import turniplabs.halplibe.helper.BlockBuilder;
import turniplabs.halplibe.util.GameStartEntrypoint;
import turniplabs.halplibe.util.RecipeEntrypoint;


public class ModernizedRedstone implements ModInitializer, GameStartEntrypoint, RecipeEntrypoint {
    public static final String MOD_ID = "modernized_redstone";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	public static BlockTarget target;
	public static BlockStockMeter stockMeterIdle;
	public static BlockStockMeter stockMeterActive;

    @Override
    public void onInitialize() {
		target = new BlockBuilder(MOD_ID).setBlockSound(BlockSounds.GRASS).setTextures(MOD_ID, "target.png").build(new BlockTarget("target", 601));
		stockMeterIdle = new BlockBuilder(MOD_ID).setTextures(14, 3).setTopTexture(3, 8).build(new BlockStockMeter("stock_meter.idle", 602));
		stockMeterActive = new BlockBuilder(MOD_ID).setTextures(14, 3).setTopTexture(3, 9).addTags(BlockTags.NOT_IN_CREATIVE_MENU).build(new BlockStockMeter("stock_meter.active", 603));

        LOGGER.info("Modernized Redstone initialized.");
    }

	@Override
	public void beforeGameStart() {

	}

	@Override
	public void afterGameStart() {

	}

	@Override
	public void onRecipesReady() {

	}
}
