package party.lemons.biomemakeover.block.blockentity;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.text.Text;
import net.minecraft.text.TranslatableText;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Nameable;
import party.lemons.biomemakeover.block.TapestryBlock;
import party.lemons.biomemakeover.init.BMBlockEntities;

public class TapestryBlockEntity extends BlockEntity implements Nameable
{
	private Text customName;
	private DyeColor nonWorldColor = DyeColor.WHITE;

	public TapestryBlockEntity() {
		super(BMBlockEntities.TAPESTRY);
	}

	@Override
	public Text getName() {
		return this.customName != null ? this.customName : new TranslatableText("block.biomemakeover.tapestry");
	}

	public Text getCustomName() {
		return this.customName;
	}

	public void setCustomName(Text customName) {
		this.customName = customName;
	}

	@Override
	public CompoundTag toTag(CompoundTag tag)
	{
		if (this.customName != null) {
			tag.putString("CustomName", Text.Serializer.toJson(this.customName));
		}
		return super.toTag(tag);
	}

	@Override
	public void fromTag(BlockState state, CompoundTag tag)
	{
		super.fromTag(state, tag);
		if (tag.contains("CustomName", 8)) {
			this.customName = Text.Serializer.fromJson(tag.getString("CustomName"));
		}
	}

	public DyeColor getColor()
	{
		if(world == null)
			return nonWorldColor;
		return ((TapestryBlock)getCachedState().getBlock()).color;
	}

	public void setNonWorldColor(DyeColor color)
	{
		this.nonWorldColor = color;
	}
}
