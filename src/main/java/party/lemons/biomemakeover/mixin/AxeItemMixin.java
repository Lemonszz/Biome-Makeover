package party.lemons.biomemakeover.mixin;

import net.minecraft.block.Block;
import net.minecraft.item.AxeItem;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import party.lemons.biomemakeover.util.access.AxeItemAccess;

import java.util.HashMap;
import java.util.Map;

@Mixin(AxeItem.class)
public class AxeItemMixin implements AxeItemAccess
{
	@Shadow
	@Final
	@Mutable
	protected static Map<Block, Block> STRIPPED_BLOCKS;

	@Inject(at = @At("TAIL"), method = "<clinit>")
	private static void onInit(CallbackInfo cbi)
	{
		STRIPPED_BLOCKS = new HashMap<>(STRIPPED_BLOCKS);
	/*	STRIPPED_BLOCKS.put(BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG), BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getBlock(WoodTypeInfo.Type.STRIPPED_LOG));
		STRIPPED_BLOCKS.put(BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG), BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.STRIPPED_LOG));
		STRIPPED_BLOCKS.put(BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.LOG), BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.STRIPPED_LOG));
		STRIPPED_BLOCKS.put(BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getBlock(WoodTypeInfo.Type.WOOD), BMBlocks.BLIGHTED_BALSA_WOOD_INFO.getBlock(WoodTypeInfo.Type.STRIPPED_WOOD));
		STRIPPED_BLOCKS.put(BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.WOOD), BMBlocks.WILLOW_WOOD_INFO.getBlock(WoodTypeInfo.Type.STRIPPED_WOOD));
		STRIPPED_BLOCKS.put(BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.WOOD), BMBlocks.SWAMP_CYPRESS_WOOD_INFO.getBlock(WoodTypeInfo.Type.STRIPPED_WOOD));*/
	}

	@Override
	public void addStrippable(Block log, Block stripped)
	{
		STRIPPED_BLOCKS.put(log, stripped);
	}
}
