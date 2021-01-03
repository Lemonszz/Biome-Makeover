package party.lemons.biomemakeover.block;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.LilyPadBlock;
import net.minecraft.item.Item;
import net.minecraft.item.LilyPadItem;
import party.lemons.biomemakeover.util.BlockWithItem;

public class WaterLilyPadBlock extends LilyPadBlock implements BlockWithItem
{
	public WaterLilyPadBlock(FabricBlockSettings settings)
	{
		super(settings);
	}

	@Override
	public Item makeItem()
	{
		return new LilyPadItem(this, makeItemSettings());
	}
}
