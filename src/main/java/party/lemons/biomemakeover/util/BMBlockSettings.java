package party.lemons.biomemakeover.util;

import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Material;
import net.minecraft.block.MaterialColor;

public class BMBlockSettings extends FabricBlockSettings
{
	private RLayer layer = null;

	public BMBlockSettings(Material material, MaterialColor color)
	{
		super(material, color);
	}

	public BMBlockSettings(AbstractBlock.Settings settings)
	{
		super(settings);
	}

	public RLayer getLayer()
	{
		return layer;
	}

	public FabricBlockSettings layer(RLayer layer)
	{
		this.layer = layer;
		return this;
	}
}
